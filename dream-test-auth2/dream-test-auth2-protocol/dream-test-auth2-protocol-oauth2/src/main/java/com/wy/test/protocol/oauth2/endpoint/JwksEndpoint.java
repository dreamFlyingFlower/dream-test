package com.wy.test.protocol.oauth2.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.constant.ContentType;
import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;

import dream.flying.flower.framework.crypto.jose.keystore.JWKSetKeyStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "OAuth2.0 JWK API")
@RestController
@Slf4j
public class JwksEndpoint extends AbstractEndpoint {

	@Operation(summary = "JWK 元数据", description = "参数auth_metadata_APPID", method = "GET")
	@RequestMapping(value = OAuth2Constants.ENDPOINT.ENDPOINT_BASE + "/jwks",
			method = { RequestMethod.POST, RequestMethod.GET })
	public String keysMetadata(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String client_id) {
		return metadata(request, response, client_id, null);
	}

	@Operation(summary = "JWK 元数据", description = "参数auth_metadata_APPID", method = "GET")
	@RequestMapping(value = "/metadata/oauth/v20/" + ConstAuthWeb.DREAM_METADATA_PREFIX + "{appid}.{mediaType}",
			method = { RequestMethod.POST, RequestMethod.GET })
	public String metadata(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "appid", required = false) String appId,
			@PathVariable(value = "mediaType", required = false) String mediaType) {
		ClientDetails clientDetails = null;
		try {
			clientDetails = getClientDetailsService().loadClientByClientId(appId, true);
		} catch (Exception e) {
			log.error("getClientDetailsService", e);
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

		return appId + " not exist . \n";
	}
}