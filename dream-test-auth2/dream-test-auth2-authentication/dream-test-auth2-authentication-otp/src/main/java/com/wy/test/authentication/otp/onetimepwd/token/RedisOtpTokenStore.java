package com.wy.test.authentication.otp.onetimepwd.token;

import org.joda.time.DateTime;

import com.wy.test.authentication.otp.onetimepwd.OneTimePassword;
import com.wy.test.core.constant.ConstTimeInterval;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.redis.RedisConnection;
import com.wy.test.core.redis.RedisConnectionFactory;

public class RedisOtpTokenStore extends AbstractOtpTokenStore {

	protected long validitySeconds = ConstTimeInterval.ONE_MINUTE * 5;

	RedisConnectionFactory connectionFactory;

	public RedisOtpTokenStore(RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	public static String PREFIX = "REDIS_OTP_SERVICE_";

	@Override
	public void store(UserEntity userInfo, String token, String receiver, String type) {
		DateTime currentDateTime = new DateTime();
		OneTimePassword otp = new OneTimePassword();
		otp.setId(userInfo.getUsername() + "_" + type + "_" + token);
		otp.setType(type);
		otp.setUsername(userInfo.getUsername());
		otp.setToken(token);
		otp.setReceiver(receiver);
		otp.setCreateTime(currentDateTime.toString("yyyy-MM-dd HH:mm:ss"));
		RedisConnection conn = connectionFactory.getConnection();
		conn.setexObject(PREFIX + otp.getId(), validitySeconds, otp);
		conn.close();
	}

	@Override
	public boolean validate(UserEntity userInfo, String token, String type, int interval) {
		RedisConnection conn = connectionFactory.getConnection();
		OneTimePassword otp =
				(OneTimePassword) conn.getObject(PREFIX + userInfo.getUsername() + "_" + type + "_" + token);
		conn.delete(PREFIX + userInfo.getUsername() + "_" + type + "_" + token);
		conn.close();
		if (otp != null) {
			return true;
		}
		return false;
	}

}
