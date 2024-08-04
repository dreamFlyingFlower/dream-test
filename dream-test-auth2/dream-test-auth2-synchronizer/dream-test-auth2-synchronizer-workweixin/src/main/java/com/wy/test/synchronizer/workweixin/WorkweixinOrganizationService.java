package com.wy.test.synchronizer.workweixin;

import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;
import com.wy.test.synchronizer.workweixin.entity.WorkWeixinDepts;
import com.wy.test.synchronizer.workweixin.entity.WorkWeixinDeptsResponse;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
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
			GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();

			for (WorkWeixinDepts dept : rsp.getDepartment()) {
				log.debug("dept : " + dept.getId() + " " + dept.getName() + " " + dept.getParentid());
				// root
				if (dept.getId() == ROOT_DEPT_ID) {
					OrgEntity rootOrganization = organizationsService.getById(OrgEntity.ROOT_ORG_ID);
					SyncRelatedEntity rootSynchroRelated = buildSynchroRelated(rootOrganization, dept);
					synchroRelatedService.updateSynchroRelated(this.synchronizer, rootSynchroRelated,
							OrgEntity.CLASS_TYPE);
				} else {
					// synchro Related
					SyncRelatedEntity synchroRelated = synchroRelatedService.findByOriginId(this.synchronizer,
							dept.getId() + "", OrgEntity.CLASS_TYPE);

					OrgEntity organization = buildOrganization(dept);
					if (synchroRelated == null) {
						organization.setId(generatorStrategyContext.generate());
						organizationsService.insert(organization);
						log.debug("Organizations : " + organization);

						synchroRelated = buildSynchroRelated(organization, dept);
					} else {
						organization.setId(synchroRelated.getObjectId());
						organizationsService.update(organization);
					}

					synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated, OrgEntity.CLASS_TYPE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SyncRelatedEntity buildSynchroRelated(OrgEntity organization, WorkWeixinDepts dept) {
		return new SyncRelatedEntity(organization.getId(), organization.getOrgName(), organization.getOrgName(),
				OrgEntity.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), dept.getId() + "", dept.getName(),
				"", dept.getParentid() + "", synchronizer.getInstId());
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

	public OrgEntity buildOrganization(WorkWeixinDepts dept) {
		// Parent
		SyncRelatedEntity synchroRelatedParent =
				synchroRelatedService.findByOriginId(this.synchronizer, dept.getParentid() + "", OrgEntity.CLASS_TYPE);
		OrgEntity org = new OrgEntity();
		org.setOrgName(dept.getName());
		org.setOrgCode(dept.getId() + "");
		org.setParentId(synchroRelatedParent.getObjectId());
		org.setParentName(synchroRelatedParent.getObjectName());
		org.setSortIndex(dept.getOrder());
		org.setInstId(this.synchronizer.getInstId());
		org.setStatus(ConstStatus.ACTIVE);
		org.setRemark("WorkWeixin");
		return org;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}