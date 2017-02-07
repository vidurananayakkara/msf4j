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
package org.wso2.msf4j.context;

import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;

import java.lang.reflect.Method;

/**
 * HttpMethodContext class will be the intermediate resource to pass values between interceptors and resources.
 */
public class HttpMethodContext {

    private final Method method;
    private final Object handler;
    private final Request request;
    private Response response;

    /**
     * Construct HttpMethodContext object.
     *
     * @param method   handler method
     * @param handler  object of the handler method
     * @param request  request object
     * @param response responder object
     */
    public HttpMethodContext(Method method, Object handler, Request request, Response response) {
        this.method = method;
        this.handler = handler;
        this.request = request;
        this.response = response;
    }

    /**
     * Get method.
     *
     * @return method to be invoked (could be http resource method or a method in the interceptor
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Get the object of the handler method.
     *
     * @return object of the handler method
     */
    public Object getHandler() {
        return handler;
    }

    /**
     * Get MSF4J request.
     *
     * @return MSF4J request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Get http responder.
     *
     * @return http responder
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Set http responder.
     *
     * @param response http responder
     */
    public void setResponse(Response response) {
        this.response = response;
    }
}
