package com.wy.test.core.repository;

import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.passay.CharacterOccurrencesRule;
import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.Rule;
import org.passay.UsernameRule;
import org.passay.WhitespaceRule;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.DictionaryBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.constant.ConstProperties;
import com.wy.test.core.entity.PasswordPolicyEntity;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PasswordPolicyRepository {

	// Dictionary topWeakPassword Source
	public static final String topWeakPasswordPropertySource = "classpath:/top_weak_password.txt";

	// Cache PasswordPolicy in memory ONE_HOUR
	protected static final Cache<String, PasswordPolicyEntity> passwordPolicyStore =
			Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();

	protected PasswordPolicyEntity passwordPolicy;

	protected JdbcTemplate jdbcTemplate;

	ArrayList<Rule> passwordPolicyRuleList;

	private static final String PASSWORD_POLICY_KEY = "PASSWORD_POLICY_KEY";

	private static final String PASSWORD_POLICY_SELECT_STATEMENT = "select * from auth_password_policy ";

	public PasswordPolicyRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * init PasswordPolicy and load Rules
	 * 
	 * @return
	 */
	public PasswordPolicyEntity getPasswordPolicy() {
		passwordPolicy = passwordPolicyStore.getIfPresent(PASSWORD_POLICY_KEY);

		if (passwordPolicy == null) {
			passwordPolicy =
					jdbcTemplate.queryForObject(PASSWORD_POLICY_SELECT_STATEMENT, new PasswordPolicyRowMapper());
			log.debug("query PasswordPolicy : " + passwordPolicy);
			passwordPolicyStore.put(PASSWORD_POLICY_KEY, passwordPolicy);

			// RandomPasswordLength =(MaxLength +MinLength)/2
			passwordPolicy.setRandomPasswordLength(
					Math.round((passwordPolicy.getMaxLength() + passwordPolicy.getMinLength()) / 2));

			passwordPolicyRuleList = new ArrayList<Rule>();
			passwordPolicyRuleList.add(new WhitespaceRule());
			passwordPolicyRuleList.add(new LengthRule(passwordPolicy.getMinLength(), passwordPolicy.getMaxLength()));

			if (passwordPolicy.getUpperCase() > 0) {
				passwordPolicyRuleList
						.add(new CharacterRule(EnglishCharacterData.UpperCase, passwordPolicy.getUpperCase()));
			}

			if (passwordPolicy.getLowerCase() > 0) {
				passwordPolicyRuleList
						.add(new CharacterRule(EnglishCharacterData.LowerCase, passwordPolicy.getLowerCase()));
			}

			if (passwordPolicy.getDigits() > 0) {
				passwordPolicyRuleList.add(new CharacterRule(EnglishCharacterData.Digit, passwordPolicy.getDigits()));
			}

			if (passwordPolicy.getSpecialChar() > 0) {
				passwordPolicyRuleList
						.add(new CharacterRule(EnglishCharacterData.Special, passwordPolicy.getSpecialChar()));
			}

			if (passwordPolicy.getUsername() > 0) {
				passwordPolicyRuleList.add(new UsernameRule());
			}

			if (passwordPolicy.getOccurances() > 0) {
				passwordPolicyRuleList.add(new CharacterOccurrencesRule(passwordPolicy.getOccurances()));
			}

			if (passwordPolicy.getAlphabetical() > 0) {
				passwordPolicyRuleList.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, false));
			}

			if (passwordPolicy.getNumerical() > 0) {
				passwordPolicyRuleList.add(new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, false));
			}

			if (passwordPolicy.getQwerty() > 0) {
				passwordPolicyRuleList.add(new IllegalSequenceRule(EnglishSequenceData.USQwerty, 4, false));
			}

			if (passwordPolicy.getDictionary() > 0) {
				try {
					ClassPathResource dictFile =
							new ClassPathResource(ConstProperties.classPathResource(topWeakPasswordPropertySource));
					Dictionary dictionary =
							new DictionaryBuilder().addReader(new InputStreamReader(dictFile.getInputStream())).build();
					passwordPolicyRuleList.add(new DictionaryRule(dictionary));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return passwordPolicy;
	}

	public ArrayList<Rule> getPasswordPolicyRuleList() {
		getPasswordPolicy();
		return passwordPolicyRuleList;
	}

	public class PasswordPolicyRowMapper implements RowMapper<PasswordPolicyEntity> {

		@Override
		public PasswordPolicyEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			PasswordPolicyEntity passwordPolicy = new PasswordPolicyEntity();
			passwordPolicy.setId(rs.getString("id"));
			passwordPolicy.setMinLength(rs.getInt("min_length"));
			passwordPolicy.setMaxLength(rs.getInt("max_length"));
			passwordPolicy.setLowerCase(rs.getInt("lower_case"));
			passwordPolicy.setUpperCase(rs.getInt("upper_case"));
			passwordPolicy.setDigits(rs.getInt("digits"));
			passwordPolicy.setSpecialChar(rs.getInt("special_char"));
			passwordPolicy.setAttempts(rs.getInt("attempts"));
			passwordPolicy.setDuration(rs.getInt("duration"));
			passwordPolicy.setExpiration(rs.getInt("expiration"));
			passwordPolicy.setUsername(rs.getInt("username"));
			passwordPolicy.setHistory(rs.getInt("history"));
			passwordPolicy.setDictionary(rs.getInt("dictionary"));
			passwordPolicy.setAlphabetical(rs.getInt("alphabetical"));
			passwordPolicy.setNumerical(rs.getInt("numerical"));
			passwordPolicy.setQwerty(rs.getInt("qwerty"));
			passwordPolicy.setOccurances(rs.getInt("occurances"));
			return passwordPolicy;
		}
	}
}