package com.wy.test.persistence.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wy.test.core.convert.AppTokenDetailConvert;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.AppTokenDetailEntity;
import com.wy.test.core.query.AppTokenDetailQuery;
import com.wy.test.core.vo.AppTokenDetailVO;
import com.wy.test.persistence.mapper.AppTokenDetailMapper;
import com.wy.test.persistence.service.AppTokenDetailService;

import dream.flying.flower.collection.CollectionHelper;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * token详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppTokenDetailServiceImpl extends AbstractServiceImpl<AppTokenDetailEntity, AppTokenDetailVO,
		AppTokenDetailQuery, AppTokenDetailConvert, AppTokenDetailMapper> implements AppTokenDetailService {

	protected final static Cache<String, AppTokenDetailVO> DETAILS_CACHE =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppTokenDetailVO getAppDetails(String id, boolean cached) {
		AppTokenDetailVO details = null;
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

	private AppTokenDetailVO getAppDetails(String id) {
		MPJLambdaWrapper<AppTokenDetailEntity> mpjLambdaWrapper = new MPJLambdaWrapper<>();
		mpjLambdaWrapper.selectAll(AppTokenDetailEntity.class)
				.innerJoin(AppEntity.class,
						cnd -> cnd.eq(AppEntity::getId, AppTokenDetailEntity::getId).eq(AppEntity::getId,
								AppTokenDetailEntity::getInstId))
				.selectAll(AppEntity.class).eq(AppEntity::getId, id).eq(AppTokenDetailEntity::getId, id);
		List<AppTokenDetailVO> appTokenDetailVOs = selectJoinList(AppTokenDetailVO.class, mpjLambdaWrapper);
		return CollectionHelper.isEmpty(appTokenDetailVOs) ? null : appTokenDetailVOs.get(0);
	}
}