package com.wy.test.web.mgt.contorller.config;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.PasswordPolicyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/passwordpolicy" })
@Slf4j
public class PasswordPolicyController {

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		PasswordPolicyEntity passwordPolicy = passwordPolicyService.getById(currentUser.getInstId());
		return new Message<PasswordPolicyEntity>(passwordPolicy).buildResponse();
	}

	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@Valid @RequestBody PasswordPolicyEntity passwordPolicy,
			@CurrentUser UserEntity currentUser, BindingResult result) {
		log.debug("updateRole passwordPolicy : " + passwordPolicy);
		// Message message = this.validate(result, passwordPolicy);

		if (passwordPolicyService.updateById(passwordPolicy)) {
			return new Message<PasswordPolicyEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<PasswordPolicyEntity>(Message.ERROR).buildResponse();
		}
	}

	public Message<?> validate(BindingResult result, PasswordPolicyEntity passwordPolicy) {
		if (result.hasErrors()) {
			return new Message<>(result);
		}
		if (passwordPolicy.getMinLength() < 3) {
			FieldError fe = new FieldError("passwordPolicy", "minLength", passwordPolicy.getMinLength(), true,
					new String[] { "ui.passwordpolicy.xe00000001" }, // 密码最小长度不能小于3位字符
					null, null);
			result.addError(fe);
			return new Message<>(result);
		}
		if (passwordPolicy.getMinLength() > passwordPolicy.getMaxLength()) {
			FieldError fe = new FieldError("passwordPolicy", "maxLength", passwordPolicy.getMinLength(), true,
					new String[] { "ui.passwordpolicy.xe00000002" }, // 密码最大长度不能小于最小长度
					null, null);
			result.addError(fe);
			return new Message<>(result);
		}

		if (passwordPolicy.getDigits() + passwordPolicy.getLowerCase() + passwordPolicy.getUpperCase()
				+ passwordPolicy.getSpecialChar() < 2) {
			FieldError fe = new FieldError("passwordPolicy", "specialChar", 2, true,
					new String[] { "ui.passwordpolicy.xe00000003" }, // 密码包含小写字母、大写字母、数字、特殊字符的个数不能小于2
					null, null);
			result.addError(fe);
			return new Message<>(result);
		}

		if (passwordPolicy.getDigits() + passwordPolicy.getLowerCase() + passwordPolicy.getUpperCase()
				+ passwordPolicy.getSpecialChar() > passwordPolicy.getMaxLength()) {
			FieldError fe = new FieldError("passwordPolicy", "specialChar", passwordPolicy.getMinLength(), true,
					new String[] { "ui.passwordpolicy.xe00000004" }, // 密码包含小写字母、大写字母、数字、特殊字符的个数不能大于密码的最大长度
					null, null);
			result.addError(fe);
			return new Message<>(result);
		}
		return null;
	}
}