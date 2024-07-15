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
 

package com.wy.test.web.apps.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.constants.ConstsProtocols;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.crypto.ReciprocalUtils;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.entity.apps.AppsJwtDetails;
import com.wy.test.persistence.service.AppsJwtDetailsService;


@Controller
@RequestMapping(value={"/apps/jwt"})
public class JwtDetailsController  extends BaseAppContorller {
	final static Logger _logger = LoggerFactory.getLogger(JwtDetailsController.class);
	
	@Autowired
	AppsJwtDetailsService jwtDetailsService;
	
	@RequestMapping(value = { "/init" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> init() {
		AppsJwtDetails jwtDetails =new AppsJwtDetails();
		jwtDetails.setId(jwtDetails.generateId());
		jwtDetails.setProtocol(ConstsProtocols.JWT);
		jwtDetails.setSecret(ReciprocalUtils.generateKey(""));
		jwtDetails.setUserPropertys("userPropertys");
		return new Message<AppsJwtDetails>(jwtDetails).buildResponse();
	}
	
	@RequestMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppsJwtDetails jwtDetails=jwtDetailsService.getAppDetails(id , false);
		decoderSecret(jwtDetails);
		jwtDetails.transIconBase64();
		return new Message<AppsJwtDetails>(jwtDetails).buildResponse();
	}
	
	@ResponseBody
	@RequestMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> insert(@RequestBody AppsJwtDetails jwtDetails,@CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + jwtDetails);
		
		transform(jwtDetails);
		
		jwtDetails.setInstId(currentUser.getInstId());
		if (jwtDetailsService.insert(jwtDetails)&&appsService.insertApp(jwtDetails)) {
			return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> update(@RequestBody AppsJwtDetails jwtDetails,@CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + jwtDetails);
		transform(jwtDetails);
		jwtDetails.setInstId(currentUser.getInstId());
		if (jwtDetailsService.update(jwtDetails)&&appsService.updateApp(jwtDetails)) {
		    return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> delete(@RequestParam("ids") String ids,@CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} " , ids);
		if (jwtDetailsService.deleteBatch(ids)&&appsService.deleteBatch(ids)) {
			 return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}
	
}
