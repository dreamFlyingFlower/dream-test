package com.wy.test.web.web.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.constants.ConstsTimeInterval;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.web.WebConstants;
import com.wy.test.web.WebContext;

@Controller
@RequestMapping(value={"/safe"})
public class SafeController {
	final static Logger _logger = LoggerFactory.getLogger(SafeController.class);
	
	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping(value="/forward/setting") 
	public ModelAndView fowardSetting(@CurrentUser UserInfo currentUser) {
			ModelAndView modelAndView=new ModelAndView("safe/setting");
			modelAndView.addObject("model", currentUser);
			return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="/setting") 
	public ResponseEntity<?> setting(
	        HttpServletRequest request,
            HttpServletResponse response,
			@RequestParam("authnType") String authnType,
			@RequestParam("mobile") String mobile,
			@RequestParam("mobileVerify") String mobileVerify,
			@RequestParam("email") String email,
			@RequestParam("emailVerify") String emailVerify,
			@RequestParam("theme") String theme,
			@CurrentUser UserInfo currentUser) {
		currentUser.setAuthnType(Integer.parseInt(authnType));
		userInfoService.updateAuthnType(currentUser);
		
		currentUser.setMobile(mobile);
		userInfoService.updateMobile(currentUser);
		
		currentUser.setEmail(email);

		currentUser.setTheme(theme);
        WebContext.setCookie(response,null, WebConstants.THEME_COOKIE_NAME, theme, ConstsTimeInterval.ONE_WEEK);
        
		userInfoService.updateEmail(currentUser);
		
		
		return new Message<UserInfo>(Message.SUCCESS).buildResponse();
		
	}
	
	
}
