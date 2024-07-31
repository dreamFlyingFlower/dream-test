package com.wy.test.synchronizer.workweixin;

import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.SynchroRelated;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;
import com.wy.test.synchronizer.workweixin.entity.WorkWeixinDepts;
import com.wy.test.synchronizer.workweixin.entity.WorkWeixinDeptsResponse;

import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkweixinOrganizationService extends AbstractSynchronizerService implements ISynchronizerService {

	String access_token;

	static String DEPTS_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=%s";

	static long ROOT_DEPT_ID = 1;

	@Override
	public void sync() {
		log.info("Sync Workweixin Organizations ...");

		try {
			WorkWeixinDeptsResponse rsp = requestDepartmentList(access_token);

			for (WorkWeixinDepts dept : rsp.getDepartment()) {
				log.debug("dept : " + dept.getId() + " " + dept.getName() + " " + dept.getParentid());
				// root
				if (dept.getId() == ROOT_DEPT_ID) {
					Organizations rootOrganization = organizationsService.get(Organizations.ROOT_ORG_ID);
					SynchroRelated rootSynchroRelated = buildSynchroRelated(rootOrganization, dept);
					synchroRelatedService.updateSynchroRelated(this.synchronizer, rootSynchroRelated,
							Organizations.CLASS_TYPE);
				} else {
					// synchro Related
					SynchroRelated synchroRelated = synchroRelatedService.findByOriginId(this.synchronizer,
							dept.getId() + "", Organizations.CLASS_TYPE);

					Organizations organization = buildOrganization(dept);
					if (synchroRelated == null) {
						organization.setId(organization.generateId());
						organizationsService.insert(organization);
						log.debug("Organizations : " + organization);

						synchroRelated = buildSynchroRelated(organization, dept);
					} else {
						organization.setId(synchroRelated.getObjectId());
						organizationsService.update(organization);
					}

					synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated,
							Organizations.CLASS_TYPE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SynchroRelated buildSynchroRelated(Organizations organization, WorkWeixinDepts dept) {
		return new SynchroRelated(organization.getId(), organization.getOrgName(), organization.getOrgName(),
				Organizations.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), dept.getId() + "",
				dept.getName(), "", dept.getParentid() + "", synchronizer.getInstId());
	}

	public WorkWeixinDeptsResponse requestDepartmentList(String access_token) {
		HttpRequestAdapter request = new HttpRequestAdapter();
		String responseBody = request.get(String.format(DEPTS_URL, access_token));
		WorkWeixinDeptsResponse deptsResponse = JsonHelpers.read(responseBody, WorkWeixinDeptsResponse.class);

		log.trace("response : " + responseBody);
		for (WorkWeixinDepts dept : deptsResponse.getDepartment()) {
			log.debug("WorkWeixinDepts : " + dept);
		}
		return deptsResponse;
	}

	public Organizations buildOrganization(WorkWeixinDepts dept) {
		// Parent
		SynchroRelated synchroRelatedParent = synchroRelatedService.findByOriginId(this.synchronizer,
				dept.getParentid() + "", Organizations.CLASS_TYPE);
		Organizations org = new Organizations();
		org.setOrgName(dept.getName());
		org.setOrgCode(dept.getId() + "");
		org.setParentId(synchroRelatedParent.getObjectId());
		org.setParentName(synchroRelatedParent.getObjectName());
		org.setSortIndex(dept.getOrder());
		org.setInstId(this.synchronizer.getInstId());
		org.setStatus(ConstStatus.ACTIVE);
		org.setDescription("WorkWeixin");
		return org;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
