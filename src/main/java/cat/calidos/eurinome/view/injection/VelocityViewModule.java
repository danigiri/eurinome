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

package cat.calidos.eurinome.view.injection;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import cat.calidos.eurinome.problems.EurinomeConfigurationException;
import cat.calidos.eurinome.problems.EurinomeParsingException;
import dagger.Module;
import dagger.Provides;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class VelocityViewModule {

private static final String VALUE_NAME = "v";
private static final String PROBLEM_NAME = "problem";

@Provides
String render(StringWriter writer,
				VelocityContext context,
				@Named("TemplatePath") String path,
				@Named("Template") String template) throws EurinomeParsingException {

	try {
		Velocity.evaluate( context, writer, path, template);
	} catch (Exception e) {
		throw new EurinomeParsingException("Could not merge '"+path+"' with the given data", e);
	}

	return writer.toString();

}


@Provides
VelocityContext context(@Named("value") Object value, Optional<String> problem) {

	Velocity.init();

	VelocityContext context = new VelocityContext();
	if (value instanceof Map<?, ?>) {
		Map<?, ?> mapValue = (Map<?, ?>) value;
		mapValue.entrySet().stream().forEach(e -> context.put(e.getKey().toString(), e.getValue())); 
	} else {
		context.put(VALUE_NAME, value);
	}
	context.put(PROBLEM_NAME, problem);
	

	return context;

}


@Provides
StringWriter stringWriter() {
	return new StringWriter();
}


@Provides @Named("Template") 
String template(@Named("TemplatePath") String path) {

	try {
		String fullTemplatePath = this.getClass().getClassLoader().getResource(path).toString();
		URI templateURI = new URI(fullTemplatePath);

		return IOUtils.toString(templateURI, "UTF-8");

	} catch (Exception e) {
		throw new EurinomeConfigurationException("Could not open template '"+path+"'", e);
	}

}


@Provides
Optional<String> problem(@Nullable @Named("problem") String problem) {
	return Optional.ofNullable(problem);
}


}
