package com.wy.test.persistence.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.persistence.repository.PasswordPolicyValidator;
import com.wy.test.core.query.UserQuery;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.mapper.UserMapper;
import com.wy.test.persistence.provision.ProvisionAction;
import com.wy.test.persistence.provision.ProvisionService;
import com.wy.test.persistence.provision.ProvisionTopic;
import com.wy.test.persistence.service.AccountService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class UserServiceImpl extends AbstractServiceImpl<UserEntity, UserVO, UserQuery, UserConvert, UserMapper>
		implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	PasswordPolicyValidator passwordPolicyValidator;

	@Autowired
	ProvisionService provisionService;

	AccountService accountService;

	@Override
	public boolean insert(UserEntity userInfo) {
		this.passwordEncoder(baseConvert.convertt(userInfo));
		if (super.save(userInfo)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserVO loadUserInfo = findUserRelated(userInfo.getId());
				provisionService.send(ProvisionTopic.USERINFO_TOPIC, loadUserInfo, ProvisionAction.CREATE_ACTION);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean insert(UserEntity userInfo, boolean passwordEncoder) {
		if (passwordEncoder) {
			this.passwordEncoder(baseConvert.convertt(userInfo));
		}
		if (super.save(userInfo)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserVO loadUserInfo = findUserRelated(userInfo.getId());
				provisionService.send(ProvisionTopic.USERINFO_TOPIC, loadUserInfo, ProvisionAction.CREATE_ACTION);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean update(UserEntity userInfo) {
		ChangePassword changePassword = this.passwordEncoder(baseConvert.convertt(userInfo));
		if (super.updateById(userInfo)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserVO loadUserInfo = findUserRelated(userInfo.getId());
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
	public boolean delete(UserEntity userInfo) {
		UserVO loadUserInfo = null;
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
	@Override
	public void accountUpdate(UserVO userInfo) {
		if (userInfo.getStatus() != ConstStatus.ACTIVE) {
			List<AccountEntity> accountEntities = accountService
					.list(new LambdaQueryWrapper<AccountEntity>().eq(AccountEntity::getUserId, userInfo.getId()));
			for (AccountEntity acount : accountEntities) {
				acount.setStatus(ConstStatus.INACTIVE);
				accountService.update(acount);
			}
		}
	}

	@Override
	public UserVO findUserRelated(String userId) {
		UserEntity loadUserInfo = this.getById(userId);
		UserVO userVO = baseConvert.convertt(loadUserInfo);
		userVO.setDepts(baseMapper.findDeptsByUserId(userId));
		userVO.setAdjoints(baseMapper.findAdjointsByUserId(userId));
		return userVO;
	}

	@Override
	public boolean updateGridList(String gridList, UserVO userInfo) {
		try {
			if (gridList != null && !gridList.equals("")) {
				userInfo.setGridList(Integer.parseInt(gridList));
				baseMapper.updateGridList(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean saveOrUpdate(UserEntity userInfo) {
		UserEntity loadUserInfo = getOne(lambdaQuery().eq(UserEntity::getUsername, userInfo.getUsername())
				.eq(UserEntity::getInstId, userInfo.getInstId()));
		if (loadUserInfo == null) {
			return insert(userInfo);
		} else {
			userInfo.setId(loadUserInfo.getId());
			userInfo.setPassword(null);
			return update(userInfo);
		}
	}

	@Override
	public boolean updateProtectedApps(UserEntity userinfo) {
		try {
			userinfo.setUpdateTime(new Date());
			return baseMapper.updateProtectedApps(userinfo) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public UserEntity findByUsername(String username) {
		return baseMapper.findByUsername(username);
	}

	@Override
	public UserEntity findByEmailMobile(String emailMobile) {
		return baseMapper.findByEmailMobile(emailMobile);
	}

	@Override
	public UserEntity findByAppIdAndUsername(String appId, String username) {
		try {
			UserEntity userinfo = new UserEntity();
			userinfo.setUsername(username);
			return baseMapper.findByAppIdAndUsername(userinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ChangePassword passwordEncoder(UserVO userInfo) {
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

	@Override
	public ChangePassword passwordEncoder(ChangePassword changePassword) {
		// 密码不为空，则需要进行加密处理
		if (StrHelper.isNotBlank(changePassword.getPassword())) {
			String password = passwordEncoder.encode(changePassword.getPassword());
			changePassword.setDecipherable(PasswordReciprocal.getInstance().encode(changePassword.getPassword()));
			log.debug("decipherable : " + changePassword.getDecipherable());
			changePassword.setPassword(password);
			changePassword.setPasswordLastSetTime(new Date());
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
	@Override
	public boolean changePassword(ChangePassword changePassword) {
		try {
			WebContext.setAttribute(PasswordPolicyValidator.PASSWORD_POLICY_VALIDATE_RESULT, "");
			UserEntity userInfo = this.findByUsername(changePassword.getUsername());
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
	@Override
	public boolean changePassword(ChangePassword changePassword, boolean passwordPolicy) {
		try {
			log.debug("decipherable old : " + changePassword.getDecipherable());
			log.debug(
					"decipherable new : " + PasswordReciprocal.getInstance().encode(changePassword.getDecipherable()));

			if (passwordPolicy && passwordPolicyValidator.validator(changePassword) == false) {
				return false;
			}

			changePassword = passwordEncoder(changePassword);

			if (baseMapper.changePassword(changePassword) > 0) {
				changePasswordProvisioning(changePassword);
				return true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public String randomPassword() {
		return passwordPolicyValidator.generateRandomPassword();
	}

	@Override
	public void changePasswordProvisioning(ChangePassword changePassworded) {
		if (changePassworded != null && StrHelper.isNotBlank(changePassworded.getPassword())) {
			UserEntity loadUserInfo = findByUsername(changePassworded.getUsername());
			ChangePassword changePassword = new ChangePassword(baseConvert.convertt(loadUserInfo));
			provisionService.send(ProvisionTopic.PASSWORD_TOPIC, changePassword, ProvisionAction.PASSWORD_ACTION);
		}
	}

	@Override
	public boolean updateAppLoginPassword(UserEntity userinfo) {
		try {
			userinfo.setUpdateTime(new Date());
			return baseMapper.updateAppLoginPassword(userinfo) > 0;
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
	@Override
	public void updateLocked(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				userInfo.setIsLocked(ConstStatus.LOCK);
				baseMapper.updateLocked(userInfo);
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
	@Override
	public void updateLockout(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				userInfo.setIsLocked(ConstStatus.START);
				userInfo.setBadPasswordCount(0);
				baseMapper.updateLockout(userInfo);
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
	@Override
	public void updateBadPasswordCount(UserEntity userInfo) {
		try {
			if (userInfo != null && StrHelper.isNotEmpty(userInfo.getId())) {
				int updateBadPWDCount = userInfo.getBadPasswordCount() + 1;
				userInfo.setBadPasswordCount(updateBadPWDCount);
				baseMapper.updateBadPWDCount(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean updateSharedSecret(UserEntity userInfo) {
		return baseMapper.updateSharedSecret(userInfo) > 0;
	}

	@Override
	public boolean updatePasswordQuestion(UserEntity userInfo) {
		return baseMapper.updatePasswordQuestion(userInfo) > 0;
	}

	@Override
	public boolean updateAuthnType(UserEntity userInfo) {
		return baseMapper.updateAuthnType(userInfo) > 0;
	}

	@Override
	public boolean updateEmail(UserEntity userInfo) {
		return baseMapper.updateEmail(userInfo) > 0;
	}

	@Override
	public boolean updateMobile(UserEntity userInfo) {
		return baseMapper.updateMobile(userInfo) > 0;
	}

	@Override
	public int updateProfile(UserVO userInfo) {
		return baseMapper.updateProfile(userInfo);
	}

	@Override
	public boolean updateStatus(UserEntity userInfo) {
		return baseMapper.updateStatus(userInfo) > 0;
	}
}