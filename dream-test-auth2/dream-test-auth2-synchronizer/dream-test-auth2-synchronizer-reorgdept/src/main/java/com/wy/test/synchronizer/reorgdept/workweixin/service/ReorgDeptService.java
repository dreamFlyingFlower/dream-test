package com.wy.test.synchronizer.reorgdept.workweixin.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.entity.Organizations;
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
			HashMap<String, Organizations> orgCastMap = new HashMap<String, Organizations>();
			Organizations queryOrganization = new Organizations();
			queryOrganization.setInstId(this.synchronizer.getInstId());
			List<Organizations> listOrg = organizationsService.query(queryOrganization);

			buildNamePath(orgCastMap, listOrg);

			for (Organizations org : listOrg) {
				log.info("Dept " + (++responseCount) + " : " + org);
				org.setStatus(ConstsStatus.ACTIVE);
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
	public void buildNamePath(HashMap<String, Organizations> orgMap, List<Organizations> listOrg) {
		Organizations tempOrg = null;
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
					Organizations parentOrg = orgMap.get(listOrg.get(i).getParentId());
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