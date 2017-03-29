/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.msf4j.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OSGi interceptor registration component.
 */
public class OSGiInterceptorConfig {

    private final List<MSF4JRequestInterceptor> globalRequestInterceptorList = new ArrayList<>();
    private final List<MSF4JResponseInterceptor> globalResponseInterceptorList = new ArrayList<>();

    /**
     * Add global request interceptors.
     * Order in which the interceptors are added are the execution priority order if the interceptors
     *
     * @param globalRequestInterceptors {@link MSF4JRequestInterceptor}
     */
    protected final void addGlobalRequestInterceptors(MSF4JRequestInterceptor... globalRequestInterceptors) {
        globalRequestInterceptorList.addAll(Arrays.asList(globalRequestInterceptors));
    }

    /**
     * Add global response interceptors.
     * Order in which the interceptors are added are the execution priority order if the interceptors
     *
     * @param globalResponseInterceptors {@link MSF4JResponseInterceptor}
     */
    protected final void addGlobalResponseInterceptors(MSF4JResponseInterceptor... globalResponseInterceptors) {
        globalResponseInterceptorList.addAll(Arrays.asList(globalResponseInterceptors));
    }

    /**
     * Get global request interceptors added.
     *
     * @return {@link MSF4JRequestInterceptor}
     */
    public final MSF4JRequestInterceptor[] getGlobalRequestInterceptorArray() {
        return globalRequestInterceptorList
                .toArray(new MSF4JRequestInterceptor[globalRequestInterceptorList.size()]);
    }

    /**
     * Get global response interceptors added.
     *
     * @return {@link MSF4JResponseInterceptor}
     */
    public final MSF4JResponseInterceptor[] getGlobalResponseInterceptorArray() {
        return globalResponseInterceptorList
                .toArray(new MSF4JResponseInterceptor[globalResponseInterceptorList.size()]);
    }
}
