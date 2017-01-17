package org.wso2.msf4j.internal.router;
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

/**
 * This class is used to track the sub-resources.
 *
 */
public class SubresourceKey {
    private Class<?> typedClass;
    private String path;
    private String httpMethod;


    public SubresourceKey(String path, Class<?> tClass, String httpMethod) {
        this.path = path;
        typedClass = tClass;
        this.httpMethod = httpMethod;
    }

    /**
     * Get the sub-resource method type.
     *
     * @return Class get the type of the sub-resource
     */
    public Class<?> getTypedClass() {
        return typedClass;
    }

    /**
     * Get sub-resource absolute path.
     *
     * @return String absolute path of the sub-resource
     */
    public String getPath() {
        return path;
    }

    /**
     * Get method's http verb.
     *
     * @return available http method
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubresourceKey)) {
            return false;
        }
        SubresourceKey other = (SubresourceKey) o;
        return path.equals(other.path) && typedClass == other.typedClass &&
               httpMethod.equals(((SubresourceKey) o).httpMethod);
    }

    @Override
    public int hashCode() {
        return typedClass.hashCode() + 37 * path.hashCode();
    }
}
