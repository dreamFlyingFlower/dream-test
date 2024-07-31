package com.wy.test.web.web.contorller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.jwt.AuthJwt;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.entity.Institutions;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.SocialsAssociate;
import com.wy.test.core.entity.SocialsProvider;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.persistence.service.SocialsAssociatesService;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;
import com.wy.test.provider.authn.support.kerberos.KerberosService;
import com.wy.test.provider.authn.support.rememberme.AbstractRemeberMeManager;
import com.wy.test.provider.authn.support.rememberme.RemeberMe;
import com.wy.test.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.social.authn.support.socialsignon.service.SocialSignOnProviderService;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-1-登录接口文档模块")
@RestController
@RequestMapping(value = "/login")
@Slf4j
@AllArgsConstructor
public class LoginEntryPoint {

	Pattern mobileRegex = Pattern.compile("^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\\\d{8}$");

	private final AuthTokenService authTokenService;

	private final DreamAuthLoginProperties dreamLoginProperties;

	private final AbstractAuthenticationProvider authenticationProvider;

	private final SocialSignOnProviderService socialSignOnProviderService;

	private final SocialsAssociatesService socialsAssociatesService;

	private final KerberosService kerberosService;

	private final UserInfoService userInfoService;

	private final AbstractOtpAuthn tfaOtpAuthn;

	private final SmsOtpAuthnService smsAuthnService;

	private final AbstractRemeberMeManager remeberMeManager;

