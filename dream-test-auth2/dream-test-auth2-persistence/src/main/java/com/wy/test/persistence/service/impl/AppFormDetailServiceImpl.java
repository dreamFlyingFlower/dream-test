package com.wy.test.persistence.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wy.test.core.convert.AppFormDetailConvert;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.AppFormDetailEntity;
import com.wy.test.core.query.AppFormDetailQuery;
import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.persistence.mapper.AppFormDetailMapper;
import com.wy.test.persistence.service.AppFormDetailService;

import dream.flying.flower.collection.CollectionHelper;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 表单信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppFormDetailServiceImpl extends AbstractServiceImpl<AppFormDetailEntity, AppFormDetailVO,
		AppFormDetailQuery, AppFormDetailConvert, AppFormDetailMapper> implements AppFormDetailService {

	protected final static Cache<String, AppFormDetailVO> DETAILS_CACHE =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppFormDetailVO getAppDetails(String id, boolean cached) {
		AppFormDetailVO details = null;
		if (cached) {
			details = DETAILS_CACHE.getIfPresent(id);
			if (details == null) {
				details = getAppDetails(id);
				DETAILS_CACHE.put(id, details);
			}
		} else {
			details = getAppDetails(id);
		}
		return details;
	}

	private AppFormDetailVO getAppDetails(String id) {
		MPJLambdaWrapper<AppFormDetailEntity> mpjLambdaWrapper = new MPJLambdaWrapper<>();
		mpjLambdaWrapper.selectAll(AppFormDetailEntity.class)
				.innerJoin(AppEntity.class,
						cnd -> cnd.eq(AppEntity::getId, AppFormDetailEntity::getId).eq(AppEntity::getId,
								AppFormDetailEntity::getInstId))
				.selectAll(AppEntity.class).eq(AppEntity::getId, id).eq(AppFormDetailEntity::getId, id);
		List<AppFormDetailVO> appFormDetailVOs = selectJoinList(AppFormDetailVO.class, mpjLambdaWrapper);
		return CollectionHelper.isEmpty(appFormDetailVOs) ? null : appFormDetailVOs.get(0);
	}
}