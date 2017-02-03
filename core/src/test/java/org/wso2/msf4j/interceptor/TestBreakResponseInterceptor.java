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

import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test response interceptor for fat jar mode.
 */
public class TestBreakResponseInterceptor implements MSF4JResponseInterceptor {

    private static AtomicInteger filterCalls = new AtomicInteger(0);

    /**
     * Reset interceptor call count.
     */
    public static void reset() {
        filterCalls.set(0);
    }

    /**
     * Get the number of interceptor calls.
     *
     * @return number of calls
     */
    public static int getFilterCalls() {
        return filterCalls.get();
    }

    @Override
    public boolean interceptResponse(Request request, Response response) throws Exception {
        filterCalls.incrementAndGet();
        return false;
    }
}
