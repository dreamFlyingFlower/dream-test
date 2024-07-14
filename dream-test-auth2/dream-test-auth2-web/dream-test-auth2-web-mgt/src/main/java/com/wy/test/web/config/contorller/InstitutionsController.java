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
 

package com.wy.test.web.config.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.authn.annotation.CurrentUser;
import com.wy.test.entity.Institutions;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.service.InstitutionsService;

@Controller
@RequestMapping(value={"/config/institutions"})
public class InstitutionsController {
		final static Logger _logger = LoggerFactory.getLogger(InstitutionsController.class);
		
		@Autowired
		private InstitutionsService institutionsService;
		
		@RequestMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
		public ResponseEntity<?> get(@CurrentUser UserInfo currentUser){
			Institutions institutions = institutionsService.get(currentUser.getInstId());
			return new Message<Institutions>(Message.SUCCESS,institutions).buildResponse();
		}
		
		@RequestMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
		public ResponseEntity<?> update(
				@RequestBody  Institutions institutions,
				@CurrentUser UserInfo currentUser,
				BindingResult result) {
			_logger.debug("updateRole institutions : "+institutions);
			if(institutionsService.update(institutions)) {
				return new Message<Institutions>(Message.SUCCESS).buildResponse();
			} else {
				return new Message<Institutions>(Message.FAIL).buildResponse();
			}
		}
}
