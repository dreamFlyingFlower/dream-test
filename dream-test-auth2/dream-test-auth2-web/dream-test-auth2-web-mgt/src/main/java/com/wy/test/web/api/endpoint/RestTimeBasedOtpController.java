package com.wy.test.web.api.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.entity.UserEntity;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.persistence.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "基于时间令牌验证 API文档模块")
@Controller
@RequestMapping(value = { "/im/api/otp" })
public class RestTimeBasedOtpController {

	@Autowired
	protected AbstractOtpAuthn timeBasedOtpAuthn;

	@Autowired
	private UserService userService;

	@Operation(summary = "基于时间令牌验证 API文档模块", description = "传递参数username和token", method = "GET")
	@ResponseBody
	@GetMapping(value = "/timebased/validate")
	public boolean getUser(@RequestParam String username, @RequestParam String token) {

		UserEntity validUserInfo = userService.findByUsername(username);
		if (validUserInfo != null) {
			if (timeBasedOtpAuthn.validate(validUserInfo, token)) {
				return true;
			}
		}

		return false;
	}

}
