package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.HistoryLoginAppConvert;
import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.query.HistoryLoginAppQuery;
import com.wy.test.core.vo.HistoryLoginAppVO;
import com.wy.test.persistence.mapper.HistoryLoginAppMapper;
import com.wy.test.persistence.service.HistoryLoginAppService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * APP登录历史记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class HistoryLoginAppServiceImpl extends AbstractServiceImpl<HistoryLoginAppEntity, HistoryLoginAppVO,
		HistoryLoginAppQuery, HistoryLoginAppConvert, HistoryLoginAppMapper> implements HistoryLoginAppService {

}