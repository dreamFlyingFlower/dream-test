package com.wy.test.web.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.constant.ConstTimeInterval;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "1-8 安全设置API")
@Controller
@AllArgsConstructor
@RequestMapping(value = { "/safe" })
public class SafeController {

	private final UserService userService;

	@Operation(summary = "转发到安全页面", description = "转发到安全页面", method = "GET")
	@GetMapping(value = "/forward/setting")
	public ModelAndView fowardSetting(@CurrentUser UserEntity currentUser) {
		ModelAndView modelAndView = new ModelAndView("safe/setting");
		modelAndView.addObject("model", currentUser);
		return modelAndView;
	}

	@Operation(summary = "修改", description = "修改", method = "POST")
	@ResponseBody
	@PostMapping(value = "/setting")
	public ResponseEntity<?> setting(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("authnType") String authnType, @RequestParam("mobile") String mobile,
			@RequestParam("mobileVerify") String mobileVerify, @RequestParam("email") String email,
			@RequestParam("emailVerify") String emailVerify, @RequestParam("theme") String theme,
			@CurrentUser UserEntity currentUser) {
		currentUser.setAuthnType(Integer.parseInt(authnType));
		userService.updateAuthnType(currentUser);

		currentUser.setMobile(mobile);
		userService.updateMobile(currentUser);

		currentUser.setEmail(email);

		currentUser.setTheme(theme);
		AuthWebContext.setCookie(response, null, ConstAuthWeb.THEME_COOKIE_NAME, theme, ConstTimeInterval.ONE_WEEK);

		userService.updateEmail(currentUser);

		return new ResultResponse<UserEntity>(ResultResponse.SUCCESS).buildResponse();
	}
}