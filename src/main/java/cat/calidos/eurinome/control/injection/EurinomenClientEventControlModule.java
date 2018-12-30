package cat.calidos.eurinome.control.injection;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.inject.Named;

import cat.calidos.morfeu.webapp.injection.ControlComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class EurinomenClientEventControlModule {


@Provides @IntoMap @Named("GET")
@StringKey("/events/([^/\\?]+)")
public static BiFunction<List<String>, Map<String, String>, String> event() {
	return (pathElems, params) -> handle(pathElems.get(1), params);
}


@Provides @IntoMap @Named("Content-Type")
@StringKey("/events/([^/\\?]+)")
public static String contentType() {
	return ControlComponent.JSON;
}


private static String handle(String event, Map<String, String> params) {
	
	StringBuffer output = new StringBuffer();
	output.append("{\"result\":\"OK\", \"desc\":\"'"+event+"'"); 
	
	if (event.equals("ContentRequestEvent")) {
		output.append("("+params.get("doc")+")");
	}
	
	output.append("\"}");
	
	return output.toString();

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

