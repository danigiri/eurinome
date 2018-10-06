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
import java.util.function.Predicate;

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.eurinome.runtime.ExecFinishedTask;
import cat.calidos.eurinome.runtime.ExecRunningTask;
import cat.calidos.eurinome.runtime.ExecStartingTask;
import cat.calidos.eurinome.runtime.api.ReadyTask;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules={ExecTaskModule.class})
public interface ExecTaskComponent {

ReadyTask readyTask();


@Component.Builder
interface Builder {

	@BindsInstance Builder type(@Named("Type") int type);
	@BindsInstance Builder exec(@Named("Path") String... command);
	@BindsInstance Builder startedMatcher(@Named("StartedMatcher") Function<String, Integer> matcher);
	@BindsInstance Builder startedCallback(BiConsumer<ExecStartingTask, ExecRunningTask> callback);
	@BindsInstance Builder runningMatcher(@Named("RunningMatcher") Function<String, Integer> matcher);
	@BindsInstance Builder finishedCallback(BiConsumer<ExecRunningTask, ExecFinishedTask> callback);
	@BindsInstance Builder problemMatcher(@Named("ProblemMatcher") Predicate<String> problemMatcher);

	ExecTaskComponent build();

}

}
