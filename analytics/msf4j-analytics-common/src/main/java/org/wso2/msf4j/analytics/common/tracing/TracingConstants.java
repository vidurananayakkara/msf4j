/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

package org.wso2.msf4j.analytics.common.tracing;

/**
 * Class to hold constants used in tracing.
 */
public class TracingConstants {

    public static final String CLIENT_TRACE_START = "CTS";
    public static final String CLIENT_TRACE_END = "CTE";
    public static final String SERVER_TRACE_START = "STS";
    public static final String SERVER_TRACE_END = "STE";
    public static final String TRACE_ID_HEADER = "X-msf4j-trace-id";
    public static final String TRACE_ORIGIN_ID_HEADER = "X-msf4j-trace-origin-id";
    public static final String DAS_RECEIVER_URL = "http://localhost:9763/endpoints/msf4jtracereceiver";
    public static final String DEFAULT_ZIPKIN_URL = "http://0.0.0.0:9411";

    /**
     * Tracing Types.
     */
    public enum TracingType {
        DAS, ZIPKIN
    }

}
