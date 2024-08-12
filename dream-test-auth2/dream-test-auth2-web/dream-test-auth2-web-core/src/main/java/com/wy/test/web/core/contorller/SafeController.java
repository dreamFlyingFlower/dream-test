package com.wy.test.web.core.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstTimeInterval;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.UserService;

@Controller
@RequestMapping(value = { "/safe" })
public class SafeController {

	@Autowired
	private UserService userService;

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
		userService.updateAuthnType(currentUser);

		currentUser.setMobile(mobile);
		userService.updateMobile(currentUser);

		currentUser.setEmail(email);

		currentUser.setTheme(theme);
		WebContext.setCookie(response, null, WebConstants.THEME_COOKIE_NAME, theme, ConstTimeInterval.ONE_WEEK);

		userService.updateEmail(currentUser);

		return new Message<UserEntity>(Message.SUCCESS).buildResponse();
	}
}