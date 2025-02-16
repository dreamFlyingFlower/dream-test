package com.wy.test.sync.feishu;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.constant.ConstUser;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.sync.core.synchronizer.AbstractSyncProcessor;
import com.wy.test.sync.core.synchronizer.SyncProcessor;
import com.wy.test.sync.feishu.entity.FeishuUsers;
import com.wy.test.sync.feishu.entity.FeishuUsersResponse;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.web.helper.TokenHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeishuUsersService extends AbstractSyncProcessor implements SyncProcessor {

	String access_token;

	static String USERS_URL =
			"https://open.feishu.cn/open-apis/contact/v3/users/find_by_department?department_id=%s&page_size=50";

	@Override
	public void sync() {
		log.info("Sync Feishu Users...");
		try {
			List<SyncRelatedEntity> synchroRelateds = synchroRelatedService.findOrgs(this.syncEntity);

			for (SyncRelatedEntity relatedOrg : synchroRelateds) {
				HttpRequestAdapter request = new HttpRequestAdapter();
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization", TokenHelpers.createBearer(access_token));
				String responseBody = request.get(String.format(USERS_URL, relatedOrg.getOriginId()), headers);
				FeishuUsersResponse usersResponse = JsonHelpers.read(responseBody, FeishuUsersResponse.class);
				log.trace("response : " + responseBody);
				if (usersResponse.getCode() == 0 && usersResponse.getData().getItems() != null) {
					for (FeishuUsers feiShuUser : usersResponse.getData().getItems()) {
						UserEntity userInfo = buildUserInfo(feiShuUser, relatedOrg);
						log.debug("userInfo : " + userInfo);
						userInfo.setPassword(userInfo.getUsername() + ConstUser.DEFAULT_PASSWORD_SUFFIX);
						userInfoService.saveOrUpdate(userInfo);

						SyncRelatedEntity synchroRelated = new SyncRelatedEntity(userInfo.getId(),
								userInfo.getUsername(), userInfo.getDisplayName(), ConstUser.CLASS_TYPE,
								syncEntity.getId(), syncEntity.getName(), feiShuUser.getOpen_id(), feiShuUser.getName(),
								feiShuUser.getUser_id(), feiShuUser.getUnion_id(), syncEntity.getInstId());
						synchroRelatedService.updateSynchroRelated(this.syncEntity, synchroRelated,
								ConstUser.CLASS_TYPE);

						synchroRelated.setOriginId(feiShuUser.getUnion_id());
						socialsAssociate(synchroRelated, "feishu");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void postSync(UserEntity userInfo) {

	}

	public UserEntity buildUserInfo(FeishuUsers user, SyncRelatedEntity relatedOrg) {
		UserEntity userInfo = new UserEntity();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		userInfo.setId(generatorStrategyContext.generate());
		userInfo.setUsername(user.getUser_id());// 账号
		userInfo.setNickName(user.getNickname());// 名字
		userInfo.setDisplayName(user.getName());// 名字

		userInfo.setMobile(user.getMobile());// 手机
		userInfo.setEmail(user.getEmail());
		userInfo.setGender(user.getGender());

		userInfo.setEmployeeNumber(user.getEmployee_no());
		userInfo.setWorkPhoneNumber(user.getMobile());// 工作电话

		userInfo.setDepartmentId(relatedOrg.getObjectId());
		userInfo.setDepartment(relatedOrg.getObjectName());

		userInfo.setJobTitle(user.getJob_title());// 职务
		userInfo.setWorkAddressFormatted(user.getWork_station());// 工作地点

		// 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
		if (user.getStatus().isIs_activated()) {
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