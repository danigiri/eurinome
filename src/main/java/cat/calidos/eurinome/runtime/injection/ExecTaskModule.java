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

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;

import org.zeroturnaround.exec.ProcessExecutor;

import cat.calidos.eurinome.runtime.ExecFinishedTask;
import cat.calidos.eurinome.runtime.ExecReadyTask;
import cat.calidos.eurinome.runtime.ExecRunningTask;
import cat.calidos.eurinome.runtime.ExecStartingTask;
import cat.calidos.eurinome.runtime.api.ReadyTask;
import cat.calidos.eurinome.runtime.api.Task;
import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ExecTaskModule {


@Provides
ReadyTask readyTask(@Named("Type") int type,
					ProcessExecutor executor,
					@Named("ProblemMatcher") Predicate<String> problemMatcher,
					ExecStartingTask startingTask,
					ExecRunningTask runningTask) {
	return new ExecReadyTask(type, executor, problemMatcher, startingTask, runningTask);
}


@Provides @Singleton
ProcessExecutor executor(@Named("Path") String... command) {
	return new ProcessExecutor().command(command);
}


@Provides @Singleton
ExecStartingTask startingTask(@Named("Type") int type, 
								@Named("StartedMatcher") Function<String, Integer> matcher,
								@Named("ProblemMatcher") Predicate<String> problemMatcher,
								@Named("StartedCallback") BiConsumer<ExecStartingTask, ExecRunningTask> startedCallback,
								ExecRunningTask runningTask,
								ExecFinishedTask finishedTask) {
	return new ExecStartingTask(type, matcher, problemMatcher, startedCallback, runningTask, finishedTask);
}


//TODO: this should be a singleton as new instances are being created
@Provides @Singleton
ExecRunningTask runningTask(@Named("Type") int type,
							@Named("EffectiveRunningMatcher") Function<String, Integer> matcher,
							@Named("ProblemMatcher") Predicate<String> problemMatcher,
							ExecFinishedTask finishedTask,
							@Named("FinishedCallback") BiConsumer<ExecRunningTask, ExecFinishedTask> callback) {
	
	final  ExecRunningTask runningTask = new ExecRunningTask(type, matcher, problemMatcher, finishedTask, callback);
	
	return runningTask;
}


@Provides @Singleton
ExecFinishedTask finishedTask(@Named("Type") int type, @Named("ProblemMatcher") Predicate<String> problemMatcher) {
	return new ExecFinishedTask(type, problemMatcher);
}


@Provides @Named("StartedCallback") 
BiConsumer<ExecStartingTask, ExecRunningTask> startedCallback(
												@Nullable BiConsumer<ExecStartingTask, ExecRunningTask> callback) {
	return (callback==null)? (s, r) -> {} : callback;
}


@Provides @Named("EffectiveRunningMatcher") 
Function<String, Integer> matcher(@Nullable @Named("RunningMatcher") Function<String, Integer> matcher) {
	return (matcher==null)? s -> Task.MAX : matcher;	// default matcher is progress is always 100%, wait for process
}


@Provides @Named("FinishedCallback") 
BiConsumer<ExecRunningTask, ExecFinishedTask> finishedCallback(
												@Nullable BiConsumer<ExecRunningTask, ExecFinishedTask> callback) {
	return (callback==null)? (s, r) -> {} : callback;
}

}
