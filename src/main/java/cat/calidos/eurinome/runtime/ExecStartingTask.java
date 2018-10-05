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

import java.util.function.Function;
import java.util.function.Predicate;

import org.zeroturnaround.exec.StartedProcess;

import cat.calidos.eurinome.runtime.api.RunningTask;
import cat.calidos.eurinome.runtime.api.StartingTask;
import cat.calidos.eurinome.runtime.api.StoppingTask;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecStartingTask extends ExecTask implements StartingTask {

private StartedProcess startedProcess;


public ExecStartingTask(int type, Function<String, Integer> matcher, Predicate<String> problemMatcher) {

	super(type, STARTING, matcher, problemMatcher);
	
}


public ExecTask setStartedProcess(StartedProcess process){
	
	this.startedProcess = process;
	
	return this;
	
}


@Override
public RunningTask runningTask() throws IllegalStateException {

	// TODO Auto-generated method stub
	return null;
}


@Override
public StoppingTask cancel() {

	// TODO Auto-generated method stub
	return null;
}

}
