package org.wso2.msf4j.example;/*
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

import feign.RequestLine;

public interface TraceableServiceInterface {

    @RequestLine("GET /service/aaaa")
    String aaaaEndpoint();

    @RequestLine("GET /service/bbbb")
    String bbbbEndpoint();

    @RequestLine("GET /service/cccc")
    String ccccEndpoint();

    @RequestLine("GET /service/dddd")
    String ddddEndpoint();

    @RequestLine("GET /service/eeee")
    String eeeeEndpoint();

    @RequestLine("GET /service/ffff")
    String ffffEndpoint();

    @RequestLine("GET /service/gggg")
    String ggggEndpoint();
}
