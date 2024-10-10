package com.wy.test.web.core.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstLogEntryType;
import com.wy.test.core.constant.ConstLogOperateType;
import com.wy.test.core.constant.ConstOperateResult;
import com.wy.test.core.convert.PasswordPolicyConvert;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.PasswordSetType;
import com.wy.test.core.repository.PasswordPolicyValidator;
import com.wy.test.core.vo.PasswordPolicyVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.FileUploadService;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.PasswordPolicyService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.lang.StrHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-2 用户信息API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = { "/config/profile" })
public class ProfileController {

	final UserService userService;

	final UserConvert userConvert;

	final FileUploadService fileUploadService;

	final HistorySysLogService historySysLogService;

	final PasswordPolicyService passwordPolicyService;

	@Operation(summary = "获取用户信息", description = "获取用户信息", method = "GET")
	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserVO currentUser) {
		UserEntity userInfo = userService.findByUsername(currentUser.getUsername());
		UserVO userVO = userConvert.convertt(userInfo);
		userVO.trans();
		return new ResultResponse<>(userVO).buildResponse();
	}

	@Operation(summary = "修改用户信息", description = "修改用户信息", method = "POST")
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

		if (userService.updateProfile(userInfo) > 0) {
			return new ResultResponse<UserEntity>(ResultResponse.SUCCESS).buildResponse();
		}

		return new ResultResponse<UserEntity>(ResultResponse.FAIL).buildResponse();
	}

	@Operation(summary = "获得密码策略", description = "获得密码策略", method = "POST")
	@PostMapping(value = { "/passwordpolicy" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> passwordpolicy(@CurrentUser UserEntity currentUser) {
		PasswordPolicyEntity passwordPolicy =
				passwordPolicyService.listOne(PasswordPolicyEntity.builder().instId(currentUser.getInstId()).build());
		PasswordPolicyVO passwordPolicyVO = PasswordPolicyConvert.INSTANCE.convertt(passwordPolicy);
		// 构建密码强度说明
		passwordPolicyVO.buildMessage();
		return new ResultResponse<>(passwordPolicyVO).buildResponse();
	}

	@Operation(summary = "修改密码", description = "修改密码", method = "POST")
	@PostMapping(value = { "/changePassword" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> changePasswod(@RequestBody ChangePassword changePassword,
			@CurrentUser UserEntity currentUser) {

		changePassword.setUserId(currentUser.getId());
		changePassword.setUsername(currentUser.getUsername());
		changePassword.setInstId(currentUser.getInstId());
		changePassword.setPasswordSetType(PasswordSetType.PASSWORD_NORMAL.ordinal());
		if (userService.changePassword(changePassword)) {
			historySysLogService.insert(ConstLogEntryType.USERINFO, changePassword, ConstLogOperateType.CHANGE_PASSWORD,
					ConstOperateResult.SUCCESS, currentUser);
			return new ResultResponse<ChangePassword>().buildResponse();
		} else {
			String message =
					(String) AuthWebContext.getAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT);
			log.info("-message:", message);
			return new ResultResponse<ChangePassword>(ResultResponse.ERROR, message).buildResponse();
		}
	}
}