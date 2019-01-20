package cat.calidos.eurinome.control;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Saver;
import cat.calidos.morfeu.utils.injection.DaggerSaverComponent;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;

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

	HttpServletRequest req = (HttpServletRequest) request;

	log.trace("------ Request filter request ({} {}) ------", req.getMethod(), req.getServletPath());
	if (req.getMethod().equals("POST")) {
		ServletContext ctxt = req.getServletContext();
		String prefix = ctxt.getInitParameter("__RESOURCES_PREFIX");
		prefix = (String)((Properties)ctxt.getAttribute(GenericHttpServlet.__CONFIG)).get("__RESOURCES_PREFIX");
		
		String uri = prefix+req.getServletPath();
		log.info("------ Request filter req path ({}) ------", uri);
		
		// we assume Morfeu is doing the validation for now
		try {
			URI destination = DaggerURIComponent.builder().from(uri).builder().uri().get();
			String content = req.getParameter("content");
			System.err.println(content);
			Saver saver = DaggerSaverComponent.builder()
												.toURI(destination)
												.content(content)
												.build()
												.saver()
												.get();
			saver.save();
			// now we give a response back
			HttpServletResponse res = (HttpServletResponse) response;
			res.setStatus(HttpServletResponse.SC_OK);
			ServletOutputStream outputStream = res.getOutputStream();
			IOUtils.write("{\n" + 
					"	\"result\": \"OK\"\n" + 
					"	,\"target\": \""+destination+"\"\n" + 
					"	,\"operation\": \"FileSaver\"\n" + 
					"	,\"operationTime\": 1\n" + 
					"}\n" + 
					"", outputStream, Config.DEFAULT_CHARSET);
			outputStream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} else {
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

