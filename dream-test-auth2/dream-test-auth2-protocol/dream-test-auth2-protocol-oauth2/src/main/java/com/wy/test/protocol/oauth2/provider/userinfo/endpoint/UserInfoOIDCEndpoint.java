package com.wy.test.protocol.oauth2.provider.userinfo.endpoint;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.constant.ContentType;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.core.web.HttpResponseAdapter;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.UserService;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;
import com.wy.test.protocol.oauth2.provider.token.DefaultTokenServices;

import dream.flying.flower.enums.GenderType;
import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.safe.jwt.encryption.DefaultJwtEncryptionAndDecryptionHandler;
import dream.flying.flower.framework.safe.jwt.sign.DefaultJwtSigningAndValidationHandler;
import dream.flying.flower.framework.web.helper.TokenHelpers;
import dream.flying.flower.generator.StringGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2-1-OAuth v2.0 API文档模块")
@Controller
@Slf4j
public class UserInfoOIDCEndpoint {

	@Autowired
	@Qualifier("oauth20JdbcClientDetailsService")
	private ClientDetailsService clientDetailsService;

	@Autowired
	@Qualifier("oauth20TokenServices")
	private DefaultTokenServices oauth20tokenServices;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	@Qualifier("appService")
	protected AppService appService;

	OAuthDefaultUserInfoAdapter defaultOAuthUserInfoAdapter = new OAuthDefaultUserInfoAdapter();

	@Autowired
	protected HttpResponseAdapter httpResponseAdapter;

