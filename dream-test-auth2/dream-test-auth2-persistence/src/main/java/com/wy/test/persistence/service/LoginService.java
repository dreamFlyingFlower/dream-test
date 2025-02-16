package com.wy.test.persistence.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.UserVO;

/**
 * 用户账号表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface LoginService {

	UserVO find(String username, String password);

	List<UserEntity> findByUsername(String username, String password);

	List<UserEntity> findByUsernameOrMobile(String username, String password);

	List<UserEntity> findByUsernameOrMobileOrEmail(String username, String password);

	List<GrantedAuthority> queryAuthorizedApps(List<GrantedAuthority> grantedAuthoritys);

	/**
	 * 根据用户ID查询用户角色列表
	 * 
	 * @param userVo 用户信息
	 * @return 用户角色列表
	 */
	List<RoleEntity> queryRoles(UserVO userVo);

	/**
	 * 按用户信息授予权限
	 * 
	 * @param userVo 用户信息
	 * @return ArrayList<GrantedAuthority> 用户权限列表
	 */
	List<GrantedAuthority> grantAuthority(UserVO userVo);

	/**
	 * 更新用户最近一次登录时间
	 * 
	 * @param userVo 用户信息
	 * @return true->成功;false->失败
	 */
	boolean updateLastLogin(UserVO userVo);
}