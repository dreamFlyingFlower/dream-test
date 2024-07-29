package com.wy.test.social.authn.support.socialsignon.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.constants.ConstsTimeInterval;
import com.wy.test.core.crypto.password.PasswordReciprocal;
import com.wy.test.core.entity.SocialsProvider;
import com.wy.test.core.entity.SocialsProviderLogin;
import com.wy.test.social.authn.support.socialsignon.token.RedisTokenStore;
import com.wy.test.social.zhyd.request.AuthFeishu2Request;
import com.wy.test.social.zhyd.request.AuthHuaweiWeLinkRequest;
import com.wy.test.social.zhyd.request.AuthMaxkeyRequest;
import com.wy.test.social.zhyd.request.AuthWeChatEnterpriseWebRequestCost;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthAlipayRequest;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthDingTalkRequest;
import me.zhyd.oauth.request.AuthDouyinRequest;
import me.zhyd.oauth.request.AuthElemeRequest;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthGitlabRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthHuaweiRequest;
import me.zhyd.oauth.request.AuthJdRequest;
import me.zhyd.oauth.request.AuthLinkedinRequest;
import me.zhyd.oauth.request.AuthMeituanRequest;
import me.zhyd.oauth.request.AuthMiRequest;
import me.zhyd.oauth.request.AuthMicrosoftRequest;
import me.zhyd.oauth.request.AuthOschinaRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthTaobaoRequest;
import me.zhyd.oauth.request.AuthToutiaoRequest;
import me.zhyd.oauth.request.AuthTwitterRequest;
import me.zhyd.oauth.request.AuthWeChatEnterpriseQrcodeRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.request.AuthWeiboRequest;

public class SocialSignOnProviderService {

	private static Logger _logger = LoggerFactory.getLogger(SocialSignOnProviderService.class);

	private static final String DEFAULT_SELECT_STATEMENT =
			"select * from mxk_socials_provider where instid = ? and status = 1  order by sortindex";

	protected static final Cache<String, SocialsProviderLogin> socialsProviderLoginStore =
			Caffeine.newBuilder().expireAfterWrite(ConstsTimeInterval.ONE_HOUR, TimeUnit.MINUTES).build();

	HashMap<String, SocialsProvider> socialSignOnProviderMaps = new HashMap<String, SocialsProvider>();

	private final JdbcTemplate jdbcTemplate;

	RedisTokenStore redisTokenStore;

	public SocialSignOnProviderService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public SocialsProvider get(String instId, String provider) {
		return socialSignOnProviderMaps.get(instId + "_" + provider);
	}

	public void setToken(String token) {
		this.redisTokenStore.store(token);
	}

	public boolean bindtoken(String token, String loginName) {
		return this.redisTokenStore.bindtoken(token, loginName);
	}

	public String getToken(String token) {
		return this.redisTokenStore.get(token);
	}

	public String getRedirectUri(String baseUri, String provider) {
		return baseUri + "/passport/callback/" + provider;
	}

	public AuthRequest getAuthRequest(String instId, String provider, String baseUri) throws Exception {
		AuthRequest authRequest = null;
		AuthConfig authConfig = AuthConfig.builder().clientId(this.get(instId, provider).getClientId())
				.clientSecret(this.get(instId, provider).getClientSecret())
				.redirectUri(getRedirectUri(baseUri, provider)).build();

		if (provider.equalsIgnoreCase("WeChatOpen")) {
			authRequest = new AuthWeChatOpenRequest(authConfig);
		} else if (provider.equalsIgnoreCase("sinaweibo")) {
			authRequest = new AuthWeiboRequest(authConfig);
		} else if (provider.equalsIgnoreCase("qq")) {
			authRequest = new AuthQqRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Alipay")) {
			String alipayPublicKey = "";
			authRequest = new AuthAlipayRequest(authConfig, alipayPublicKey);
		} else if (provider.equalsIgnoreCase("Twitter")) {
			authRequest = new AuthTwitterRequest(authConfig);
		} else if (provider.equalsIgnoreCase("google")) {
			authRequest = new AuthGoogleRequest(authConfig);
		} else if (provider.equalsIgnoreCase("microsoft")) {
			authRequest = new AuthMicrosoftRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Linkedin")) {
			authRequest = new AuthLinkedinRequest(authConfig);
		} else if (provider.equalsIgnoreCase("DingTalk")) {
			authRequest = new AuthDingTalkRequest(authConfig);
		} else if (provider.equalsIgnoreCase("gitee")) {
			authRequest = new AuthGiteeRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Baidu")) {
			authRequest = new AuthBaiduRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Douyin")) {
			authRequest = new AuthDouyinRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Eleme")) {
			authRequest = new AuthElemeRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Feishu")) {
			// authRequest = new AuthFeishuRequest(authConfig);
			authRequest = new AuthFeishu2Request(authConfig);
		} else if (provider.equalsIgnoreCase("Github")) {
			authRequest = new AuthGithubRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Gitlab")) {
			authRequest = new AuthGitlabRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Huawei")) {
			authRequest = new AuthHuaweiRequest(authConfig);
		} else if (provider.equalsIgnoreCase("jd")) {
			authRequest = new AuthJdRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Meituan")) {
			authRequest = new AuthMeituanRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Mi")) {
			authRequest = new AuthMiRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Oschina")) {
			authRequest = new AuthOschinaRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Taobao")) {
			authRequest = new AuthTaobaoRequest(authConfig);
		} else if (provider.equalsIgnoreCase("Toutiao")) {
			authRequest = new AuthToutiaoRequest(authConfig);
		} else if (provider.equalsIgnoreCase("WeChatQyQrcode")) {
			authRequest = new AuthWeChatEnterpriseQrcodeRequest(authConfig);
		} else if (provider.equalsIgnoreCase("workweixin")) {
			authRequest = new AuthWeChatEnterpriseWebRequestCost(authConfig);
		} else if (provider.equalsIgnoreCase("welink")) {
			authRequest = new AuthHuaweiWeLinkRequest(authConfig);
		} else if (provider.equalsIgnoreCase("maxkey")) {
			authRequest = new AuthMaxkeyRequest(authConfig);
		}

		return authRequest;
	}

