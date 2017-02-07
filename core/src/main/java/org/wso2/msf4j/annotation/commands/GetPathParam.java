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
package org.wso2.msf4j.annotation.commands;

import org.wso2.msf4j.annotation.Command;
import org.wso2.msf4j.internal.router.HttpResourceModel;

import java.util.Map;
import java.util.Objects;
import javax.ws.rs.PathParam;

/**
 * Command to obtain the path param.
 */
// TODO: Introduce command patter if applicable
public class GetPathParam implements Command {

    private HttpResourceModel.ParameterInfo<String> info;
    private Map<String, String> groupValues;

    public GetPathParam(HttpResourceModel.ParameterInfo<String> info, Map<String, String> groupValues) {
        this.info = info;
        this.groupValues = groupValues;
    }

    @Override
    public Object execute() {
        return getPathParamValue(info, groupValues);
    }

    /**
     * Get the path param value.
     *
     * @param info        parameter information
     * @param groupValues group values
     * @return path parameter value
     */
    private Object getPathParamValue(HttpResourceModel.ParameterInfo<String> info, Map<String, String> groupValues) {
        PathParam pathParam = info.getAnnotation();
        String value = groupValues.get(pathParam.value());
        if (value == null) {
            String defaultVal = info.getDefaultVal();
            if (defaultVal != null) {
                value = defaultVal;
            }
        }
        Objects.requireNonNull(value, String.format("Could not resolve value for parameter %s", pathParam.value()));
        return info.convert(value);
    }
}
