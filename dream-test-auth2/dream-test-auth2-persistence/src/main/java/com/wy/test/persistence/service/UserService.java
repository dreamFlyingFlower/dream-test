package com.wy.test.persistence.service;

import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.UserQuery;
import com.wy.test.core.vo.UserVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 用户信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface UserService extends BaseServices<UserEntity, UserVO, UserQuery> {

	boolean insert(UserEntity userInfo);

	boolean insert(UserEntity userInfo, boolean passwordEncoder);

	boolean update(UserEntity userInfo);

	boolean delete(UserEntity userInfo);

	// 更新账号状态
	void accountUpdate(UserVO userInfo);

	UserVO findUserRelated(String userId);

	boolean updateGridList(String gridList, UserVO userInfo);

	boolean updateProtectedApps(UserEntity userinfo);

	UserEntity findByUsername(String username);

	UserEntity findByEmailMobile(String emailMobile);

	UserEntity findByAppIdAndUsername(String appId, String username);

	ChangePassword passwordEncoder(UserVO userInfo);

	ChangePassword passwordEncoder(ChangePassword changePassword);

	/**
	 * 认证密码修改
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmPassword
	 * @param passwordSetType
	 * @return
	 */
	boolean changePassword(ChangePassword changePassword);

	/**
	 * 后台密码修改
	 * 
	 * @param changeUserInfo
	 * @param passwordPolicy
	 * @return
	 */
	boolean changePassword(ChangePassword changePassword, boolean passwordPolicy);

	String randomPassword();

	void changePasswordProvisioning(ChangePassword changePassworded);

	boolean updateAppLoginPassword(UserEntity userinfo);

	/**
	 * 锁定用户:1-用户解锁;2-用户锁定
	 * 
	 * @param userInfo
	 */
	void updateLocked(UserEntity userInfo);

	/**
	 * 用户登录成功后,重置错误密码次数和解锁用户
	 * 
	 * @param userInfo
	 */
	void updateLockout(UserEntity userInfo);

	/**
	 * 更新错误密码次数
	 * 
	 * @param userInfo
	 */
	void updateBadPasswordCount(UserEntity userInfo);

	boolean updateSharedSecret(UserEntity userInfo);

	boolean updatePasswordQuestion(UserEntity userInfo);

	boolean updateAuthnType(UserEntity userInfo);

	boolean updateEmail(UserEntity userInfo);

	boolean updateMobile(UserEntity userInfo);

	int updateProfile(UserVO userInfo);

	boolean updateStatus(UserEntity userInfo);
}