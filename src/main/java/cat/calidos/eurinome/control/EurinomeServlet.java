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

package cat.calidos.eurinome.control;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.eurinome.control.injection.DaggerEurinomeControlComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.injection.DaggerControlComponent;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class EurinomeServlet extends GenericHttpServlet {

protected final static Logger log = LoggerFactory.getLogger(EurinomeServlet.class);

@Override
public ControlComponent getControl(String path, Map<String, String> params) {
	return DaggerEurinomeControlComponent.builder()
											.withPath(path)
											.method(DaggerControlComponent.GET)
											.withParams(params)
											.andContext(context)
											.build();
}


@Override
public ControlComponent putControl(String path, Map<String, String> params) {
	return DaggerEurinomeControlComponent.builder()
											.withPath(path)
											.method(DaggerControlComponent.POST)
											.withParams(params)
											.andContext(context)
											.build();
}


}
