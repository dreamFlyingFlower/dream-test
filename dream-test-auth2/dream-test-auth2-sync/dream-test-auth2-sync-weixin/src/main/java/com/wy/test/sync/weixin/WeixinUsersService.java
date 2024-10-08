package com.wy.test.sync.weixin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.constant.ConstUser;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.sync.core.synchronizer.AbstractSyncProcessor;
import com.wy.test.sync.core.synchronizer.SyncProcessor;
import com.wy.test.sync.weixin.entity.WeixinUsers;
import com.wy.test.sync.weixin.entity.WeixinUsersResponse;

import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeixinUsersService extends AbstractSyncProcessor implements SyncProcessor {

	String access_token;

	static String USERS_URL =
			"https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=%s&department_id=%s&fetch_child=0";

	@Override
	public void sync() {
		log.info("Sync Workweixin Users...");
		try {
			List<SyncRelatedEntity> synchroRelateds = synchroRelatedService.findOrgs(this.syncEntity);

			for (SyncRelatedEntity relatedOrg : synchroRelateds) {
				HttpRequestAdapter request = new HttpRequestAdapter();
				String responseBody = request.get(String.format(USERS_URL, access_token, relatedOrg.getOriginId()));
				WeixinUsersResponse usersResponse = JsonHelpers.read(responseBody, WeixinUsersResponse.class);
				log.trace("response : " + responseBody);

				for (WeixinUsers user : usersResponse.getUserlist()) {
					UserEntity userInfo = buildUserInfo(user);
					log.debug("userInfo : " + userInfo);
					userInfo.setPassword(userInfo.getUsername() + ConstUser.DEFAULT_PASSWORD_SUFFIX);
					userInfoService.saveOrUpdate(userInfo);

					SyncRelatedEntity synchroRelated = new SyncRelatedEntity(userInfo.getId(), userInfo.getUsername(),
							userInfo.getDisplayName(), ConstUser.CLASS_TYPE, syncEntity.getId(), syncEntity.getName(),
							user.getUserid(), user.getName(), user.getUserid(), "", syncEntity.getInstId());

					synchroRelatedService.updateSynchroRelated(this.syncEntity, synchroRelated, ConstUser.CLASS_TYPE);
					socialsAssociate(synchroRelated, "workweixin");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void postSync(UserEntity userInfo) {

	}

	public UserEntity buildUserInfo(WeixinUsers user) {
		UserEntity userInfo = new UserEntity();
		userInfo.setUsername(user.getUserid());// 账号
		userInfo.setNickName(user.getAlias());// 名字
		userInfo.setDisplayName(user.getName());// 名字

		userInfo.setMobile(user.getMobile());// 手机
		userInfo.setEmail(user.getEmail());
		userInfo.setGender(Integer.parseInt(user.getGender()));

		userInfo.setWorkPhoneNumber(user.getTelephone());// 工作电话
		userInfo.setDepartmentId(user.getMain_department() + "");
		userInfo.setJobTitle(user.getPosition());// 职务
		userInfo.setWorkAddressFormatted(user.getAddress());// 工作地点

		// 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
		if (user.getStatus() == 1) {
			userInfo.setStatus(ConstStatus.ACTIVE);
		} else {
			userInfo.setStatus(ConstStatus.INACTIVE);
		}
		userInfo.setInstId(this.syncEntity.getInstId());
		return userInfo;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}