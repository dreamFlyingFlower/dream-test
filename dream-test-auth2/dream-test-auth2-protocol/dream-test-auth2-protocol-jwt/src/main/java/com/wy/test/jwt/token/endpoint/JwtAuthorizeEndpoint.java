package com.wy.test.jwt.token.endpoint;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.AuthorizeBaseEndpoint;
import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.constants.ContentType;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.entity.apps.AppsJwtDetails;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.core.web.WebConstants;
import com.wy.test.jwt.jwt.endpoint.adapter.JwtAdapter;
import com.wy.test.persistence.service.AppsJwtDetailsService;

import dream.flying.flower.framework.web.crypto.jose.keystore.JWKSetKeyStore;
import dream.flying.flower.reflect.ReflectHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2-5-JWT令牌接口")
@Controller
public class JwtAuthorizeEndpoint extends AuthorizeBaseEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(JwtAuthorizeEndpoint.class);

	@Autowired
	AppsJwtDetailsService jwtDetailsService;

	@Autowired
	DreamServerProperties dreamServerProperties;

	@Operation(summary = "JWT应用ID认证接口", description = "应用ID", method = "GET")
	@GetMapping("/authz/jwt/{id}")
	public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") String id, @CurrentUser UserInfo currentUser)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ModelAndView modelAndView = new ModelAndView();
		Apps application = getApp(id);
		AppsJwtDetails jwtDetails = jwtDetailsService.getAppDetails(application.getId(), true);
		_logger.debug("" + jwtDetails);
		jwtDetails.setAdapter(application.getAdapter());
		jwtDetails.setIsAdapter(application.getIsAdapter());

		AbstractAuthorizeAdapter adapter;
		if (ConstsBoolean.isTrue(jwtDetails.getIsAdapter())) {
			Object jwtAdapter = ReflectHelper.newInstance(jwtDetails.getAdapter());
			try {
				BeanUtils.setProperty(jwtAdapter, "jwtDetails", jwtDetails);
			} catch (IllegalAccessException | InvocationTargetException e) {
				_logger.error("setProperty error . ", e);
			}
			adapter = (AbstractAuthorizeAdapter) jwtAdapter;
		} else {
			JwtAdapter jwtAdapter = new JwtAdapter(jwtDetails);
			adapter = (AbstractAuthorizeAdapter) jwtAdapter;
		}

		adapter.setPrincipal(AuthorizationUtils.getPrincipal());

		adapter.generateInfo();
		// sign
		adapter.sign(null, jwtDetails.getSignatureKey(), jwtDetails.getSignature());
		// encrypt
		adapter.encrypt(null, jwtDetails.getAlgorithmKey(), jwtDetails.getAlgorithm());

		return adapter.authorize(modelAndView);
	}

	@Operation(summary = "JWT JWK元数据接口", description = "参数mxk_metadata_APPID", method = "GET")
	@RequestMapping(value = "/metadata/jwt/" + WebConstants.MXK_METADATA_PREFIX + "{appid}.{mediaType}",
			method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String metadata(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("appid") String appId, @PathVariable("mediaType") String mediaType) {
		AppsJwtDetails jwtDetails = jwtDetailsService.getAppDetails(appId, true);
		if (jwtDetails != null) {
			String jwkSetString = "";
			if (!jwtDetails.getSignature().equalsIgnoreCase("none")) {
				jwkSetString = jwtDetails.getSignatureKey();
			}
			if (!jwtDetails.getAlgorithm().equalsIgnoreCase("none")) {
				if (StringUtils.isBlank(jwkSetString)) {
					jwkSetString = jwtDetails.getAlgorithmKey();
				} else {
					jwkSetString = jwkSetString + "," + jwtDetails.getAlgorithmKey();
				}
			}

			JWKSetKeyStore jwkSetKeyStore = new JWKSetKeyStore("{\"keys\": [" + jwkSetString + "]}");
			if (StringUtils.isNotBlank(mediaType) && mediaType.equalsIgnoreCase("xml")) {
				response.setContentType(ContentType.APPLICATION_XML_UTF8);
			} else {
				response.setContentType(ContentType.APPLICATION_JSON_UTF8);
			}
			return jwkSetKeyStore.toString(mediaType);

		}
		return appId + " not exist. \n" + MybatisJpaContext.version();
	}
}
