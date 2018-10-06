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


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecFinishedTask extends ExecTask implements FinishedTask {


public ExecFinishedTask(int type, 
						Predicate<String> problemMatcher) {
	super(type, FINISHED, (s) -> NEXT, problemMatcher);
}


@Override
public boolean wasOk() {
	return result()==0;
}


@Override
public int result() {
	return process.getProcess().exitValue();
}

}
