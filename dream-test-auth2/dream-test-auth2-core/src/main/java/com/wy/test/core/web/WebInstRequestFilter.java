package com.wy.test.core.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.repository.InstitutionsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebInstRequestFilter extends GenericFilterBean {

	public final static String HEADER_HOST = "host";

	public final static String HEADER_HOSTNAME = "hostname";

	InstitutionsRepository institutionsRepository;

	DreamAuthServerProperties dreamServerProperties;

	public WebInstRequestFilter(InstitutionsRepository institutionsRepository,
			DreamAuthServerProperties dreamServerProperties) {
		super();
		this.institutionsRepository = institutionsRepository;
		this.dreamServerProperties = dreamServerProperties;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		log.trace("WebInstRequestFilter");
		HttpServletRequest request = ((HttpServletRequest) servletRequest);

		if (request.getSession().getAttribute(ConstAuthWeb.CURRENT_INST) == null) {
			if (log.isTraceEnabled()) {
				AuthWebContext.printRequest(request);
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
			// TODO 可能为null,未处理
			InstitutionEntity institution = institutionsRepository.get(host);
			log.trace("{}", institution);
			request.getSession().setAttribute(ConstAuthWeb.CURRENT_INST, institution);

			String origin = request.getHeader(HttpHeaders.ORIGIN);
			if (StringUtils.isEmpty(origin)) {
				origin = dreamServerProperties.getFrontendUri();
			}
		}
		chain.doFilter(servletRequest, servletResponse);
	}
}