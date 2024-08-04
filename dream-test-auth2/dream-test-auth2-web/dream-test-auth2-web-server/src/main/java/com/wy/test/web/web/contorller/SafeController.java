package com.wy.test.web.web.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstTimeInterval;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.UserInfoService;

@Controller
@RequestMapping(value = { "/safe" })
public class SafeController {

	final static Logger _logger = LoggerFactory.getLogger(SafeController.class);

	@Autowired
	private UserInfoService userInfoService;

	@GetMapping(value = "/forward/setting")
	public ModelAndView fowardSetting(@CurrentUser UserEntity currentUser) {
		ModelAndView modelAndView = new ModelAndView("safe/setting");
		modelAndView.addObject("model", currentUser);
		return modelAndView;
	}

	@ResponseBody
	@PostMapping(value = "/setting")
	public ResponseEntity<?> setting(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("authnType") String authnType, @RequestParam("mobile") String mobile,
			@RequestParam("mobileVerify") String mobileVerify, @RequestParam("email") String email,
			@RequestParam("emailVerify") String emailVerify, @RequestParam("theme") String theme,
			@CurrentUser UserEntity currentUser) {
		currentUser.setAuthnType(Integer.parseInt(authnType));
		userInfoService.updateAuthnType(currentUser);

		currentUser.setMobile(mobile);
		userInfoService.updateMobile(currentUser);

		currentUser.setEmail(email);

		currentUser.setTheme(theme);
		WebContext.setCookie(response, null, WebConstants.THEME_COOKIE_NAME, theme, ConstTimeInterval.ONE_WEEK);

		userInfoService.updateEmail(currentUser);

		return new Message<UserEntity>(Message.SUCCESS).buildResponse();
	}
}