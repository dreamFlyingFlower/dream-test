package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.Accounts;
import com.wy.test.core.entity.AccountsStrategy;
import com.wy.test.core.entity.UserInfo;

public interface AccountsMapper extends IJpaMapper<Accounts> {

	public List<UserInfo> queryUserNotInStrategy(AccountsStrategy strategy);

	public long deleteByStrategy(AccountsStrategy strategy);

	public List<Accounts> queryByAppIdAndDate(Accounts account);

	@Select("select * from mxk_accounts where appid=#{appId} and	relatedusername=#{relatedUsername}")
	public List<Accounts> queryByAppIdAndAccount(@Param("appId") String appId,
			@Param("relatedUsername") String relatedUsername);

	@Update("update mxk_accounts set status = #{status}  where id= #{id}")
	public int updateStatus(Accounts accounts);
}
