package cat.calidos.eurinome.control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.injection.DaggerMorfeuPOSTFilterComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class EurinomeRequestFilter implements Filter {

protected final static Logger log = LoggerFactory.getLogger(EurinomeRequestFilter.class);


@Override
public void init(FilterConfig filterConfig) throws ServletException {
	log.info("------ Request filter initialized ({}) ------", filterConfig.getFilterName());
}


@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

	boolean handled = DaggerMorfeuPOSTFilterComponent.builder().request(request).response(response).build().handle();

	if (!handled) {
		chain.doFilter(request, response);
	}

}


@Override
public void destroy() {}



}


/*
 *    Copyright 2019 Daniel Giribet
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

