package cat.calidos.eurinome.runtime.injection;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.Task;
import cat.calidos.morfeu.runtime.injection.DaggerExecTaskComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class HelmfileTaskModule {

private static final String HELMFILE_BINARY = "/usr/local/bin/helmfile";


@Provides
ReadyTask helmfileTask(String command) {

	return DaggerExecTaskComponent.builder()
									.exec("/bin/bash", "-c", command)
									.startedMatcher(s -> Task.NEXT)
									.type(Task.ONE_TIME)
									.problemMatcher(s -> true)
									.build()
									.readyTask();

}


@Provides
String command(@Named("EffectiveHelm") String effectiveHelmBinaryPath, 
				@Named("Command") String command,
				@Named("Path") String path) {
	return effectiveHelmBinaryPath+" -q --file '"+path+"' "+command;
}


@Provides @Named("EffectiveHelm") 
String effectiveHelmBinaryPath(@Nullable @Named("HelmfileBinary") String path) {
	return path==null ? HELMFILE_BINARY : path;
}


}


/*
 *    Copyright 2018 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

