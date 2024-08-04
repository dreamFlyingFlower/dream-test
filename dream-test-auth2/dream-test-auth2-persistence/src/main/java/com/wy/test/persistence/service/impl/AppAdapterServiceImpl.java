package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.AppAdapterConvert;
import com.wy.test.core.entity.AppAdapterEntity;
import com.wy.test.core.query.AppAdapterQuery;
import com.wy.test.core.vo.AppAdapterVO;
import com.wy.test.persistence.mapper.AppAdapterMapper;
import com.wy.test.persistence.service.AppAdapterService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import lombok.AllArgsConstructor;

/**
 * 应用适配
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class AppAdapterServiceImpl extends
		AbstractServiceImpl<AppAdapterEntity, AppAdapterVO, AppAdapterQuery, AppAdapterConvert, AppAdapterMapper>
		implements AppAdapterService {
}