package com.wy.test.authentication.otp.onetimepwd;

import com.wy.test.authentication.otp.onetimepwd.token.AbstractOtpTokenStore;
import com.wy.test.authentication.otp.onetimepwd.token.InMemoryOtpTokenStore;
import com.wy.test.core.entity.UserEntity;

import dream.flying.flower.generator.StringGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractOTPAuthn.
 */
@Slf4j
public abstract class AbstractOtpAuthn {

	protected AbstractOtpTokenStore optTokenStore = new InMemoryOtpTokenStore();

	// 验证码有效間隔
	protected int interval = 30;

	// 验证码长度,范围4～10,默认为6
	protected int digits = 6;

	protected String crypto = "HmacSHA1";

	protected String defaultEncoding = "utf-8";

	StringGenerator stringGenerator;

	protected String otpType = OtpTypes.TIMEBASED_OTP;

	public static final class OtpTypes {

		// 手机
		public static String MOBILE = "MOBILE";

		// 短信
		public static String SMS = "SMS";

		// 邮箱
		public static String EMAIL = "EMAIL";

		// TIMEBASED_OPT
		public static String TIMEBASED_OTP = "TOPT";

		// HmacOTP
		public static String HOTP_OTP = "HOTP";

		public static String RSA_OTP = "RSA";

		public static String CAP_OTP = "CAP";
	}

	public abstract boolean produce(UserEntity userInfo);

	public abstract boolean validate(UserEntity userInfo, String token);

	protected String defaultProduce(UserEntity userInfo) {
		return genToken(userInfo);
	}

	/**
	 * genToken.
	 * 
	 * @param userInfo UserInfo
	 * @return
	 */
	public String genToken(UserEntity userInfo) {
		if (stringGenerator == null) {
			stringGenerator = new StringGenerator(StringGenerator.DEFAULT_CODE_NUMBER, digits);
		}
		String token = stringGenerator.randomGenerate();
		log.debug("Generator token " + token);
		return token;
	}

	/**
	 * the interval.
	 * 
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * interval the interval to set.
	 * 
	 * @param interval the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * digits.
	 * 
	 * @return the digits
	 */
	public int getDigits() {
		return digits;
	}

	/**
	 * digits the digits to set.
	 * 
	 * @param digits the digits to set
	 */
	public void setDigits(int digits) {
		this.digits = digits;
	}

	/**
	 * crypto.
	 * 
	 * @return the crypto
	 */
	public String getCrypto() {
		return crypto;
	}

	/**
	 * crypto the crypto to set.
	 * 
	 * @param crypto the crypto to set
	 */
	public void setCrypto(String crypto) {
		this.crypto = crypto;
	}

	public String getOtpType() {
		return otpType;
	}

	public void setOtpType(String optType) {
		this.otpType = optType;
	}

	public void setOptTokenStore(AbstractOtpTokenStore optTokenStore) {
		this.optTokenStore = optTokenStore;
	}

	public void initPropertys() {

	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}
}