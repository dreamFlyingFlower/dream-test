package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.AccountQuery;
import com.wy.test.core.vo.AccountVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 用户账号表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AccountMapper extends BaseMappers<AccountEntity, AccountVO, AccountQuery> {

	List<UserEntity> queryUserNotInStrategy(AccountStrategyEntity strategy);

	long deleteByStrategy(AccountStrategyEntity strategy);

	List<AccountEntity> queryByAppIdAndDate(AccountEntity account);

	@Select("select * from auth_account where appid=#{appId} and	relatedusername=#{relatedUsername}")
	List<AccountEntity> queryByAppIdAndAccount(@Param("appId") String appId,
			@Param("relatedUsername") String relatedUsername);

	@Update("update auth_account set status = #{status}  where id= #{id}")
	int updateStatus(AccountEntity accounts);
}