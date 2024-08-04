package com.wy.test.synchronizer.reorgdept.workweixin.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReorgDeptService extends AbstractSynchronizerService implements ISynchronizerService {

	String rootParentOrgId = "-1";

	@Override
	public void sync() {
		log.info("Sync Organizations ...");

		try {
			long responseCount = 0;
			HashMap<String, OrgEntity> orgCastMap = new HashMap<String, OrgEntity>();
			OrgEntity queryOrganization = new OrgEntity();
			queryOrganization.setInstId(this.synchronizer.getInstId());
			List<OrgEntity> listOrg = organizationsService.query(queryOrganization);

			buildNamePath(orgCastMap, listOrg);

			for (OrgEntity org : listOrg) {
				log.info("Dept " + (++responseCount) + " : " + org);
				org.setStatus(ConstStatus.ACTIVE);
				organizationsService.update(org);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reorganization name path & code path
	 * 
	 * @param orgCastMap
	 * @param listOrgCast
	 */
	public void buildNamePath(HashMap<String, OrgEntity> orgMap, List<OrgEntity> listOrg) {
		OrgEntity tempOrg = null;
		// root org
		for (int i = 0; i < listOrg.size(); i++) {
			if (listOrg.get(i).getParentId().equals(rootParentOrgId)) {
				tempOrg = listOrg.get(i);
				tempOrg.setReorgNamePath(true);
				tempOrg.setNamePath("/" + tempOrg.getOrgName());
				tempOrg.setCodePath("/" + tempOrg.getId());
				tempOrg.setParentId("-1");
				tempOrg.setParentName("");
				orgMap.put(tempOrg.getId(), tempOrg);
			}
		}

		do {
			for (int i = 0; i < listOrg.size(); i++) {
				if (!listOrg.get(i).isReorgNamePath()) {
					OrgEntity parentOrg = orgMap.get(listOrg.get(i).getParentId());
					tempOrg = listOrg.get(i);
					if (!tempOrg.isReorgNamePath() && parentOrg != null) {
						tempOrg.setReorgNamePath(true);
						tempOrg.setParentName(parentOrg.getOrgName());
						tempOrg.setCodePath(parentOrg.getCodePath() + "/" + tempOrg.getId());
						tempOrg.setNamePath(parentOrg.getNamePath() + "/" + tempOrg.getOrgName());
						orgMap.put(tempOrg.getId(), tempOrg);
						log.info("reorg : " + tempOrg);
					}
				}
			}
		} while (listOrg.size() > listOrg.size());
	}
}