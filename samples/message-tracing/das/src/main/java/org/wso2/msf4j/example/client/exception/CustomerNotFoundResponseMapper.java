/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.msf4j.example.client.exception;

import org.wso2.msf4j.client.codec.RestErrorResponseMapper;

/**
 * RestErrorResponseMapper which maps REST service response error to CustomerNotFoundRestServiceException
 */
public class CustomerNotFoundResponseMapper extends RestErrorResponseMapper<CustomerNotFoundRestServiceException> {

    public static final String ERROR_CODE = "30001";

    @Override
    public String getExceptionKey() {
        return ERROR_CODE;
    }

    @Override
    public Class<CustomerNotFoundRestServiceException> getExceptionClass() {
        return CustomerNotFoundRestServiceException.class;
    }
}
