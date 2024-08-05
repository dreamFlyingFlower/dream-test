package com.wy.test.core.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.wy.test.core.constants.ConstRoles;
import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.UserVO;

import dream.flying.flower.lang.StrHelper;

public class LoginRepository {

	private static Logger _logger = LoggerFactory.getLogger(LoginRepository.class);

	private static final String LOCK_USER_UPDATE_STATEMENT =
			"update mxk_userinfo set islocked = ?  , unlocktime = ? where id = ?";

	private static final String UNLOCK_USER_UPDATE_STATEMENT =
			"update mxk_userinfo set islocked = ? , unlocktime = ? where id = ?";

	private static final String BADPASSWORDCOUNT_UPDATE_STATEMENT =
			"update mxk_userinfo set badpasswordcount = ? , badpasswordtime = ?  where id = ?";

	private static final String BADPASSWORDCOUNT_RESET_UPDATE_STATEMENT =
			"update mxk_userinfo set badpasswordcount = ? , islocked = ? ,unlocktime = ?  where id = ?";

	private static final String LOGIN_USERINFO_UPDATE_STATEMENT =
			"update mxk_userinfo set lastlogintime = ?  , lastloginip = ? , logincount = ?, online = "
					+ UserEntity.ONLINE.ONLINE + "  where id = ?";

	private static final String ROLES_SELECT_STATEMENT =
			"select distinct r.id,r.rolecode,r.rolename from mxk_userinfo u,mxk_roles r,mxk_role_member rm where u.id = ?  and u.id=rm.memberid and rm.roleid=r.id ";

	private static final String DEFAULT_USERINFO_SELECT_STATEMENT = "select * from  mxk_userinfo where username = ? ";

	private static final String DEFAULT_USERINFO_SELECT_STATEMENT_USERNAME_MOBILE =
			"select * from  mxk_userinfo where (username = ? or mobile = ?)";

	private static final String DEFAULT_USERINFO_SELECT_STATEMENT_USERNAME_MOBILE_EMAIL =
			"select * from  mxk_userinfo where (username = ? or mobile = ? or email = ?) ";

	private static final String DEFAULT_MYAPPS_SELECT_STATEMENT =
			"select distinct app.id,app.appname from mxk_apps app,mxk_role_permissions pm,mxk_roles r  where app.id=pm.appid and app.status =	1 and pm.roleid=r.id and r.id in(%s)";

	protected JdbcTemplate jdbcTemplate;

	/**
	 * 1 (USERNAME) 2 (USERNAME | MOBILE) 3 (USERNAME | MOBILE | EMAIL)
	 */
	public static int LOGIN_ATTRIBUTE_TYPE = 2;

	public LoginRepository() {

	}

	public LoginRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserVO find(String username, String password) {
		List<UserEntity> listUserInfo = null;
		if (LOGIN_ATTRIBUTE_TYPE == 1) {
			listUserInfo = findByUsername(username, password);
		} else if (LOGIN_ATTRIBUTE_TYPE == 2) {
			listUserInfo = findByUsernameOrMobile(username, password);
		} else if (LOGIN_ATTRIBUTE_TYPE == 3) {
			listUserInfo = findByUsernameOrMobileOrEmail(username, password);
		}

		UserVO userInfo = null;
		if (listUserInfo != null && listUserInfo.size() > 0) {
			userInfo = UserConvert.INSTANCE.convertt(listUserInfo.get(0));
		}
		_logger.debug("load UserInfo : " + userInfo);
		return userInfo;
	}

	public List<UserEntity> findByUsername(String username, String password) {
		return jdbcTemplate.query(DEFAULT_USERINFO_SELECT_STATEMENT, new UserInfoRowMapper(), username);
	}

	public List<UserEntity> findByUsernameOrMobile(String username, String password) {
		return jdbcTemplate.query(DEFAULT_USERINFO_SELECT_STATEMENT_USERNAME_MOBILE, new UserInfoRowMapper(), username,
				username);
	}

