package com.wy.test.synchronizer.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.wy.test.constants.ConstsStatus;
import com.wy.test.entity.Organizations;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import dream.flying.flower.db.JdbcHelper;
import dream.flying.flower.db.TableMetaData;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JdbcOrganizationService extends AbstractSynchronizerService implements ISynchronizerService {

	static ArrayList<ColumnFieldMapper> mapperList = new ArrayList<ColumnFieldMapper>();

	@Override
	public void sync() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (StringUtils.isNotBlank(synchronizer.getOrgFilters())) {
				log.info("Sync Org Filters {}", synchronizer.getOrgFilters());
				conn = JdbcHelper.connect(synchronizer.getProviderUrl(), synchronizer.getPrincipal(),
						synchronizer.getCredentials(), synchronizer.getDriverClass());

				stmt = conn.createStatement();
				rs = stmt.executeQuery(synchronizer.getOrgFilters());
				while (rs.next()) {
					Organizations org = buildOrganization(rs);
					Organizations queryOrg = this.organizationsService.get(org.getId());
					if (queryOrg == null) {
						organizationsService.insert(org);
					} else {
						this.organizationsService.update(org);
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception:{} ", e);
		} finally {
			try {
				JdbcHelper.release(conn, stmt, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Organizations buildOrganization(ResultSet rs) throws SQLException {
		TableMetaData meta = JdbcHelper.getMetaData(rs);
		Organizations org = new Organizations();

		for (ColumnFieldMapper mapper : mapperList) {
			if (meta.getColumnDetail().containsKey(mapper.getColumn())) {
				Object value = null;
				if (mapper.getType().equalsIgnoreCase("String")) {
					value = rs.getString(mapper.getColumn());
				} else {
					value = rs.getInt(mapper.getColumn());
				}
				if (value != null) {
					try {
						PropertyUtils.setSimpleProperty(org, mapper.getField(), value);
					} catch (Exception e) {
						log.error("setSimpleProperty {}", e);
					}
				}
			}
		}

		org.setId(org.generateId());
		org.setInstId(this.synchronizer.getInstId());
		if (meta.getColumnDetail().containsKey("status")) {
			org.setStatus(rs.getInt("status"));
		} else {
			org.setStatus(ConstsStatus.ACTIVE);
		}

		log.debug("Organization {}", org);
		return org;
	}

	static {
		mapperList.add(new ColumnFieldMapper("id", "id", "String"));
		mapperList.add(new ColumnFieldMapper("orgcode", "orgCode", "String"));
		mapperList.add(new ColumnFieldMapper("orgname", "orgName", "String"));
		mapperList.add(new ColumnFieldMapper("fullname", "fullName", "String"));
		mapperList.add(new ColumnFieldMapper("parentid", "parentId", "String"));
		mapperList.add(new ColumnFieldMapper("parentcode", "parentCode", "String"));
		mapperList.add(new ColumnFieldMapper("parentname", "parentName", "String"));

		mapperList.add(new ColumnFieldMapper("type", "type", "String"));
		mapperList.add(new ColumnFieldMapper("codepath", "codePath", "String"));
		mapperList.add(new ColumnFieldMapper("namepath", "namePath", "String"));
		mapperList.add(new ColumnFieldMapper("level", "level", "Int"));
		mapperList.add(new ColumnFieldMapper("haschild", "hasChild", "String"));
		mapperList.add(new ColumnFieldMapper("division", "division", "String"));
		mapperList.add(new ColumnFieldMapper("country", "country", "String"));
		mapperList.add(new ColumnFieldMapper("region", "region", "String"));
		mapperList.add(new ColumnFieldMapper("locality", "locality", "String"));
		mapperList.add(new ColumnFieldMapper("street", "street", "String"));
		mapperList.add(new ColumnFieldMapper("address", "address", "String"));
		mapperList.add(new ColumnFieldMapper("contact", "contact", "String"));
		mapperList.add(new ColumnFieldMapper("postalcode", "postalCode", "String"));
		mapperList.add(new ColumnFieldMapper("phone", "phone", "String"));
		mapperList.add(new ColumnFieldMapper("fax", "fax", "String"));
		mapperList.add(new ColumnFieldMapper("email", "email", "String"));
		mapperList.add(new ColumnFieldMapper("sortindex", "sortIndex", "Int"));
		mapperList.add(new ColumnFieldMapper("ldapdn", "ldapDn", "String"));
		mapperList.add(new ColumnFieldMapper("description", "description", "String"));
		mapperList.add(new ColumnFieldMapper("status", "status", "int"));
	}
}
