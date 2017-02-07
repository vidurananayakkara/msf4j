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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.internal.MSF4JConstants;

/**
 * Empty context provider to handle null pointer exceptions in early stages.
 */
public final class EmptyContextProvider extends ContextProvider<Object> {

    private static final Logger log = LoggerFactory.getLogger(EmptyContextProvider.class);

    public EmptyContextProvider() {
        super(Object.class, MSF4JConstants.EMPTY_CONTEXT_STRING);
    }

    @Override
    public Object createContext(HttpMethodContext httpMethodContext) {
        log.warn("Context for the given value type not found");
        return null;
    }
}
