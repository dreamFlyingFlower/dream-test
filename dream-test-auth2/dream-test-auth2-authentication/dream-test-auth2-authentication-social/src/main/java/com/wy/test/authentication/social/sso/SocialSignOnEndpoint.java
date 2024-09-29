package com.wy.test.authentication.social.sso;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.authentication.core.jwt.AuthJwt;
import com.wy.test.authentication.social.request.AuthDreamRequest;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.convert.SocialProviderConvert;
import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.uuid.UUID;
import com.wy.test.core.vo.SocialProviderVO;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.request.AuthRequest;

@Controller
@RequestMapping(value = "/logon/oauth20")
@Slf4j
public class SocialSignOnEndpoint extends AbstractSocialSignOnEndpoint {

	@Autowired
	private SocialProviderConvert socialProviderConvert;

	@GetMapping(value = { "/authorize/{provider}" })
	@ResponseBody
	public ResponseEntity<?> authorize(HttpServletRequest request, @PathVariable String provider) {
		log.trace("SocialSignOn provider : " + provider);
		String instId = AuthWebContext.getInst().getId();
		String originURL = AuthWebContext.getContextPath(request, false);
		String authorizationUrl = buildAuthRequest(instId, provider, originURL + dreamServerProperties.getFrontendUri())
				.authorize(authTokenService.genRandomJwt());

		log.trace("authorize SocialSignOn : " + authorizationUrl);
		return new ResultResponse<Object>((Object) authorizationUrl).buildResponse();
	}

	@GetMapping(value = { "/scanqrcode/{provider}" })
	@ResponseBody
	public ResponseEntity<?> scanQRCode(HttpServletRequest request, @PathVariable("provider") String provider) {
		String instId = AuthWebContext.getInst().getId();
		String originURL = AuthWebContext.getContextPath(request, false);
		AuthRequest authRequest =
				buildAuthRequest(instId, provider, originURL + dreamServerProperties.getFrontendUri());

		if (authRequest == null) {
			log.error("build authRequest fail .");
		}
		String state = UUID.generate().toString();
		// String state = authTokenService.genRandomJwt();
		authRequest.authorize(state);

		SocialProviderEntity socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
		SocialProviderEntity scanQrProvider = new SocialProviderEntity(socialSignOnProvider);
		SocialProviderVO socialProviderVO = socialProviderConvert.convertt(scanQrProvider);
		socialProviderVO.setStatus(state);
		socialProviderVO.setRedirectUri(socialSignOnProviderService
				.getRedirectUri(originURL + dreamServerProperties.getFrontendUri(), provider));
		// 缓存state票据在缓存或者是redis中五分钟过期
		if (provider.equalsIgnoreCase(AuthDreamRequest.KEY)) {
			socialSignOnProviderService.setToken(state);
		}

		return new ResultResponse<SocialProviderVO>(socialProviderVO).buildResponse();
	}

	@GetMapping(value = { "/bind/{provider}" })
	public ResponseEntity<?> bind(@PathVariable String provider, @CurrentUser UserEntity userInfo,
			HttpServletRequest request) {
		// auth call back may exception
		try {
			String originURL = AuthWebContext.getContextPath(request, false);
			SocialAssociateEntity socialsAssociate = this.authCallback(userInfo.getInstId(), provider,
					originURL + dreamServerProperties.getFrontendUri());
			socialsAssociate.setSocialUserInfo(accountJsonString);
			socialsAssociate.setUserId(userInfo.getId());
			socialsAssociate.setUsername(userInfo.getUsername());
			socialsAssociate.setInstId(userInfo.getInstId());
			// socialsAssociate.setAccessToken(JsonUtils.object2Json(accessToken));
			// socialsAssociate.setExAttribute(JsonUtils.object2Json(accessToken.getResponseObject()));
			log.debug("Social Bind : " + socialsAssociate);
			this.socialsAssociateService.delete(socialsAssociate);
			this.socialsAssociateService.insert(socialsAssociate);
			return new ResultResponse<AuthJwt>().buildResponse();
		} catch (Exception e) {
			log.error("callback Exception  ", e);
		}

		return new ResultResponse<AuthJwt>(ResultResponse.ERROR).buildResponse();
	}

