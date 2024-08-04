package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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

	int insertApp(AppEntity app);

	int updateApp(AppEntity app);

	@Update("update auth_app set extendattr=#{extendAttr} where id = #{id}")
	int updateExtendAttr(AppEntity app);

	List<UserApps> queryMyApps(UserApps userApplications);
}