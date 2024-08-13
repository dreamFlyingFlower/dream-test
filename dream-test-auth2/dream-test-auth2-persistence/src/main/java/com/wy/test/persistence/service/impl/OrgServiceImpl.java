package com.wy.test.persistence.service.impl;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Service;

import com.wy.test.core.convert.OrgConvert;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.query.OrgQuery;
import com.wy.test.core.vo.OrgVO;
import com.wy.test.persistence.mapper.OrgMapper;
import com.wy.test.persistence.provision.ProvisionAction;
import com.wy.test.persistence.provision.ProvisionService;
import com.wy.test.persistence.provision.ProvisionTopic;
import com.wy.test.persistence.service.OrgService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import lombok.AllArgsConstructor;

/**
 * 组织机构
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class OrgServiceImpl extends AbstractServiceImpl<OrgEntity, OrgVO, OrgQuery, OrgConvert, OrgMapper>
		implements OrgService {

	ProvisionService provisionService;

	@Override
	public boolean insert(OrgEntity organization) {
		if (super.save(organization)) {
			provisionService.send(ProvisionTopic.ORG_TOPIC, organization, ProvisionAction.CREATE_ACTION);
			return true;
		}
		return false;
	}

	@Override
	public boolean update(OrgEntity organization) {
		if (super.updateById(organization)) {
			provisionService.send(ProvisionTopic.ORG_TOPIC, organization, ProvisionAction.UPDATE_ACTION);
			return true;
		}
		return false;
	}

	@Override
	public boolean saveOrUpdate(OrgEntity organization) {
		OrgEntity loadOrg = getOne(lambdaQuery().eq(OrgEntity::getId, organization.getId()).eq(OrgEntity::getInstId,
				organization.getInstId()));
		if (loadOrg == null) {
			return insert(organization);
		} else {
			organization.setId(organization.getId());
			return update(organization);
		}
	}

	@Override
	public List<OrgEntity> queryOrgs(OrgEntity organization) {
		return baseMapper.queryOrgs(organization);
	}

	@Override
	public boolean delete(OrgEntity organization) {
		if (super.delete(organization)) {
			provisionService.send(ProvisionTopic.ORG_TOPIC, organization, ProvisionAction.DELETE_ACTION);
			return true;
		}
		return false;
	}

	/**
	 * 根据数据格式返回数据
	 *
	 * @param cell
	 * @return
	 */
	public static String getValue(Cell cell) {
		if (cell == null) {
			return "";
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == CellType.NUMERIC) {
			cell.setBlank();
			return String.valueOf(cell.getStringCellValue().trim());
		} else {
			return String.valueOf(cell.getStringCellValue().trim());
		}
	}
}