	@Operation(summary = "OIDC 用户信息接口", description = "请求参数access_token , header Authorization , token ",
			method = "GET")
	@RequestMapping(value = OAuth2Constants.ENDPOINT.ENDPOINT_OPENID_CONNECT_USERINFO,
			method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String connect10aUserInfo(HttpServletRequest request, HttpServletResponse response) {
		String access_token = TokenHelpers.resolveAccessToken(request);
		log.debug("access_token {}", access_token);
		if (!StringGenerator.uuidMatches(access_token)) {
			return JsonHelpers.toString(accessTokenFormatError(access_token));
		}

		String principal = "";
		OAuth2Authentication oAuth2Authentication = null;
		try {
			oAuth2Authentication = oauth20tokenServices.loadAuthentication(access_token);

			principal = ((SignPrincipal) oAuth2Authentication.getPrincipal()).getUsername();

			Set<String> scopes = oAuth2Authentication.getOAuth2Request().getScope();
			ClientDetails clientDetails = clientDetailsService
					.loadClientByClientId(oAuth2Authentication.getOAuth2Request().getClientId(), true);

			UserEntity userInfo = queryUserInfo(principal);
			String userJson = "";
			Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();

			SignPrincipal authentication = (SignPrincipal) oAuth2Authentication.getUserAuthentication().getPrincipal();

			String subject = AbstractAuthorizeAdapter.getValueByUserAttr(UserConvert.INSTANCE.convertt(userInfo),
					clientDetails.getSubject());
			log.debug("userId : {} , username : {} , displayName : {} , subject : {}", userInfo.getId(),
					userInfo.getUsername(), userInfo.getDisplayName(), subject);

			jwtClaimsSetBuilder.claim("sub", subject);
			jwtClaimsSetBuilder.claim("institution", userInfo.getInstId());
			jwtClaimsSetBuilder.claim(ConstAuthWeb.ONLINE_TICKET_NAME, authentication.getSession().getFormattedId());

			if (scopes.contains("profile")) {
				jwtClaimsSetBuilder.claim("userId", userInfo.getId());
				jwtClaimsSetBuilder.claim("user", userInfo.getUsername());
				jwtClaimsSetBuilder.claim("name", userInfo.getDisplayName());
				jwtClaimsSetBuilder.claim("preferred_username", userInfo.getDisplayName());
				jwtClaimsSetBuilder.claim("given_name", userInfo.getGivenName());
				jwtClaimsSetBuilder.claim("family_name", userInfo.getFamilyName());
				jwtClaimsSetBuilder.claim("middle_name", userInfo.getMiddleName());
				jwtClaimsSetBuilder.claim("nickname", userInfo.getNickName());
				jwtClaimsSetBuilder.claim("profile", "profile");
				jwtClaimsSetBuilder.claim("picture", "picture");
				// jwtClaimsSetBuilder.claim("website", userInfo.getWebSite());
				jwtClaimsSetBuilder.claim("displayName", userInfo.getDisplayName());

				jwtClaimsSetBuilder.claim("departmentId", userInfo.getDepartmentId());
				jwtClaimsSetBuilder.claim("department", userInfo.getDepartment());

				GenderType genderType = GenderType.get(userInfo.getGender());
				String gender = null == genderType ? GenderType.UNKNOWN.name() : genderType.name();

				jwtClaimsSetBuilder.claim("gender", gender);
				jwtClaimsSetBuilder.claim("zoneinfo", userInfo.getTimeZone());
				jwtClaimsSetBuilder.claim("locale", userInfo.getLocale());
				jwtClaimsSetBuilder.claim("updated_time", userInfo.getUpdateTime());
				jwtClaimsSetBuilder.claim("birthdate", userInfo.getBirthDate());
			}

			if (scopes.contains("email")) {
				jwtClaimsSetBuilder.claim("email", userInfo.getWorkEmail());
				jwtClaimsSetBuilder.claim("email_verified", false);
			}

			if (scopes.contains("phone")) {
				jwtClaimsSetBuilder.claim("phone_number", userInfo.getWorkPhoneNumber());
				jwtClaimsSetBuilder.claim("phone_number_verified", false);
			}

			if (scopes.contains("address")) {
				HashMap<String, String> addressFields = new HashMap<String, String>();
				addressFields.put("country", userInfo.getWorkCountry());
				addressFields.put("region", userInfo.getWorkRegion());
				addressFields.put("locality", userInfo.getWorkLocality());
				addressFields.put("street_address", userInfo.getWorkStreetAddress());
				addressFields.put("formatted", userInfo.getWorkAddressFormatted());
				addressFields.put("postal_code", userInfo.getWorkPostalCode());

				jwtClaimsSetBuilder.claim("address", addressFields);
			}

			jwtClaimsSetBuilder.jwtID(UUID.randomUUID().toString())// set a random NONCE in the middle of it
					.audience(Arrays.asList(clientDetails.getClientId())).issueTime(new Date()).expirationTime(
							new Date(new Date().getTime() + clientDetails.getAccessTokenValiditySeconds() * 1000));

			// default ContentType
			response.setContentType(ContentType.APPLICATION_JWT_UTF8);

			JWTClaimsSet userInfoJWTClaims = jwtClaimsSetBuilder.build();
			JWT userInfoJWT = null;

			if (clientDetails.getUserInfoResponse().equalsIgnoreCase("NORMAL")) {
				response.setContentType(ContentType.APPLICATION_JSON_UTF8);
				userJson = userInfoJWTClaims.toString();
			} else if (StringUtils.isNotBlank(clientDetails.getSignature())
					&& !clientDetails.getSignature().equalsIgnoreCase("none")
					&& clientDetails.getUserInfoResponse().equalsIgnoreCase("ENCRYPTION")) {
				// 需要签名 signed ID token
				DefaultJwtSigningAndValidationHandler jwtSignerService = null;
				try {
					jwtSignerService = new DefaultJwtSigningAndValidationHandler(clientDetails.getSignatureKey(),
							clientDetails.getClientId() + "_sig", clientDetails.getSignature());
				} catch (Exception e) {
					log.error("Couldn't create Jwt Signing Service", e);
				}

				JWSAlgorithm signingAlg = jwtSignerService.getDefaultSigningAlgorithm();
				userInfoJWTClaims = new JWTClaimsSet.Builder(userInfoJWTClaims)
						.claim("kid", jwtSignerService.getDefaultSignerKeyId()).build();

				userInfoJWT = new SignedJWT(new JWSHeader(signingAlg), userInfoJWTClaims);
				// sign it with the server's key
				jwtSignerService.signJwt((SignedJWT) userInfoJWT);

				userJson = userInfoJWT.serialize();
			} else if (StringUtils.isNotBlank(clientDetails.getAlgorithm())
					&& !clientDetails.getAlgorithm().equalsIgnoreCase("none")
					&& clientDetails.getUserInfoResponse().equalsIgnoreCase("SIGNING")) {
				// 需要加密
				try {
					DefaultJwtEncryptionAndDecryptionHandler jwtEncryptionService =
							new DefaultJwtEncryptionAndDecryptionHandler(clientDetails.getAlgorithmKey(),
									clientDetails.getClientId() + "_enc", clientDetails.getAlgorithm());

					Payload payload = userInfoJWTClaims.toPayload();

					// Example Request JWT encrypted with RSA-OAEP-256 and 128-bit AES/GCM
					// JWEHeader jweHeader = new JWEHeader(JWEAlgorithm.RSA1_5,
					// EncryptionMethod.A128GCM);
					JWEHeader jweHeader =
							new JWEHeader(jwtEncryptionService.getDefaultAlgorithm(clientDetails.getAlgorithm()),
									jwtEncryptionService.parseEncryptionMethod(clientDetails.getEncryptionMethod()));

					JWEObject jweObject = new JWEObject(new JWEHeader.Builder(jweHeader).contentType("JWT") // required
																											// to
																											// indicate
																											// nested
																											// JWT
							.build(), payload);

					jwtEncryptionService.encryptJwt(jweObject);
					userJson = jweObject.serialize();
				} catch (NoSuchAlgorithmException | InvalidKeySpecException | JOSEException e) {
					log.error("Couldn't create Jwt Encryption Exception", e);
				}
			} else {
				// 不需要加密和签名 unsigned ID token
				userInfoJWT = new PlainJWT(userInfoJWTClaims);
				userJson = userInfoJWT.serialize();
			}

			log.trace("OpenID Connect Response {}", userJson);
			return userJson;

		} catch (OAuth2Exception e) {
			HashMap<String, Object> authzException = new HashMap<String, Object>();
			authzException.put(OAuth2Exception.ERROR, e.getOAuth2ErrorCode());
			authzException.put(OAuth2Exception.DESCRIPTION, e.getMessage());
			return JsonHelpers.toString(authzException);
		}
	}

	public HashMap<String, Object> accessTokenFormatError(String access_token) {
		HashMap<String, Object> atfe = new HashMap<String, Object>();
		atfe.put(OAuth2Exception.ERROR, "token Format Invalid");
		atfe.put(OAuth2Exception.DESCRIPTION, "access Token Format Invalid , access_token : " + access_token);

		return atfe;
	}

	public UserEntity queryUserInfo(String userId) {
		return userService.findByUsername(userId);
	}

	public void setOauth20tokenServices(DefaultTokenServices oauth20tokenServices) {
		this.oauth20tokenServices = oauth20tokenServices;
	}
}