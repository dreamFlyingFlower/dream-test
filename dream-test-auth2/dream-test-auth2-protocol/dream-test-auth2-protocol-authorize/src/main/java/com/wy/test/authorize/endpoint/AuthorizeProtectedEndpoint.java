package com.wy.test.authorize.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.common.crypto.password.PasswordReciprocal;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;

@Controller
public class AuthorizeProtectedEndpoint {

	@GetMapping("/authz/protected/forward")
	public ModelAndView forwardProtectedForward(HttpServletRequest request) {
		String redirect_uri = request.getAttribute("redirect_uri").toString();
		ModelAndView modelAndView = new ModelAndView("authorize/protected/forward");
		modelAndView.addObject("redirect_uri", redirect_uri);
		return modelAndView;
	}

	@PostMapping("/authz/protected")
	public ModelAndView authorizeProtected(@RequestParam("password") String password,
			@RequestParam("redirect_uri") String redirect_uri, @CurrentUser UserInfo currentUser) {
		if (currentUser.getAppLoginPassword().equals(PasswordReciprocal.getInstance().encode(password))) {
			WebContext.setAttribute(WebConstants.CURRENT_SINGLESIGNON_URI, redirect_uri);
			return WebContext.redirect(redirect_uri);
		}

		ModelAndView modelAndView = new ModelAndView("authorize/protected/forward");
		modelAndView.addObject("redirect_uri", redirect_uri);
		return modelAndView;
	}
}