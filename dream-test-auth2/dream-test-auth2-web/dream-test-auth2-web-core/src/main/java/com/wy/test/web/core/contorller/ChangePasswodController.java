package com.wy.test.web.core.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstEntryType;
import com.wy.test.core.constant.ConstOperateAction;
import com.wy.test.core.constant.ConstOperateResult;
import com.wy.test.core.convert.PasswordPolicyConvert;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.PasswordSetType;
import com.wy.test.core.repository.PasswordPolicyValidator;
import com.wy.test.core.vo.PasswordPolicyVO;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.PasswordPolicyService;
import com.wy.test.persistence.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/config" })
@Slf4j
public class ChangePasswodController {

	@Autowired
	private UserService userService;

	@Autowired
	private HistorySysLogService historySysLogService;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@PostMapping(value = { "/passwordpolicy" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> passwordpolicy(@CurrentUser UserEntity currentUser) {
		PasswordPolicyEntity passwordPolicy =
				passwordPolicyService.listOne(PasswordPolicyEntity.builder().instId(currentUser.getInstId()).build());
		PasswordPolicyVO passwordPolicyVO = PasswordPolicyConvert.INSTANCE.convertt(passwordPolicy);
		// 构建密码强度说明
		passwordPolicyVO.buildMessage();
		return new Message<>(passwordPolicyVO).buildResponse();
	}

	@PostMapping(value = { "/changePassword" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> changePasswod(@RequestBody ChangePassword changePassword,
			@CurrentUser UserEntity currentUser) {

		changePassword.setUserId(currentUser.getId());
		changePassword.setUsername(currentUser.getUsername());
		changePassword.setInstId(currentUser.getInstId());
		changePassword.setPasswordSetType(PasswordSetType.PASSWORD_NORMAL.ordinal());
		if (userService.changePassword(changePassword)) {
			historySysLogService.insert(ConstEntryType.USERINFO, changePassword, ConstOperateAction.CHANGE_PASSWORD,
					ConstOperateResult.SUCCESS, currentUser);
			return new Message<ChangePassword>().buildResponse();
		} else {
			String message = (String) WebContext.getAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT);
			log.info("-message:", message);
			return new Message<ChangePassword>(Message.ERROR, message).buildResponse();
		}
	}
}