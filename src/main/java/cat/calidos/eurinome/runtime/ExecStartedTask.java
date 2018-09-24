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

import org.zeroturnaround.exec.StartedProcess;

import cat.calidos.eurinome.runtime.api.RunningTask;
import cat.calidos.eurinome.runtime.api.StartingTask;
import cat.calidos.eurinome.runtime.api.StoppingTask;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecStartedTask extends ExecTask implements StartingTask {

private StartedProcess startedProcess;


public ExecStartedTask(int type) {
		super(type, STARTING);
}


public ExecStartedTask setStartedProcess(StartedProcess process){
	
	this.startedProcess = process;
	
	return this;
	
}

@Override
public int remaining() {

	// TODO Auto-generated method stub
	return 0;
}


public void finishedStarting() {

	// TODO Auto-generated method stub
	
}


@Override
public boolean hasFinishedStarting() {

	// TODO Auto-generated method stub
	return false;
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
