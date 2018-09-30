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

package cat.calidos.eurinome.runtime.injection;

import dagger.BindsInstance;
import dagger.Component;
import dagger.producers.ProductionComponent;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.eurinome.runtime.ExecRunningTask;
import cat.calidos.eurinome.runtime.api.ReadyTask;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules={ExecTaskModule.class})
public interface ExecTaskComponent {


ReadyTask readyTask();


@Component.Builder
interface Builder {

	@BindsInstance Builder path(@Named("Path") String path);
	@BindsInstance Builder startedMatcher(@Named("StartedMatcher") Function<String, Integer> matcher);
	@BindsInstance Builder startedCallback(@Named("StartedMatcher") BiConsumer<ExecRunningTask, String> callback);

	ExecTaskComponent build();

}

}