	/**
	 * init login
	 * 
	 * @return
	 */
	@Operation(summary = "登录接口", description = "用户登录地址", method = "GET")
	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@RequestParam(value = "remember_me", required = false) String rememberMeJwt) {
		log.debug("/get.");
		// Remember Me
		if (StringUtils.isNotBlank(rememberMeJwt) && authTokenService.validateJwtToken(rememberMeJwt)) {
			try {
				RemeberMe remeberMe = remeberMeManager.resolve(rememberMeJwt);
				if (remeberMe != null) {
					LoginCredential credential = new LoginCredential();
					String remeberMeJwt = remeberMeManager.updateRemeberMe(remeberMe);
					credential.setUsername(remeberMe.getUsername());
					Authentication authentication = authenticationProvider.authenticate(credential, true);
					if (authentication != null) {
						AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
						authJwt.setRemeberMe(remeberMeJwt);
						return new Message<AuthJwt>(authJwt).buildResponse();
					}
				}
			} catch (ParseException e) {
			}
		}
		// for normal login
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("isRemeberMe", dreamLoginProperties.isRememberMe());
		model.put("isKerberos", dreamLoginProperties.getKerberos().isEnabled());
		if (dreamLoginProperties.isMfa()) {
			model.put("otpType", tfaOtpAuthn.getOtpType());
			model.put("otpInterval", tfaOtpAuthn.getInterval());
		}

		if (dreamLoginProperties.getKerberos().isEnabled()) {
			model.put("userDomainUrlJson", kerberosService.buildKerberosProxys());
		}

		Institutions inst = (Institutions) WebContext.getAttribute(WebConstants.CURRENT_INST);
		model.put("inst", inst);
		if (dreamLoginProperties.isCaptcha()) {
			model.put("captcha", "true");
		} else {
			model.put("captcha", inst.getCaptcha());
		}
		model.put("state", authTokenService.genRandomJwt());
		// load Social Sign On Providers
		model.put("socials", socialSignOnProviderService.loadSocials(inst.getId()));

		return new Message<HashMap<String, Object>>(model).buildResponse();
	}

	@GetMapping(value = { "/sendotp/{mobile}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceOtp(@PathVariable("mobile") String mobile) {
		UserInfo userInfo = userInfoService.findByEmailMobile(mobile);
		if (userInfo != null) {
			smsAuthnService.getByInstId(WebContext.getInst().getId()).produce(userInfo);
			return new Message<AuthJwt>(Message.SUCCESS).buildResponse();
		}

		return new Message<AuthJwt>(Message.FAIL).buildResponse();
	}

	@PostMapping(value = { "/signin/bindusersocials" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> bindusersocials(@RequestBody LoginCredential credential) {
		// 短信验证码
		String code = credential.getCode();
		// 映射社交服务的账号
		String username = credential.getUsername();
		// dream存储的手机号
		String mobile = credential.getMobile();
		// 社交服务类型
		AuthLoginType authType = credential.getAuthLoginType();

		UserInfo userInfo = userInfoService.findByEmailMobile(mobile);
		// 验证码验证是否合法
		if (smsAuthnService.getByInstId(WebContext.getInst().getId()).validate(userInfo, code)) {
			// 合法进行用户绑定
			SocialsAssociate socialsAssociate = new SocialsAssociate();
			socialsAssociate.setUserId(userInfo.getId());
			socialsAssociate.setUsername(userInfo.getUsername());
			socialsAssociate.setProvider(authType.getMsg());
			socialsAssociate.setSocialUserId(username);
			socialsAssociate.setInstId(userInfo.getInstId());
			// 插入dream和社交服务的用户映射表
			socialsAssociatesService.insert(socialsAssociate);

			// 设置完成后，进行登录认证
			LoginCredential loginCredential =
					new LoginCredential(socialsAssociate.getUsername(), "", AuthLoginType.SOCIALSIGNON);

			SocialsProvider socialSignOnProvider =
					socialSignOnProviderService.get(socialsAssociate.getInstId(), socialsAssociate.getProvider());

			loginCredential.setProvider(socialSignOnProvider.getProviderName());

			Authentication authentication = authenticationProvider.authenticate(loginCredential, true);

			return new Message<AuthJwt>(authTokenService.genAuthJwt(authentication)).buildResponse();

		}
		return new Message<AuthJwt>(Message.FAIL).buildResponse();
	}

	/**
	 * normal
	 * 
	 * @param loginCredential
	 * @return
	 */
	@PostMapping(value = { "/signin" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> signin(HttpServletRequest request, HttpServletResponse response,
			@RequestBody LoginCredential credential) {
		Message<AuthJwt> authJwtMessage = new Message<AuthJwt>(Message.FAIL);
		if (authTokenService.validateJwtToken(credential.getState())) {
			AuthLoginType authType = credential.getAuthLoginType();
			log.debug("Login AuthN Type  " + authType);
			if (null != authType) {
				Authentication authentication = authenticationProvider.authenticate(credential);
				if (authentication != null) {
					AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
					if (StringUtils.isNotBlank(credential.getRemeberMe())
							&& credential.getRemeberMe().equalsIgnoreCase("true")) {
						String remeberMe = remeberMeManager.createRemeberMe(authentication, request, response);
						authJwt.setRemeberMe(remeberMe);
					}
					if (WebContext.getAttribute(WebConstants.CURRENT_USER_PASSWORD_SET_TYPE) != null)
						authJwt.setPasswordSetType(
								(Integer) WebContext.getAttribute(WebConstants.CURRENT_USER_PASSWORD_SET_TYPE));
					authJwtMessage = new Message<AuthJwt>(authJwt);

				} else {// fail
					String errorMsg = WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE) == null ? ""
							: WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE).toString();
					authJwtMessage.setMessage(errorMsg);
					log.debug("login fail , message {}", errorMsg);
				}
			} else {
				log.error("Login AuthN type must eq normal , tfa or mobile . ");
			}
		}
		return authJwtMessage.buildResponse();
	}

	/**
	 * for congress
	 * 
	 * @param loginCredential
	 * @return
	 */
	@PostMapping(value = { "/congress" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> congress(@RequestBody LoginCredential credential) {
		if (StringUtils.isNotBlank(credential.getCongress())) {
			AuthJwt authJwt = authTokenService.consumeCongress(credential.getCongress());
			if (authJwt != null) {
				return new Message<AuthJwt>(authJwt).buildResponse();
			}
		}
		return new Message<AuthJwt>(Message.FAIL).buildResponse();
	}
}