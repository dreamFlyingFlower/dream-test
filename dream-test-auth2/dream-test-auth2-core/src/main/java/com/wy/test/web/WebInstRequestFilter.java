package com.wy.test.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import com.wy.test.configuration.ApplicationConfig;
import com.wy.test.entity.Institutions;
import com.wy.test.persistence.repository.InstitutionsRepository;

public class WebInstRequestFilter extends GenericFilterBean {

	final static Logger _logger = LoggerFactory.getLogger(GenericFilterBean.class);

	public final static String HEADER_HOST = "host";

	public final static String HEADER_HOSTNAME = "hostname";

	public final static String HEADER_ORIGIN = "Origin";

	InstitutionsRepository institutionsRepository;

	ApplicationConfig applicationConfig;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		_logger.trace("WebInstRequestFilter");
		HttpServletRequest request = ((HttpServletRequest) servletRequest);

		if (request.getSession().getAttribute(WebConstants.CURRENT_INST) == null) {
			if (_logger.isTraceEnabled()) {
				WebContext.printRequest(request);
			}
			String host = request.getHeader(HEADER_HOSTNAME);
			_logger.trace("hostname {}", host);
			if (StringUtils.isEmpty(host)) {
				host = request.getHeader(HEADER_HOST);
				_logger.trace("host {}", host);
			}
			if (StringUtils.isEmpty(host)) {
				host = applicationConfig.getDomainName();
				_logger.trace("config domain {}", host);
			}
			if (host.indexOf(":") > -1) {
				host = host.split(":")[0];
				_logger.trace("domain split {}", host);
			}
			Institutions institution = institutionsRepository.get(host);
			_logger.trace("{}", institution);
			request.getSession().setAttribute(WebConstants.CURRENT_INST, institution);

			String origin = request.getHeader(HEADER_ORIGIN);
			if (StringUtils.isEmpty(origin)) {
				origin = applicationConfig.getFrontendUri();
			}
		}
		chain.doFilter(servletRequest, servletResponse);
	}

	public WebInstRequestFilter(InstitutionsRepository institutionsRepository, ApplicationConfig applicationConfig) {
		super();
		this.institutionsRepository = institutionsRepository;
		this.applicationConfig = applicationConfig;
	}

}
