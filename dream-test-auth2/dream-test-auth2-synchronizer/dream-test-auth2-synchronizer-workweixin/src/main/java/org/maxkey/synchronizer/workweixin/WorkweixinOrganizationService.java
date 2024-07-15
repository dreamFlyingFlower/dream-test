package org.maxkey.synchronizer.workweixin;

import org.maxkey.synchronizer.workweixin.entity.WorkWeixinDepts;
import org.maxkey.synchronizer.workweixin.entity.WorkWeixinDeptsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wy.test.constants.ConstsStatus;
import com.wy.test.entity.Organizations;
import com.wy.test.entity.SynchroRelated;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;
import com.wy.test.util.JsonUtils;
import com.wy.test.web.HttpRequestAdapter;

@Service
public class WorkweixinOrganizationService extends AbstractSynchronizerService implements ISynchronizerService{
	final static Logger _logger = LoggerFactory.getLogger(WorkweixinOrganizationService.class);
	
	String access_token;
	
	static String DEPTS_URL="https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=%s";
	static long ROOT_DEPT_ID = 1;
	
	public void sync() {
		_logger.info("Sync Workweixin Organizations ...");

		try {			
			WorkWeixinDeptsResponse rsp = requestDepartmentList(access_token);
			
			for(WorkWeixinDepts dept : rsp.getDepartment()) {
				_logger.debug("dept : " + dept.getId()+" "+ dept.getName()+" "+ dept.getParentid());
				//root
				if(dept.getId() == ROOT_DEPT_ID) {
					Organizations rootOrganization = organizationsService.get(Organizations.ROOT_ORG_ID);
					SynchroRelated rootSynchroRelated = buildSynchroRelated(rootOrganization,dept);
					synchroRelatedService.updateSynchroRelated(
							this.synchronizer,rootSynchroRelated,Organizations.CLASS_TYPE);
				}else {
					//synchro Related
					SynchroRelated synchroRelated = 
							synchroRelatedService.findByOriginId(
									this.synchronizer,dept.getId() + "",Organizations.CLASS_TYPE );
					
					Organizations organization = buildOrganization(dept);
					if(synchroRelated == null) {
						organization.setId(organization.generateId());
						organizationsService.insert(organization);
						_logger.debug("Organizations : " + organization);
						
						synchroRelated = buildSynchroRelated(organization,dept);
					}else {
						organization.setId(synchroRelated.getObjectId());
						organizationsService.update(organization);
					}
					
					synchroRelatedService.updateSynchroRelated(
							this.synchronizer,synchroRelated,Organizations.CLASS_TYPE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public SynchroRelated buildSynchroRelated(Organizations organization,WorkWeixinDepts dept) {
		return new SynchroRelated(
					organization.getId(),
					organization.getOrgName(),
					organization.getOrgName(),
					Organizations.CLASS_TYPE,
					synchronizer.getId(),
					synchronizer.getName(),
					dept.getId()+"",
					dept.getName(),
					"",
					dept.getParentid()+"",
					synchronizer.getInstId());
	}
	
	public WorkWeixinDeptsResponse requestDepartmentList(String access_token) {
		HttpRequestAdapter request =new HttpRequestAdapter();
		String responseBody = request.get(String.format(DEPTS_URL, access_token));
		WorkWeixinDeptsResponse deptsResponse  =JsonUtils.gsonStringToObject(responseBody, WorkWeixinDeptsResponse.class);
		
		_logger.trace("response : " + responseBody);
		for(WorkWeixinDepts dept : deptsResponse.getDepartment()) {
			_logger.debug("WorkWeixinDepts : " + dept);
		}
		return deptsResponse;
	}
	
	public Organizations buildOrganization(WorkWeixinDepts dept) {
		//Parent
		SynchroRelated synchroRelatedParent = 
				synchroRelatedService.findByOriginId(
				this.synchronizer,dept.getParentid() + "",Organizations.CLASS_TYPE);
		Organizations org = new Organizations();
		org.setOrgName(dept.getName());
		org.setOrgCode(dept.getId()+"");
		org.setParentId(synchroRelatedParent.getObjectId());
		org.setParentName(synchroRelatedParent.getObjectName());
		org.setSortIndex(dept.getOrder());
		org.setInstId(this.synchronizer.getInstId());
		org.setStatus(ConstsStatus.ACTIVE);
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
