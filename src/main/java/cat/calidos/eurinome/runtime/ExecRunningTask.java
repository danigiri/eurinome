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

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import cat.calidos.eurinome.runtime.api.FinishedTask;
import cat.calidos.eurinome.runtime.api.RunningTask;
import cat.calidos.eurinome.runtime.api.StoppingTask;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecRunningTask extends ExecTask implements RunningTask {

private ExecFinishedTask finishedTask;
private BiConsumer<ExecRunningTask, ExecFinishedTask> finishedCallBack;

public ExecRunningTask(int type, 
						Function<String, Integer> matcher, 
						Predicate<String> problemMatcher, 
						ExecFinishedTask finishedTask,
						BiConsumer<ExecRunningTask, ExecFinishedTask> finishedCallBack) {
	
	super(type, RUNNING, matcher, problemMatcher);
	
	this.finishedTask = finishedTask;
	this.finishedCallBack = finishedCallBack;
	
}


@Override
public StoppingTask stop() {

	// TODO Auto-generated method stub
	return null;
}


@Override
public FinishedTask markAsFinished() {

	System.out.println("Mark as finished process="+startedProcess);
	finishedTask.setProcess(startedProcess);
	status = FINISHED;
	setRemaining(NEXT);
	
	finishedCallBack.accept(this, finishedTask);
	
	return finishedTask;
}

@Override
public FinishedTask markAsFailed() {

	finishedTask.setProcess(startedProcess);
	setKO();
	setRemaining(NEXT);
	
	return finishedTask();

}


@Override
public FinishedTask finishedTask() {
	return finishedTask;
}


}
