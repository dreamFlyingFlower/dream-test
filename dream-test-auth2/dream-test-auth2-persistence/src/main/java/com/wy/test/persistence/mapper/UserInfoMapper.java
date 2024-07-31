package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.UserInfoAdjoint;

public interface UserInfoMapper extends IJpaMapper<UserInfo> {

	// login query
	public UserInfo findByAppIdAndUsername(UserInfo userInfo);

	@Select("select * from  mxk_userinfo where username = #{value} and status = " + ConstStatus.ACTIVE)
	public UserInfo findByUsername(String username);

	@Select("select * from  mxk_userinfo where ( email = #{value} or mobile= #{value} ) and status = "
			+ ConstStatus.ACTIVE)
	public UserInfo findByEmailMobile(String emailMobile);

	public List<Organizations> findDeptsByUserId(String userId);

	public List<UserInfoAdjoint> findAdjointsByUserId(String userId);

	public void updateLocked(UserInfo userInfo);

	public void updateLockout(UserInfo userInfo);

	public void updateBadPWDCount(UserInfo userInfo);

	public int changePassword(ChangePassword changePassword);

	public int updateAppLoginPassword(UserInfo userInfo);

	public int updateProtectedApps(UserInfo userInfo);

	public int updateSharedSecret(UserInfo userInfo);

	public int updatePasswordQuestion(UserInfo userInfo);

	public int updateAuthnType(UserInfo userInfo);

	public int updateEmail(UserInfo userInfo);

	public int updateMobile(UserInfo userInfo);

	public int updateProfile(UserInfo userInfo);

	@Update("update mxk_userinfo set gridlist =  #{gridList} where id = #{id}")
	public int updateGridList(UserInfo userInfo);

	@Update("update mxk_userinfo set status =  #{status} where id = #{id}")
	public int updateStatus(UserInfo userInfo);
}
