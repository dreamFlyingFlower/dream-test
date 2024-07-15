package com.wy.test.otp.password.onetimepwd.token;

import com.wy.test.entity.UserInfo;

public abstract class AbstractOtpTokenStore {

	public abstract void store(UserInfo userInfo, String token, String receiver, String type);

	public abstract boolean validate(UserInfo userInfo, String token, String type, int interval);
}