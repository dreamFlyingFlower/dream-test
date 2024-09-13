package com.wy.test.authentication.otp.onetimepwd.token;

import com.wy.test.core.entity.UserEntity;

public abstract class AbstractOtpTokenStore {

	public abstract void store(UserEntity userInfo, String token, String receiver, String type);

	public abstract boolean validate(UserEntity userInfo, String token, String type, int interval);
}