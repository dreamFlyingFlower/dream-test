package com.wy.test.protocol.authorize.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.session.Session;
import com.wy.test.authentication.core.authn.session.SessionManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "3-1-在线ticket文档模块")
@Controller
@RequestMapping(value = { "/onlineticket" })
public class OnlineSessionEndpoint {

	@Autowired
	protected SessionManager sessionManager;

	@Operation(summary = "在线ticket验证接口", description = "", method = "GET")
	@ResponseBody
	@GetMapping(value = "/validate")
	public String ticketValidate(@RequestParam(value = "ticket", required = true) String ticket) {
		Session session = sessionManager.get(ticket);
		return session == null ? "" : session.getFormattedId();
	}
}
