package com.wy.test.persistence.service;

import java.sql.Types;

import org.dromara.mybatis.jpa.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.entity.Accounts;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.persistence.repository.PasswordPolicyValidator;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.mapper.UserInfoMapper;
import com.wy.test.persistence.provision.ProvisionAction;
import com.wy.test.persistence.provision.ProvisionService;
import com.wy.test.persistence.provision.ProvisionTopic;

import dream.flying.flower.helper.DateTimeHelper;
import dream.flying.flower.lang.StrHelper;

@Repository
public class UserInfoService extends JpaService<UserInfo> {

	final static Logger _logger = LoggerFactory.getLogger(UserInfoService.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	PasswordPolicyValidator passwordPolicyValidator;

	@Autowired
	ProvisionService provisionService;

	AccountsService accountsService;

	public UserInfoService() {
		super(UserInfoMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public UserInfoMapper getMapper() {
		return (UserInfoMapper) super.getMapper();
	}

	@Override
	public boolean insert(UserInfo userInfo) {
		this.passwordEncoder(userInfo);
		if (super.insert(userInfo)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserInfo loadUserInfo = findUserRelated(userInfo.getId());
				provisionService.send(ProvisionTopic.USERINFO_TOPIC, loadUserInfo, ProvisionAction.CREATE_ACTION);
			}

			return true;
		}

		return false;
	}

	public boolean insert(UserInfo userInfo, boolean passwordEncoder) {
		if (passwordEncoder) {
			this.passwordEncoder(userInfo);
		}
		if (super.insert(userInfo)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserInfo loadUserInfo = findUserRelated(userInfo.getId());
				provisionService.send(ProvisionTopic.USERINFO_TOPIC, loadUserInfo, ProvisionAction.CREATE_ACTION);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean update(UserInfo userInfo) {
		ChangePassword changePassword = this.passwordEncoder(userInfo);
		if (super.update(userInfo)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserInfo loadUserInfo = findUserRelated(userInfo.getId());
				accountUpdate(loadUserInfo);
				provisionService.send(ProvisionTopic.USERINFO_TOPIC, loadUserInfo, ProvisionAction.UPDATE_ACTION);
			}
			if (userInfo.getPassword() != null) {
				changePasswordProvisioning(changePassword);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(UserInfo userInfo) {
		UserInfo loadUserInfo = null;
		if (provisionService.getDreamServerProperties().isProvision()) {
			loadUserInfo = findUserRelated(userInfo.getId());
		}

		if (super.delete(userInfo)) {
			provisionService.send(ProvisionTopic.USERINFO_TOPIC, loadUserInfo, ProvisionAction.DELETE_ACTION);
			accountUpdate(loadUserInfo);
			return true;
		}
		return false;
	}

	// 更新账号状态
	public void accountUpdate(UserInfo userInfo) {
		if (userInfo.getStatus() != ConstsStatus.ACTIVE) {
			if (accountsService == null) {
				accountsService = WebContext.getBean("accountsService", AccountsService.class);
			}
			Accounts queryAcount = new Accounts();
			queryAcount.setUserId(userInfo.getId());
			for (Accounts acount : accountsService.query(queryAcount)) {
				acount.setStatus(ConstsStatus.INACTIVE);
				accountsService.update(acount);
			}
		}
	}

	public UserInfo findUserRelated(String userId) {
		UserInfo loadUserInfo = this.get(userId);
		loadUserInfo.setDepts(getMapper().findDeptsByUserId(userId));
		loadUserInfo.setAdjoints(getMapper().findAdjointsByUserId(userId));
		return loadUserInfo;
	}

	public boolean updateGridList(String gridList, UserInfo userInfo) {
		try {
			if (gridList != null && !gridList.equals("")) {
				userInfo.setGridList(Integer.parseInt(gridList));
				getMapper().updateGridList(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void saveOrUpdate(UserInfo userInfo) {
		UserInfo loadUserInfo =
				findOne(" username = ? and instid = ?", new Object[] { userInfo.getUsername(), userInfo.getInstId() },
						new int[] { Types.VARCHAR, Types.VARCHAR });
		if (loadUserInfo == null) {
			insert(userInfo);
		} else {
			userInfo.setId(loadUserInfo.getId());
			userInfo.setPassword(null);
			update(userInfo);
		}
	}

	public boolean updateProtectedApps(UserInfo userinfo) {
		try {
			userinfo.setModifiedDate(DateTimeHelper.formatDateTime());
			return getMapper().updateProtectedApps(userinfo) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public UserInfo findByUsername(String username) {
		return getMapper().findByUsername(username);
	}

	public UserInfo findByEmailMobile(String emailMobile) {
		return getMapper().findByEmailMobile(emailMobile);
	}

	public UserInfo findByAppIdAndUsername(String appId, String username) {
		try {
			UserInfo userinfo = new UserInfo();
			userinfo.setUsername(username);
			return getMapper().findByAppIdAndUsername(userinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ChangePassword passwordEncoder(UserInfo userInfo) {
		ChangePassword changePassword = null;
		if (StrHelper.isNotBlank(userInfo.getPassword())) {
			changePassword = new ChangePassword(userInfo);
			passwordEncoder(changePassword);
			userInfo.setPassword(changePassword.getPassword());
			userInfo.setDecipherable(changePassword.getDecipherable());
			userInfo.setPasswordLastSetTime(changePassword.getPasswordLastSetTime());
		} else {
			userInfo.setPassword(null);
			userInfo.setDecipherable(null);
		}
		return changePassword;
	}

	public ChangePassword passwordEncoder(ChangePassword changePassword) {
		// 密码不为空，则需要进行加密处理
		if (StrHelper.isNotBlank(changePassword.getPassword())) {
			String password = passwordEncoder.encode(changePassword.getPassword());
			changePassword.setDecipherable(PasswordReciprocal.getInstance().encode(changePassword.getPassword()));
			_logger.debug("decipherable : " + changePassword.getDecipherable());
			changePassword.setPassword(password);
			changePassword.setPasswordLastSetTime(DateTimeHelper.formatDateTime());

		} else {
			changePassword.setPassword(null);
			changePassword.setDecipherable(null);
		}
		return changePassword;
	}

	/**
	 * 认证密码修改
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmPassword
	 * @param passwordSetType
	 * @return
	 */
	public boolean changePassword(ChangePassword changePassword) {
		try {
			WebContext.setAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT, "");
			UserInfo userInfo = this.findByUsername(changePassword.getUsername());
			if (changePassword.getPassword().equals(changePassword.getConfirmPassword())) {
				if (StrHelper.isNotBlank(changePassword.getOldPassword())
						&& passwordEncoder.matches(changePassword.getOldPassword(), userInfo.getPassword())) {
					if (changePassword(changePassword, true)) {
						return true;
					}
					return false;
				} else {
					if (StrHelper.isNotBlank(changePassword.getOldPassword())
							&& passwordEncoder.matches(changePassword.getPassword(), userInfo.getPassword())) {
						WebContext.setAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT,
								WebContext.getI18nValue("PasswordPolicy.OLD_PASSWORD_MATCH"));
					} else {
						WebContext.setAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT,
								WebContext.getI18nValue("PasswordPolicy.OLD_PASSWORD_NOT_MATCH"));
					}
				}
			} else {
				WebContext.setAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT,
						WebContext.getI18nValue("PasswordPolicy.CONFIRMPASSWORD_NOT_MATCH"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 后台密码修改
	 * 
	 * @param changeUserInfo
	 * @param passwordPolicy
	 * @return
	 */
	public boolean changePassword(ChangePassword changePassword, boolean passwordPolicy) {
		try {
			_logger.debug("decipherable old : " + changePassword.getDecipherable());
			_logger.debug(
					"decipherable new : " + PasswordReciprocal.getInstance().encode(changePassword.getDecipherable()));

			if (passwordPolicy && passwordPolicyValidator.validator(changePassword) == false) {
				return false;
			}

			changePassword = passwordEncoder(changePassword);

			if (getMapper().changePassword(changePassword) > 0) {
				changePasswordProvisioning(changePassword);
				return true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public String randomPassword() {
		return passwordPolicyValidator.generateRandomPassword();
	}

	public void changePasswordProvisioning(ChangePassword changePassworded) {
		if (changePassworded != null && StrHelper.isNotBlank(changePassworded.getPassword())) {
			UserInfo loadUserInfo = findByUsername(changePassworded.getUsername());
			ChangePassword changePassword = new ChangePassword(loadUserInfo);
			provisionService.send(ProvisionTopic.PASSWORD_TOPIC, changePassword, ProvisionAction.PASSWORD_ACTION);
		}
	}

	public boolean updateAppLoginPassword(UserInfo userinfo) {
		try {
			userinfo.setModifiedDate(DateTimeHelper.formatDateTime());
			return getMapper().updateAppLoginPassword(userinfo) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 锁定用户：islock：1 用户解锁 2 用户锁定
	 * 
	 * @param userInfo
	 */
	public void updateLocked(UserInfo userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				userInfo.setIsLocked(ConstsStatus.LOCK);
				getMapper().updateLocked(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户登录成功后，重置错误密码次数和解锁用户
	 * 
	 * @param userInfo
	 */
	public void updateLockout(UserInfo userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				userInfo.setIsLocked(ConstsStatus.START);
				userInfo.setBadPasswordCount(0);
				getMapper().updateLockout(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新错误密码次数
	 * 
	 * @param userInfo
	 */
	public void updateBadPasswordCount(UserInfo userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				int updateBadPWDCount = userInfo.getBadPasswordCount() + 1;
				userInfo.setBadPasswordCount(updateBadPWDCount);
				getMapper().updateBadPWDCount(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean updateSharedSecret(UserInfo userInfo) {
		return getMapper().updateSharedSecret(userInfo) > 0;
	}

	public boolean updatePasswordQuestion(UserInfo userInfo) {
		return getMapper().updatePasswordQuestion(userInfo) > 0;
	}

	public boolean updateAuthnType(UserInfo userInfo) {
		return getMapper().updateAuthnType(userInfo) > 0;
	}

	public boolean updateEmail(UserInfo userInfo) {
		return getMapper().updateEmail(userInfo) > 0;
	}

	public boolean updateMobile(UserInfo userInfo) {
		return getMapper().updateMobile(userInfo) > 0;
	}

	public int updateProfile(UserInfo userInfo) {
		return getMapper().updateProfile(userInfo);
	}

	public boolean updateStatus(UserInfo userInfo) {
		return getMapper().updateStatus(userInfo) > 0;
	}

	public void setPasswordPolicyValidator(PasswordPolicyValidator passwordPolicyValidator) {
		this.passwordPolicyValidator = passwordPolicyValidator;
	}

}
