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

package cat.calidos.eurinome.runtime.api;

import java.util.function.Function;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public interface Task extends Showable {

public static int IDLE = 1001;
public static int STARTING = 1002;
public static int RUNNING = 1003;
public static int STOPPING = 1004;
public static int FINISHED = 1005;

public static int COMPLETED = 0;
public static int MAX_REMAINING = 100;

public static int ONE_TIME = 1000;
public static int LONG_RUNNING = 1100;


/**	@return the current output matching function, given a log line, it returns how much is remaining in the current state,
* 	or negative if there is an error or some kind of problem. The remaining time is undefined for long running tasks.
* 	Values are from 100 to 1 for percentage, 0 for stage completed and negative for errors
*/
public Function<String, Integer> outputMatcher();


/**	@return */
public int status();


/**	@return */
public int type();


public void setRemaining(int percent);


public int getRemaining();


public boolean isDone();


}
