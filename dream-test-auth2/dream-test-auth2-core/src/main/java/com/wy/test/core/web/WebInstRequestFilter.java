package com.wy.test.core.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.wy.test.core.entity.Institutions;
import com.wy.test.core.persistence.repository.InstitutionsRepository;
import com.wy.test.core.properties.DreamServerProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebInstRequestFilter extends GenericFilterBean {

	public final static String HEADER_HOST = "host";

	public final static String HEADER_HOSTNAME = "hostname";

	public final static String HEADER_ORIGIN = "Origin";

	InstitutionsRepository institutionsRepository;

	DreamServerProperties dreamServerProperties;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		log.trace("WebInstRequestFilter");
		HttpServletRequest request = ((HttpServletRequest) servletRequest);

		if (request.getSession().getAttribute(WebConstants.CURRENT_INST) == null) {
			if (log.isTraceEnabled()) {
				WebContext.printRequest(request);
			}
			String host = request.getHeader(HEADER_HOSTNAME);
			log.trace("hostname {}", host);
			if (StringUtils.isEmpty(host)) {
				host = request.getHeader(HEADER_HOST);
				log.trace("host {}", host);
			}
			if (StringUtils.isEmpty(host)) {
				host = dreamServerProperties.getDomain();
				log.trace("config domain {}", host);
			}
			if (host.indexOf(":") > -1) {
				host = host.split(":")[0];
				log.trace("domain split {}", host);
			}
			Institutions institution = institutionsRepository.get(host);
			log.trace("{}", institution);
			request.getSession().setAttribute(WebConstants.CURRENT_INST, institution);

			String origin = request.getHeader(HEADER_ORIGIN);
			if (StringUtils.isEmpty(origin)) {
				origin = dreamServerProperties.getFrontendUri();
			}
		}
		chain.doFilter(servletRequest, servletResponse);
	}

	public WebInstRequestFilter(InstitutionsRepository institutionsRepository,
			DreamServerProperties dreamServerProperties) {
		super();
		this.institutionsRepository = institutionsRepository;
		this.dreamServerProperties = dreamServerProperties;
	}
}