	public List<UserEntity> findByUsernameOrMobileOrEmail(String username, String password) {
		return jdbcTemplate.query(DEFAULT_USERINFO_SELECT_STATEMENT_USERNAME_MOBILE_EMAIL, new UserInfoRowMapper(),
				username, username, username);
	}

	/**
	 * 閿佸畾鐢ㄦ埛锛歩slock锛�1 鐢ㄦ埛瑙ｉ攣 2 鐢ㄦ埛閿佸畾
	 * 
	 * @param userInfo
	 */
	public void updateLock(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				jdbcTemplate.update(LOCK_USER_UPDATE_STATEMENT,
						new Object[] { ConstStatus.LOCK, new Date(), userInfo.getId() },
						new int[] { Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
				userInfo.setIsLocked(ConstStatus.LOCK);
			}
		} catch (Exception e) {
			_logger.error("lockUser Exception", e);
		}
	}

	/**
	 * 閿佸畾鐢ㄦ埛锛歩slock锛�1 鐢ㄦ埛瑙ｉ攣 2 鐢ㄦ埛閿佸畾
	 * 
	 * @param userInfo
	 */
	public void updateUnlock(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				jdbcTemplate.update(UNLOCK_USER_UPDATE_STATEMENT,
						new Object[] { ConstStatus.ACTIVE, new Date(), userInfo.getId() },
						new int[] { Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
				userInfo.setIsLocked(ConstStatus.ACTIVE);
			}
		} catch (Exception e) {
			_logger.error("unlockUser Exception", e);
		}
	}

