package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.HistoryProvisionConvert;
import com.wy.test.core.entity.HistoryProvisionEntity;
import com.wy.test.core.query.HistoryProvisionQuery;
import com.wy.test.core.vo.HistoryProvisionVO;
import com.wy.test.persistence.mapper.HistoryProvisionMapper;
import com.wy.test.persistence.service.HistoryProvisionService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 第三方历史
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class HistoryProvisionServiceImpl extends AbstractServiceImpl<HistoryProvisionEntity, HistoryProvisionVO,
		HistoryProvisionQuery, HistoryProvisionConvert, HistoryProvisionMapper> implements HistoryProvisionService {

}