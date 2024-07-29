package com.wy.test.oauth2.provider.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.constants.ContentType;
import com.wy.test.core.entity.apps.oauth2.provider.ClientDetails;
import com.wy.test.core.web.WebConstants;
import com.wy.test.oauth2.common.OAuth2Constants;

import dream.flying.flower.framework.web.crypto.jose.keystore.JWKSetKeyStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2-1-OAuth v2.0 API文档模块")
@Controller
public class OauthJwksEndpoint extends AbstractEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(OauthJwksEndpoint.class);

	@Operation(summary = "OAuth JWk 元数据接口", description = "参数mxk_metadata_APPID", method = "GET")
	@RequestMapping(value = OAuth2Constants.ENDPOINT.ENDPOINT_BASE + "/jwks",
			method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String keysMetadata(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "client_id", required = false) String client_id) {
		return metadata(request, response, client_id, null);
	}

	@Operation(summary = "OAuth JWk 元数据接口", description = "参数mxk_metadata_APPID", method = "GET")
	@RequestMapping(value = "/metadata/oauth/v20/" + WebConstants.MXK_METADATA_PREFIX + "{appid}.{mediaType}",
			method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String metadata(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "appid", required = false) String appId,
			@PathVariable(value = "mediaType", required = false) String mediaType) {
		ClientDetails clientDetails = null;
		try {
			clientDetails = getClientDetailsService().loadClientByClientId(appId, true);
		} catch (Exception e) {
			_logger.error("getClientDetailsService", e);
		}
		if (clientDetails != null) {
			String jwkSetString = "";
			if (!clientDetails.getSignature().equalsIgnoreCase("none")) {
				jwkSetString = clientDetails.getSignatureKey();
			}
			if (!clientDetails.getAlgorithm().equalsIgnoreCase("none")) {
				if (!StringUtils.hasText(jwkSetString)) {
					jwkSetString = clientDetails.getAlgorithmKey();
				} else {
					jwkSetString = jwkSetString + "," + clientDetails.getAlgorithmKey();
				}
			}
			JWKSetKeyStore jwkSetKeyStore = new JWKSetKeyStore("{\"keys\": [" + jwkSetString + "]}");

			if (StringUtils.hasText(mediaType) && mediaType.equalsIgnoreCase(ContentType.XML)) {
				response.setContentType(ContentType.APPLICATION_XML_UTF8);
			} else {
				response.setContentType(ContentType.APPLICATION_JSON_UTF8);
			}
			return jwkSetKeyStore.toString(mediaType);
		}

		return appId + " not exist . \n" + MybatisJpaContext.version();
	}

}
