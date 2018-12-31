package cat.calidos.eurinome.runtime.injection;

import javax.annotation.Nullable;
import javax.inject.Named;

import cat.calidos.eurinome.runtime.api.ReadyTask;
import cat.calidos.eurinome.runtime.injection.ExecTaskComponent.Builder;
import dagger.BindsInstance;
import dagger.Component;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules=HelmfileTaskModule.class)
public interface HelmfileTaskComponent {

ReadyTask helmfileTask();


@Component.Builder
interface Builder {

	@BindsInstance Builder with(@Named("Path") String path);
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

