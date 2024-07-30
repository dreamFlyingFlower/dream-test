package com.wy.test.core.authn.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.core.entity.Institutions;
import com.wy.test.core.entity.Message;
import com.wy.test.core.persistence.repository.InstitutionsRepository;
import com.wy.test.core.properties.DreamServerProperties;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/inst")
@Slf4j
public class InstitutionEndpoint {

	public final static String HEADER_HOST = "host";

	public final static String HEADER_HOSTNAME = "hostname";

	@Autowired
	InstitutionsRepository institutionsRepository;

	@Autowired
	DreamServerProperties dreamServerProperties;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(HttpServletRequest request,
			@RequestHeader(value = "Origin", required = false) String originURL,
			@RequestHeader(value = HEADER_HOSTNAME, required = false) String headerHostName,
			@RequestHeader(value = HEADER_HOST, required = false) String headerHost) {
		log.debug("get Institution");

		String host = headerHostName;
		log.trace("hostname {}", host);
		if (StringUtils.isEmpty(host)) {
			host = headerHost;
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

		Institutions inst = institutionsRepository.get(host);
		if (inst != null) {
			log.debug("inst {}", inst);
			return new Message<Institutions>(inst).buildResponse();
		} else {
			Institutions defaultInst = institutionsRepository.get("1");
			log.debug("default inst {}", inst);
			return new Message<Institutions>(defaultInst).buildResponse();
		}
	}
}