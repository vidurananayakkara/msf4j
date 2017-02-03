/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.msf4j.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.DefaultSessionManager;
import org.wso2.msf4j.Interceptor;
import org.wso2.msf4j.MicroservicesRegistry;
import org.wso2.msf4j.SessionManager;
import org.wso2.msf4j.SwaggerService;
import org.wso2.msf4j.filter.MSF4JRequestFilter;
import org.wso2.msf4j.filter.MSF4JResponseFilter;
import org.wso2.msf4j.internal.router.MicroserviceMetadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.Path;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * MicroservicesRegistry for the MSF4J component.
 */
public class MicroservicesRegistryImpl implements MicroservicesRegistry {

    private static final Logger log = LoggerFactory.getLogger(MicroservicesRegistryImpl.class);
    private final Map<String, Object> services = new HashMap<>();

    private final List<Interceptor> interceptors = new ArrayList<>();
    private Map<Class<?>, MSF4JRequestFilter> msf4JRequestFilterListMap = new HashMap<>();
    private Map<Class<?>, MSF4JResponseFilter> msf4JResponseFilterListMap = new HashMap<>();
    private Set<Class<?>> globalRequestFilterClassSet = new HashSet<>();
    private Set<Class<?>> globalResponseFilterClassSet = new HashSet<>();
    private volatile MicroserviceMetadata metadata = new MicroserviceMetadata(Collections.emptyList());
    private Map<Class, ExceptionMapper> exceptionMappers = new TreeMap<>(new ClassComparator());
    private SessionManager sessionManager = new DefaultSessionManager();

    public MicroservicesRegistryImpl() {
        /* In non OSGi mode, if we can find the SwaggerDefinitionService, Deploy the Swagger definition service which
        will return the Swagger definition.*/
        if (DataHolder.getInstance().getBundleContext() == null) {
            ServiceLoader<SwaggerService> swaggerServices = ServiceLoader.load(SwaggerService.class);
            Iterator<SwaggerService> iterator = swaggerServices.iterator();
            if (iterator.hasNext()) {
                SwaggerService swaggerService = iterator.next();
                swaggerService.init(this);
                services.put("/swagger", swaggerService);
            }
        }
    }

    public void addService(Object... service) {
        for (Object svc : service) {
            services.put(svc.getClass().getAnnotation(Path.class).value(), svc);
        }
        updateMetadata();
        Arrays.stream(service).forEach(svc -> log.info("Added microservice: " + svc));
    }

    public void addService(String basePath, Object service) {
        services.put(basePath, service);
        metadata.addMicroserviceMetadata(service, basePath);
        log.info("Added microservice: " + service);
    }

    public Optional<Map.Entry<String, Object>> getServiceWithBasePath(String path) {
        return services.entrySet().stream().filter(svc -> svc.getKey().equals(path)).findAny();
    }

    public void removeService(Object service) {
        services.remove(service);
        updateMetadata();
    }

    public void setSessionManager(SessionManager sessionManager) {
        if (sessionManager == null) {
            throw new IllegalArgumentException("SessionManager cannot be null");
        }
        this.sessionManager = sessionManager;
    }

    public MicroserviceMetadata getMetadata() {
        return metadata;
    }

    public Set<Object> getHttpServices() {
        return Collections.unmodifiableSet(services.values().stream().collect(Collectors.toSet()));
    }

    public void addInterceptor(Interceptor... interceptor) {
        Collections.addAll(interceptors, interceptor);
    }

    /**
     * Register MSF4J request filter.
     *
     * @param requestFilter MSF4J filter instance.
     */
    public void registerRequestFilter(MSF4JRequestFilter requestFilter, Class<?> type) {
        if (!msf4JRequestFilterListMap.containsKey(type)) {
            msf4JRequestFilterListMap.put(type, requestFilter);
            updateGlobalRequestFilterSet(); // Update global filter class set
        }
    }

    /**
     * Register MSF4J response filter.
     *
     * @param responseFilter MSF4J filter instance.
     */
    public void registerResponseFilter(MSF4JResponseFilter responseFilter, Class<?> type) {
        if (!msf4JResponseFilterListMap.containsKey(type)) {
            msf4JResponseFilterListMap.put(type, responseFilter);
            updateGlobalResponseFilterSet(); // Update global filter class set
        }
    }

    /**
     * Remove msf4j request filter.
     *
     * @param requestFilterClass MSF4J filter instance type.
     */
    public void removeRequestFilter(Class<?> requestFilterClass) {
        msf4JRequestFilterListMap.remove(requestFilterClass);
        updateGlobalRequestFilterSet(); // Update global filter class set
    }

    /**
     * Remove msf4j response filter.
     *
     * @param responseFilterClass MSF4J filter instance type.
     */
    public void removeResponseFilter(Class<?> responseFilterClass) {
        msf4JResponseFilterListMap.remove(responseFilterClass);
        updateGlobalResponseFilterSet(); // Update global filter class set
    }

