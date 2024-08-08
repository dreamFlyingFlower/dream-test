package com.wy.test.persistence.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wy.test.core.convert.AppSamlDetailConvert;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.AppSamlDetailEntity;
import com.wy.test.core.query.AppSamlDetailQuery;
import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.persistence.mapper.AppSamlDetailMapper;
import com.wy.test.persistence.service.AppSamlDetailService;

import dream.flying.flower.collection.CollectionHelper;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * SAML详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppSamlDetailServiceImpl extends AbstractServiceImpl<AppSamlDetailEntity, AppSamlDetailVO,
		AppSamlDetailQuery, AppSamlDetailConvert, AppSamlDetailMapper> implements AppSamlDetailService {

	protected final static Cache<String, AppSamlDetailVO> DETAILS_CACHE =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppSamlDetailVO getAppDetails(String id, boolean cached) {
		AppSamlDetailVO details = null;
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

	private AppSamlDetailVO getAppDetails(String id) {
		MPJLambdaWrapper<AppSamlDetailEntity> mpjLambdaWrapper = new MPJLambdaWrapper<>();
		mpjLambdaWrapper.selectAll(AppSamlDetailEntity.class)
				.innerJoin(AppEntity.class,
						cnd -> cnd.eq(AppEntity::getId, AppSamlDetailEntity::getId).eq(AppEntity::getId,
								AppSamlDetailEntity::getInstId))
				.selectAll(AppEntity.class).eq(AppEntity::getId, id).eq(AppSamlDetailEntity::getId, id);
		List<AppSamlDetailVO> appSamlDetailVOs = selectJoinList(AppSamlDetailVO.class, mpjLambdaWrapper);
		return CollectionHelper.isEmpty(appSamlDetailVOs) ? null : appSamlDetailVOs.get(0);
	}
}