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

import org.zeroturnaround.exec.ProcessExecutor;

import cat.calidos.eurinome.problems.EurinomeRuntimeException;
import cat.calidos.eurinome.runtime.api.FinishedTask;
import cat.calidos.eurinome.runtime.api.StoppingTask;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecStoppingTask extends ExecTask implements StoppingTask {

private ExecFinishedTask finishedTask;


public ExecStoppingTask(int type, 
		ProcessExecutor executor,
		ExecOutputProcessor logMatcher, 
		ExecProblemProcessor problemMatcher,
		ExecFinishedTask finishedTask) {

	super(type, STARTING, executor, logMatcher, problemMatcher);

	this.finishedTask = finishedTask;
	
	//problemMatcher.setTask(this);
	
}

@Override
public FinishedTask finishedTask() throws IllegalStateException {
	return finishedTask;
}

@Override
public StoppingTask stop() throws EurinomeRuntimeException {

	// TODO Auto-generated method stub
	return null;
}

@Override
public FinishedTask markAsFailed() {

	// TODO Auto-generated method stub
	return null;
}

}
