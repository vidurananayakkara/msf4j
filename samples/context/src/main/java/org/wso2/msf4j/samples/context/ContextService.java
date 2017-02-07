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
package org.wso2.msf4j.samples.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.context.annotation.MSF4JContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Context example micro-service class.
 */
@Path("/context-service")
public class ContextService {

    private static final Logger log = LoggerFactory.getLogger(ContextService.class);

    @MSF4JContext
    String value;

    /**
     * Method for setting a sample context.
     * curl http://localhost:8081/context-service/set-context
     *
     * @return success message
     */
    @GET
    @Path("/set-context")
    public String setContext(@MSF4JContext String n, @MSF4JContext String m) {
        log.info("HTTP Method Execution - setContext()");
        return "Context set to " + value + " - " + n + " - " + m;
    }
}
