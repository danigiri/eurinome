package cat.calidos.eurinome.runtime.injection;

import javax.annotation.Nullable;
import javax.inject.Named;

import cat.calidos.eurinome.runtime.api.ReadyTask;
import cat.calidos.eurinome.runtime.api.Task;
import dagger.Module;
import dagger.Provides;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class HelmfileTaskModule {

private static final String HELMFILE = "/usr/local/bin/helmfile";

@Provides
ReadyTask helmfileTask(@Named("EffectiveHelm") String effectiveHelmBinaryPath, @Named("Path") String path) {

	return DaggerExecTaskComponent.builder()
									.exec("/bin/bash", "-c","/usr/local/bin/helmfile apply --file '"+path+"'")
									.type(Task.ONE_TIME)
									//.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
									.runningMatcher(s -> Integer.parseInt(s))
									//.finishedCallback((r, f) -> {finishedCallbackCalled = true;})
									.problemMatcher(s -> true)
									.build()
									.readyTask();
}


@Provides @Named("EffectiveHelm") 
String effectiveHelmBinaryPath(@Nullable @Named("HelmfileBinary") String path) {
	return path==null ? HELMFILE : path;
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

