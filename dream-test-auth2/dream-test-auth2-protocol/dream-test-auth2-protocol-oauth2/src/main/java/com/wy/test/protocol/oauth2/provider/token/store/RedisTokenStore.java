package com.wy.test.protocol.oauth2.provider.token.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import com.wy.test.core.constant.ConstRedisToken;
import com.wy.test.core.persistence.redis.RedisConnection;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.protocol.oauth2.common.OAuth2AccessToken;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;
import com.wy.test.protocol.oauth2.provider.token.AuthenticationKeyGenerator;
import com.wy.test.protocol.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import com.wy.test.protocol.oauth2.provider.token.TokenStore;

import dream.flying.flower.lang.SerializableHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisTokenStore implements TokenStore {

	private final RedisConnectionFactory connectionFactory;

	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

	public RedisTokenStore(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
		this.authenticationKeyGenerator = authenticationKeyGenerator;
	}

	private RedisConnection getConnection() {
		return connectionFactory.getConnection();
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		String key = authenticationKeyGenerator.extractKey(authentication);
		String serializedKey = (ConstRedisToken.AUTH_TO_ACCESS + key);
		RedisConnection conn = getConnection();
		try {
			OAuth2AccessToken accessToken = conn.getObject(serializedKey);
			if (accessToken != null
					&& !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
				// Keep the stores consistent (maybe the same user is
				// represented by this authentication but the details have
				// changed)
				storeAccessToken(accessToken, authentication);
			}
			return accessToken;
		} finally {
			conn.close();
		}
	}

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		return readAuthentication(token.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {
		log.trace("read Authentication by token " + token + " , token key " + ConstRedisToken.AUTH + token);
		RedisConnection conn = getConnection();
		try {
			OAuth2Authentication auth = conn.getObject(ConstRedisToken.AUTH + token);
			return auth;
		} finally {
			conn.close();
		}
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		return readAuthenticationForRefreshToken(token.getValue());
	}

	public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
		RedisConnection conn = getConnection();
		try {
			OAuth2Authentication auth = conn.getObject(ConstRedisToken.REFRESH_AUTH + token);
			return auth;
		} finally {
			conn.close();
		}
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		String accessKey = (ConstRedisToken.ACCESS + token.getValue());
		String authKey = (ConstRedisToken.AUTH + token.getValue());
		String authToAccessKey = (ConstRedisToken.AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
		String approvalKey = (ConstRedisToken.UNAME_TO_ACCESS + getApprovalKey(authentication));
		String clientId = (ConstRedisToken.CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
		log.trace("accessKey " + accessKey);
		log.trace("authKey " + authKey);
		log.trace("authToAccessKey " + authToAccessKey);
		log.trace("approvalKey " + approvalKey);
		log.trace("clientId " + clientId);

		RedisConnection conn = getConnection();
		try {
			conn.openPipeline();
			conn.setObject(accessKey, token);
			conn.setObject(authKey, authentication);
			conn.setObject(authToAccessKey, token);
			if (!authentication.isClientOnly()) {
				conn.rPush(approvalKey, token);
			}
			conn.rPush(clientId, token);
			if (token.getExpiration() != null) {
				int seconds = token.getExpiresIn();
				conn.expire(accessKey, seconds);
				conn.expire(authKey, seconds);
				conn.expire(authToAccessKey, seconds);
				conn.expire(clientId, seconds);
				conn.expire(approvalKey, seconds);
			}
			OAuth2RefreshToken refreshToken = token.getRefreshToken();
			if (refreshToken != null && refreshToken.getValue() != null) {
				String refresh = (token.getRefreshToken().getValue());
				String auth = (token.getValue());
				String refreshToAccessKey = (ConstRedisToken.REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
				log.trace("refreshToAccessKey " + refreshToAccessKey);
				conn.set(refreshToAccessKey, auth);
				String accessToRefreshKey = (ConstRedisToken.ACCESS_TO_REFRESH + token.getValue());
				log.trace("accessToRefreshKey " + accessToRefreshKey);
				conn.set(accessToRefreshKey, refresh);
				if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
					ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
					Date expiration = expiringRefreshToken.getExpiration();
					if (expiration != null) {
						int seconds =
								Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
						conn.expire(refreshToAccessKey, seconds);
						conn.expire(accessToRefreshKey, seconds);
					}
				}
			}
			conn.closePipeline();
		} finally {
			conn.close();
		}
	}

	private static String getApprovalKey(OAuth2Authentication authentication) {
		String userName =
				authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication().getName();
		return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
	}

	private static String getApprovalKey(String clientId, String userName) {
		return clientId + (userName == null ? "" : "_" + userName);
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken accessToken) {
		removeAccessToken(accessToken.getValue());
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		RedisConnection conn = getConnection();
		try {
			String key = (ConstRedisToken.ACCESS + tokenValue);
			OAuth2AccessToken accessToken = conn.getObject(key);
			return accessToken;
		} finally {
			conn.close();
		}
	}

	public void removeAccessToken(String tokenValue) {
		String accessKey = (ConstRedisToken.ACCESS + tokenValue);
		String authKey = (ConstRedisToken.AUTH + tokenValue);
		String accessToRefreshKey = (ConstRedisToken.ACCESS_TO_REFRESH + tokenValue);
		RedisConnection conn = getConnection();
		try {
			conn.openPipeline();
			conn.getPipeline().get(accessKey);
			conn.getPipeline().get(authKey);
			conn.getPipeline().del(accessKey);
			conn.getPipeline().del(accessToRefreshKey);
			// Don't remove the refresh token - it's up to the caller to do that
			conn.getPipeline().del(authKey);
			List<Object> results = conn.closePipeline();
			String access = (String) results.get(0);
			String auth = (String) results.get(1);
			OAuth2Authentication authentication = SerializableHelper.deserializeHex(auth);
			if (authentication != null) {
				String key = authenticationKeyGenerator.extractKey(authentication);
				String authToAccessKey = (ConstRedisToken.AUTH_TO_ACCESS + key);
				String unameKey = (ConstRedisToken.UNAME_TO_ACCESS + getApprovalKey(authentication));
				String clientId = (ConstRedisToken.CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
				conn.openPipeline();
				conn.delete(authToAccessKey);
				conn.lRem(unameKey, 1, access);
				conn.lRem(clientId, 1, access);
				conn.delete(ConstRedisToken.ACCESS + key);
				conn.closePipeline();
			}
		} finally {
			conn.close();
		}
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		String refreshKey = (ConstRedisToken.REFRESH + refreshToken.getValue());
		String refreshAuthKey = (ConstRedisToken.REFRESH_AUTH + refreshToken.getValue());
		RedisConnection conn = getConnection();
		try {
			conn.openPipeline();
			conn.setObject(refreshKey, refreshToken);
			conn.setObject(refreshAuthKey, authentication);

			if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
				ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
				Date expiration = expiringRefreshToken.getExpiration();
				if (expiration != null) {
					int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
					conn.expire(refreshKey, seconds);
					conn.expire(refreshAuthKey, seconds);
				}
			}
			conn.closePipeline();
		} finally {
			conn.close();
		}
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		String key = (ConstRedisToken.REFRESH + tokenValue);
		RedisConnection conn = getConnection();
		try {
			OAuth2RefreshToken refreshToken = conn.getObject(key);
			conn.close();
			return refreshToken;
		} finally {
			conn.close();
		}
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
		removeRefreshToken(refreshToken.getValue());
	}

	public void removeRefreshToken(String tokenValue) {
		String refreshKey = (ConstRedisToken.REFRESH + tokenValue);
		String refreshAuthKey = (ConstRedisToken.REFRESH_AUTH + tokenValue);
		String refresh2AccessKey = (ConstRedisToken.REFRESH_TO_ACCESS + tokenValue);
		String access2RefreshKey = (ConstRedisToken.ACCESS_TO_REFRESH + tokenValue);
		RedisConnection conn = getConnection();
		try {
			conn.openPipeline();
			conn.delete(refreshKey);
			conn.delete(refreshAuthKey);
			conn.delete(refresh2AccessKey);
			conn.delete(access2RefreshKey);
			conn.closePipeline();
		} finally {
			conn.close();
		}
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		removeAccessTokenUsingRefreshToken(refreshToken.getValue());
	}

	private void removeAccessTokenUsingRefreshToken(String refreshToken) {
		String key = (ConstRedisToken.REFRESH_TO_ACCESS + refreshToken);
		List<Object> results = null;
		RedisConnection conn = getConnection();
		try {
			conn.openPipeline();
			conn.getPipeline().get(key);
			conn.getPipeline().del(key);
			results = conn.closePipeline();
		} finally {
			conn.close();
		}
		if (results == null) {
			return;
		}
		String accessToken = (String) results.get(0);
		// String accessToken = ObjectTransformer.deserialize(bytes);
		if (accessToken != null) {
			removeAccessToken(accessToken);
		}

	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		String approvalKey = (ConstRedisToken.UNAME_TO_ACCESS + getApprovalKey(clientId, userName));
		log.trace("approvalKey " + approvalKey);
		List<String> stringList = null;
		RedisConnection conn = getConnection();
		try {
			stringList = conn.lRange(approvalKey, 0, -1);
		} finally {
			conn.close();
		}
		if (stringList == null || stringList.size() == 0) {
			return Collections.<OAuth2AccessToken>emptySet();
		}
		List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>(stringList.size());
		for (String str : stringList) {
			// accessToken may expired
			OAuth2AccessToken accessToken = conn.getObject(str);
			accessTokens.add(accessToken);
		}
		return Collections.<OAuth2AccessToken>unmodifiableCollection(accessTokens);
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		String key = (ConstRedisToken.CLIENT_ID_TO_ACCESS + clientId);
		log.trace("TokensByClientId  " + key);
		List<String> stringList = null;
		RedisConnection conn = getConnection();
		try {
			stringList = conn.lRange(key, 0, -1);
		} finally {
			conn.close();
		}
		if (stringList == null || stringList.size() == 0) {
			return Collections.<OAuth2AccessToken>emptySet();
		}
		List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>(stringList.size());
		for (String str : stringList) {
			OAuth2AccessToken accessToken = conn.getObject(str);
			accessTokens.add(accessToken);
		}
		return Collections.<OAuth2AccessToken>unmodifiableCollection(accessTokens);
	}

}
