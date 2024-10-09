package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.query.AppQuery;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserApps;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 应用表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AppMapper extends BaseMappers<AppEntity, AppVO, AppQuery> {

	/**
	 * 查询用户已授权的APP列表
	 * 
	 * @param userApplications
	 * @return 授权列表
	 */
	List<UserApps> queryMyApps(UserApps userApplications);
}