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

package org.wso2.msf4j.examples.petstore.security;

import org.wso2.carbon.ldap.server.util.EmbeddingLDAPException;
import org.wso2.msf4j.MicroservicesRunner;
import org.wso2.msf4j.analytics.httpmonitoring.HTTPMonitoringInterceptor;
import org.wso2.msf4j.analytics.metrics.MetricsInterceptor;
import org.wso2.msf4j.examples.petstore.security.ldap.server.ApacheDirectoryServerActivator;

import java.io.IOException;

/**
 * Microservice runner for the userAuthentication microservice.
 */
public class Runner {

    private Runner() {
    }

    public static void main(String[] args) throws IOException, EmbeddingLDAPException {
        ApacheDirectoryServerActivator apacheDS = new ApacheDirectoryServerActivator();
        apacheDS.start();
        HTTPMonitoringInterceptor httpMonitoringInterceptor = new HTTPMonitoringInterceptor();
        MetricsInterceptor MetricsInterceptor = new MetricsInterceptor();
        new MicroservicesRunner()
                .addGlobalRequestInterceptor(httpMonitoringInterceptor, MetricsInterceptor)
                .addGlobalResponseInterceptor(httpMonitoringInterceptor, MetricsInterceptor)
                .deploy(new UserAuthenticationService())
                .start();
    }
}
