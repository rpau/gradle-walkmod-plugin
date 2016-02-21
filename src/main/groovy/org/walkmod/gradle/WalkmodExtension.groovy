/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.walkmod.gradle

import org.gradle.api.Project


/**
 * @author abelsromero
 */
class WalkmodExtension {

    String version = '2.2.0'
	
	List<String> chains = null

	Boolean offline = Boolean.FALSE

	Boolean verbose = Boolean.TRUE

	Boolean printErrors = Boolean.TRUE
	
	File configFile = new File('walkmod.xml')

    Project project

    WalkmodExtension(Project project) {
        this.project = project
    }
}
