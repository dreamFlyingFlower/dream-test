package com.wy.test.core.authn.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.core.entity.Institutions;
import com.wy.test.core.persistence.repository.InstitutionsRepository;
import com.wy.test.entity.Message;

@Controller
@RequestMapping(value = "/inst")
public class InstitutionEndpoint {

	private static final Logger _logger = LoggerFactory.getLogger(InstitutionEndpoint.class);

	public final static String HEADER_HOST = "host";

	public final static String HEADER_HOSTNAME = "hostname";

	@Autowired
	InstitutionsRepository institutionsRepository;

	@Autowired
	ApplicationConfig applicationConfig;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(HttpServletRequest request,
			@RequestHeader(value = "Origin", required = false) String originURL,
			@RequestHeader(value = HEADER_HOSTNAME, required = false) String headerHostName,
			@RequestHeader(value = HEADER_HOST, required = false) String headerHost) {
		_logger.debug("get Institution");

		String host = headerHostName;
		_logger.trace("hostname {}", host);
		if (StringUtils.isEmpty(host)) {
			host = headerHost;
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

		Institutions inst = institutionsRepository.get(host);
		if (inst != null) {
			_logger.debug("inst {}", inst);
			return new Message<Institutions>(inst).buildResponse();
		} else {
			Institutions defaultInst = institutionsRepository.get("1");
			_logger.debug("default inst {}", inst);
			return new Message<Institutions>(defaultInst).buildResponse();
		}
	}
}
