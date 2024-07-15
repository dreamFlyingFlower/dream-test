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
import com.wy.test.entity.apps.AppsTokenBasedDetails;
import com.wy.test.persistence.service.AppsTokenBasedDetailsService;


@Controller
@RequestMapping(value={"/apps/tokenbased"})
public class TokenBasedDetailsController  extends BaseAppContorller {
	final static Logger _logger = LoggerFactory.getLogger(TokenBasedDetailsController.class);
	
	@Autowired
	AppsTokenBasedDetailsService tokenBasedDetailsService;
	
	@RequestMapping(value = { "/init" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> init() {
		AppsTokenBasedDetails tokenBasedDetails =new AppsTokenBasedDetails();
		tokenBasedDetails.setId(tokenBasedDetails.generateId());
		tokenBasedDetails.setProtocol(ConstsProtocols.TOKENBASED);
		tokenBasedDetails.setSecret(ReciprocalUtils.generateKey(ReciprocalUtils.Algorithm.AES));
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setUserPropertys("userPropertys");
		return new Message<AppsTokenBasedDetails>(tokenBasedDetails).buildResponse();
	}
	
	@RequestMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppsTokenBasedDetails tokenBasedDetails=tokenBasedDetailsService.getAppDetails(id , false);
		decoderSecret(tokenBasedDetails);
		String algorithmKey=passwordReciprocal.decoder(tokenBasedDetails.getAlgorithmKey());
		tokenBasedDetails.setAlgorithmKey(algorithmKey);
		tokenBasedDetails.transIconBase64();
		return new Message<AppsTokenBasedDetails>(tokenBasedDetails).buildResponse();
	}
	
	@ResponseBody
	@RequestMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> add(
			@RequestBody AppsTokenBasedDetails tokenBasedDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + tokenBasedDetails);
		
		transform(tokenBasedDetails);
		
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setInstId(currentUser.getInstId());
		if (tokenBasedDetailsService.insert(tokenBasedDetails)
				&&appsService.insertApp(tokenBasedDetails)) {
			return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> update(
			@RequestBody AppsTokenBasedDetails tokenBasedDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + tokenBasedDetails);
		transform(tokenBasedDetails);
		tokenBasedDetails.setAlgorithmKey(tokenBasedDetails.getSecret());
		tokenBasedDetails.setInstId(currentUser.getInstId());
		if (tokenBasedDetailsService.update(tokenBasedDetails)
				&&appsService.updateApp(tokenBasedDetails)) {
		    return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> delete(
			@RequestParam("ids") String ids,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} " , ids);
		if (tokenBasedDetailsService.deleteBatch(ids)&&appsService.deleteBatch(ids)) {
			 return new Message<AppsJwtDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsJwtDetails>(Message.FAIL).buildResponse();
		}
	}
	
}
