package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.SmsProviderConvert;
import com.wy.test.core.entity.SmsProviderEntity;
import com.wy.test.core.query.SmsProviderQuery;
import com.wy.test.core.vo.SmsProviderVO;
import com.wy.test.persistence.mapper.SmsProviderMapper;
import com.wy.test.persistence.service.SmsProviderService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 短信记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class SmsProviderServiceImpl extends
		AbstractServiceImpl<SmsProviderEntity, SmsProviderVO, SmsProviderQuery, SmsProviderConvert, SmsProviderMapper>
		implements SmsProviderService {

}