	public String getAccountId(String provider, AuthResponse<?> authResponse) throws Exception {
		if (authResponse.getData() != null) {
			AuthUser authUser = (AuthUser) authResponse.getData();
			_logger.debug("AuthUser[{},{},{},{},{},{},{},{},{},{},{},{}]", authUser.getUuid(), authUser.getUsername(),
					authUser.getNickname(), authUser.getGender(), authUser.getEmail(), authUser.getCompany(),
					authUser.getBlog(), authUser.getLocation(), authUser.getRemark(), authUser.getSource(),
					authUser.getBlog(), authUser.getAvatar());
			_logger.debug("RawUserInfo {}", authUser.getRawUserInfo());
			if (provider.equalsIgnoreCase("WeChatOpen")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("sinaweibo")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("qq")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("Alipay")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("Twitter")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("google")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("microsoft")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("Linkedin")) {
				return authUser.getUuid();
			} else if (provider.equalsIgnoreCase("DingTalk")) {
				return authUser.getUuid();
			} else {
				return authUser.getUuid();
			}
		}
		return null;
	}

	public SocialsProviderLogin loadSocials(String instId) {
		SocialsProviderLogin socialsLogin = socialsProviderLoginStore.getIfPresent(instId);
		if (socialsLogin == null) {
			List<SocialsProvider> listSocialsProvider =
					jdbcTemplate.query(DEFAULT_SELECT_STATEMENT, new SocialsProviderRowMapper(), instId);
			_logger.trace("query SocialsProvider " + listSocialsProvider);

			List<SocialsProvider> socialSignOnProviders = new ArrayList<SocialsProvider>();
			socialsLogin = new SocialsProviderLogin(socialSignOnProviders);
			for (SocialsProvider socialsProvider : listSocialsProvider) {
				_logger.debug("Social Provider {} ({})", socialsProvider.getProvider(),
						socialsProvider.getProviderName());

				if (socialsProvider.getDisplay().equals("true")) {
					socialSignOnProviders.add(new SocialsProvider(socialsProvider));
				}

				if (socialsProvider.getScanCode().equalsIgnoreCase("true")) {
					socialsLogin.setQrScan(socialsProvider.getProvider());
				}

				// add to socialSignOnProviderMaps
				socialSignOnProviderMaps.put(instId + "_" + socialsProvider.getProvider(), socialsProvider);
			}

			_logger.debug("social SignOn Providers Login {}", socialsLogin);

			socialsProviderLoginStore.put(instId, socialsLogin);
		}
		return socialsLogin;
	}

	private final class SocialsProviderRowMapper implements RowMapper<SocialsProvider> {

		@Override
		public SocialsProvider mapRow(ResultSet rs, int rowNum) throws SQLException {
			SocialsProvider socialsProvider = new SocialsProvider();
			socialsProvider.setId(rs.getString("id"));
			socialsProvider.setProvider(rs.getString("provider"));
			socialsProvider.setProviderName(rs.getString("providername"));
			socialsProvider.setIcon(rs.getString("icon"));
			socialsProvider.setClientId(rs.getString("clientid"));
			String clientSecret = rs.getString("clientsecret");
			clientSecret = PasswordReciprocal.getInstance().decoder(clientSecret);
			socialsProvider.setClientSecret(clientSecret);
			socialsProvider.setAgentId(rs.getString("agentId"));
			socialsProvider.setDisplay(rs.getString("display"));
			socialsProvider.setSortIndex(rs.getInt("sortindex"));
			socialsProvider.setScanCode(rs.getString("scancode"));
			socialsProvider.setStatus(rs.getInt("status"));
			socialsProvider.setInstId(rs.getString("instid"));
			return socialsProvider;
		}
	}

	public void setRedisTokenStore(RedisTokenStore redisTokenStore) {
		this.redisTokenStore = redisTokenStore;
	}
}
