package com.wy.test.synchronizer.dingtalk;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserListRequest;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.dingtalk.api.response.OapiV2UserListResponse.ListUserResponse;
import com.wy.test.constants.ConstsStatus;
import com.wy.test.entity.SynchroRelated;
import com.wy.test.entity.UserInfo;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

@Service
public class DingtalkUsersService extends AbstractSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(DingtalkUsersService.class);

	String access_token;

	@Override
	public void sync() {
		_logger.info("Sync Dingtalk Users...");
		try {
			List<SynchroRelated> synchroRelateds = synchroRelatedService.findOrgs(this.synchronizer);

			for (SynchroRelated relatedOrg : synchroRelateds) {
				DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
				OapiV2UserListRequest req = new OapiV2UserListRequest();
				_logger.debug("DingTalk deptId : {}", relatedOrg.getOriginId());
				req.setDeptId(Long.parseLong(relatedOrg.getOriginId()));
				req.setCursor(0L);
				req.setSize(100L);
				req.setOrderField("modify_desc");
				req.setContainAccessLimit(true);
				req.setLanguage("zh_CN");
				OapiV2UserListResponse rsp = client.execute(req, access_token);
				_logger.trace("response : {}", rsp.getBody());

				if (rsp.getErrcode() == 0) {
					for (ListUserResponse user : rsp.getResult().getList()) {
						_logger.debug("name : {} , {} , {}", user.getName(), user.getLoginId(), user.getUserid());

						UserInfo userInfo = buildUserInfo(user, relatedOrg);
						_logger.trace("userInfo {}", userInfo);
						userInfo.setPassword(userInfo.getUsername() + UserInfo.DEFAULT_PASSWORD_SUFFIX);
						userInfoService.saveOrUpdate(userInfo);

						SynchroRelated synchroRelated = new SynchroRelated(userInfo.getId(), userInfo.getUsername(),
								userInfo.getDisplayName(), UserInfo.CLASS_TYPE, synchronizer.getId(),
								synchronizer.getName(), user.getUnionid(), user.getName(), user.getUserid(), "",
								synchronizer.getInstId());
						synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated,
								UserInfo.CLASS_TYPE);

						socialsAssociate(synchroRelated, "dingtalk");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public UserInfo buildUserInfo(ListUserResponse user, SynchroRelated relatedOrg) {
		UserInfo userInfo = new UserInfo();

		userInfo.setUsername(user.getUserid());
		userInfo.setNickName(user.getName());
		userInfo.setDisplayName(user.getName());
		userInfo.setFormattedName(user.getName());

		userInfo.setEmail(StringUtils.isBlank(user.getEmail()) ? user.getUserid() + "@maxkey.top" : user.getEmail());
		userInfo.setEntryDate(new DateTime(user.getHiredDate()).toString(DateTimeFormat.forPattern("yyyy-MM-dd")));
		userInfo.setMobile(user.getMobile());

		userInfo.setDepartmentId(relatedOrg.getObjectId() + "");
		userInfo.setDepartment(relatedOrg.getObjectName());
		userInfo.setEmployeeNumber(user.getJobNumber());
		userInfo.setJobTitle(user.getTitle());
		userInfo.setWorkEmail(user.getOrgEmail());
		userInfo.setWorkPhoneNumber(user.getTelephone());
		userInfo.setWorkOfficeName(user.getWorkPlace());
		if (user.getActive()) {
			userInfo.setStatus(ConstsStatus.ACTIVE);
		} else {
			userInfo.setStatus(ConstsStatus.INACTIVE);
		}

		userInfo.setInstId(this.synchronizer.getInstId());
		userInfo.setDescription("dingtalk " + user.getRemark());
		return userInfo;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
