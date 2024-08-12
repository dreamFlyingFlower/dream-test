package com.wy.test.sync.dingtalk;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserListRequest;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.dingtalk.api.response.OapiV2UserListResponse.ListUserResponse;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.constant.ConstUser;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.sync.core.synchronizer.AbstractSyncProcessor;
import com.wy.test.sync.core.synchronizer.SyncProcessor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DingtalkUsersService extends AbstractSyncProcessor implements SyncProcessor {

	String access_token;

	@Override
	public void sync() {
		log.info("Sync Dingtalk Users...");
		try {
			List<SyncRelatedEntity> synchroRelateds = synchroRelatedService.findOrgs(this.syncEntity);

			for (SyncRelatedEntity relatedOrg : synchroRelateds) {
				DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
				OapiV2UserListRequest req = new OapiV2UserListRequest();
				log.debug("DingTalk deptId : {}", relatedOrg.getOriginId());
				req.setDeptId(Long.parseLong(relatedOrg.getOriginId()));
				req.setCursor(0L);
				req.setSize(100L);
				req.setOrderField("modify_desc");
				req.setContainAccessLimit(true);
				req.setLanguage("zh_CN");
				OapiV2UserListResponse rsp = client.execute(req, access_token);
				log.trace("response : {}", rsp.getBody());

				if (rsp.getErrcode() == 0) {
					for (ListUserResponse user : rsp.getResult().getList()) {
						log.debug("name : {} , {} , {}", user.getName(), user.getLoginId(), user.getUserid());

						UserEntity userInfo = buildUserInfo(user, relatedOrg);
						log.trace("userInfo {}", userInfo);
						userInfo.setPassword(userInfo.getUsername() + ConstUser.DEFAULT_PASSWORD_SUFFIX);
						userInfoService.saveOrUpdate(userInfo);

						SyncRelatedEntity synchroRelated = new SyncRelatedEntity(userInfo.getId(),
								userInfo.getUsername(), userInfo.getDisplayName(), ConstUser.CLASS_TYPE,
								syncEntity.getId(), syncEntity.getName(), user.getUnionid(), user.getName(),
								user.getUserid(), "", syncEntity.getInstId());
						synchroRelatedService.updateSynchroRelated(this.syncEntity, synchroRelated,
								ConstUser.CLASS_TYPE);

						socialsAssociate(synchroRelated, "dingtalk");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public UserEntity buildUserInfo(ListUserResponse user, SyncRelatedEntity relatedOrg) {
		UserEntity userInfo = new UserEntity();

		userInfo.setUsername(user.getUserid());
		userInfo.setNickName(user.getName());
		userInfo.setDisplayName(user.getName());
		userInfo.setFormattedName(user.getName());

		userInfo.setEmail(StringUtils.isBlank(user.getEmail()) ? user.getUserid() + "@dream.top" : user.getEmail());
		userInfo.setEntryDate(new Date(user.getHiredDate()));
		userInfo.setMobile(user.getMobile());

		userInfo.setDepartmentId(relatedOrg.getObjectId() + "");
		userInfo.setDepartment(relatedOrg.getObjectName());
		userInfo.setEmployeeNumber(user.getJobNumber());
		userInfo.setJobTitle(user.getTitle());
		userInfo.setWorkEmail(user.getOrgEmail());
		userInfo.setWorkPhoneNumber(user.getTelephone());
		userInfo.setWorkOfficeName(user.getWorkPlace());
		if (user.getActive()) {
			userInfo.setStatus(ConstStatus.ACTIVE);
		} else {
			userInfo.setStatus(ConstStatus.INACTIVE);
		}

		userInfo.setInstId(this.syncEntity.getInstId());
		userInfo.setRemark("dingtalk " + user.getRemark());
		return userInfo;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}