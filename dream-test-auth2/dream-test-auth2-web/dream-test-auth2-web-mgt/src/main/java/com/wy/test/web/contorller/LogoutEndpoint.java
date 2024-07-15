/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
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
 

package com.wy.test.web.contorller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;

@Controller
public class LogoutEndpoint {
	private static Logger _logger = LoggerFactory.getLogger(LogoutEndpoint.class);
	
	@Autowired
    protected SessionManager sessionManager;
	
 	@RequestMapping(value={"/logout"}, produces = {MediaType.APPLICATION_JSON_VALUE})
 	public  ResponseEntity<?> logout(HttpServletRequest request,@CurrentUser UserInfo currentUser){
 		sessionManager.terminate(
 				currentUser.getSessionId(), 
 				currentUser.getId(),
 				currentUser.getUsername());
 		//invalidate http session
		_logger.debug("/logout invalidate http Session id {}",request.getSession().getId());
 		request.getSession().invalidate();
 		return new Message<String>().buildResponse();
 	}
 	
}
