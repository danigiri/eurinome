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

import cat.calidos.eurinome.runtime.api.Task;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecTask implements Task {

protected int type;
protected int status;
private Function<String, Integer> matcher;
private int remaining;
private Predicate<String> problemMatcher;
protected StartedProcess process;
protected StringBuilder output;
protected boolean isOK;

public ExecTask(int type, int status) {
	
	this.type = type;
	this.status = status;
	this.remaining = MAX;
	this.output = new StringBuilder();
	this.isOK = true;
}


public ExecTask(int type, int status, Function<String, Integer> matcher, Predicate<String> problemMatcher) {

	this(type, status);
	
	this.matcher = matcher;
	this.problemMatcher = problemMatcher;

}


public void setProcess(StartedProcess process) {
	this.process = process;
}


@Override
public int matchesOutput(String line) {
	return matcher.apply(line);
}


@Override
public boolean matchesProblem(String line) {
	return problemMatcher.test(line);
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
public void setRemaining(int percent) {
	remaining = Math.min(MAX, percent);
}


@Override
public int getRemaining() {
	return remaining;
}


@Override
public boolean isOK() {
	return isOK;
}


@Override
public boolean isDone() {
	return remaining<=Task.NEXT;
}


}
