package com.wy.test.persistence.service;

import com.wy.test.core.entity.LdapContextEntity;
import com.wy.test.core.query.LdapContextQuery;
import com.wy.test.core.vo.LdapContextVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * LDAP上下文
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface LdapContextService extends BaseServices<LdapContextEntity, LdapContextVO, LdapContextQuery> {

}