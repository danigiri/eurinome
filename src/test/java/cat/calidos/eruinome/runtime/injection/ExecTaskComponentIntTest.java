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
import org.junit.jupiter.api.RepeatedTest;
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


private boolean startedCallbackCalled = false;
private boolean finishedCallbackCalled = false;


@Test @DisplayName("Simple onetime task (IntTest)")
public void testOneTimeExecSimpleTask() throws Exception {

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'hello world'")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("hello world") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> true)	// if anything shows on STDERR
												.build()
												.readyTask();
	assertAll("simple task",
				() -> assertFalse(task.isDone(), "Task not started should not be 'done'"),
				() -> assertEquals(Task.READY, task.getStatus(), "Task not started should be ready")
	);
	
	StartingTask start = task.start();
	start.spinUntil(Task.STARTED);
	assertEquals("hello world", start.show());
	
	RunningTask runningTask = start.runningTask();
	runningTask.spinUntil(Task.FINISHED);
	
	FinishedTask finishedTask = runningTask.finishedTask();
	assertAll("simple task",
				() -> assertTrue(finishedTask.isDone(), "Task finished should be 'done'"),
				() -> assertTrue(finishedTask.isOK()),
				() -> assertEquals(0, finishedTask.result())
	);

}


@Test @DisplayName("Complex onetime task (IntTest)")
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
	
	assertAll("complex task",
				() -> assertFalse(task.isDone()),
				() -> assertEquals(Task.READY, task.getStatus())
	);
	
	StartingTask start = task.start();
	start.spinUntil(Task.STARTED);
	assertEquals("started", start.show());
	
	RunningTask runningTask = start.runningTask();
	runningTask.spinUntil(Task.FINISHED);

	assertAll("complex task callbacks",
			() -> assertTrue(startedCallbackCalled, "Start callback was not called at all"),
			() -> assertTrue(finishedCallbackCalled, "Finished callback was not called at all")
	);
	
	FinishedTask finishedTask = runningTask.finishedTask();
	assertAll("complex task",
				() -> assertTrue(finishedTask.isOK()),
				() -> assertEquals(0, finishedTask.result())
	);

}


@Test @DisplayName("Simple problematic task (IntTest)") @RepeatedTest(5)
public void testOneTimeExecProblematicTask() throws Exception {

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'started' && sleep 1 && notfound")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> s.contains("command not found"))
												.build()
												.readyTask();
	assertAll("problematic task",
				() -> assertFalse(task.isDone()),
				() -> assertEquals(Task.READY, task.getStatus())
	);

	StartingTask start = task.start();
	
	start.spinUntil(Task.STARTED);
	assertAll("problematic task",	// THIS CAN BE CHECKED ONLY AS WE ARE ORDERING STARTING MESSAGE AND ERROR
			() -> assertTrue(start.isDone(), "Started task should be completed"),
			() -> assertTrue(start.isOK(), "Started task should be OK as failure is in the running state")
			);
	assertEquals("started", start.show());
	
	RunningTask runningTask = start.runningTask();
	assertThrows(InterruptedException.class, () -> runningTask.spinUntil(Task.FINISHED)); 
	assertAll("problematic task",
			() -> assertTrue(runningTask.isDone(), "Running failed task should also be done"),
			() -> assertFalse(runningTask.isOK(), "Running failed task should not be OK")
	);

	assertThrows(InterruptedException.class, () -> runningTask.spinUntil(Task.FINISHED));	// failed means throw
	FinishedTask finishedTask = runningTask.finishedTask();
	finishedTask.waitFor();
	assertAll("complex task",
				() -> assertFalse(finishedTask.isOK(), "finished failed task should not be OK"),
				() -> assertEquals(127, finishedTask.result(), "result of command not found should be 127")
	);
	
}

}
