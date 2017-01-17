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
package org.wso2.msf4j.samples.deployablejarinterceptorservice.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;
import org.wso2.msf4j.interceptor.MSF4JResponseInterceptor;

/**
 * Interceptor demonstrating getting a property.
 */
public class PropertyGetResponseInterceptor implements MSF4JResponseInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PropertyGetResponseInterceptor.class);

    @Override
    public boolean interceptResponse(Request request, Response response) throws Exception {
        String propertyName = "SampleProperty";
        String property = (String) request.getProperty(propertyName);
        log.info("Value of property {} is {} ", propertyName, property);
        return true;
    }
}
