package com.wy.test.authentication.social.zhyd.config;

import com.wy.test.authentication.social.zhyd.request.AuthFeishu2Request;

import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

public enum AuthDefaultSource implements AuthSource {
	FEISHU2 {

		@Override
		public String authorize() {
			return "https://passport.feishu.cn/suite/passport/oauth/authorize";
		}

		@Override
		public String accessToken() {
			return "https://passport.feishu.cn/suite/passport/oauth/token";
		}

		@Override
		public String userInfo() {
			return "https://passport.feishu.cn/suite/passport/oauth/userinfo";
		}

		@Override
		public String refresh() {
			return "https://passport.feishu.cn/suite/passport/oauth/token";
		}

		@Override
		public Class<? extends AuthDefaultRequest> getTargetClass() {
			return AuthFeishu2Request.class;
		}
	}

}
