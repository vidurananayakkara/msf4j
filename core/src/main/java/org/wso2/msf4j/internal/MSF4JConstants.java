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

import org.wso2.msf4j.context.EmptyContextProvider;

/**
 * MSF4J Constants.
 */
public class MSF4JConstants {

    public static final String SESSION_ID = "JSESSIONID=";
    public static final String CHANNEL_ID = "CHANNEL_ID";

    // Property constants
    public static final String METHOD_PROPERTY_NAME = "method";
    public static final String EMPTY_CONTEXT_STRING = EmptyContextProvider.class.getSimpleName();
    public static final String CONTEXT_SEPARATOR = "@";

    private MSF4JConstants() {
    }
}
