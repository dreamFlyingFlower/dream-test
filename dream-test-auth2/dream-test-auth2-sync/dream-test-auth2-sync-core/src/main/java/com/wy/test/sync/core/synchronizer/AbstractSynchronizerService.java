package com.wy.test.sync.core.synchronizer;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.persistence.service.HistorySyncService;
import com.wy.test.persistence.service.OrgService;
import com.wy.test.persistence.service.SocialAssociateService;
import com.wy.test.persistence.service.SyncRelatedService;
import com.wy.test.persistence.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSynchronizerService {

	@Autowired
	protected OrgService organizationsService;

	@Autowired
	protected UserService userInfoService;

	@Autowired
	protected SyncRelatedService synchroRelatedService;

	@Autowired
	protected SocialAssociateService socialsAssociatesService;

	@Autowired
	protected HistorySyncService historySynchronizerService;

	protected SyncEntity synchronizer;

	protected HashMap<String, OrgEntity> orgsNamePathMap;

	protected OrgEntity rootOrganization;

	public HashMap<String, OrgEntity> loadOrgsByInstId(String instId, String rootOrgId) {
		List<OrgEntity> orgsList =
				organizationsService.list(new LambdaQueryWrapper<OrgEntity>().eq(OrgEntity::getInstId, instId));
		if (rootOrgId == null || rootOrgId.equals("")) {
			rootOrgId = "1";
		}

		for (OrgEntity org : orgsList) {
			if (org.getId().equals(rootOrgId) && rootOrgId.equals("1")) {
				rootOrganization = org;
				rootOrganization.setNamePath("/" + rootOrganization.getOrgName());
				rootOrganization.setCodePath("/1");
				rootOrganization.setParentId("-1");
				rootOrganization.setParentName("");
			} else if (org.getId().equals(rootOrgId)) {
				rootOrganization = org;
			}
		}

		orgsNamePathMap = new HashMap<String, OrgEntity>();
		orgsNamePathMap.put(rootOrganization.getNamePath(), rootOrganization);
		push(orgsNamePathMap, orgsList, rootOrganization);

		log.trace("orgsNamePathMap " + orgsNamePathMap);
		return orgsNamePathMap;
	}

	public void socialsAssociate(SyncRelatedEntity synchroRelated, String provider) {
		SocialAssociateEntity socialsAssociate =
				socialsAssociatesService.getOne(new LambdaQueryWrapper<SocialAssociateEntity>()
						.eq(SocialAssociateEntity::getInstId, synchroRelated.getInstId())
						.eq(SocialAssociateEntity::getUserId, synchroRelated.getObjectId())
						.eq(SocialAssociateEntity::getSocialUserId, synchroRelated.getOriginId())
						.eq(SocialAssociateEntity::getProvider, provider));
		if (socialsAssociate == null) {
			socialsAssociate = new SocialAssociateEntity();
			socialsAssociate.setUserId(synchroRelated.getObjectId());
			socialsAssociate.setUsername(synchroRelated.getObjectName());
			socialsAssociate.setInstId(synchroRelated.getInstId());
			socialsAssociate.setProvider(provider);
			socialsAssociate.setSocialUserId(synchroRelated.getOriginId());
			socialsAssociatesService.save(socialsAssociate);
		}
	}

	public void push(HashMap<String, OrgEntity> orgsNamePathMap, List<OrgEntity> orgsList, OrgEntity parentOrg) {
		for (OrgEntity org : orgsList) {
			if (org.getParentId().equals(parentOrg.getId())) {
				if (org.getNamePath() == null
						|| !org.getNamePath().equals(parentOrg.getNamePath() + "/" + org.getOrgName())) {
					org.setParentName(parentOrg.getOrgName());
					org.setNamePath(parentOrg.getNamePath() + "/" + org.getOrgName());
					org.setCodePath(parentOrg.getCodePath() + "/" + org.getId());
					organizationsService.update(org);
				}
				orgsNamePathMap.put(org.getNamePath(), org);
				push(orgsNamePathMap, orgsList, org);
			}
		}
		return;
	}

	public HashMap<String, OrgEntity> getOrgsNamePathMap() {
		return orgsNamePathMap;
	}

	public void setOrgsNamePathMap(HashMap<String, OrgEntity> orgsNamePathMap) {
		this.orgsNamePathMap = orgsNamePathMap;
	}

	public OrgEntity getRootOrganization() {
		return rootOrganization;
	}

	public void setRootOrganization(OrgEntity rootOrganization) {
		this.rootOrganization = rootOrganization;
	}

	public SyncEntity getSynchronizer() {
		return synchronizer;
	}

	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;
	}
}