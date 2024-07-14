/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.maxkey.web.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.maxkey.authn.annotation.CurrentUser;
import org.maxkey.constants.ConstsTimeInterval;
import org.maxkey.entity.Message;
import org.maxkey.entity.UserInfo;
import org.maxkey.persistence.service.UserInfoService;
import org.maxkey.web.WebConstants;
import org.maxkey.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
