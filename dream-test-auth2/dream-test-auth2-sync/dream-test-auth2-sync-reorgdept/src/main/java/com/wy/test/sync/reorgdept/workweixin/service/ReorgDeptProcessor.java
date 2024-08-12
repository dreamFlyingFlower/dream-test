package com.wy.test.sync.reorgdept.workweixin.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.convert.OrgConvert;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.vo.OrgVO;
import com.wy.test.sync.core.synchronizer.AbstractSyncProcessor;
import com.wy.test.sync.core.synchronizer.SyncProcessor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReorgDeptProcessor extends AbstractSyncProcessor implements SyncProcessor {

	String rootParentOrgId = "-1";

	private OrgConvert orgConvert;

	@Override
	public void sync() {
		log.info("Sync Organizations ...");

		try {
			long responseCount = 0;
			HashMap<String, OrgVO> orgCastMap = new HashMap<>();
			List<OrgEntity> listOrg = organizationsService
					.list(new LambdaQueryWrapper<OrgEntity>().eq(OrgEntity::getInstId, this.syncEntity.getInstId()));

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
	public void buildNamePath(HashMap<String, OrgVO> orgMap, List<OrgEntity> listOrg) {
		List<OrgVO> orgVOs = orgConvert.convertt(listOrg);
		OrgVO tempOrg = null;
		// root org
		for (int i = 0; i < orgVOs.size(); i++) {
			if (orgVOs.get(i).getParentId().equals(rootParentOrgId)) {
				tempOrg = orgVOs.get(i);
				tempOrg.setReorgNamePath(true);
				tempOrg.setNamePath("/" + tempOrg.getOrgName());
				tempOrg.setCodePath("/" + tempOrg.getId());
				tempOrg.setParentId("-1");
				tempOrg.setParentName("");
				orgMap.put(tempOrg.getId(), tempOrg);
			}
		}

		do {
			for (int i = 0; i < orgVOs.size(); i++) {
				if (!orgVOs.get(i).isReorgNamePath()) {
					OrgVO parentOrg = orgMap.get(orgVOs.get(i).getParentId());
					tempOrg = orgVOs.get(i);
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