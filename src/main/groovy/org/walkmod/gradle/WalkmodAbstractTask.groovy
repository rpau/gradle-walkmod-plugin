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

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.walkmod.OptionsBuilder
import org.walkmod.WalkModFacade
import org.walkmod.conf.ConfigurationProvider
import org.walkmod.conf.entities.impl.ConfigurationImpl
import org.walkmod.gradle.configuration.GradleConfigurationProvider


/**
 * Base abstract Walkmod task 
 * @author abelsromero
 */
abstract class WalkmodAbstractTask extends DefaultTask {

	@Optional
	protected List<String> chains

	@Optional
	protected Boolean offline

	@Optional
	protected Boolean verbose

	@Optional
	protected Boolean printErrors

	@Optional
	protected File configFile

	/**
	 * Delegated walkmod service  
	 */
	WalkmodProxy walkmodProxy
	final WalkmodExtension extension

	Configuration configuration
	static ClassLoader cl

	/* Unused
	private static final boolean IS_WINDOWS = System.getProperty('os.name').contains('Windows')

	private static final String DOUBLE_BACKLASH = '\\\\'
	private static final String BACKLASH = '\\'
	
	private static String normalizePath(String path) {
		if (IS_WINDOWS) {
			path = path.replace(DOUBLE_BACKLASH, BACKLASH)
			path = path.replace(BACKLASH, DOUBLE_BACKLASH)
		}
		return path
	}
	*/

    protected WalkmodAbstractTask () {
        if (!extension) {
            extension = project.extensions.getByName(WalkmodPlugin.EXTENSION)
        }
    }

    @TaskAction
    void executeTask() {
		validateInputs()
        initWalkmod()

        executeTask(chains)

        if (!walkmodProxy) {
            initWalkmod()
        }
    }

	abstract void executeTask(String... chains)

	/**
	 * Validates input values. If an input value is not valid an exception is thrown.
	 */
	private void validateInputs() {
        // setupClassLoader()
	}

	void initWalkmod() {
		if (!walkmodProxy) {
			logger.lifecycle("Creating facade config:$configFile o:$offline v:$verbose e:$printErrors")

            ConfigurationProvider confProvider = new GradleConfigurationProvider(new ConfigurationImpl())
            confProvider.defaultClassloader = project.getExtensions().findByName('walkmod').get
            confProvider.artifacts = project.configurations.getByName('walkmod').artifacts

            OptionsBuilder options = OptionsBuilder.options().offline(offline).verbose(verbose).printErrors(printErrors)

            project.configurations.getByName('walkmod').artifacts.each {
                println "$it"
            }

			walkmodProxy = new WalkmodProxyImpl(delegate: new WalkModFacade(configFile, options, confProvider))
		}
	}

    List<String> getChains() {
        chains ?: extension.chains
    }

    Object getOffline() {
        offline ?: extension.offline
    }

    Object getVerbose() {
        verbose ?: extension.verbose
    }

    boolean isShowErrors() {
		printErrors ?: extension.printErrors
    }

	File getConfigFile() {
        configFile ?: extension.configFile
    }

    Configuration getConfiguration() {
        configuration
    }

}
