package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.RememberMeConvert;
import com.wy.test.core.entity.RememberMeEntity;
import com.wy.test.core.query.RememberMeQuery;
import com.wy.test.core.vo.RememberMeVO;
import com.wy.test.persistence.mapper.RememberMeMapper;
import com.wy.test.persistence.service.RememberMeService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 记住我
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class RememberMeServiceImpl extends
		AbstractServiceImpl<RememberMeEntity, RememberMeVO, RememberMeQuery, RememberMeConvert, RememberMeMapper>
		implements RememberMeService {

}