package com.wy.test.web.web.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.service.FileUploadService;
import com.wy.test.persistence.service.UserInfoService;

import dream.flying.flower.lang.StrHelper;

@Controller
@RequestMapping(value = { "/config/profile" })
public class ProfileController {

	static final Logger _logger = LoggerFactory.getLogger(ProfileController.class);

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	FileUploadService fileUploadService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserInfo currentUser) {
		UserInfo userInfo = userInfoService.findByUsername(currentUser.getUsername());
		userInfo.trans();
		return new Message<UserInfo>(userInfo).buildResponse();
	}

	/**
	 * 修改用户.
	 * 
	 * @param userInfo
	 * @param result
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody UserInfo userInfo, @CurrentUser UserInfo currentUser,
			BindingResult result) {
		_logger.debug(userInfo.toString());

		// if(userInfo.getExtraAttributeValue()!=null){
		// String []extraAttributeLabel=userInfo.getExtraAttributeName().split(",");
		// String []extraAttributeValue=userInfo.getExtraAttributeValue().split(",");
		// Map<String,String> extraAttributeMap=new HashMap<String,String> ();
		// for(int i=0;i<extraAttributeLabel.length;i++){
		// extraAttributeMap.put(extraAttributeLabel[i], extraAttributeValue[i]);
		// }
		// String extraAttribute=JsonUtils.object2Json(extraAttributeMap);
		// userInfo.setExtraAttribute(extraAttribute);
		// }
		if (StrHelper.isNotBlank(userInfo.getPictureId())) {
			userInfo.setPicture(fileUploadService.get(userInfo.getPictureId()).getUploaded());
			fileUploadService.remove(userInfo.getPictureId());
		}

		if (userInfoService.updateProfile(userInfo) > 0) {
			return new Message<UserInfo>(Message.SUCCESS).buildResponse();
		}

		return new Message<UserInfo>(Message.FAIL).buildResponse();
	}
}