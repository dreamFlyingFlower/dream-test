package com.wy.test.extend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wy.test.extend.adapter.netease.NeteaseRSATool;

import dream.flying.flower.framework.core.pretty.strategy.JsonPretty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2-8-Extend接口文档模块-元数据")
@Controller
public class ExtendApiMetadata {

	@Operation(summary = "netease qiye mail RSA Key", description = "网易企业邮箱RSA Key生成器", method = "GET")
	@RequestMapping(value = "/metadata/netease/qiye/mail/rsa/gen", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String metadata(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		NeteaseRSATool neteaseRSATool = new NeteaseRSATool();
		neteaseRSATool.genRSAKeyPair();
		return JsonPretty.getInstance().formatln(neteaseRSATool);
	}
}