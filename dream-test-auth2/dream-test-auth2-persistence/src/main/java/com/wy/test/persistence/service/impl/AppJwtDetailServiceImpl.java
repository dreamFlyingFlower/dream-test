package com.wy.test.persistence.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wy.test.core.convert.AppJwtDetailConvert;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.AppJwtDetailEntity;
import com.wy.test.core.query.AppJwtDetailQuery;
import com.wy.test.core.vo.AppJwtDetailVO;
import com.wy.test.persistence.mapper.AppJwtDetailMapper;
import com.wy.test.persistence.service.AppJwtDetailService;

import dream.flying.flower.collection.CollectionHelper;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * JWT详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppJwtDetailServiceImpl extends AbstractServiceImpl<AppJwtDetailEntity, AppJwtDetailVO, AppJwtDetailQuery,
		AppJwtDetailConvert, AppJwtDetailMapper> implements AppJwtDetailService {

	protected final static Cache<String, AppJwtDetailVO> DETAILS_CACHE =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppJwtDetailVO getAppDetails(String id, boolean cached) {
		AppJwtDetailVO details = null;
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

	private AppJwtDetailVO getAppDetails(String id) {
		MPJLambdaWrapper<AppJwtDetailEntity> mpjLambdaWrapper = new MPJLambdaWrapper<>();
		mpjLambdaWrapper.selectAll(AppJwtDetailEntity.class)
				.innerJoin(AppEntity.class,
						cnd -> cnd.eq(AppEntity::getId, AppJwtDetailEntity::getId).eq(AppEntity::getId,
								AppJwtDetailEntity::getInstId))
				.selectAll(AppEntity.class).eq(AppEntity::getId, id).eq(AppJwtDetailEntity::getId, id);
		List<AppJwtDetailVO> appJwtDetailVos = selectJoinList(AppJwtDetailVO.class, mpjLambdaWrapper);
		return CollectionHelper.isEmpty(appJwtDetailVos) ? null : appJwtDetailVos.get(0);
	}
}