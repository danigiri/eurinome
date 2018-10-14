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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;
import org.zeroturnaround.process.JavaProcess;
import org.zeroturnaround.process.ProcessUtil;
import org.zeroturnaround.process.Processes;
import org.zeroturnaround.process.SystemProcess;

import cat.calidos.eurinome.problems.EurinomeRuntimeException;
import cat.calidos.eurinome.runtime.api.FinishedTask;
import cat.calidos.eurinome.runtime.api.RunningTask;
import cat.calidos.eurinome.runtime.api.StartingTask;
import cat.calidos.eurinome.runtime.api.StoppingTask;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecStartingTask extends ExecTask implements StartingTask {

private BiConsumer<ExecStartingTask, ExecRunningTask> startedCallback;
private ExecRunningTask runningTask;
private ExecStoppingTask stoppingTask;
private ExecFinishedTask finishedTask;


public ExecStartingTask(int type, 
						ProcessExecutor executor,
						StartingOutputProcessor outputProcessor, 
						ExecProblemProcessor problemProcessor,
						BiConsumer<ExecStartingTask, ExecRunningTask> startedCallback,
						ExecRunningTask runningTask,
						ExecStoppingTask stoppingTask,
						ExecFinishedTask finishedTask) {

	super(type, RUNNING, executor, outputProcessor, problemProcessor);

	this.startedCallback = startedCallback;
	this.runningTask = runningTask;
	this.stoppingTask = stoppingTask;
	this.finishedTask = finishedTask;
	
	outputProcessor.setTask(this); 	// the processors need to mark us as NEXT or failed, so they need a ref to this
	problemProcessor.setTask(this);	// specific instance

}


public void setRunningTask(ExecRunningTask runningTask) {
	this.runningTask = runningTask;	//HACK: this is a hack as we are not creating singletons of running task in dagger
}


@Override
public RunningTask runningTask() throws IllegalStateException {
	return runningTask;
}


public StoppingTask stop() throws EurinomeRuntimeException {

	status = STOPPED;
//	try {
//		int pid = (int) startedProcess.getProcess().pid();
//		SystemProcess systemprocess = Processes.newPidProcess(pid);
//		ProcessUtil.destroyGracefullyAndWait(systemprocess, 5, TimeUnit.MILLISECONDS);	//FIXME: this random
//	} catch (IOException | InterruptedException | TimeoutException e) {
//		throw new EurinomeRuntimeException("Problem destroying gracefully process", e);
//	}

	return stoppingTask;

}


@Override
public RunningTask markAsStarted() {
	
	System.out.println("STARTING: Mark as started");
	runningTask.startRedirectingOutput();
	status = STARTED;
	setRemaining(NEXT);
	
	startedCallback.accept(this, runningTask);

	return runningTask;

}


@Override
public FinishedTask markAsFailed() {

	setKO();
	setRemaining(NEXT);

	return finishedTask();

}


@Override
public FinishedTask finishedTask() {
	return finishedTask;
}

}
