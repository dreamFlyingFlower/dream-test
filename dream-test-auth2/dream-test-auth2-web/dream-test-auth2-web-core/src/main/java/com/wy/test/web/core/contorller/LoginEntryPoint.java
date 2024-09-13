package com.wy.test.web.core.contorller;

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

import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.authentication.core.jwt.AuthJwt;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.otp.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.provider.provider.AbstractAuthenticationProvider;
import com.wy.test.authentication.provider.support.kerberos.KerberosService;
import com.wy.test.authentication.provider.support.rememberme.AbstractRemeberMeManager;
import com.wy.test.authentication.provider.support.rememberme.RemeberMe;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.authentication.social.sso.service.SocialSignOnProviderService;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.SocialAssociateService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-1-登录接口文档模块")
@RestController
@RequestMapping(value = "/login")
@Slf4j
@RequiredArgsConstructor
public class LoginEntryPoint {

	Pattern mobileRegex = Pattern.compile("^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\\\d{8}$");

	private final AuthTokenService authTokenService;

	private final DreamAuthLoginProperties dreamLoginProperties;

	private final AbstractAuthenticationProvider authenticationProvider;

	private final SocialSignOnProviderService socialSignOnProviderService;

	private final SocialAssociateService socialsAssociatesService;

	private final KerberosService kerberosService;

	private final UserService userInfoService;

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
						return new ResultResponse<AuthJwt>(authJwt).buildResponse();
					}
				}
			} catch (ParseException e) {
			}
		}
		// for normal login
		HashMap<String, Object> model = new HashMap<>();
		model.put("isRemeberMe", dreamLoginProperties.isRememberMe());
		model.put("isKerberos", dreamLoginProperties.getKerberos().isEnabled());
		if (dreamLoginProperties.getMfa().isEnabled()) {
			model.put("otpType", tfaOtpAuthn.getOtpType());
			model.put("otpInterval", tfaOtpAuthn.getInterval());
		}

		if (dreamLoginProperties.getKerberos().isEnabled()) {
			model.put("userDomainUrlJson", kerberosService.buildKerberosProxys());
		}

		InstitutionEntity inst = (InstitutionEntity) AuthWebContext.getAttribute(ConstAuthWeb.CURRENT_INST);
		model.put("inst", inst);
		if (dreamLoginProperties.getCaptcha().isEnabled()) {
			model.put("captcha", "true");
		} else {
			model.put("captcha", inst.getCaptcha());
		}
		model.put("state", authTokenService.genRandomJwt());
		// load Social Sign On Providers
		model.put("socials", socialSignOnProviderService.loadSocials(inst.getId()));

		return new ResultResponse<>(model).buildResponse();
	}

	@GetMapping(value = { "/sendotp/{mobile}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceOtp(@PathVariable("mobile") String mobile) {
		UserEntity userInfo = userInfoService.findByEmailMobile(mobile);
		if (userInfo != null) {
			smsAuthnService.getByInstId(AuthWebContext.getInst().getId()).produce(userInfo);
			return new ResultResponse<>(ResultResponse.SUCCESS).buildResponse();
		}

		return new ResultResponse<>(ResultResponse.FAIL).buildResponse();
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

		UserEntity userInfo = userInfoService.findByEmailMobile(mobile);
		// 验证码验证是否合法
		if (smsAuthnService.getByInstId(AuthWebContext.getInst().getId()).validate(userInfo, code)) {
			// 合法进行用户绑定
			SocialAssociateEntity socialsAssociate = new SocialAssociateEntity();
			socialsAssociate.setUserId(userInfo.getId());
			socialsAssociate.setUsername(userInfo.getUsername());
			socialsAssociate.setProvider(authType.getMsg());
			socialsAssociate.setSocialUserId(username);
			socialsAssociate.setInstId(userInfo.getInstId());
			// 插入dream和社交服务的用户映射表
			socialsAssociatesService.save(socialsAssociate);

			// 设置完成后,进行登录认证
			LoginCredential loginCredential =
					new LoginCredential(socialsAssociate.getUsername(), "", AuthLoginType.SOCIALSIGNON);

			SocialProviderEntity socialSignOnProvider =
					socialSignOnProviderService.get(socialsAssociate.getInstId(), socialsAssociate.getProvider());

			loginCredential.setProvider(socialSignOnProvider.getProviderName());

			Authentication authentication = authenticationProvider.authenticate(loginCredential, true);

			return new ResultResponse<AuthJwt>(authTokenService.genAuthJwt(authentication)).buildResponse();

		}
		return new ResultResponse<AuthJwt>(ResultResponse.FAIL).buildResponse();
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
		ResultResponse<AuthJwt> authJwtMessage = new ResultResponse<>(ResultResponse.FAIL);
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
					if (AuthWebContext.getAttribute(ConstAuthWeb.CURRENT_USER_PASSWORD_SET_TYPE) != null)
						authJwt.setPasswordSetType(
								(Integer) AuthWebContext.getAttribute(ConstAuthWeb.CURRENT_USER_PASSWORD_SET_TYPE));
					authJwtMessage = new ResultResponse<>(authJwt);

				} else {
					// fail
					String errorMsg = AuthWebContext.getAttribute(ConstAuthWeb.LOGIN_ERROR_SESSION_MESSAGE) == null ? ""
							: AuthWebContext.getAttribute(ConstAuthWeb.LOGIN_ERROR_SESSION_MESSAGE).toString();
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
				return new ResultResponse<>(authJwt).buildResponse();
			}
		}
		return new ResultResponse<>(ResultResponse.FAIL).buildResponse();
	}
}