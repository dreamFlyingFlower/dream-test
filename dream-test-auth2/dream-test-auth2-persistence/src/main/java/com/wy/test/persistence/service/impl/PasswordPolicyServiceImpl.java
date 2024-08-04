package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.PasswordPolicyConvert;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.query.PasswordPolicyQuery;
import com.wy.test.core.vo.PasswordPolicyVO;
import com.wy.test.persistence.mapper.PasswordPolicyMapper;
import com.wy.test.persistence.service.PasswordPolicyService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 密码策略
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class PasswordPolicyServiceImpl extends
		AbstractServiceImpl<PasswordPolicyEntity, PasswordPolicyVO, PasswordPolicyQuery, PasswordPolicyConvert, PasswordPolicyMapper >
		implements PasswordPolicyService {

}