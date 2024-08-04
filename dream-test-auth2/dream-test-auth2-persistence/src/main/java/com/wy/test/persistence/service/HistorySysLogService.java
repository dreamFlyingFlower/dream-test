package com.wy.test.persistence.service;

import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.HistorySysLogEntity;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.ResourceEntity;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.RolePrivilegeEntity;
import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.HistorySysLogQuery;
import com.wy.test.core.vo.HistorySysLogVO;
import com.wy.test.core.vo.RoleMemberVO;
import com.wy.test.core.vo.RolePermissionVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 系统操作日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface HistorySysLogService extends BaseServices<HistorySysLogEntity, HistorySysLogVO, HistorySysLogQuery> {

	void insert(String topic, Object entity, String action, String result, UserEntity operator);

	void insert(String topic, String message, String action, String result, UserEntity operator, Object entity);

	String buildMsg(UserEntity userInfo);

	String buildMsg(OrgEntity org);

	String buildMsg(AccountEntity account);

	String buildMsg(ChangePassword changePassword);

	String buildMsg(RoleEntity g);

	String buildMsg(RoleMemberVO rm);

	String buildMsg(RolePermissionVO permission);

	String buildMsg(RolePrivilegeEntity privilege);

	String buildMsg(ResourceEntity r);

	String buildMsg(SyncEntity s);

	String buildMsg(SocialProviderEntity s);
}