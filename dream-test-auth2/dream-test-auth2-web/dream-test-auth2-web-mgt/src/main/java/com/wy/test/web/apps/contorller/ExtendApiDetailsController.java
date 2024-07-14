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
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authn.annotation.CurrentUser;
import com.wy.test.constants.ConstsProtocols;
import com.wy.test.crypto.ReciprocalUtils;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.entity.apps.Apps;
import com.wy.test.entity.apps.AppsExtendApiDetails;


@Controller
@RequestMapping(value={"/apps/extendapi"})
public class ExtendApiDetailsController  extends BaseAppContorller {
	final static Logger _logger = LoggerFactory.getLogger(ExtendApiDetailsController.class);

	@RequestMapping(value = { "/init" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> init() {
		AppsExtendApiDetails extendApiDetails=new AppsExtendApiDetails();
		extendApiDetails.setId(extendApiDetails.generateId());
		extendApiDetails.setProtocol(ConstsProtocols.EXTEND_API);
		extendApiDetails.setSecret(ReciprocalUtils.generateKey(""));
		return new Message<AppsExtendApiDetails>(extendApiDetails).buildResponse();
	}
	
	@RequestMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Apps application= appsService.get(id);
		super.decoderSecret(application);
		AppsExtendApiDetails extendApiDetails=new AppsExtendApiDetails();
		BeanUtils.copyProperties(application, extendApiDetails);
		extendApiDetails.transIconBase64();
		return new Message<AppsExtendApiDetails>(extendApiDetails).buildResponse();
	}
	
	@ResponseBody
	@RequestMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> add(
			@RequestBody AppsExtendApiDetails extendApiDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + extendApiDetails);
		
		transform(extendApiDetails);
		extendApiDetails.setInstId(currentUser.getInstId());
		if (appsService.insertApp(extendApiDetails)) {
			return new Message<AppsExtendApiDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsExtendApiDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> update(
			@RequestBody AppsExtendApiDetails extendApiDetails,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + extendApiDetails);
		transform(extendApiDetails);
		extendApiDetails.setInstId(currentUser.getInstId());
		if (appsService.updateApp(extendApiDetails)) {
		    return new Message<AppsExtendApiDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsExtendApiDetails>(Message.FAIL).buildResponse();
		}
	}
	
	@ResponseBody
	@RequestMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> delete(
			@RequestParam("ids") String ids,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} " , ids);
		if (appsService.deleteBatch(ids)) {
			 return new Message<AppsExtendApiDetails>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsExtendApiDetails>(Message.FAIL).buildResponse();
		}
	}

}
