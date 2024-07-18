package com.wy.test.otp.password.onetimepwd.token;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.otp.password.onetimepwd.OneTimePassword;

public class InMemoryOtpTokenStore extends AbstractOtpTokenStore {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryOtpTokenStore.class);

	protected static final Cache<String, OneTimePassword> optTokenStore =
			Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

	@Override
	public void store(UserInfo userInfo, String token, String receiver, String type) {
		DateTime currentDateTime = new DateTime();
		OneTimePassword otp = new OneTimePassword();
		otp.setId(userInfo.getUsername() + "_" + type + "_" + token);
		otp.setType(type);
		otp.setUsername(userInfo.getUsername());
		otp.setToken(token);
		otp.setReceiver(receiver);
		otp.setCreateTime(currentDateTime.toString("yyyy-MM-dd HH:mm:ss"));
		optTokenStore.put(otp.getId(), otp);

	}

	@Override
	public boolean validate(UserInfo userInfo, String token, String type, int interval) {
		OneTimePassword otp = optTokenStore.getIfPresent(userInfo.getUsername() + "_" + type + "_" + token);
		if (otp != null) {
			DateTime currentdateTime = new DateTime();
			DateTime oneCreateTime =
					DateTime.parse(otp.getCreateTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
			Duration duration = new Duration(oneCreateTime, currentdateTime);
			int intDuration = Integer.parseInt(duration.getStandardSeconds() + "");
			logger.debug("validate duration " + intDuration);
			logger.debug("validate result " + (intDuration <= interval));
			if (intDuration <= interval) {
				return true;
			}
		}
		return false;
	}

	public InMemoryOtpTokenStore() {

	}
}
