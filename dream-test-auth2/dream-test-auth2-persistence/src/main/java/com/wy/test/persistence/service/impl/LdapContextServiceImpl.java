package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.LdapContextConvert;
import com.wy.test.core.entity.LdapContextEntity;
import com.wy.test.core.query.LdapContextQuery;
import com.wy.test.core.vo.LdapContextVO;
import com.wy.test.persistence.mapper.LdapContextMapper;
import com.wy.test.persistence.service.LdapContextService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * LDAP上下文
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class LdapContextServiceImpl extends
		AbstractServiceImpl<LdapContextEntity, LdapContextVO, LdapContextQuery, LdapContextConvert, LdapContextMapper>
		implements LdapContextService {

}