package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.UserAdjunctEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.UserQuery;
import com.wy.test.core.vo.UserVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 用户信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserMapper extends BaseMappers<UserEntity, UserVO, UserQuery> {

	UserEntity findByAppIdAndUsername(UserEntity userInfo);

	@Select("select * from  auth_user where username = #{value} and status = " + ConstStatus.ACTIVE)
	UserEntity findByUsername(String username);

	@Select("select * from  auth_user where ( email = #{value} or mobile= #{value} ) and status = "
			+ ConstStatus.ACTIVE)
	UserEntity findByEmailMobile(String emailMobile);

	List<OrgEntity> findDeptsByUserId(String userId);

	List<UserAdjunctEntity> findAdjointsByUserId(String userId);

	void updateLocked(UserEntity userInfo);

	void updateLockout(UserEntity userInfo);

	void updateBadPWDCount(UserEntity userInfo);

	int changePassword(ChangePassword changePassword);

	int updateAppLoginPassword(UserEntity userInfo);

	int updateProtectedApps(UserEntity userInfo);

	int updateSharedSecret(UserEntity userInfo);

	int updatePasswordQuestion(UserEntity userInfo);

	int updateAuthnType(UserEntity userInfo);

	int updateEmail(UserEntity userInfo);

	int updateMobile(UserEntity userInfo);

	int updateProfile(UserVO userInfo);

	@Update("update auth_user set gridlist =  #{gridList} where id = #{id}")
	int updateGridList(UserVO userInfo);

	@Update("update auth_user set status =  #{status} where id = #{id}")
	int updateStatus(UserEntity userInfo);
}