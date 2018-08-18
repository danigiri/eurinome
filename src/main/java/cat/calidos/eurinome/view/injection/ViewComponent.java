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

import javax.annotation.Nullable;
import javax.inject.Named;

import cat.calidos.eurinome.problems.EurinomeParsingException;
import dagger.BindsInstance;
import dagger.Component;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = VelocityViewModule.class)
public interface ViewComponent {

String render() throws EurinomeParsingException;

@Component.Builder
interface Builder {

	@BindsInstance Builder withValue(@Named("value") Object value);
	@BindsInstance Builder withTemplate(@Named("TemplatePath") String path);
	@BindsInstance Builder andProblem(@Nullable @Named("problem") String problem);

	ViewComponent build();

}

}
