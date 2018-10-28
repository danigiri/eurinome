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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import cat.calidos.eurinome.problems.EurinomeRuntimeException;
import cat.calidos.eurinome.runtime.api.FinishedTask;
import cat.calidos.eurinome.runtime.api.ReadyTask;
import cat.calidos.eurinome.runtime.api.RunningTask;
import cat.calidos.eurinome.runtime.api.StartingTask;
import cat.calidos.eurinome.runtime.api.StoppingTask;
import cat.calidos.eurinome.runtime.api.Task;
import cat.calidos.eurinome.runtime.injection.DaggerExecTaskComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecTaskComponentIntTest {


private static final int FIVESECS = 5000;
private boolean startedCallbackCalled = false;
private boolean finishedCallbackCalled = false;


@Test @DisplayName("Simple onetime task (IntTest)") @RepeatedTest(2)
public void testOneTimeExecSimpleTask() throws Exception {

	System.out.println("TEST: START");
	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'hello world' && sleep 1")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("hello world") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> true)	// if anything shows on STDERR
												.build()
												.readyTask();
	assertAll("simple task",
				() -> assertFalse(task.isDone(), "Task not started should not be 'done'"),
				() -> assertEquals(Task.READY, task.getStatus(), "Task not started should be ready")
	);
	System.out.println("TEST: about to call start");
	StartingTask starting = task.start();
	starting.spinUntil(Task.STARTED);
	assertEquals("hello world\n", starting.show());
	
	RunningTask running = starting.runningTask();
	running.spinUntil(Task.FINISHED);
	
	FinishedTask finishedTask = running.finishedTask();
	assertAll("simple task",
				() -> assertTrue(finishedTask.isDone(), "Task finished should be 'done'"),
				() -> assertTrue(finishedTask.isOK()),
				() -> assertEquals(0, finishedTask.result())
	);

}


@Test @DisplayName("Complex onetime task (IntTest)") @RepeatedTest(2)
public void testOneTimeExecComplexTask() throws Exception {
	
	String command = "echo 'started' && sleep 1 && echo 100 && sleep 1 && echo 50 && echo 0";
	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec("/bin/bash", "-c", command)
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
	start.spinUntil(Task.STARTED, FIVESECS);
	assertEquals("started\n", start.show());
	
	RunningTask running = start.runningTask();
	running.spinUntil(Task.FINISHED);
	assertEquals("100\n50\n0\n", running.show());
	assertAll("complex task callbacks",
			() -> assertTrue(startedCallbackCalled, "Start callback was not called at all"),
			() -> assertTrue(finishedCallbackCalled, "Finished callback was not called at all")
	);

	
	FinishedTask finished = running.finishedTask();
	assertAll("complex task",
				() -> assertTrue(finished.isOK()),
				() -> assertEquals(0, finished.result())
	);

}


@Test @DisplayName("Simple problematic task (IntTest)") @RepeatedTest(2)
public void testOneTimeExecProblematicTask() throws Exception {

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'started' && sleep 2 && notfound")
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
	assertAll("problematic task",
			() -> assertTrue(start.isDone(), "Started task should be completed"),
			() -> assertTrue(start.isOK(), "Started task should be OK as failure is in the running state")
			);
	assertEquals("started\n", start.show());
	
	RunningTask runningTask = start.runningTask();
	
	try {
		runningTask.spinUntil(Task.FINISHED); 
	} catch (InterruptedException e) {
		assertFalse(runningTask.isOK(), "Spinning throwing exception should not be OK");
	} // task may finish before throwing the exception
	assertTrue(runningTask.isDone(), "Running failed task should also be done");
	
	FinishedTask finishedTask = runningTask.finishedTask();
	finishedTask.waitFor();
	assertAll("complex task",
				() -> assertFalse(finishedTask.isOK(), "finished failed task should not be OK"),
				() -> assertEquals(127, finishedTask.result(), "result of command not found should be 127")
	);
	
}


@Test @DisplayName("Binary not found problematic task (IntTest)")
@Disabled("Avoid dumping the stack trace for expected exception")
public void testOneTimeBinaryNotFoundTask() {
	// can't avoid the stacktrace being printed, so disabling for the time being

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/NOTFOUNDFORSURE")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> s.contains("command not found"))
												.build()
												.readyTask();
	assertAll("problematic task",
			() -> assertFalse(task.isDone()),
			() -> assertEquals(Task.READY, task.getStatus())
	);
	assertThrows(EurinomeRuntimeException.class, () -> task.start());

}


@Test @DisplayName("Stop starting task (IntTest)") @RepeatedTest(2)
public void testStopOneTimeStartingTask() throws Exception {

	String command = "trap 'sleep 1 && echo stopped' SIGTERM; sleep 500 & wait %1";
	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", command)
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.stoppedMatcher(s -> s.equals("stopped") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> !s.contains("hangup"))
												.build()
												.readyTask();
	assertAll("task",
			() -> assertFalse(task.isDone()),
			() -> assertEquals(Task.READY, task.getStatus())
	);

	StartingTask starting = task.start();
	assertAll("task",
			() -> assertFalse(starting.isDone()),
			() -> {
				int status = starting.getStatus();
				assertEquals(Task.STARTING, status, "Starting task should be STARTING ("+starting.translate(status)+")");
			}
	);
	
	StoppingTask stopping = starting.stop();
	assertNotNull(stopping);
	assertAll("task",
			() -> assertEquals(Task.STOPPED, starting.getStatus()),
			() -> assertFalse(stopping.isDone()),
			() -> {
				int status = stopping.getStatus();
				assertEquals(Task.STOPPING, status, "Starting task should be STOPPING ("+starting.translate(status)+")");
			}
	);

	stopping.spinUntil(Task.FINISHED);
	assertEquals("stopped\n", stopping.show());

}


@Test @DisplayName("Stop running task (IntTest)") @RepeatedTest(2)
public void testStopOneTimeRunningTask() throws Exception {
	
	String command = "trap 'sleep 1 && echo 0' SIGTERM; echo 'started' && sleep 500 & wait %1";
	ReadyTask task = DaggerExecTaskComponent.builder()
			.exec( "/bin/bash", "-c", command)
			.type(Task.ONE_TIME)
			.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)	// this matches
			.stoppedMatcher(s -> Integer.parseInt(s))
			.problemMatcher(s -> true)	// if anything shows on STDERR
			.build()
			.readyTask();
	assertAll("task",
			() -> assertFalse(task.isDone()),
			() -> assertEquals(Task.READY, task.getStatus())
	);
	
	StartingTask start = task.start();
	start.spinUntil(Task.STARTED);
	assertEquals("started\n", start.show());
	
	RunningTask running = start.runningTask();
	assertAll("task",
			() -> assertFalse(running.isDone()),
			() -> assertEquals(Task.RUNNING, running.getStatus())
	);
	
	StoppingTask stopping = running.stop();
	assertAll("task",
			() -> assertTrue(running.isDone()),
			() -> {
				int status = running.getStatus();
				assertEquals(Task.STOPPED, status, "Stopped task should not be ("+running.translate(status)+")");
			}
	);
	assertNotNull(stopping);

	stopping.spinUntil(Task.FINISHED);
	assertEquals("0\n",stopping.show());
	assertAll("task",
			() -> assertTrue(stopping.isDone()),
			() -> assertEquals(Task.FINISHED, stopping.getStatus())
	);

	FinishedTask finished = stopping.finishedTask();
	finished.waitFor();
	assertAll("stopped task",
				() -> assertFalse(finished.isOK(), "finished failed task should not be OK"),
				() -> assertEquals(143, finished.result(), "result of stopped task should be 143")
	);
	
}

}
