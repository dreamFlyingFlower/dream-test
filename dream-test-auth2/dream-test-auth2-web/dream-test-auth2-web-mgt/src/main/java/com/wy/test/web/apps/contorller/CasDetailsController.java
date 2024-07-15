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
import com.wy.test.entity.apps.AppsCasDetails;
import com.wy.test.persistence.service.AppsCasDetailsService;


@Controller
@RequestMapping(value={"/apps/cas"})
public class CasDetailsController  extends BaseAppContorller {
	final static Logger _logger = LoggerFactory.getLogger(CasDetailsController.class);
	
	@Autowired
	AppsCasDetailsService casDetailsService;
	
	@RequestMapping(value = { "/init" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> init() {
		AppsCasDetails casDetails =new AppsCasDetails();
		casDetails.setId(casDetails.generateId());
		casDetails.setProtocol(ConstsProtocols.CAS);
		casDetails.setSecret(ReciprocalUtils.generateKey(""));
		return new Message<AppsCasDetails>(casDetails).buildResponse();
	}
	
	@RequestMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppsCasDetails casDetails=casDetailsService.getAppDetails(id , false);
		super.decoderSecret(casDetails);
		casDetails.transIconBase64();
		return new Message<AppsCasDetails>(casDetails).buildResponse();
	}
	
	@ResponseBody
	@RequestMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> insert(@RequestBody AppsCasDetails casDetails,@CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + casDetails);
		transform(casDetails);
		casDetails.setInstId(currentUser.getInstId());
		if (casDetailsService.insert(casDetails)&&appsService.insertApp(casDetails)) {
			return new Message<AppsCasDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsCasDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> update(@RequestBody AppsCasDetails casDetails,@CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + casDetails);
		transform(casDetails);
		casDetails.setInstId(currentUser.getInstId());
		if (casDetailsService.update(casDetails)&&appsService.updateApp(casDetails)) {
		    return new Message<AppsCasDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsCasDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> delete(@RequestParam("ids") String ids,@CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} " , ids);
		if (casDetailsService.deleteBatch(ids)&&appsService.deleteBatch(ids)) {
			 return new Message<AppsCasDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsCasDetails>(Message.FAIL).buildResponse();
		}
	}
	
}
