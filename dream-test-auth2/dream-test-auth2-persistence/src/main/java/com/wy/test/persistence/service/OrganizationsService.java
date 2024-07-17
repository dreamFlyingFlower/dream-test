package com.wy.test.persistence.service;

import java.sql.Types;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.dromara.mybatis.jpa.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.Organizations;
import com.wy.test.persistence.mapper.OrganizationsMapper;
import com.wy.test.provision.ProvisionAction;
import com.wy.test.provision.ProvisionService;
import com.wy.test.provision.ProvisionTopic;

@Repository
public class OrganizationsService extends JpaService<Organizations> {

	@Autowired
	ProvisionService provisionService;

	public OrganizationsService() {
		super(OrganizationsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public OrganizationsMapper getMapper() {
		return (OrganizationsMapper) super.getMapper();
	}

	@Override
	public boolean insert(Organizations organization) {
		if (super.insert(organization)) {
			provisionService.send(ProvisionTopic.ORG_TOPIC, organization, ProvisionAction.CREATE_ACTION);
			return true;
		}
		return false;
	}

	@Override
	public boolean update(Organizations organization) {
		if (super.update(organization)) {
			provisionService.send(ProvisionTopic.ORG_TOPIC, organization, ProvisionAction.UPDATE_ACTION);
			return true;
		}
		return false;
	}

	public void saveOrUpdate(Organizations organization) {
		Organizations loadOrg = findOne(" id = ? and instid = ?",
				new Object[] { organization.getId().toString(), organization.getInstId() },
				new int[] { Types.VARCHAR, Types.VARCHAR });
		if (loadOrg == null) {
			insert(organization);
		} else {
			organization.setId(organization.getId());
			update(organization);
		}
	}

	public List<Organizations> queryOrgs(Organizations organization) {
		return getMapper().queryOrgs(organization);
	}

	@Override
	public boolean delete(Organizations organization) {
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
