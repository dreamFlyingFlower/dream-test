package com.wy.test.core.repository;

import java.sql.Types;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;

import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.PasswordSetType;
import com.wy.test.core.password.PasswordGen;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.helper.DateTimeHelper;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PasswordPolicyValidator {

	PasswordPolicyRepository passwordPolicyRepository;

	protected JdbcTemplate jdbcTemplate;

	MessageSource messageSource;

	public static final String PASSWORD_POLICY_VALIDATE_RESULT = "PASSWORD_POLICY_SESSION_VALIDATE_RESULT_KEY";

	private static final String LOCK_USER_UPDATE_STATEMENT =
			"update auth_user set islocked = ?  , unlocktime = ? where id = ?";

	private static final String UNLOCK_USER_UPDATE_STATEMENT =
			"update auth_user set islocked = ? , unlocktime = ? where id = ?";

	private static final String BADPASSWORDCOUNT_UPDATE_STATEMENT =
			"update auth_user set badpasswordcount = ? , badpasswordtime = ?  where id = ?";

	private static final String BADPASSWORDCOUNT_RESET_UPDATE_STATEMENT =
			"update auth_user set badpasswordcount = ? , islocked = ? ,unlocktime = ?  where id = ?";

	public PasswordPolicyValidator() {
	}

	public PasswordPolicyValidator(JdbcTemplate jdbcTemplate, MessageSource messageSource) {
		this.messageSource = messageSource;
		this.jdbcTemplate = jdbcTemplate;
		this.passwordPolicyRepository = new PasswordPolicyRepository(jdbcTemplate);

	}

	/**
	 * static validator .
	 * 
	 * @param userInfo
	 * @return boolean
	 */
	public boolean validator(ChangePassword changePassword) {

		String password = changePassword.getPassword();
		String username = changePassword.getUsername();

		if (password.equals("") || password == null) {
			log.debug("password  is Empty ");
			return false;
		}

		PasswordValidator validator = new PasswordValidator(new PasswordPolicyMessageResolver(messageSource),
				passwordPolicyRepository.getPasswordPolicyRuleList());

		RuleResult result = validator.validate(new PasswordData(username, password));

		if (result.isValid()) {
			log.debug("Password is valid");
			return true;
		} else {
			log.debug("Invalid password:");
			String passwordPolicyMessage = "";
			for (String msg : validator.getMessages(result)) {
				passwordPolicyMessage = passwordPolicyMessage + msg + "<br>";
				log.debug("Rule Message {}", msg);
			}
			WebContext.setAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT, passwordPolicyMessage);
			return false;
		}
	}

	/**
	 * dynamic passwordPolicy Valid for user login.
	 * 
	 * @param userInfo
	 * @return boolean
	 */
	public boolean passwordPolicyValid(UserVO userInfo) {

		PasswordPolicyEntity passwordPolicy = passwordPolicyRepository.getPasswordPolicy();

		DateTime currentdateTime = new DateTime();
		/*
		 * check login attempts fail times
		 */
		if (userInfo.getBadPasswordCount() >= passwordPolicy.getAttempts()) {
			log.debug("login Attempts is " + userInfo.getBadPasswordCount());
			// duration
			String badPasswordTimeString = DateTimeHelper.formatDateTime(userInfo.getBadPasswordTime());
			log.trace("bad Password Time " + badPasswordTimeString);

			DateTime badPasswordTime =
					DateTime.parse(badPasswordTimeString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
			Duration duration = new Duration(badPasswordTime, currentdateTime);
			int intDuration = Integer.parseInt(duration.getStandardMinutes() + "");
			log.debug("bad Password duration {} , " + "password policy Duration {} , " + "validate result {}",
					intDuration, passwordPolicy.getDuration(), (intDuration > passwordPolicy.getDuration()));
			// auto unlock attempts when intDuration >= set Duration
			if (intDuration >= passwordPolicy.getDuration()) {
				log.debug("resetAttempts ...");
				resetAttempts(userInfo);
			} else {
				lockUser(userInfo);
				throw new BadCredentialsException(WebContext.getI18nValue("login.error.attempts",
						new Object[] { userInfo.getBadPasswordCount(), passwordPolicy.getDuration() }));
			}
		}

		// locked
		if (userInfo.getIsLocked() == ConstStatus.LOCK) {
			throw new BadCredentialsException(
					userInfo.getUsername() + " " + WebContext.getI18nValue("login.error.locked"));
		}
		// inactive
		if (userInfo.getStatus() != ConstStatus.ACTIVE) {
			throw new BadCredentialsException(userInfo.getUsername() + WebContext.getI18nValue("login.error.inactive"));
		}

		return true;
	}

	public void applyPasswordPolicy(UserVO userInfo) {
		PasswordPolicyEntity passwordPolicy = passwordPolicyRepository.getPasswordPolicy();

		DateTime currentdateTime = new DateTime();
		// initial password need change
		if (userInfo.getLoginCount() <= 0) {
			WebContext.getSession().setAttribute(WebConstants.CURRENT_USER_PASSWORD_SET_TYPE,
					PasswordSetType.INITIAL_PASSWORD.ordinal());
		}

		if (userInfo.getPasswordSetType() != PasswordSetType.PASSWORD_NORMAL.ordinal()) {
			WebContext.getSession().setAttribute(WebConstants.CURRENT_USER_PASSWORD_SET_TYPE,
					userInfo.getPasswordSetType());
			return;
		} else {
			WebContext.getSession().setAttribute(WebConstants.CURRENT_USER_PASSWORD_SET_TYPE,
					PasswordSetType.PASSWORD_NORMAL.ordinal());
		}

		/*
		 * check password is Expired,Expiration is Expired date ,if Expiration equals 0,not need check
		 *
		 */
		if (passwordPolicy.getExpiration() > 0) {
			String passwordLastSetTimeString = DateTimeHelper.formatDateTime(userInfo.getPasswordLastSetTime());
			log.info("last password set date {}", passwordLastSetTimeString);

			DateTime changePwdDateTime =
					DateTime.parse(passwordLastSetTimeString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
			Duration duration = new Duration(changePwdDateTime, currentdateTime);
			int intDuration = Integer.parseInt(duration.getStandardDays() + "");
			log.debug(
					"password Last Set duration day {} , " + "password policy Expiration {} , " + "validate result {}",
					intDuration, passwordPolicy.getExpiration(), intDuration <= passwordPolicy.getExpiration());
			if (intDuration > passwordPolicy.getExpiration()) {
				WebContext.getSession().setAttribute(WebConstants.CURRENT_USER_PASSWORD_SET_TYPE,
						PasswordSetType.PASSWORD_EXPIRED.ordinal());
			}
		}

		resetBadPasswordCount(userInfo);
	}

	/**
	 * lockUser
	 * 
	 * @param userInfo
	 */
	public void lockUser(UserVO userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				if (userInfo.getIsLocked() == ConstStatus.ACTIVE) {
					jdbcTemplate.update(LOCK_USER_UPDATE_STATEMENT,
							new Object[] { ConstStatus.LOCK, new Date(), userInfo.getId() },
							new int[] { Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
					userInfo.setIsLocked(ConstStatus.LOCK);
				}
			}
		} catch (Exception e) {
			log.error("lockUser Exception", e);
		}
	}

	/**
	 * unlockUser
	 * 
	 * @param userInfo
	 */
	public void unlockUser(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				jdbcTemplate.update(UNLOCK_USER_UPDATE_STATEMENT,
						new Object[] { ConstStatus.ACTIVE, new Date(), userInfo.getId() },
						new int[] { Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
				userInfo.setIsLocked(ConstStatus.ACTIVE);
			}
		} catch (Exception e) {
			log.error("unlockUser Exception", e);
		}
	}

	/**
	 * reset BadPasswordCount And Lockout
	 * 
	 * @param userInfo
	 */
	public void resetAttempts(UserVO userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				jdbcTemplate.update(BADPASSWORDCOUNT_RESET_UPDATE_STATEMENT,
						new Object[] { 0, ConstStatus.ACTIVE, new Date(), userInfo.getId() },
						new int[] { Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR });
				userInfo.setIsLocked(ConstStatus.ACTIVE);
				userInfo.setBadPasswordCount(0);
			}
		} catch (Exception e) {
			log.error("resetAttempts Exception", e);
		}
	}

	/**
	 * if login password is error ,BadPasswordCount++ and set bad date
	 * 
	 * @param userInfo
	 */
	private void setBadPasswordCount(String userId, int badPasswordCount) {
		try {
			jdbcTemplate.update(BADPASSWORDCOUNT_UPDATE_STATEMENT,
					new Object[] { badPasswordCount, new Date(), userId },
					new int[] { Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR });
		} catch (Exception e) {
			log.error("setBadPasswordCount Exception", e);
		}
	}

	public void plusBadPasswordCount(UserVO userInfo) {
		if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
			userInfo.setBadPasswordCount(userInfo.getBadPasswordCount() + 1);
			setBadPasswordCount(userInfo.getId(), userInfo.getBadPasswordCount());
			PasswordPolicyEntity passwordPolicy = passwordPolicyRepository.getPasswordPolicy();
			if (userInfo.getBadPasswordCount() >= passwordPolicy.getAttempts()) {
				log.debug("Bad Password Count {} , Max Attempts {}", userInfo.getBadPasswordCount() + 1,
						passwordPolicy.getAttempts());
				this.lockUser(userInfo);
			}
		}
	}

	public void resetBadPasswordCount(UserVO userInfo) {
		if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
			if (userInfo.getBadPasswordCount() > 0) {
				setBadPasswordCount(userInfo.getId(), 0);
			}
		}
	}

	public String generateRandomPassword() {
		PasswordPolicyEntity passwordPolicy = passwordPolicyRepository.getPasswordPolicy();

		PasswordGen passwordGen = new PasswordGen(passwordPolicy.getRandomPasswordLength());

		return passwordGen.gen(passwordPolicy.getLowerCase(), passwordPolicy.getUpperCase(), passwordPolicy.getDigits(),
				passwordPolicy.getSpecialChar());
	}

	public PasswordPolicyRepository getPasswordPolicyRepository() {
		return passwordPolicyRepository;
	}
}