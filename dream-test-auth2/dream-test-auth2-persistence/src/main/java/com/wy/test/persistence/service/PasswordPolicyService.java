package com.wy.test.persistence.service;

import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.query.PasswordPolicyQuery;
import com.wy.test.core.vo.PasswordPolicyVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;


/**
 * 密码策略
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface PasswordPolicyService extends BaseServices<PasswordPolicyEntity, PasswordPolicyVO, PasswordPolicyQuery> {

}