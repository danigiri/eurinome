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
import java.util.function.Predicate;

import org.zeroturnaround.exec.ProcessExecutor;
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


public ExecReadyTask(int type, ProcessExecutor executor, Predicate<String> problemMatcher, ExecStartingTask startingTask, ExecRunningTask runningTask) {

		super(type, READY, null, problemMatcher);	// no matcher for ready tasks (hence the null)

		this.executor = executor;
		this.startingTask = startingTask;
		this.runningTask = runningTask;
		System.out.println("Ready, running obj="+runningTask.toString());

		startingTask.setRunningTask(runningTask);	// enforce the same instance

}


@Override
public StartingTask start() {

	try {

		// race condition here, stdout and stderr are to all intents and purposes being examined simultaneously, this
		// means that in the case of an error, we can have the following cases:
		// a)
		// starting:
		// STDIN (match)
		// STDERR (match)
		// markasFinished(starting)
		// markAsFailed(starting)
		// b)
		// starting:
		// STDIN (match)
		// markasFinished(starting)
		// running:
		// STDERR (match)
		// markAsFailed(running)
		// c)
		// starting:
		// STDERR (match)
		// STDIN (match)
		// markasFinished(starting)
		// running:
		// markAsFailed(running)
		// and a few other cases...
		// GUARD 1:
		// Therefore, if we fail in the started state we mark started as failed, but if we fail in running, we mark both
		// Also, we synchronize the setRemaning method, plus it cannot go up on value, so we ensure that we always set
		// startedTask to NEXT eventually. (Either the problem sets it to NEXT or it's marked complete and therefore
		// NEXT as well).
		// GUARD 2:
		// Moreover, status is untouched/undefined when erroring, so if we have:
		// a)
		// status=STARTED
		// isOK=FALSE
		// b)
		// isOK=false
		// (status is not changed and therefore undefined)
		// c)
		// isOK=FALSE
		// status=STARTED
		// we still maintain the invariant.
		// GUARD 3:
		// only setKO implemented, isOK is private, so isOK is never set to true, we cannot override with OK
		// GUARD 4:
		// TODO: FIXME: isOK() checks previous tasks so if any is not OK, we're not OK, as the most conservative
		//
		// In summary: 	a) remaining is sync and always goes down, cannot be overriden and go up
		//				b) ok variable cannot be set to true, cannot be overriden and go true
		//				c) if any of the tasks fails, we are marked as failed

		status = STARTING;
		executor.redirectOutput(new LogOutputStream() {
			
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
						if (isOneTime() && !runningTask.isDone()) {
							percent = runningTask.matchesOutput(line);
							runningTask.setRemaining(percent);
							// we do not take action as we expect the process to finish
						} else {
							
						}
						break;
				}

			}
			
		});

		ProcessExecutor preparedExecutor = executor.redirectError(new LogOutputStream() {

			@Override
			protected void processLine(String line) {
				System.err.println(line);
				boolean problem = false;
				switch(status) {
					case STARTING:
						if (startingTask.matchesProblem(line)) {
							startingTask.markAsFailed();
							problem = true;
						}
						break;
					case RUNNING:
						if (runningTask.matchesProblem(line)) {
							runningTask.markAsFailed();		// start is marked as failed as STDOUT logger could still
							problem = true;					// be running, so that's in undefined state
						}
						break;
				}
				// TODO: wait for process to exit
			}
		});

		startedProcess = preparedExecutor.start();
		startingTask.setProcess(startedProcess);
		runningTask.setProcess(startedProcess);
		startedProcess.getProcess().onExit().thenAccept(a -> runningTask.markAsFinished());
		System.out.println("Setting process="+startedProcess);
		
		return startingTask;

	} catch (IOException e) {
		throw new EurinomeRuntimeException("Had a problem starting the task", e);
	}

}



}
