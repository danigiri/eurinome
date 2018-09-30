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

import javax.inject.Named;

import org.zeroturnaround.exec.ProcessExecutor;

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
					ExecStartingTask startingTask,
					BiConsumer<ExecRunningTask, String> startedCallback,
					ExecRunningTask runningTask) {
	return new ExecReadyTask(type, executor, startingTask, startedCallback, runningTask);
}


@Provides
ProcessExecutor executor(@Named("Path") String path) {
	return new ProcessExecutor().command(path);
}

@Provides
ExecStartingTask startingTask() {
	
}

}