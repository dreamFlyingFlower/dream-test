package com.wy.test.synchronizer.dingtalk;

import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentGetRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse.DeptBaseResponse;
import com.taobao.api.ApiException;
import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.SynchroRelated;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DingtalkOrganizationService extends AbstractSynchronizerService implements ISynchronizerService {

	static Long ROOT_DEPT_ID = 1L;

	String access_token;

	@Override
	public void sync() {
		log.info("Sync Dingtalk Organizations ...");
		LinkedBlockingQueue<Long> deptsQueue = new LinkedBlockingQueue<Long>();
		deptsQueue.add(ROOT_DEPT_ID);
		try {
			// root
			Organizations rootOrganization = organizationsService.get(Organizations.ROOT_ORG_ID);
			OapiV2DepartmentGetResponse rootDeptRsp = requestDepartment(access_token, ROOT_DEPT_ID);
			log.debug("root dept   deptId {} , name {} ,  parentId {}", rootDeptRsp.getResult().getDeptId(),
					rootDeptRsp.getResult().getName(), rootDeptRsp.getResult().getParentId());
			// root
			SynchroRelated rootSynchroRelated =
					buildSynchroRelated(rootOrganization, rootDeptRsp.getResult().getDeptId() + "",
							rootDeptRsp.getResult().getName(), rootDeptRsp.getResult().getParentId() + "");

			synchroRelatedService.updateSynchroRelated(this.synchronizer, rootSynchroRelated, Organizations.CLASS_TYPE);

			while (deptsQueue.element() != null) {
				OapiV2DepartmentListsubResponse rsp = requestDepartmentList(access_token, deptsQueue.poll());

				for (DeptBaseResponse dept : rsp.getResult()) {
					log.debug("dept  deptId {} , name {} ,  parentId {} ", dept.getDeptId(), dept.getName(),
							dept.getParentId());

					deptsQueue.add(dept.getDeptId());

					// synchro Related
					SynchroRelated synchroRelated = synchroRelatedService.findByOriginId(this.synchronizer,
							dept.getDeptId() + "", Organizations.CLASS_TYPE);

					Organizations organization = buildOrganization(dept);
					if (synchroRelated == null) {
						organization.setId(organization.generateId());
						organizationsService.insert(organization);
						log.debug("Organizations : " + organization);

						synchroRelated = buildSynchroRelated(organization, dept.getDeptId() + "", dept.getName(),
								dept.getParentId() + "");

					} else {
						organization.setId(synchroRelated.getObjectId());
						organizationsService.update(organization);
					}

					synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated,
							Organizations.CLASS_TYPE);

					log.debug("Organizations : " + organization);
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			log.debug("Sync Department successful .");
		}

	}

	public OapiV2DepartmentListsubResponse requestDepartmentList(String access_token, Long deptId) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
		OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
		req.setDeptId(deptId);
		req.setLanguage("zh_CN");
		OapiV2DepartmentListsubResponse rspDepts = client.execute(req, access_token);
		log.trace("response : " + rspDepts.getBody());
		return rspDepts;
	}

	public OapiV2DepartmentGetResponse requestDepartment(String access_token, Long deptId) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/get");
		OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
		req.setDeptId(deptId);
		req.setLanguage("zh_CN");
		OapiV2DepartmentGetResponse rspDepts = client.execute(req, access_token);
		log.trace("response : " + rspDepts.getBody());
		return rspDepts;
	}

	public SynchroRelated buildSynchroRelated(Organizations organization, String deptId, String name, String parentId) {
		return new SynchroRelated(organization.getId(), organization.getOrgName(), organization.getOrgName(),
				Organizations.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), deptId + "", name, "", parentId,
				synchronizer.getInstId());
	}

	public Organizations buildOrganization(DeptBaseResponse dept) {
		// Parent
		SynchroRelated synchroRelatedParent = synchroRelatedService.findByOriginId(this.synchronizer,
				dept.getParentId() + "", Organizations.CLASS_TYPE);
		Organizations org = new Organizations();
		org.setId(dept.getDeptId() + "");
		org.setOrgCode(dept.getDeptId() + "");
		org.setOrgName(dept.getName());
		org.setParentCode(dept.getParentId() + "");
		if (synchroRelatedParent != null) {
			org.setParentId(synchroRelatedParent.getObjectId());
			org.setParentName(synchroRelatedParent.getObjectName());
		}
		org.setInstId(this.synchronizer.getInstId());
		org.setStatus(ConstsStatus.ACTIVE);
		org.setDescription("dingtalk");
		return org;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}