package com.wy.test.web.web.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.constants.ConstsEntryType;
import com.wy.test.constants.ConstsOperateAction;
import com.wy.test.constants.ConstsOperateResult;
import com.wy.test.constants.ConstsPasswordSetType;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.entity.ChangePassword;
import com.wy.test.entity.Message;
import com.wy.test.entity.PasswordPolicy;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.repository.PasswordPolicyValidator;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.PasswordPolicyService;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.web.WebContext;

@Controller
@RequestMapping(value={"/config"})
public class ChangePasswodController {
	final static Logger _logger = LoggerFactory.getLogger(ChangePasswodController.class);

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	HistorySystemLogsService systemLog;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@RequestMapping(value={"/passwordpolicy"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> passwordpolicy(@CurrentUser UserInfo currentUser){
		PasswordPolicy passwordPolicy = passwordPolicyService.get(currentUser.getInstId());
		//构建密码强度说明
		passwordPolicy.buildMessage();
		return new Message<PasswordPolicy>(passwordPolicy).buildResponse();
	}


	@ResponseBody
	@RequestMapping(value = { "/changePassword" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> changePasswod(
			@RequestBody ChangePassword changePassword,
			@CurrentUser UserInfo currentUser) {

		changePassword.setUserId(currentUser.getId());
		changePassword.setUsername(currentUser.getUsername());
		changePassword.setInstId(currentUser.getInstId());
		changePassword.setPasswordSetType(ConstsPasswordSetType.PASSWORD_NORMAL);
		if(userInfoService.changePassword(changePassword)) {
			systemLog.insert(
					ConstsEntryType.USERINFO,
					changePassword,
					ConstsOperateAction.CHANGE_PASSWORD,
					ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<ChangePassword>().buildResponse();
		}else {
			String message = (String) WebContext.getAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT);
			_logger.info("-message:",message);
			return new Message<ChangePassword>(Message.ERROR,message).buildResponse();
		}
	}

}