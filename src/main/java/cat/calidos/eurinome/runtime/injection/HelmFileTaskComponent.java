package cat.calidos.eurinome.runtime.injection;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;

import cat.calidos.eurinome.runtime.api.ReadyTask;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules=HelmfileTaskModule.class)
public interface HelmfileTaskComponent {

ReadyTask task();
String command();


@Component.Builder
interface Builder {

	@BindsInstance Builder withFile(@Named("Path") String path);
	@BindsInstance Builder run(@Named("Command") String command);
	@BindsInstance Builder helmfileBinaryAt(@Nullable @Named("HelmfileBinary") String path);
	HelmfileTaskComponent build();

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
