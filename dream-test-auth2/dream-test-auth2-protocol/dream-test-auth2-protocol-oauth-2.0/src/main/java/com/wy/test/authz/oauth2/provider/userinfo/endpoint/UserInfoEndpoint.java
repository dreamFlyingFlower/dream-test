package com.wy.test.authz.oauth2.provider.userinfo.endpoint;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.authz.oauth2.common.OAuth2Constants;
import com.wy.test.authz.oauth2.common.exceptions.OAuth2Exception;
import com.wy.test.authz.oauth2.provider.ClientDetailsService;
import com.wy.test.authz.oauth2.provider.OAuth2Authentication;
import com.wy.test.authz.oauth2.provider.token.DefaultTokenServices;
import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.entity.apps.oauth2.provider.ClientDetails;
import com.wy.test.core.web.HttpResponseAdapter;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.util.Instance;
import com.wy.test.util.JsonUtils;
import com.wy.test.util.RequestTokenUtils;
import com.wy.test.util.StringGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2-1-OAuth v2.0 API文档模块")
@Controller
public class UserInfoEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(UserInfoEndpoint.class);

	@Autowired
	@Qualifier("oauth20JdbcClientDetailsService")
	private ClientDetailsService clientDetailsService;

	@Autowired
	@Qualifier("oauth20TokenServices")
	private DefaultTokenServices oauth20tokenServices;

	@Autowired
	@Qualifier("userInfoService")
	private UserInfoService userInfoService;

	@Autowired
	@Qualifier("appsService")
	protected AppsService appsService;

	@Autowired
	protected HttpResponseAdapter httpResponseAdapter;

	@Operation(summary = "OAuth 2.0 用户信息接口", description = "请求参数access_token , header Authorization , token ",
			method = "GET")
	@RequestMapping(value = OAuth2Constants.ENDPOINT.ENDPOINT_USERINFO,
			method = { RequestMethod.POST, RequestMethod.GET })
	public void apiV20UserInfo(HttpServletRequest request, HttpServletResponse response) {
		String access_token = RequestTokenUtils.resolveAccessToken(request);
		_logger.debug("access_token {}", access_token);
		if (!StringGenerator.uuidMatches(access_token)) {
			httpResponseAdapter.write(response, JsonUtils.gsonToString(accessTokenFormatError(access_token)), "json");
		}

		OAuth2Authentication oAuth2Authentication = null;
		try {
			oAuth2Authentication = oauth20tokenServices.loadAuthentication(access_token);

			String client_id = oAuth2Authentication.getOAuth2Request().getClientId();
			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(client_id, true);

			Apps app = appsService.get(client_id);

			AbstractAuthorizeAdapter adapter;
			if (ConstsBoolean.isTrue(app.getIsAdapter())) {
				adapter = (AbstractAuthorizeAdapter) Instance.newInstance(app.getAdapter());
				try {
					BeanUtils.setProperty(adapter, "clientDetails", clientDetails);
				} catch (IllegalAccessException | InvocationTargetException e) {
					_logger.error("setProperty error . ", e);
				}
			} else {
				adapter = (AbstractAuthorizeAdapter) new OAuthDefaultUserInfoAdapter(clientDetails);
			}
			adapter.setPrincipal((SignPrincipal) oAuth2Authentication.getUserAuthentication().getPrincipal());
			adapter.setApp(app);

			Object jsonData = adapter.generateInfo();
			httpResponseAdapter.write(response, jsonData.toString(), "json");
		} catch (OAuth2Exception e) {
			HashMap<String, Object> authzException = new HashMap<String, Object>();
			authzException.put(OAuth2Exception.ERROR, e.getOAuth2ErrorCode());
			authzException.put(OAuth2Exception.DESCRIPTION, e.getMessage());
			httpResponseAdapter.write(response, JsonUtils.gsonToString(authzException), "json");
		}
	}

	public HashMap<String, Object> accessTokenFormatError(String access_token) {
		HashMap<String, Object> atfe = new HashMap<String, Object>();
		atfe.put(OAuth2Exception.ERROR, "token Format Invalid");
		atfe.put(OAuth2Exception.DESCRIPTION, "access Token Format Invalid , access_token : " + access_token);

		return atfe;
	}

	public UserInfo queryUserInfo(String userId) {
		_logger.debug("userId : " + userId);
		UserInfo userInfo = (UserInfo) userInfoService.findByUsername(userId);
		return userInfo;
	}

	public void setOauth20tokenServices(DefaultTokenServices oauth20tokenServices) {
		this.oauth20tokenServices = oauth20tokenServices;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

}
