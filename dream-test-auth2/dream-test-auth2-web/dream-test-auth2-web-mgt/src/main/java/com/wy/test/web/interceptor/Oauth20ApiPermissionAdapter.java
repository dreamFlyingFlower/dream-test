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
 

package com.wy.test.web.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.util.RequestTokenUtils;

/**
 * OAuth v2.0 accessToken认证Interceptor处理.
 * @author Crystal.Sea
 *
 */
@Component
public class Oauth20ApiPermissionAdapter  implements AsyncHandlerInterceptor  {
	private static final Logger _logger = LoggerFactory.getLogger(Oauth20ApiPermissionAdapter.class);
	
	@Autowired
    protected PasswordReciprocal passwordReciprocal;
	
	@Autowired
    private DefaultTokenServices oauth20TokenServices;
	
	static  ConcurrentHashMap<String ,String >navigationsMap=null;
	
	/*
	 * 请求前处理
	 *  (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		 _logger.trace("OAuth20 API Permission Adapter pre handle");
		 String accessToken =  RequestTokenUtils.resolveAccessToken(request);
		 _logger.trace("access_token {} " , accessToken);
		 try {
			 OAuth2Authentication authentication = oauth20TokenServices.loadAuthentication(accessToken);
			//判断应用的accessToken信息
			if(authentication != null ){
			    _logger.trace("authentication "+ authentication);
			    return true;
			}
		 }catch(Exception e) {
			 _logger.error("load Authentication Exception ! ",e);
		 }
		
		_logger.trace("No Authentication ... forward to /login");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
        dispatcher.forward(request, response);
        
		return false;
	}
}