    /**
     * Map against class and msf4j filter instance.
     *
     * @return Map against class -> msf4j filter instance.
     */
    public Map<Class<?>, MSF4JRequestFilter> getMsf4JRequestFilterListMap() {
        return msf4JRequestFilterListMap;
    }

    /**
     * Map against class and msf4j filter instance.
     *
     * @return Map against class -> msf4j filter instance.
     */
    public Map<Class<?>, MSF4JResponseFilter> getMsf4JResponseFilterListMap() {
        return msf4JResponseFilterListMap;
    }

    /**
     * Get a set of request filter classes,
     *
     * @return set of request filter classes
     */
    public Set<Class<?>> getGlobalRequestFilterClassSet() {
        return globalRequestFilterClassSet;
    }

    /**
     * Get a set of response filter classes,
     *
     * @return set of response filter classes
     */
    public Set<Class<?>> getGlobalResponseFilterClassSet() {
        return globalResponseFilterClassSet;
    }

    public void addExceptionMapper(ExceptionMapper... mapper) {
        Arrays.stream(mapper).forEach(em -> {
            Arrays.stream(em.getClass().getMethods()).
                    filter(method -> "toResponse".equals(method.getName()) && method.getParameterCount() == 1 &&
                            !Throwable.class.getName().equals(method.getParameterTypes()[0].getTypeName())).
                    findAny().
                    ifPresent(method -> {
                        try {
                            exceptionMappers.put(Class.forName(method.getParameterTypes()[0].getTypeName(), false,
                                    em.getClass().getClassLoader()), em);
                        } catch (ClassNotFoundException e) {
                            log.error("Could not load class", e);
                        }
                    });
        });
    }

    Optional<ExceptionMapper> getExceptionMapper(Throwable throwable) {
        return exceptionMappers.entrySet().
                stream().
                filter(entry -> entry.getKey().isAssignableFrom(throwable.getClass())).
                findFirst().
                flatMap(entry -> Optional.ofNullable(entry.getValue()));
    }


    public void removeExceptionMapper(ExceptionMapper em) {
        Arrays.stream(em.getClass().getMethods()).
                filter(method -> method.getName().equals("toResponse") && method.getParameterCount() == 1).
                findAny().
                ifPresent(method -> {
                    try {
                        exceptionMappers.remove(Class.forName(method.getGenericParameterTypes()[0].getTypeName(),
                                false, em.getClass().getClassLoader()));
                    } catch (ClassNotFoundException e) {
                        log.error("Could not load class", e);
                    }
                });
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void removeInterceptor(Interceptor interceptor) {
        interceptors.remove(interceptor);
    }

    public int getServiceCount() {
        return services.size();
    }

    /**
     * Update request global filter class set.
     */
    private void updateGlobalRequestFilterSet() {
        Set<Class<?>> appliedFiltersList =
                metadata.scanRequestFilterAnnotations(Collections.unmodifiableCollection(services.values()));
        globalRequestFilterClassSet = msf4JRequestFilterListMap.keySet().stream()
                .filter(c -> !appliedFiltersList.contains(c))
                .collect(Collectors.toSet());
    }

    /**
     * Update response global filter class set.
     */
    private void updateGlobalResponseFilterSet() {
        Set<Class<?>> appliedFiltersList =
                metadata.scanResponseFilterAnnotations(Collections.unmodifiableCollection(services.values()));
        globalResponseFilterClassSet = msf4JResponseFilterListMap.keySet().stream()
                .filter(c -> !appliedFiltersList.contains(c))
                .collect(Collectors.toSet());
    }

    private void updateMetadata() {
        metadata = new MicroserviceMetadata(Collections.unmodifiableCollection(services.values()));
    }

    public void initServices() {
        invokeLifecycleMethods(PostConstruct.class);
    }

    public void initService(Object httpService) {
        invokeLifecycleMethod(httpService, PostConstruct.class);
    }

    public void preDestroyServices() {
        invokeLifecycleMethods(PreDestroy.class);
    }

    public void preDestroyService(Object httpService) {
        invokeLifecycleMethod(httpService, PreDestroy.class);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private void invokeLifecycleMethods(Class lcAnnotation) {
        services.values().stream().forEach(httpService -> invokeLifecycleMethod(httpService, lcAnnotation));
    }

    private void invokeLifecycleMethod(Object httpService, Class lcAnnotation) {
        Optional<Method> lcMethod = Optional.ofNullable(getLifecycleMethod(httpService, lcAnnotation));
        if (lcMethod.isPresent()) {
            try {
                lcMethod.get().invoke(httpService);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MicroservicesLCException("Exception occurs calling lifecycle method", e);
            }
        }
    }

    private Method getLifecycleMethod(Object httpService, Class lcAnnotation) {
        return Arrays.stream(httpService.getClass().getDeclaredMethods()).filter(m -> isValidLifecycleMethod
                (Optional.of(m), lcAnnotation)).findFirst().orElse(null);
    }

    private boolean isValidLifecycleMethod(Optional<Method> method, Class lcAnnotation) {
        return method.filter(m -> Modifier.isPublic(m.getModifiers())
                && m.getAnnotation(lcAnnotation) != null).isPresent();
    }
}
