package cat.calidos.eruinome.runtime.injection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.eurinome.runtime.injection.DaggerHelmfileTaskComponent;
import cat.calidos.eurinome.runtime.injection.HelmfileTaskComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class HelmfileTaskComponentIntTest {


@Test @DisplayName("Generate a fake helmfile command with a custom binary")
public void testHelmfileCustomBinaryCommand() throws Exception, InterruptedException {

	HelmfileTaskComponent taskBuilder = DaggerHelmfileTaskComponent.builder()
																	.run("apply")
																	.withFile("foo")
																	.helmfileBinaryAt("/bin/foo")
																	.build();
	assertAll("Custom command",
			() -> assertEquals("/bin/foo -q --file 'foo' apply", taskBuilder.command()),
			() -> assertTrue(taskBuilder.task().isOneTime())
	);

}


@Test @DisplayName("Generate a fake helmfile command")
public void testHelmfileCommand() throws Exception, InterruptedException {

	HelmfileTaskComponent taskBuilder = DaggerHelmfileTaskComponent.builder()
																	.run("apply")
																	.withFile("foo")
																	.build();
	assertAll("Default command",
			() -> assertEquals("/usr/local/bin/helmfile -q --file 'foo' apply", taskBuilder.command()),
			() -> assertTrue(taskBuilder.task().isOneTime())
	);

}

}


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

