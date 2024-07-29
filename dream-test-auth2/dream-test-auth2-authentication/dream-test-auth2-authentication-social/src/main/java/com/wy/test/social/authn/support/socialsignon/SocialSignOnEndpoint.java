package com.wy.test.social.authn.support.socialsignon;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.jwt.AuthJwt;
import com.wy.test.core.constants.ConstsLoginType;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.SocialsAssociate;
import com.wy.test.core.entity.SocialsProvider;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.uuid.UUID;
import com.wy.test.core.web.WebContext;
import com.wy.test.social.zhyd.request.AuthDreamRequest;

import me.zhyd.oauth.request.AuthRequest;

@Controller
@RequestMapping(value = "/logon/oauth20")
public class SocialSignOnEndpoint extends AbstractSocialSignOnEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(SocialSignOnEndpoint.class);

	@GetMapping(value = { "/authorize/{provider}" })
	@ResponseBody
	public ResponseEntity<?> authorize(HttpServletRequest request, @PathVariable String provider) {
		_logger.trace("SocialSignOn provider : " + provider);
		String instId = WebContext.getInst().getId();
		String originURL = WebContext.getContextPath(request, false);
		String authorizationUrl = buildAuthRequest(instId, provider, originURL + applicationConfig.getFrontendUri())
				.authorize(authTokenService.genRandomJwt());

		_logger.trace("authorize SocialSignOn : " + authorizationUrl);
		return new Message<Object>((Object) authorizationUrl).buildResponse();
	}

	@GetMapping(value = { "/scanqrcode/{provider}" })
	@ResponseBody
	public ResponseEntity<?> scanQRCode(HttpServletRequest request, @PathVariable("provider") String provider) {
		String instId = WebContext.getInst().getId();
		String originURL = WebContext.getContextPath(request, false);
		AuthRequest authRequest = buildAuthRequest(instId, provider, originURL + applicationConfig.getFrontendUri());

		if (authRequest == null) {
			_logger.error("build authRequest fail .");
		}
		String state = UUID.generate().toString();
		// String state = authTokenService.genRandomJwt();
		authRequest.authorize(state);

		SocialsProvider socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
		SocialsProvider scanQrProvider = new SocialsProvider(socialSignOnProvider);
		scanQrProvider.setState(state);
		scanQrProvider.setRedirectUri(
				socialSignOnProviderService.getRedirectUri(originURL + applicationConfig.getFrontendUri(), provider));
		// 缓存state票据在缓存或者是redis中五分钟过期
		if (provider.equalsIgnoreCase(AuthDreamRequest.KEY)) {
			socialSignOnProviderService.setToken(state);
		}

		return new Message<SocialsProvider>(scanQrProvider).buildResponse();
	}

	@GetMapping(value = { "/bind/{provider}" })
	public ResponseEntity<?> bind(@PathVariable String provider, @CurrentUser UserInfo userInfo,
			HttpServletRequest request) {
		// auth call back may exception
		try {
			String originURL = WebContext.getContextPath(request, false);
			SocialsAssociate socialsAssociate =
					this.authCallback(userInfo.getInstId(), provider, originURL + applicationConfig.getFrontendUri());
			socialsAssociate.setSocialUserInfo(accountJsonString);
			socialsAssociate.setUserId(userInfo.getId());
			socialsAssociate.setUsername(userInfo.getUsername());
			socialsAssociate.setInstId(userInfo.getInstId());
			// socialsAssociate.setAccessToken(JsonUtils.object2Json(accessToken));
			// socialsAssociate.setExAttribute(JsonUtils.object2Json(accessToken.getResponseObject()));
			_logger.debug("Social Bind : " + socialsAssociate);
			this.socialsAssociateService.delete(socialsAssociate);
			this.socialsAssociateService.insert(socialsAssociate);
			return new Message<AuthJwt>().buildResponse();
		} catch (Exception e) {
			_logger.error("callback Exception  ", e);
		}

		return new Message<AuthJwt>(Message.ERROR).buildResponse();
	}

	@GetMapping(value = { "/callback/{provider}" })
	public ResponseEntity<?> callback(@PathVariable String provider, HttpServletRequest request) {
		// auth call back may exception
		try {
			String originURL = WebContext.getContextPath(request, false);
			String instId = WebContext.getInst().getId();
			SocialsAssociate socialsAssociate =
					this.authCallback(instId, provider, originURL + applicationConfig.getFrontendUri());

			SocialsAssociate socialssssociate1 = this.socialsAssociateService.get(socialsAssociate);

			_logger.debug("Loaded SocialSignOn Socials Associate : " + socialssssociate1);

			if (null == socialssssociate1) {
				// 如果存在第三方ID并且在数据库无法找到映射关系，则进行绑定逻辑
				if (StringUtils.isNotEmpty(socialsAssociate.getSocialUserId())) {
					// 返回message为第三方用户标识
					return new Message<AuthJwt>(Message.PROMPT, socialsAssociate.getSocialUserId()).buildResponse();
				}
			}

			socialsAssociate = socialssssociate1;
			_logger.debug("Social Sign On from {} mapping to user {}", socialsAssociate.getProvider(),
					socialsAssociate.getUsername());

			LoginCredential loginCredential =
					new LoginCredential(socialsAssociate.getUsername(), "", ConstsLoginType.SOCIALSIGNON);
			SocialsProvider socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
			loginCredential.setProvider(socialSignOnProvider.getProviderName());

			Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
			// socialsAssociate.setAccessToken(JsonUtils.object2Json(this.accessToken));
			socialsAssociate.setSocialUserInfo(accountJsonString);
			// socialsAssociate.setExAttribute(JsonUtils.object2Json(accessToken.getResponseObject()));

			this.socialsAssociateService.update(socialsAssociate);
			return new Message<AuthJwt>(authTokenService.genAuthJwt(authentication)).buildResponse();
		} catch (Exception e) {
			_logger.error("callback Exception  ", e);
			return new Message<AuthJwt>(Message.ERROR).buildResponse();
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
				return new Message<AuthJwt>(Message.ERROR).buildResponse();
			}
			String token = param.get("token");
			String username = param.get("username");
			// 判断token是否合法
			String redisusername = this.socialSignOnProviderService.getToken(token);
			if (StringUtils.isNotEmpty(redisusername)) {
				// 设置token和用户绑定
				boolean flag = this.socialSignOnProviderService.bindtoken(token, username);
				if (flag) {
					return new Message<AuthJwt>().buildResponse();
				}
			} else {
				return new Message<AuthJwt>(Message.WARNING, "Invalid token").buildResponse();
			}
		} catch (Exception e) {
			_logger.error("qrAuthLogin Exception  ", e);
		}
		return new Message<AuthJwt>(Message.ERROR).buildResponse();
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
				return new Message<AuthJwt>(Message.ERROR).buildResponse();
			}

			String loginName = socialSignOnProviderService.getToken(state);
			if (StringUtils.isEmpty(loginName)) {
				// 二维码过期
				return new Message<AuthJwt>(Message.PROMPT).buildResponse();
			}
			if ("-1".equalsIgnoreCase(loginName)) {
				// 暂无用户扫码
				return new Message<AuthJwt>(Message.WARNING).buildResponse();
			}
			String instId = WebContext.getInst().getId();

			SocialsAssociate socialsAssociate = new SocialsAssociate();
			socialsAssociate.setProvider(provider);
			socialsAssociate.setSocialUserId(loginName);
			socialsAssociate.setInstId(instId);

			socialsAssociate = this.socialsAssociateService.get(socialsAssociate);

			_logger.debug("qrcallback Loaded SocialSignOn Socials Associate : " + socialsAssociate);

			if (null == socialsAssociate) {
				return new Message<AuthJwt>(Message.ERROR).buildResponse();
			}

			_logger.debug("qrcallback Social Sign On from {} mapping to user {}", socialsAssociate.getProvider(),
					socialsAssociate.getUsername());

			LoginCredential loginCredential =
					new LoginCredential(socialsAssociate.getUsername(), "", ConstsLoginType.SOCIALSIGNON);
			SocialsProvider socialSignOnProvider = socialSignOnProviderService.get(instId, provider);
			loginCredential.setProvider(socialSignOnProvider.getProviderName());

			Authentication authentication = authenticationProvider.authenticate(loginCredential, true);
			// socialsAssociate.setAccessToken(JsonUtils.object2Json(this.accessToken));
			socialsAssociate.setSocialUserInfo(accountJsonString);
			// socialsAssociate.setExAttribute(JsonUtils.object2Json(accessToken.getResponseObject()));

			this.socialsAssociateService.update(socialsAssociate);
			return new Message<AuthJwt>(authTokenService.genAuthJwt(authentication)).buildResponse();
		} catch (Exception e) {
			_logger.error("qrcallback Exception  ", e);
			return new Message<AuthJwt>(Message.ERROR).buildResponse();
		}
	}
}
