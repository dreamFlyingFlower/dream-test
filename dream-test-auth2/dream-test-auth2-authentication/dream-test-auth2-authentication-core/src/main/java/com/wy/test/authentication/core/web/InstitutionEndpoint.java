package com.wy.test.authentication.core.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.repository.InstitutionsRepository;

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
	DreamAuthServerProperties dreamServerProperties;

	/**
	 * 单点登录系统调用,获得当前登录者的组织机构信息
	 * 
	 * @param request 请求
	 * @param originURL 原始Origin
	 * @param headerHostName 域名
	 * @param headerHost 域名
	 * @return 结果
	 */
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

		InstitutionEntity inst = institutionsRepository.get(host);
		if (inst != null) {
			log.debug("inst {}", inst);
			return new ResultResponse<InstitutionEntity>(inst).buildResponse();
		} else {
			InstitutionEntity defaultInst = institutionsRepository.get("1");
			log.debug("default inst {}", inst);
			return new ResultResponse<InstitutionEntity>(defaultInst).buildResponse();
		}
	}
}