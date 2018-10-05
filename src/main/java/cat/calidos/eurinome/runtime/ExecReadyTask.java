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

package cat.calidos.eurinome.runtime;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;
import org.zeroturnaround.exec.stream.LogOutputStream;

import cat.calidos.eurinome.problems.EurinomeRuntimeException;
import cat.calidos.eurinome.runtime.api.ReadyTask;
import cat.calidos.eurinome.runtime.api.StartingTask;
import cat.calidos.eurinome.runtime.api.Task;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecReadyTask extends ExecTask implements ReadyTask {

private ProcessExecutor executor;
protected ExecStartingTask startingTask;
private ExecRunningTask runningTask;
private Process process;


public ExecReadyTask(int type, ProcessExecutor executor, ExecStartingTask startingTask, ExecRunningTask runningTask) {

		super(type, READY);

		this.executor = executor;
		this.startingTask = startingTask;
		this.runningTask = runningTask;
		System.out.println("Ready, running obj="+runningTask.toString());

		startingTask.setRunningTask(runningTask);	// enforce the same instance

}


@Override
public StartingTask start() {

	try {

		status = STARTING;
		ProcessExecutor preparedExecutor = executor.redirectOutput(new LogOutputStream() {
			@Override
			protected void processLine(String line) {
				System.out.println(line);
				int percent = Task.MAX;
				switch(status) {
					case STARTING:
						startingTask.output.append(line);
						percent = startingTask.matchesOutput(line);
						startingTask.setRemaining(percent);
						if (startingTask.isDone()) {
							System.out.println("Task is started, running callback");
							startingTask.markAsStarted();
							status = RUNNING;
						}
						break;
					case RUNNING:
						runningTask.output.append(line);
						if (isOneTime() && runningTask.isDone()) {
							percent = runningTask.matchesOutput(line);
							runningTask.setRemaining(percent);
							//TODO: decide what to do if the actual process is not done yet
						} else {
							
						}
						break;
				}

			}
		});

		process = preparedExecutor.start().getProcess();
		process.onExit().thenAccept(a -> runningTask.markAsFinished());
		startingTask.setProcess(process);
		
		return startingTask;

	} catch (IOException e) {
		throw new EurinomeRuntimeException("Had a problem starting the task", e);
	}

}



}
