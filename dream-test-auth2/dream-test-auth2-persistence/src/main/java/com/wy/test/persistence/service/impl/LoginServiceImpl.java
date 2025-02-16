package com.wy.test.persistence.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wy.test.core.constant.ConstRole;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.RoleMemberEntity;
import com.wy.test.core.entity.RolePermissionEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.LoginType;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.LoginService;
import com.wy.test.persistence.service.RoleService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.collection.CollectionHelper;
import dream.flying.flower.enums.YesNoEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户登录Service
 *
 * @author 飞花梦影
 * @date 2024-09-08 21:08:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@EnableConfigurationProperties(DreamAuthLoginProperties.class)
@RequiredArgsConstructor
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	final DreamAuthLoginProperties dreamAuthLoginProperties;

	final UserService userService;

	final RoleService roleService;

	final AppService appService;

	@Override
	public UserVO find(String username, String password) {
		List<UserEntity> userEntities = null;
		if (LoginType.NORMAL == dreamAuthLoginProperties.getLoginType()) {
			userEntities = findByUsername(username, password);
		} else if (LoginType.NORMAL_MOBILE == dreamAuthLoginProperties.getLoginType()) {
			userEntities = findByUsernameOrMobile(username, password);
		} else if (LoginType.NORMAL_MOBILE_EMAIL == dreamAuthLoginProperties.getLoginType()) {
			userEntities = findByUsernameOrMobileOrEmail(username, password);
		}

		UserVO userVo = null;
		if (CollectionHelper.isNotEmpty(userEntities)) {
			userVo = UserConvert.INSTANCE.convertt(userEntities.get(0));
		}
		log.debug("load UserInfo : " + userVo);
		return userVo;
	}

	@Override
	public List<UserEntity> findByUsername(String username, String password) {
		return userService.list(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username));
	}

	@Override
	public List<UserEntity> findByUsernameOrMobile(String username, String password) {
		return userService.list(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username)
				.or()
				.eq(UserEntity::getMobile, username));
	}

	@Override
	public List<UserEntity> findByUsernameOrMobileOrEmail(String username, String password) {
		return userService.list(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username)
				.or()
				.eq(UserEntity::getMobile, username)
				.or()
				.eq(UserEntity::getEmail, username));
	}

	@Override
	public List<GrantedAuthority> queryAuthorizedApps(List<GrantedAuthority> grantedAuthoritys) {
		List<String> grantedAuthorities = new ArrayList<>(Arrays.asList("ROLE_ALL_USER"));
		if (CollectionHelper.isNotEmpty(grantedAuthoritys)) {
			grantedAuthoritys.forEach(t -> grantedAuthorities.add(t.getAuthority()));
		}

		List<AppEntity> appEntities = appService.selectJoinList(AppEntity.class,
				new MPJLambdaWrapper<AppEntity>().select(AppEntity::getId)
						.innerJoin(RolePermissionEntity.class, RolePermissionEntity::getAppId, AppEntity::getId)
						.innerJoin(RoleEntity.class, RoleEntity::getId, RolePermissionEntity::getRoleId)
						.eq(AppEntity::getStatus, 1)
						.in(RoleEntity::getId, grantedAuthorities)
						.distinct());
		if (CollectionHelper.isNotEmpty(appEntities)) {
			return appEntities.stream().map(t -> new SimpleGrantedAuthority(t.getId())).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	@Override
	public List<RoleEntity> queryRoles(UserVO userVo) {
		List<RoleEntity> roleEntities = roleService.selectJoinList(RoleEntity.class,
				new MPJLambdaWrapper<RoleEntity>()
						.select(RoleEntity::getId, RoleEntity::getRoleCode, RoleEntity::getRoleName)
						.innerJoin(RoleMemberEntity.class, RoleMemberEntity::getRoleId, RoleEntity::getId)
						.innerJoin(UserEntity.class, UserEntity::getId, RoleMemberEntity::getMemberId)
						.eq(UserEntity::getId, userVo.getId())
						.distinct());
		if (CollectionHelper.isNotEmpty(roleEntities)) {
			roleEntities.forEach(t -> t.setIsDefault(0));
		}

		log.debug("list Roles  " + roleEntities);
		return roleEntities;
	}

	@Override
	public List<GrantedAuthority> grantAuthority(UserVO userVo) {
		List<RoleEntity> roleEntities = queryRoles(userVo);

		List<GrantedAuthority> grantedAuthority = new ArrayList<>();
		grantedAuthority.add(ConstRole.ROLE_USER);
		grantedAuthority.add(ConstRole.ROLE_ALL_USER);
		grantedAuthority.add(ConstRole.ROLE_ORDINARY_USER);
		for (RoleEntity roleEntity : roleEntities) {
			grantedAuthority.add(new SimpleGrantedAuthority(roleEntity.getId()));
			if (roleEntity.getRoleCode().startsWith(ConstRole.ROLE_PREFIX)
					&& !grantedAuthority.contains(new SimpleGrantedAuthority(roleEntity.getRoleCode()))) {
				grantedAuthority.add(new SimpleGrantedAuthority(roleEntity.getRoleCode()));
			}
		}
		log.debug("Authority : " + grantedAuthority);

		return grantedAuthority;
	}

	@Override
	public boolean updateLastLogin(UserVO userVo) {
		return userService.lambdaUpdate()
				.set(UserEntity::getLastLoginTime, userVo.getLastLoginTime())
				.set(UserEntity::getLastLoginIp, userVo.getLastLoginIp())
				.set(UserEntity::getLoginCount, userVo.getLoginCount() + 1)
				.set(UserEntity::getOnline, YesNoEnum.YES.getValue())
				.eq(UserEntity::getId, userVo.getId())
				.update();
	}

	// public class UserInfoRowMapper implements RowMapper<UserEntity> {
	//
	// @Override
	// public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
	//
	// UserEntity userInfo = new UserEntity();
	// userInfo.setId(rs.getString("id"));
	// userInfo.setUsername(rs.getString("username"));
	// userInfo.setPassword(rs.getString("password"));
	// userInfo.setSharedSecret(rs.getString("shared_secret"));
	// userInfo.setSharedCounter(rs.getString("shared_counter"));
	// userInfo.setDecipherable(rs.getString("decipherable"));
	// userInfo.setWindowsAccount(rs.getString("windows_account"));
	// userInfo.setUserType(rs.getString("user_type"));
	//
	// userInfo.setDisplayName(rs.getString("display_name"));
	// userInfo.setNickName(rs.getString("nick_name"));
	// userInfo.setNameZhSpell(rs.getString("name_zh_spell"));// nameZHSpell
	// userInfo.setNameZhShortSpell(rs.getString("name_zh_short_spell"));//
	// nameZHSpell
	// userInfo.setGivenName(rs.getString("given_name"));
	// userInfo.setMiddleName(rs.getString("middle_name"));
	// userInfo.setFamilyName(rs.getString("family_name"));
	// userInfo.setHonorificPrefix(rs.getString("honorific_prefix"));
	// userInfo.setHonorificSuffix(rs.getString("honorific_suffix"));
	// userInfo.setFormattedName(rs.getString("formatted_name"));
	//
	// userInfo.setGender(rs.getInt("gender"));
	// userInfo.setBirthDate(rs.getDate("birth_date"));
	// userInfo.setPicture(rs.getBytes("picture"));
	// userInfo.setMarried(rs.getInt("married"));
	// userInfo.setIdType(rs.getInt("id_type"));
	// userInfo.setIdCardNo(rs.getString("id_card_no"));
	// userInfo.setWebSite(rs.getString("web_site"));
	//
	// userInfo.setAuthnType(rs.getInt("authn_type"));
	// userInfo.setMobile(rs.getString("mobile"));
	// userInfo.setMobileVerified(rs.getInt("mobile_verified"));
	// userInfo.setEmail(rs.getString("email"));
	// userInfo.setEmailVerified(rs.getInt("email_verified"));
	// userInfo.setPasswordQuestion(rs.getString("password_question"));
	// userInfo.setPasswordAnswer(rs.getString("password_answer"));
	//
	// userInfo.setAppLoginAuthnType(rs.getInt("app_login_authn_type"));
	// userInfo.setAppLoginPassword(rs.getString("app_login_password"));
	// userInfo.setProtectedApps(rs.getString("protected_apps"));
	//
	// userInfo.setPasswordLastSetTime(rs.getDate("password_last_set_time"));
	// userInfo.setPasswordSetType(rs.getInt("password_set_type"));
	// userInfo.setBadPasswordCount(rs.getInt("bad_password_count"));
	// userInfo.setBadPasswordTime(rs.getDate("bad_password_time"));
	// userInfo.setUnLockTime(rs.getString("unlock_time"));
	// userInfo.setIsLocked(rs.getInt("is_locked"));
	// userInfo.setLastLoginTime(rs.getDate("last_login_time"));
	// userInfo.setLastLoginIp(rs.getString("last_login_ip"));
	// userInfo.setLastLogoffTime(rs.getDate("last_logoff_time"));
	// userInfo.setLoginCount(rs.getInt("login_count"));
	// userInfo.setRegionHistory(rs.getString("region_history"));
	// userInfo.setPasswordHistory(rs.getString("password_history"));
	//
	// userInfo.setTimeZone(rs.getString("time_zone"));
	// userInfo.setLocale(rs.getString("locale"));
	// userInfo.setPreferredLanguage(rs.getString("preferred_language"));
	//
	// userInfo.setWorkEmail(rs.getString("work_email"));
	// userInfo.setWorkPhoneNumber(rs.getString("work_phone_number"));
	// userInfo.setWorkCountry(rs.getString("work_country"));
	// userInfo.setWorkRegion(rs.getString("work_region"));
	// userInfo.setWorkLocality(rs.getString("work_locality"));
	// userInfo.setWorkStreetAddress(rs.getString("work_street_address"));
	// userInfo.setWorkAddressFormatted(rs.getString("work_address_formatted"));
	// userInfo.setWorkPostalCode(rs.getString("work_postal_code"));
	// userInfo.setWorkFax(rs.getString("work_fax"));
	//
	// userInfo.setHomeEmail(rs.getString("home_email"));
	// userInfo.setHomePhoneNumber(rs.getString("home_phone_number"));
	// userInfo.setHomeCountry(rs.getString("home_country"));
	// userInfo.setHomeRegion(rs.getString("home_region"));
	// userInfo.setHomeLocality(rs.getString("home_locality"));
	// userInfo.setHomeStreetAddress(rs.getString("home_street_address"));
	// userInfo.setHomeAddressFormatted(rs.getString("home_address_formatted"));
	// userInfo.setHomePostalCode(rs.getString("home_postal_code"));
	// userInfo.setHomeFax(rs.getString("home_fax"));
	//
	// userInfo.setEmployeeNumber(rs.getString("employee_number"));
	// userInfo.setDivision(rs.getString("division"));
	// userInfo.setCostCenter(rs.getString("cost_center"));
	// userInfo.setOrganization(rs.getString("organization"));
	// userInfo.setDepartmentId(rs.getString("department_id"));
	// userInfo.setDepartment(rs.getString("department"));
	// userInfo.setJobTitle(rs.getString("job_title"));
	// userInfo.setJobLevel(rs.getString("job_level"));
	// userInfo.setManagerId(rs.getString("manager_id"));
	// userInfo.setManager(rs.getString("manager"));
	// userInfo.setAssistantId(rs.getString("assistant_id"));
	// userInfo.setAssistant(rs.getString("assistant"));
	// userInfo.setEntryDate(rs.getDate("entry_date"));//
	// userInfo.setQuitDate(rs.getDate("quit_date"));
	// userInfo.setStartWorkDate(rs.getDate("start_work_date"));
	//
	// userInfo.setExtraAttribute(rs.getString("extra_attribute"));
	//
	// userInfo.setCreateUser(rs.getLong("create_user"));
	// userInfo.setCreateTime(rs.getDate("create_time"));
	// userInfo.setUpdateUser(rs.getLong("update_user"));
	// userInfo.setUpdateTime(rs.getDate("update_time"));
	//
	// userInfo.setStatus(rs.getInt("status"));
	// userInfo.setGridList(rs.getInt("grid_list"));
	// userInfo.setRemark(rs.getString("remark"));
	// userInfo.setTheme(rs.getString("theme"));
	// userInfo.setInstId(rs.getString("inst_id"));
	// if (userInfo.getTheme() == null || userInfo.getTheme().equalsIgnoreCase(""))
	// {
	// userInfo.setTheme("default");
	// }
	//
	// return userInfo;
	// }
	// }
}