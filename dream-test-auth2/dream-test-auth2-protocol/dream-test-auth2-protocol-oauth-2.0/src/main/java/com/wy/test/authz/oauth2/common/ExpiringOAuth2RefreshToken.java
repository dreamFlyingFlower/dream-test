package com.wy.test.authz.oauth2.common;

import java.util.Date;

public interface ExpiringOAuth2RefreshToken extends OAuth2RefreshToken {

	Date getExpiration();

}
