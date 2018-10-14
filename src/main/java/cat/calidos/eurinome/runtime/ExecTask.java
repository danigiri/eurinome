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
import org.zeroturnaround.exec.StartedProcess;
import org.zeroturnaround.exec.stream.LogOutputStream;
import org.zeroturnaround.process.JavaProcess;
import org.zeroturnaround.process.Processes;

import cat.calidos.eurinome.runtime.api.Task;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecTask implements Task {

protected int type;
protected int status;
protected ProcessExecutor executor;

protected ExecOutputProcessor logMatcher;
protected ExecProblemProcessor problemMatcher;
protected StringBuilder output;

private int remaining = MAX;	// by default we have everything left to do
private boolean isOK = true;


public ExecTask(int type, int status, ProcessExecutor executor) {
	
	this.type = type;
	this.status = status;
	this.executor = executor;
	
}

public ExecTask(int type, int status, ProcessExecutor executor, ExecOutputProcessor logMatcher, ExecProblemProcessor problemMatcher) {
	
	this(type, status, executor);
	
	this.logMatcher = logMatcher;
	this.problemMatcher = problemMatcher;
	
	this.remaining = MAX;
	this.output = new StringBuilder();

}


public void startRedirectingOutput() {

	executor.redirectError(problemMatcher);
	executor.redirectOutput(logMatcher);

}

public void stopRedirectingOutput() {
	
	executor.redirectError(null);
	executor.redirectOutput(null);

}


@Override
public String show() {
	return output.toString();
}


@Override
public int getStatus() {
	return status;
}


@Override
public int getType() {
	return type;
}


@Override
public synchronized void setRemaining(int percent) {	// we have the STDOUT logger and STDEER logger changing this
	remaining = Math.min(Math.min(MAX, percent), remaining); // we never go up
}


@Override
public int getRemaining() {
	return remaining;
}

public void setKO() {
	isOK = false;
}

@Override
public boolean isOK() {
	return isOK;
}


}
