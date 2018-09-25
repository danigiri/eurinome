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

import cat.calidos.eurinome.runtime.api.ReadyTask;
import cat.calidos.eurinome.runtime.api.StartingTask;
import cat.calidos.eurinome.runtime.api.Task;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecTask implements Task {

protected int type;
protected int status;
private Function<String, Integer> matcher;
private int remaining;

public ExecTask(int type, int status) {
	
	this.type = type;
	this.status = status;
	this.remaining = MAX_REMAINING;
}


public ExecTask(int type, int status, Function<String, Integer> matcher) {

	this(type, status);
	
	this.matcher = matcher;

}


@Override
public String show() {

	// TODO Auto-generated method stub
	return null;
}


@Override
public int status() {
	return status;
}


@Override
public int type() {
	return type;
}


@Override
public void setRemaining(int percent) {

	if (percent>MAX_REMAINING) {
		throw new IndexOutOfBoundsException("");
	}
	
	
	
}


@Override
public Function<String, Integer> outputMatcher() {
	return matcher;
}


@Override
public int getRemaining() {
	return remaining;
}


@Override
public boolean isDone() {
	return remaining<=0;
}


}
