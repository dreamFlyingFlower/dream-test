package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.LdapContextEntity;
import com.wy.test.core.query.LdapContextQuery;
import com.wy.test.core.vo.LdapContextVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * LDAP上下文
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface LdapContextMapper extends BaseMappers<LdapContextEntity, LdapContextVO, LdapContextQuery> {

}