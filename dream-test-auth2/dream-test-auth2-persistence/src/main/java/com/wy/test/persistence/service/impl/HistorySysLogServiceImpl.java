package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.HistorySysLogConvert;
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
import com.wy.test.persistence.mapper.HistorySysLogMapper;
import com.wy.test.persistence.service.HistorySysLogService;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统操作日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class HistorySysLogServiceImpl extends AbstractServiceImpl<HistorySysLogEntity, HistorySysLogVO,
		HistorySysLogQuery, HistorySysLogConvert, HistorySysLogMapper> implements HistorySysLogService {

	@Override
	public void insert(String topic, Object entity, String action, String result, UserEntity operator) {
		String message = "";
		if (entity != null) {
			if (entity instanceof UserEntity) {
				message = buildMsg((UserEntity) entity);
			} else if (entity instanceof OrgEntity) {
				message = buildMsg((OrgEntity) entity);
			} else if (entity instanceof ChangePassword) {
				message = buildMsg((ChangePassword) entity);
			} else if (entity instanceof AccountEntity) {
				message = buildMsg((AccountEntity) entity);
			} else if (entity instanceof RoleEntity) {
				message = buildMsg((RoleEntity) entity);
			} else if (entity instanceof RoleMemberVO) {
				message = buildMsg((RoleMemberVO) entity);
			} else if (entity instanceof RolePermissionVO) {
				message = buildMsg((RolePermissionVO) entity);
			} else if (entity instanceof ResourceEntity) {
				message = buildMsg((ResourceEntity) entity);
			} else if (entity instanceof SyncEntity) {
				message = buildMsg((SyncEntity) entity);
			} else if (entity instanceof SocialProviderEntity) {
				message = buildMsg((SocialProviderEntity) entity);
			} else if (entity instanceof RolePrivilegeEntity) {
				message = buildMsg((RolePrivilegeEntity) entity);
			} else if (entity instanceof String) {
				message = entity.toString();
			}
		}
		insert(topic, message, action, result, operator, entity);
	}

	@Override
	public void insert(String topic, String message, String action, String result, UserEntity operator, Object entity) {
		HistorySysLogEntity systemLog = new HistorySysLogEntity();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		systemLog.setId(generatorStrategyContext.generate());
		systemLog.setTopic(topic);
		systemLog.setMessage(message);
		systemLog.setMessageAction(action);
		systemLog.setMessageResult(result);
		systemLog.setUserId(operator.getId());
		systemLog.setUsername(operator.getUsername());
		systemLog.setDisplayName(operator.getDisplayName());
		systemLog.setInstId(operator.getInstId());
		systemLog.setJsonCotent(JsonHelpers.toString(entity));
		log.trace("System Log {}", systemLog);
		baseMapper.insert(systemLog);
	}

	@Override
	public String buildMsg(UserEntity userInfo) {
		return new StringBuilder().append(userInfo.getDisplayName()).append("[").append(userInfo.getUsername())
				.append("]").toString();
	}

	@Override
	public String buildMsg(OrgEntity org) {
		return new StringBuilder().append(org.getOrgName()).append("[").append(org.getOrgCode()).append("]").toString();
	}

	@Override
	public String buildMsg(AccountEntity account) {
		return new StringBuilder().append(account.getRelatedUsername()).append("[").append(account.getDisplayName())
				.append(",").append(account.getUsername()).append(",").append(account.getAppName()).append("]")
				.toString();
	}

	@Override
	public String buildMsg(ChangePassword changePassword) {
		return new StringBuilder().append(changePassword.getDisplayName()).append("[")
				.append(changePassword.getUsername()).append("]").toString();
	}

	@Override
	public String buildMsg(RoleEntity g) {
		return new StringBuilder().append(g.getRoleName()).toString();
	}

	@Override
	public String buildMsg(RoleMemberVO rm) {
		return new StringBuilder().append(rm.getRoleName()).append("[").append(rm.getUsername()).append(",")
				.append(rm.getDisplayName()).append("]").toString();
	}

	@Override
	public String buildMsg(RolePermissionVO permission) {
		return new StringBuilder().append(permission.getRoleName()).append("[").append(permission.getAppName())
				.append("]").toString();
	}

	@Override
	public String buildMsg(RolePrivilegeEntity privilege) {
		return new StringBuilder().append(privilege.getRoleId()).append("[").append(privilege.getResourceId())
				.append("]").toString();
	}

	@Override
	public String buildMsg(ResourceEntity r) {
		return new StringBuilder().append(r.getResourceName()).append("[").append(r.getResourceType()).append("]")
				.toString();
	}

	@Override
	public String buildMsg(SyncEntity s) {
		return new StringBuilder().append(s.getName()).append("[").append(s.getSourceType()).append(",")
				.append(s.getScheduler()).append(",").append("]").toString();
	}

	@Override
	public String buildMsg(SocialProviderEntity s) {
		return new StringBuilder().append(s.getProviderName()).append("[").append(s.getProvider()).append("]")
				.toString();
	}
}