	@GetMapping(value = { "/callback/{provider}" })
	public ResponseEntity<?> callback(@PathVariable String provider, HttpServletRequest request) {
		// auth call back may exception
		try {
			String originURL = AuthWebContext.getContextPath(request, false);
			String instId = AuthWebContext.getInst().getId();
			SocialAssociateEntity socialsAssociate =
					this.authCallback(instId, provider, originURL + dreamServerProperties.getFrontendUri());

			SocialAssociateEntity socialssssociate1 = this.socialsAssociateService.get(socialsAssociate);

			log.debug("Loaded SocialSignOn Socials Associate : " + socialssssociate1);

			if (null == socialssssociate1) {
				// 如果存在第三方ID并且在数据库无法找到映射关系，则进行绑定逻辑
				if (StringUtils.isNotEmpty(socialsAssociate.getSocialUserId())) {
					// 返回message为第三方用户标识
					return new ResultResponse<AuthJwt>(ResultResponse.PROMPT, socialsAssociate.getSocialUserId())
							.buildResponse();
				}
			}

			socialsAssociate = socialssssociate1;
			log.debug("Social Sign On from {} mapping to user {}", socialsAssociate.getProvider(),
					socialsAssociate.getUsername());

			LoginCredential loginCredential =
					new LoginCredential(socialsAssociate.getUsername(), "", AuthLoginType.SOCIAL_SIGN_ON.getMsg());
			SocialProviderEntity socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
			loginCredential.setProvider(socialSignOnProvider.getProviderName());

			Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
			// socialsAssociate.setAccessToken(JsonUtils.object2Json(this.accessToken));
			socialsAssociate.setSocialUserInfo(accountJsonString);
			// socialsAssociate.setExAttribute(JsonUtils.object2Json(accessToken.getResponseObject()));

			this.socialsAssociateService.update(socialsAssociate);
			return new ResultResponse<AuthJwt>(authTokenService.genAuthJwt(authentication)).buildResponse();
		} catch (Exception e) {
			log.error("callback Exception  ", e);
			return new ResultResponse<AuthJwt>(ResultResponse.ERROR).buildResponse();
		}
	}

	/**
	 * 提供给第三方应用关联用户接口
	 * 
	 * @return
	 */
	@PostMapping(value = { "/workweixin/qr/auth/login" })
	public ResponseEntity<?> qrAuthLogin(@RequestParam Map<String, String> param, HttpServletRequest request) {

		try {
			if (null == param) {
				return new ResultResponse<AuthJwt>(ResultResponse.ERROR).buildResponse();
			}
			String token = param.get("token");
			String username = param.get("username");
			// 判断token是否合法
			String redisusername = this.socialSignOnProviderService.getToken(token);
			if (StringUtils.isNotEmpty(redisusername)) {
				// 设置token和用户绑定
				boolean flag = this.socialSignOnProviderService.bindtoken(token, username);
				if (flag) {
					return new ResultResponse<AuthJwt>().buildResponse();
				}
			} else {
				return new ResultResponse<AuthJwt>(ResultResponse.WARNING, "Invalid token").buildResponse();
			}
		} catch (Exception e) {
			log.error("qrAuthLogin Exception  ", e);
		}
		return new ResultResponse<AuthJwt>(ResultResponse.ERROR).buildResponse();
	}

	/**
	 * 监听扫码回调
	 * 
	 * @param provider
	 * @param state
	 * @param request
	 * @return
	 */
	@GetMapping(value = { "/qrcallback/{provider}/{state}" })
	public ResponseEntity<?> qrcallback(@PathVariable String provider, @PathVariable String state,
			HttpServletRequest request) {
		try {
			// 判断只有dream扫码
			if (!provider.equalsIgnoreCase(AuthDreamRequest.KEY)) {
				return new ResultResponse<AuthJwt>(ResultResponse.ERROR).buildResponse();
			}

			String loginName = socialSignOnProviderService.getToken(state);
			if (StringUtils.isEmpty(loginName)) {
				// 二维码过期
				return new ResultResponse<AuthJwt>(ResultResponse.PROMPT).buildResponse();
			}
			if ("-1".equalsIgnoreCase(loginName)) {
				// 暂无用户扫码
				return new ResultResponse<AuthJwt>(ResultResponse.WARNING).buildResponse();
			}
			String instId = AuthWebContext.getInst().getId();

			SocialAssociateEntity socialsAssociate = new SocialAssociateEntity();
			socialsAssociate.setProvider(provider);
			socialsAssociate.setSocialUserId(loginName);
			socialsAssociate.setInstId(instId);

			socialsAssociate = this.socialsAssociateService.get(socialsAssociate);

			log.debug("qrcallback Loaded SocialSignOn Socials Associate : " + socialsAssociate);

			if (null == socialsAssociate) {
				return new ResultResponse<AuthJwt>(ResultResponse.ERROR).buildResponse();
			}

			log.debug("qrcallback Social Sign On from {} mapping to user {}", socialsAssociate.getProvider(),
					socialsAssociate.getUsername());

			LoginCredential loginCredential =
					new LoginCredential(socialsAssociate.getUsername(), "", AuthLoginType.SOCIAL_SIGN_ON.getMsg());
			SocialProviderEntity socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
			loginCredential.setProvider(socialSignOnProvider.getProviderName());

			Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
			// socialsAssociate.setAccessToken(JsonUtils.object2Json(this.accessToken));
			socialsAssociate.setSocialUserInfo(accountJsonString);
			// socialsAssociate.setExAttribute(JsonUtils.object2Json(accessToken.getResponseObject()));

			this.socialsAssociateService.update(socialsAssociate);
			return new ResultResponse<AuthJwt>(authTokenService.genAuthJwt(authentication)).buildResponse();
		} catch (Exception e) {
			log.error("qrcallback Exception  ", e);
			return new ResultResponse<AuthJwt>(ResultResponse.ERROR).buildResponse();
		}
	}
}