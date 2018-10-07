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

package cat.calidos.eruinome.runtime.injection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.eurinome.runtime.api.FinishedTask;
import cat.calidos.eurinome.runtime.api.ReadyTask;
import cat.calidos.eurinome.runtime.api.RunningTask;
import cat.calidos.eurinome.runtime.api.StartingTask;
import cat.calidos.eurinome.runtime.api.Task;
import cat.calidos.eurinome.runtime.injection.DaggerExecTaskComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecTaskComponentIntTest {


@Test @DisplayName("Execute a simple onetime task (IntTest)")
public void testOneTimeExecSimpleTask() {

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'hello world'")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("hello world") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> false)
												.build()
												.readyTask();
	assertFalse(task.isDone());
	assertEquals(Task.READY, task.getStatus());
	
	StartingTask start = task.start();
	start.blockUntil(Task.STARTED);
	assertEquals("hello world", start.show());
	
	RunningTask runningTask = start.runningTask();
	runningTask.blockUntil(Task.FINISHED);
	
	FinishedTask finishedTask = runningTask.finishedTask();
	assertTrue(finishedTask.wasOk());
	assertEquals(0, finishedTask.result());

}

@Test @DisplayName("Execute a complex onetime task (IntTest)")
public void testOneTimeExecComplexTask() {
	
	ReadyTask task = DaggerExecTaskComponent.builder()
			.exec( "/bin/bash", "-c", "echo 'hello world'")
			.type(Task.ONE_TIME)
			.startedMatcher(s -> s.equals("hello world")? Task.NEXT : Task.MAX)
			.startedCallback((s, r) -> {})
			.runningMatcher(s -> Task.MAX)	// we wait for the process to finish
			.finishedCallback((r, f) -> {})
			.problemMatcher(s -> false)
			.build()
			.readyTask();
	
}


}
