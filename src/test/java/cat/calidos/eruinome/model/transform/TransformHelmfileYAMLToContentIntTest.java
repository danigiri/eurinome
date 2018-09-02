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

package cat.calidos.eruinome.model.transform;

import org.junit.Test;

import cat.calidos.morfeu.model.transform.TransformTezt;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformHelmfileYAMLToContentIntTest extends TransformTezt {


@Test
public void testTransformUsingTemplateGettingStarted() throws Exception {

	String yamlPath = "target/classes/documents/getting-started.yaml";
	String documentPath = "documents/getting-started.json";
	//String xmlPath = "src/test/resources/test-resources/documents/document1.xml";

	String transformed = transformYAMLToXML(yamlPath, documentPath);
	System.err.println(transformed);
	//compareWithXML(transformed,  xmlPath);

}


}
