package com.wy.test.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.constants.ContentType;

@Controller
public class MetadataEndpoint {

	@GetMapping(value = "/metadata/version", produces = ContentType.TEXT_PLAIN_UTF8)
	@ResponseBody
	public String metadata(HttpServletRequest request, HttpServletResponse response) {
		return MybatisJpaContext.version();
	}
}