	/**
	 * reset BadPasswordCount And Lockout
	 * 
	 * @param userInfo
	 */
	public void updateLockout(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				jdbcTemplate.update(BADPASSWORDCOUNT_RESET_UPDATE_STATEMENT,
						new Object[] { 0, ConstStatus.ACTIVE, new Date(), userInfo.getId() },
						new int[] { Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR });
				userInfo.setIsLocked(ConstStatus.ACTIVE);
			}
		} catch (Exception e) {
			_logger.error("resetBadPasswordCountAndLockout Exception", e);
		}
	}

	/**
	 * if login password is error ,BadPasswordCount++ and set bad date
	 * 
	 * @param userInfo
	 */
	public void updateBadPasswordCount(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				int badPasswordCount = userInfo.getBadPasswordCount() + 1;
				userInfo.setBadPasswordCount(badPasswordCount);
				jdbcTemplate.update(BADPASSWORDCOUNT_UPDATE_STATEMENT,
						new Object[] { badPasswordCount, new Date(), userInfo.getId() },
						new int[] { Types.INTEGER, Types.TIMESTAMP, Types.VARCHAR });
			}
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error(e.getMessage());
		}
	}

	public ArrayList<GrantedAuthority> queryAuthorizedApps(ArrayList<GrantedAuthority> grantedAuthoritys) {
		String grantedAuthorityString = "'ROLE_ALL_USER'";
		for (GrantedAuthority grantedAuthority : grantedAuthoritys) {
			grantedAuthorityString += ",'" + grantedAuthority.getAuthority() + "'";
		}

		ArrayList<GrantedAuthority> listAuthorizedApps = (ArrayList<GrantedAuthority>) jdbcTemplate.query(
				String.format(DEFAULT_MYAPPS_SELECT_STATEMENT, grantedAuthorityString),
				new RowMapper<GrantedAuthority>() {

					@Override
					public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new SimpleGrantedAuthority(rs.getString("id"));
					}
				});

		_logger.debug("list Authorized Apps  " + listAuthorizedApps);
		return listAuthorizedApps;
	}

	public List<RoleEntity> queryRoles(UserVO userInfo) {
		List<RoleEntity> listRoles = jdbcTemplate.query(ROLES_SELECT_STATEMENT, new RowMapper<RoleEntity>() {

			@Override
			public RoleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				RoleEntity role =
						new RoleEntity(rs.getString("id"), rs.getString("rolecode"), rs.getString("rolename"), 0);

				return role;
			}
		}, userInfo.getId());

		_logger.debug("list Roles  " + listRoles);
		return listRoles;
	}

	/**
	 * grant Authority by userinfo
	 * 
	 * @param userInfo
	 * @return ArrayList<GrantedAuthority>
	 */
	public ArrayList<GrantedAuthority> grantAuthority(UserVO userInfo) {
		// query roles for user
		List<RoleEntity> listRoles = queryRoles(userInfo);

		// set default roles
		ArrayList<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
		grantedAuthority.add(ConstRoles.ROLE_USER);
		grantedAuthority.add(ConstRoles.ROLE_ALL_USER);
		grantedAuthority.add(ConstRoles.ROLE_ORDINARY_USER);
		for (RoleEntity role : listRoles) {
			grantedAuthority.add(new SimpleGrantedAuthority(role.getId()));
			if (role.getRoleCode().startsWith("ROLE_")
					&& !grantedAuthority.contains(new SimpleGrantedAuthority(role.getRoleCode()))) {
				grantedAuthority.add(new SimpleGrantedAuthority(role.getRoleCode()));
			}
		}
		_logger.debug("Authority : " + grantedAuthority);

		return grantedAuthority;
	}

	public void updateLastLogin(UserVO userInfo) {
		jdbcTemplate.update(
				LOGIN_USERINFO_UPDATE_STATEMENT, new Object[] { userInfo.getLastLoginTime(), userInfo.getLastLoginIp(),
						userInfo.getLoginCount() + 1, userInfo.getId() },
				new int[] { Types.TIMESTAMP, Types.VARCHAR, Types.INTEGER, Types.VARCHAR });
	}

	public class UserInfoRowMapper implements RowMapper<UserEntity> {

		@Override
		public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

			UserEntity userInfo = new UserEntity();
			userInfo.setId(rs.getString("id"));
			userInfo.setUsername(rs.getString("username"));
			userInfo.setPassword(rs.getString("password"));
			userInfo.setSharedSecret(rs.getString("sharedsecret"));
			userInfo.setSharedCounter(rs.getString("sharedcounter"));
			userInfo.setDecipherable(rs.getString("decipherable"));
			userInfo.setWindowsAccount(rs.getString("windowsaccount"));
			userInfo.setUserType(rs.getString("usertype"));

			userInfo.setDisplayName(rs.getString("displayname"));
			userInfo.setNickName(rs.getString("nickname"));
			userInfo.setNameZhSpell(rs.getString("namezhspell"));// nameZHSpell
			userInfo.setNameZhShortSpell(rs.getString("namezhshortspell"));// nameZHSpell
			userInfo.setGivenName(rs.getString("givenname"));
			userInfo.setMiddleName(rs.getString("middlename"));
			userInfo.setFamilyName(rs.getString("familyname"));
			userInfo.setHonorificPrefix(rs.getString("honorificprefix"));
			userInfo.setHonorificSuffix(rs.getString("honorificsuffix"));
			userInfo.setFormattedName(rs.getString("formattedname"));

			userInfo.setGender(rs.getInt("gender"));
			userInfo.setBirthDate(rs.getDate("birthdate"));
			userInfo.setPicture(rs.getBytes("picture"));
			userInfo.setMarried(rs.getInt("married"));
			userInfo.setIdType(rs.getInt("idtype"));
			userInfo.setIdCardNo(rs.getString("idcardno"));
			userInfo.setWebSite(rs.getString("website"));

			userInfo.setAuthnType(rs.getInt("authntype"));
			userInfo.setMobile(rs.getString("mobile"));
			userInfo.setMobileVerified(rs.getInt("mobileverified"));
			userInfo.setEmail(rs.getString("email"));
			userInfo.setEmailVerified(rs.getInt("emailverified"));
			userInfo.setPasswordQuestion(rs.getString("passwordquestion"));
			userInfo.setPasswordAnswer(rs.getString("passwordanswer"));

			userInfo.setAppLoginAuthnType(rs.getInt("apploginauthntype"));
			userInfo.setAppLoginPassword(rs.getString("apploginpassword"));
			userInfo.setProtectedApps(rs.getString("protectedapps"));

			userInfo.setPasswordLastSetTime(rs.getDate("passwordlastsettime"));
			userInfo.setPasswordSetType(rs.getInt("passwordsettype"));
			userInfo.setBadPasswordCount(rs.getInt("badpasswordcount"));
			userInfo.setBadPasswordTime(rs.getDate("badpasswordtime"));
			userInfo.setUnLockTime(rs.getString("unlocktime"));
			userInfo.setIsLocked(rs.getInt("islocked"));
			userInfo.setLastLoginTime(rs.getDate("lastlogintime"));
			userInfo.setLastLoginIp(rs.getString("lastloginip"));
			userInfo.setLastLogoffTime(rs.getDate("lastlogofftime"));
			userInfo.setLoginCount(rs.getInt("logincount"));
			userInfo.setRegionHistory(rs.getString("regionhistory"));
			userInfo.setPasswordHistory(rs.getString("passwordhistory"));

			userInfo.setTimeZone(rs.getString("timezone"));
			userInfo.setLocale(rs.getString("locale"));
			userInfo.setPreferredLanguage(rs.getString("preferredlanguage"));

			userInfo.setWorkEmail(rs.getString("workemail"));
			userInfo.setWorkPhoneNumber(rs.getString("workphonenumber"));
			userInfo.setWorkCountry(rs.getString("workcountry"));
			userInfo.setWorkRegion(rs.getString("workregion"));
			userInfo.setWorkLocality(rs.getString("worklocality"));
			userInfo.setWorkStreetAddress(rs.getString("workstreetaddress"));
			userInfo.setWorkAddressFormatted(rs.getString("workaddressformatted"));
			userInfo.setWorkPostalCode(rs.getString("workpostalcode"));
			userInfo.setWorkFax(rs.getString("workfax"));

			userInfo.setHomeEmail(rs.getString("homeemail"));
			userInfo.setHomePhoneNumber(rs.getString("homephonenumber"));
			userInfo.setHomeCountry(rs.getString("homecountry"));
			userInfo.setHomeRegion(rs.getString("homeregion"));
			userInfo.setHomeLocality(rs.getString("homelocality"));
			userInfo.setHomeStreetAddress(rs.getString("homestreetaddress"));
			userInfo.setHomeAddressFormatted(rs.getString("homeaddressformatted"));
			userInfo.setHomePostalCode(rs.getString("homepostalcode"));
			userInfo.setHomeFax(rs.getString("homefax"));

			userInfo.setEmployeeNumber(rs.getString("employeenumber"));
			userInfo.setDivision(rs.getString("division"));
			userInfo.setCostCenter(rs.getString("costcenter"));
			userInfo.setOrganization(rs.getString("organization"));
			userInfo.setDepartmentId(rs.getString("departmentid"));
			userInfo.setDepartment(rs.getString("department"));
			userInfo.setJobTitle(rs.getString("jobtitle"));
			userInfo.setJobLevel(rs.getString("joblevel"));
			userInfo.setManagerId(rs.getString("managerid"));
			userInfo.setManager(rs.getString("manager"));
			userInfo.setAssistantId(rs.getString("assistantid"));
			userInfo.setAssistant(rs.getString("assistant"));
			userInfo.setEntryDate(rs.getDate("entrydate"));//
			userInfo.setQuitDate(rs.getDate("quitdate"));
			userInfo.setStartWorkDate(rs.getDate("startworkdate"));// STARTWORKDATE

			userInfo.setExtraAttribute(rs.getString("extraattribute"));

			userInfo.setCreateUser(rs.getLong("create_user"));
			userInfo.setCreateTime(rs.getDate("create_time"));
			userInfo.setUpdateUser(rs.getLong("update_user"));
			userInfo.setUpdateTime(rs.getDate("update_time"));

			userInfo.setStatus(rs.getInt("status"));
			userInfo.setGridList(rs.getInt("gridlist"));
			userInfo.setRemark(rs.getString("remark"));
			userInfo.setTheme(rs.getString("theme"));
			userInfo.setInstId(rs.getString("instid"));
			if (userInfo.getTheme() == null || userInfo.getTheme().equalsIgnoreCase("")) {
				userInfo.setTheme("default");
			}

			return userInfo;
		}
	}
}
