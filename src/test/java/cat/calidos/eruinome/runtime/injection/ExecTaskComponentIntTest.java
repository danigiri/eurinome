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
import org.opentest4j.MultipleFailuresError;

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


private boolean startedCallbackCalled = false;
private boolean finishedCallbackCalled = false;


@Test @DisplayName("Execute a simple onetime task (IntTest)")
public void testOneTimeExecSimpleTask() throws Exception {

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'hello world'")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("hello world") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> true)	// if anything shows on STDERR
												.build()
												.readyTask();
	assertAll("task",
				() -> assertFalse(task.isDone()),
				() -> assertEquals(Task.READY, task.getStatus())
	);
	
	StartingTask start = task.start();
	start.spinUntil(Task.STARTED);
	assertEquals("hello world", start.show());
	
	RunningTask runningTask = start.runningTask();
	runningTask.spinUntil(Task.FINISHED);
	
	FinishedTask finishedTask = runningTask.finishedTask();
	assertAll("task",
				() -> assertTrue(finishedTask.isDone()),
				() -> assertTrue(finishedTask.isOK()),
				() -> assertEquals(0, finishedTask.result())
	);

}


@Test @DisplayName("Execute a complex onetime task (IntTest)")
public void testOneTimeExecComplexTask() throws Exception {
	
	ReadyTask task = DaggerExecTaskComponent.builder()
			.exec( "/bin/bash", "-c", "echo 'started' && sleep 1 && echo 100 && sleep 1 && echo 50 && echo 0")
			.type(Task.ONE_TIME)
			.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
			.startedCallback((s, r) -> {startedCallbackCalled = true;})
			.runningMatcher(s -> Integer.parseInt(s))
			.finishedCallback((r, f) -> {finishedCallbackCalled = true;})
			.problemMatcher(s -> true)
			.build()
			.readyTask();
	
	assertAll("task",
				() -> assertFalse(task.isDone()),
				() -> assertEquals(Task.READY, task.getStatus())
	);
	
	StartingTask start = task.start();
	start.spinUntil(Task.STARTED);
	assertEquals("started", start.show());
	
	RunningTask runningTask = start.runningTask();
	runningTask.spinUntil(Task.FINISHED);

	assertAll("callbacks",
			() -> assertTrue(startedCallbackCalled, "Start callback was not called at all"),
			() -> assertTrue(finishedCallbackCalled, "Finished callback was not called at all")
	);
	
	FinishedTask finishedTask = runningTask.finishedTask();
	assertAll("task",
				() -> assertTrue(finishedTask.isOK()),
				() -> assertEquals(0, finishedTask.result())
	);

}


@Test @DisplayName("Execute a simple problematic task (IntTest)")
public void testOneTimeExecProblematicTask() throws Exception {

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'started' && notfound")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> s.contains("command not found"))
												.build()
												.readyTask();
	assertAll("task",
			() -> assertFalse(task.isDone()),
			() -> assertEquals(Task.READY, task.getStatus())
	);

	StartingTask start = task.start();

//	FinishedTask finishedTask = runningTask.finishedTask();
//	assertAll("task",
//				() -> assertTrue(finishedTask.isOK()),
//				() -> assertEquals(0, finishedTask.result())
//	);
	
}

}
