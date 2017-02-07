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

import org.wso2.msf4j.internal.MSF4JConstants;

import java.util.Objects;

/**
 * Provider of custom contexts representing the current request
 *
 * @param <T> Context class
 */
public abstract class ContextProvider<T> {

    private final Class<T> type;
    private final String contextName;

    public ContextProvider(Class<T> type) {
        this(type, type.getName());
    }

    public ContextProvider(Class<T> type, String contextName) {
        this.type = type;
        this.contextName = type.getName() + MSF4JConstants.CONTEXT_SEPARATOR + contextName;
    }

    /**
     * Get the name of the context.
     *
     * @return context name
     */
    public final String getContextName() {
        return contextName;
    }

    /**
     * Creates the context instance
     *
     * @param httpMethodContext http method context.
     * @return the context
     */
    public abstract T createContext(HttpMethodContext httpMethodContext);

    /**
     * Execute when removing context provider.
     * Do memory clean up operations here
     *
     * @param params parameters
     */
    public void onRemoveContextProvider(Object... params) {
        // By default do nothing
    }

    @Override
    public final boolean equals(Object object) {
        if (object == null || !(object instanceof ContextProvider)) {
            return false;
        }
        final ContextProvider other = (ContextProvider) object;
        return this.contextName.equals(other.contextName) && this.type.equals(other.type);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(contextName, type);
    }
}
