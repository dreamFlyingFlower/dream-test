package com.wy.test.web.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.PasswordSetType;
import com.wy.test.core.repository.PasswordPolicyValidator;
import com.wy.test.core.vo.PasswordPolicyVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.PasswordPolicyService;
import com.wy.test.persistence.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用{@link ProfileController}相同接口
 *
 * @author 飞花梦影
 * @date 2024-10-10 17:18:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Tag(name = "1-6 修改密码API")
@Slf4j
@RestController
@RequestMapping(value = { "/config" })
@Deprecated
public class ChangePasswodController {

	@Autowired
	private UserService userService;

	@Autowired
	private HistorySysLogService historySysLogService;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

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