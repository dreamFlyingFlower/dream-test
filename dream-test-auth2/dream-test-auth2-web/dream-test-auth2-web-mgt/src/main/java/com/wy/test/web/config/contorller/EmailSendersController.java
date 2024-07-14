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
 

package com.wy.test.web.config.contorller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authn.annotation.CurrentUser;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.EmailSenders;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.service.EmailSendersService;

@Controller
@RequestMapping(value={"/config/emailsenders"})
public class EmailSendersController {
	final static Logger _logger = LoggerFactory.getLogger(EmailSendersController.class);
	
	@Autowired
	private EmailSendersService emailSendersService;

	@RequestMapping(value={"/get"})
	public ResponseEntity<?> get(@CurrentUser UserInfo currentUser){
		EmailSenders emailSenders = emailSendersService.get(currentUser.getInstId());
		if(emailSenders != null && StringUtils.isNotBlank(emailSenders.getCredentials())) {
			emailSenders.setCredentials(PasswordReciprocal.getInstance().decoder(emailSenders.getCredentials()));
		}else {
			emailSenders =new EmailSenders();
			emailSenders.setProtocol("smtp");
			emailSenders.setEncoding("utf-8");
		}
		return new Message<EmailSenders>(emailSenders).buildResponse();	
	}

	@RequestMapping(value={"/update"})
	@ResponseBody
	public ResponseEntity<?> update( @RequestBody  EmailSenders emailSenders,@CurrentUser UserInfo currentUser,BindingResult result) {
		_logger.debug("update emailSenders : "+emailSenders);
		emailSenders.setInstId(currentUser.getInstId());
		emailSenders.setCredentials(PasswordReciprocal.getInstance().encode(emailSenders.getCredentials()));
		if(StringUtils.isBlank(emailSenders.getId())) {
			emailSenders.setId(emailSenders.getInstId());
			if(emailSendersService.insert(emailSenders)) {
				return new Message<EmailSenders>(Message.SUCCESS).buildResponse();
			}else {
				return new Message<EmailSenders>(Message.ERROR).buildResponse();
			}
		}else {
			if(emailSendersService.update(emailSenders)) {
				return new Message<EmailSenders>(Message.SUCCESS).buildResponse();
			}else {
				return new Message<EmailSenders>(Message.ERROR).buildResponse();
			}
		}
		
	}
}
