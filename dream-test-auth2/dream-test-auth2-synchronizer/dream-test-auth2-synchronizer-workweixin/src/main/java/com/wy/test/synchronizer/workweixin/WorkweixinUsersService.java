package com.wy.test.synchronizer.workweixin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.SynchroRelated;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;
import com.wy.test.synchronizer.workweixin.entity.WorkWeixinUsers;
import com.wy.test.synchronizer.workweixin.entity.WorkWeixinUsersResponse;

import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkweixinUsersService extends AbstractSynchronizerService implements ISynchronizerService {

	String access_token;

	static String USERS_URL =
			"https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=%s&department_id=%s&fetch_child=0";

	@Override
	public void sync() {
		log.info("Sync Workweixin Users...");
		try {
			List<SynchroRelated> synchroRelateds = synchroRelatedService.findOrgs(this.synchronizer);

			for (SynchroRelated relatedOrg : synchroRelateds) {
				HttpRequestAdapter request = new HttpRequestAdapter();
				String responseBody = request.get(String.format(USERS_URL, access_token, relatedOrg.getOriginId()));
				WorkWeixinUsersResponse usersResponse = JsonHelpers.read(responseBody, WorkWeixinUsersResponse.class);
				log.trace("response : " + responseBody);

				for (WorkWeixinUsers user : usersResponse.getUserlist()) {
					UserInfo userInfo = buildUserInfo(user);
					log.debug("userInfo : " + userInfo);
					userInfo.setPassword(userInfo.getUsername() + UserInfo.DEFAULT_PASSWORD_SUFFIX);
					userInfoService.saveOrUpdate(userInfo);

					SynchroRelated synchroRelated =
							new SynchroRelated(userInfo.getId(), userInfo.getUsername(), userInfo.getDisplayName(),
									UserInfo.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), user.getUserid(),
									user.getName(), user.getUserid(), "", synchronizer.getInstId());

					synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated, UserInfo.CLASS_TYPE);

					socialsAssociate(synchroRelated, "workweixin");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void postSync(UserInfo userInfo) {

	}

	public UserInfo buildUserInfo(WorkWeixinUsers user) {
		UserInfo userInfo = new UserInfo();
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
		userInfo.setInstId(this.synchronizer.getInstId());
		return userInfo;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}
