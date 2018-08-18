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

package cat.calidos.eruinome.view.injection;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cat.calidos.eurinome.view.injection.DaggerViewComponent;
import cat.calidos.eurinome.view.injection.ViewComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ViewComponentTest {


@Test
public void testRender() {

	String templatePath = "templates/test.txt.vtl";
	Map<String, String> v = new HashMap<String, String>(3);
	v.put("v1", "aa");
	v.put("v2", "bb");
	v.put("v3", "cc");

	ViewComponent view = DaggerViewComponent.builder().withValue(v).withTemplate(templatePath).build();
	assertNotNull(view);

	String output = view.render();
	assertEquals("This is a test aa bb cc", output);

}


}
