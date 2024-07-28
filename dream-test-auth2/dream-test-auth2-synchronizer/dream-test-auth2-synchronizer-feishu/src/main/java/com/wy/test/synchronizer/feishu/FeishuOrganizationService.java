package com.wy.test.synchronizer.feishu;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.SynchroRelated;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;
import com.wy.test.synchronizer.feishu.entity.FeishuDepts;
import com.wy.test.synchronizer.feishu.entity.FeishuDeptsResponse;

import dream.flying.flower.framework.core.helper.TokenHelpers;
import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeishuOrganizationService extends AbstractSynchronizerService implements ISynchronizerService {

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
		Organizations rootOrganization = organizationsService.get(Organizations.ROOT_ORG_ID);
		SynchroRelated rootSynchroRelated = buildSynchroRelated(rootOrganization, rspRoot.getData().getDepartment());

		synchroRelatedService.updateSynchroRelated(this.synchronizer, rootSynchroRelated, Organizations.CLASS_TYPE);

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
						SynchroRelated synchroRelated = synchroRelatedService.findByOriginId(this.synchronizer,
								dept.getOpen_department_id(), Organizations.CLASS_TYPE);
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

	public SynchroRelated buildSynchroRelated(Organizations org, FeishuDepts dept) {
		return new SynchroRelated(org.getId(), org.getOrgName(), org.getOrgName(), Organizations.CLASS_TYPE,
				synchronizer.getId(), synchronizer.getName(), dept.getOpen_department_id(), dept.getName(),
				dept.getDepartment_id(), dept.getParent_department_id(), synchronizer.getInstId());
	}

	public Organizations buildOrganization(FeishuDepts dept) {
		// Parent
		SynchroRelated synchroRelatedParent = synchroRelatedService.findByOriginId(this.synchronizer,
				dept.getParent_department_id(), Organizations.CLASS_TYPE);

		Organizations org = new Organizations();
		org.setOrgCode(dept.getDepartment_id() + "");
		org.setOrgName(dept.getName());
		org.setFullName(dept.getName());
		org.setParentId(synchroRelatedParent.getObjectId());
		org.setParentName(synchroRelatedParent.getObjectName());
		org.setSortIndex(Integer.parseInt(dept.getOrder()));
		org.setInstId(this.synchronizer.getInstId());
		org.setStatus(ConstsStatus.ACTIVE);
		org.setDescription("Feishu");
		return org;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
