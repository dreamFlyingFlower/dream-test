package com.wy.test.protocol.oauth2.oidc;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.core.entity.oidc.OidcProviderMetadata;
import com.wy.test.protocol.oauth2.common.DefaultOAuth2AccessToken;
import com.wy.test.protocol.oauth2.common.OAuth2AccessToken;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;
import com.wy.test.protocol.oauth2.provider.OAuth2Request;
import com.wy.test.protocol.oauth2.provider.token.TokenEnhancer;

import dream.flying.flower.framework.crypto.jwt.encryption.DefaultJwtEncryptionAndDecryptionHandler;
import dream.flying.flower.framework.crypto.jwt.sign.DefaultJwtSigningAndValidationHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OidcIdTokenEnhancer implements TokenEnhancer {

	public final static String ID_TOKEN_SCOPE = "openid";

	@SuppressWarnings("unused")
	private OidcProviderMetadata providerMetadata;

	private ClientDetailsService clientDetailsService;

	public void setProviderMetadata(OidcProviderMetadata providerMetadata) {
		this.providerMetadata = providerMetadata;
	}

	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		OAuth2Request request = authentication.getOAuth2Request();
		if (request.getScope().contains(ID_TOKEN_SCOPE)) {// Enhance for OpenID Connect
			ClientDetails clientDetails =
					clientDetailsService.loadClientByClientId(authentication.getOAuth2Request().getClientId(), true);

			DefaultJwtSigningAndValidationHandler jwtSignerService = null;
			JWSAlgorithm signingAlg = null;
			try {// jwtSignerService
				if (StringUtils.isNotBlank(clientDetails.getSignature())
						&& !clientDetails.getSignature().equalsIgnoreCase("none")) {
					jwtSignerService = new DefaultJwtSigningAndValidationHandler(clientDetails.getSignatureKey(),
							clientDetails.getClientId() + "_sig", clientDetails.getSignature());

					signingAlg = jwtSignerService.getDefaultSigningAlgorithm();
				}
			} catch (Exception e) {
				log.error("Couldn't create Jwt Signing Service", e);
			}

			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.subject(authentication.getName()).expirationTime(accessToken.getExpiration())
					.issuer(clientDetails.getIssuer()).issueTime(new Date())
					.audience(Arrays.asList(authentication.getOAuth2Request().getClientId()))
					.jwtID(UUID.randomUUID().toString());

			/**
			 * https://self-issued.me
			 * 
			 * @see http://openid.net/specs/openid-connect-core-1_0.html#SelfIssuedDiscovery
			 *      7. Self-Issued OpenID Provider
			 */
			if (clientDetails.getIssuer() != null && jwtSignerService != null
					&& clientDetails.getIssuer().equalsIgnoreCase("https://self-issued.me")) {
				builder.claim("sub_jwk",
						jwtSignerService.getAllPublicKeys().get(jwtSignerService.getDefaultSignerKeyId()));
			}

			// if the auth time claim was explicitly requested OR if the client always wants
			// the auth time, put it in
			// parse the ID Token claims (#473) -- for now assume it could be in there
			if (request.getExtensions().containsKey("max_age") || (request.getExtensions().containsKey("idtoken"))) {
				builder.claim("auth_time", AuthorizationUtils.getUserInfo().getLastLoginTime().getTime() / 1000);
			}

			String nonce = request.getRequestParameters().get("nonce");
			log.debug("getRequestParameters nonce {}", nonce);
			if (!Strings.isNullOrEmpty(nonce)) {
				builder.claim("nonce", nonce);
			}
			if (jwtSignerService != null) {
				SignedJWT signed = new SignedJWT(new JWSHeader(signingAlg), builder.build());
				Set<String> responseTypes = request.getResponseTypes();

				if (responseTypes.contains("token")) {
					// calculate the token hash
					Base64URL at_hash = IdTokenHashUtils.getAccessTokenHash(signingAlg, signed);
					builder.claim("at_hash", at_hash);
				}
				log.debug("idClaims {}", builder.build());
			}
			String idTokenString = "";
			if (StringUtils.isNotBlank(clientDetails.getSignature())
					&& !clientDetails.getSignature().equalsIgnoreCase("none")) {
				try {
					builder.claim("kid", jwtSignerService.getDefaultSignerKeyId());
					// signed ID token
					JWT idToken = new SignedJWT(new JWSHeader(signingAlg), builder.build());
					// sign it with the server's key
					jwtSignerService.signJwt((SignedJWT) idToken);
					idTokenString = idToken.serialize();
					log.debug("idToken {}", idTokenString);
				} catch (Exception e) {
					log.error("Couldn't create Jwt Signing Exception", e);
				}
			} else if (StringUtils.isNotBlank(clientDetails.getAlgorithm())
					&& !clientDetails.getAlgorithm().equalsIgnoreCase("none")) {
				try {
					DefaultJwtEncryptionAndDecryptionHandler jwtEncryptionService =
							new DefaultJwtEncryptionAndDecryptionHandler(clientDetails.getAlgorithmKey(),
									clientDetails.getClientId() + "_enc", clientDetails.getAlgorithm());
					Payload payload = builder.build().toPayload();
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
					idTokenString = jweObject.serialize();
				} catch (NoSuchAlgorithmException | InvalidKeySpecException | JOSEException e) {
					log.error("Couldn't create Jwt Encryption Exception", e);
				}
			} else {
				// not need a PlainJWT idToken
				// JWT idToken = new PlainJWT(builder.build());
				// idTokenString = idToken.serialize();
			}

			accessToken = new DefaultOAuth2AccessToken(accessToken);
			if (StringUtils.isNotBlank(idTokenString)) {
				accessToken.getAdditionalInformation().put("id_token", idTokenString);
			}
		}
		return accessToken;
	}
}