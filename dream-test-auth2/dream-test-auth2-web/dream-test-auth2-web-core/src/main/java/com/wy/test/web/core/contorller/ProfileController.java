package com.wy.test.web.core.contorller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.service.FileUploadService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.lang.StrHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/profile" })
@Slf4j
@AllArgsConstructor
public class ProfileController {

	private UserService userInfoService;

	private UserConvert userConvert;

	private FileUploadService fileUploadService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		UserEntity userInfo = userInfoService.findByUsername(currentUser.getUsername());
		UserVO userVO = userConvert.convertt(userInfo);
		userVO.trans();
		return new Message<>(userVO).buildResponse();
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
	public ResponseEntity<?> update(@RequestBody UserVO userInfo, @CurrentUser UserEntity currentUser,
			BindingResult result) {
		log.debug(userInfo.toString());

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
			userInfo.setPicture(fileUploadService.getById(userInfo.getPictureId()).getUploaded());
			fileUploadService.removeById(userInfo.getPictureId());
		}

		if (userInfoService.updateProfile(userInfo) > 0) {
			return new Message<UserEntity>(Message.SUCCESS).buildResponse();
		}

		return new Message<UserEntity>(Message.FAIL).buildResponse();
	}
}