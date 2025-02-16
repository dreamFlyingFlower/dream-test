package com.wy.test.sync.feishu;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import com.wy.test.core.constant.ConstCommon;
import com.wy.test.core.constant.ConstOrg;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.sync.core.synchronizer.AbstractSyncProcessor;
import com.wy.test.sync.core.synchronizer.SyncProcessor;
import com.wy.test.sync.feishu.entity.FeishuDepts;
import com.wy.test.sync.feishu.entity.FeishuDeptsResponse;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.web.helper.TokenHelpers;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeishuOrganizationService extends AbstractSyncProcessor implements SyncProcessor {

	String access_token;

	static String DEPTS_URL = "https://open.feishu.cn/open-apis/contact/v3/departments/%s/children?page_size=50";

	static String ROOT_DEPT_URL = "https://open.feishu.cn/open-apis/contact/v3/departments/%s";

	static String ROOT_DEPT_ID = "0";

	@Override
	public void sync() {
		log.info("Sync Feishu Organizations ...");

		LinkedBlockingQueue<String> deptsQueue = new LinkedBlockingQueue<String>();

		deptsQueue.add(ROOT_DEPT_ID);
		// root
		FeishuDeptsResponse rspRoot = requestDepartment(ROOT_DEPT_URL, ROOT_DEPT_ID, access_token);
		OrgEntity rootOrganization = organizationsService.getById(ConstCommon.ROOT_ORG_ID);
		SyncRelatedEntity rootSynchroRelated = buildSynchroRelated(rootOrganization, rspRoot.getData().getDepartment());

		synchroRelatedService.updateSynchroRelated(this.syncEntity, rootSynchroRelated, ConstOrg.CLASS_TYPE);

		// child
		try {
			while (deptsQueue.element() != null) {
				FeishuDeptsResponse rsp = requestDepartmentList(access_token, deptsQueue.poll());
				if (rsp.getCode() == 0 && rsp.getData().getItems() != null) {
					for (FeishuDepts dept : rsp.getData().getItems()) {
						log.debug("dept : id {} , Parent {} , Name {} , od {}", dept.getDepartment_id(),
								dept.getParent_department_id(), dept.getName(), dept.getOpen_department_id());
						deptsQueue.add(dept.getOpen_department_id());
						// synchro Related
						SyncRelatedEntity synchroRelated = synchroRelatedService.findByOriginId(this.syncEntity,
								dept.getOpen_department_id(), ConstOrg.CLASS_TYPE);
						OrgEntity organization = buildOrganization(dept);
						if (synchroRelated == null) {
							GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
							organization.setId(generatorStrategyContext.generate());
							organizationsService.insert(organization);
							log.debug("Organizations : " + organization);
							synchroRelated = buildSynchroRelated(organization, dept);

						} else {
							organization.setId(synchroRelated.getObjectId());
							organizationsService.update(organization);
						}

						synchroRelatedService.updateSynchroRelated(this.syncEntity, synchroRelated,
								ConstOrg.CLASS_TYPE);
					}
				}
			}
		} catch (NoSuchElementException e) {
			log.debug("Sync Department successful .");
		}

	}

	public FeishuDeptsResponse requestDepartmentList(String access_token, String deptId) {
		HttpRequestAdapter request = new HttpRequestAdapter();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", TokenHelpers.createBearer(access_token));
		String responseBody = request.get(String.format(DEPTS_URL, deptId), headers);
		FeishuDeptsResponse deptsResponse = JsonHelpers.read(responseBody, FeishuDeptsResponse.class);

		log.trace("response : " + responseBody);

		return deptsResponse;
	}

	public FeishuDeptsResponse requestDepartment(String url, String deptId, String access_token) {
		HttpRequestAdapter request = new HttpRequestAdapter();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", TokenHelpers.createBearer(access_token));
		String responseBody = request.get(String.format(url, deptId), headers);
		FeishuDeptsResponse deptsResponse = JsonHelpers.read(responseBody, FeishuDeptsResponse.class);

		log.trace("response : " + responseBody);

		return deptsResponse;
	}

	public SyncRelatedEntity buildSynchroRelated(OrgEntity org, FeishuDepts dept) {
		return new SyncRelatedEntity(org.getId(), org.getOrgName(), org.getOrgName(), ConstOrg.CLASS_TYPE,
				syncEntity.getId(), syncEntity.getName(), dept.getOpen_department_id(), dept.getName(),
				dept.getDepartment_id(), dept.getParent_department_id(), syncEntity.getInstId());
	}

	public OrgEntity buildOrganization(FeishuDepts dept) {
		// Parent
		SyncRelatedEntity synchroRelatedParent = synchroRelatedService.findByOriginId(this.syncEntity,
				dept.getParent_department_id(), ConstOrg.CLASS_TYPE);

		OrgEntity org = new OrgEntity();
		org.setOrgCode(dept.getDepartment_id() + "");
		org.setOrgName(dept.getName());
		org.setFullName(dept.getName());
		org.setParentId(synchroRelatedParent.getObjectId());
		org.setParentName(synchroRelatedParent.getObjectName());
		org.setSortIndex(Integer.parseInt(dept.getOrder()));
		org.setInstId(this.syncEntity.getInstId());
		org.setStatus(ConstStatus.ACTIVE);
		org.setRemark("Feishu");
		return org;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}