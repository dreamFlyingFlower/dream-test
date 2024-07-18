package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.entity.apps.UserApps;

public interface AppsMapper extends IJpaMapper<Apps> {

	public int insertApp(Apps app);

	public int updateApp(Apps app);

	@Update("update mxk_apps set extendattr=#{extendAttr} where id = #{id}")
	public int updateExtendAttr(Apps app);

	public List<UserApps> queryMyApps(UserApps userApplications);
}
