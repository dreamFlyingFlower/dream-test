package com.wy.test.synchronizer.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import dream.flying.flower.db.JdbcHelper;
import dream.flying.flower.db.TableMetaData;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JdbcUsersService extends AbstractSynchronizerService implements ISynchronizerService {

	static ArrayList<ColumnFieldMapper> mapperList = new ArrayList<ColumnFieldMapper>();

	@Override
	public void sync() {
		log.info("Sync Jdbc Users...");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			if (StrHelper.isNotBlank(synchronizer.getOrgFilters())) {
				log.info("Sync User Filters {}", synchronizer.getOrgFilters());
				conn = JdbcHelper.connect(synchronizer.getProviderUrl(), synchronizer.getPrincipal(),
						synchronizer.getCredentials(), synchronizer.getDriverClass());

				stmt = conn.createStatement();
				rs = stmt.executeQuery(synchronizer.getUserFilters());
				long insertCount = 0;
				long updateCount = 0;
				long readCount = 0;
				while (rs.next()) {
					UserInfo user = buildUserInfo(rs);
					UserInfo queryUser = this.userInfoService.findByUsername(user.getUsername());
					readCount++;
					if (queryUser == null) {
						if (user.getPassword().indexOf("{") > -1 && user.getPassword().indexOf("}") > -1) {
							userInfoService.insert(user, false);
						} else {
							// passwordEncoder
							userInfoService.insert(user, true);
						}
						user.setBadPasswordCount(1);
						insertCount++;
					} else {
						// no need update password , set null
						user.setPassword(null);
						userInfoService.update(user);
						updateCount++;
					}
					log.trace("read Count {} , insert Count {} , updateCount {} ", readCount, insertCount, updateCount);
				}
				log.info("read Count {} , insert Count {} , updateCount {} ", readCount, insertCount, updateCount);
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

	public UserInfo buildUserInfo(ResultSet rs) throws SQLException {
		TableMetaData meta = JdbcHelper.getMetaData(rs);
		UserInfo user = new UserInfo();
		// basic
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
						PropertyUtils.setSimpleProperty(user, mapper.getField(), value);
					} catch (Exception e) {
						log.error("setSimpleProperty {}", e);
					}
				}
			}
		}

		if (meta.getColumnDetail().containsKey("status")) {
			user.setStatus(rs.getInt("status"));
		} else {
			user.setStatus(ConstsStatus.ACTIVE);
		}
		user.setInstId(this.synchronizer.getInstId());

		// password
		if (meta.getColumnDetail().containsKey("password")) {
			user.setPassword(rs.getString("password"));
		} else {
			// 后4位
			String last4Char = "6666";
			if (StrHelper.isNotBlank(user.getIdCardNo())) {
				last4Char = user.getIdCardNo().substring(user.getIdCardNo().length() - 4);
			} else if (StrHelper.isNotBlank(user.getMobile())) {
				last4Char = user.getMobile().substring(user.getMobile().length() - 4);
			} else if (StrHelper.isNotBlank(user.getEmployeeNumber())) {
				last4Char = user.getEmployeeNumber().substring(user.getEmployeeNumber().length() - 4);
			}
			user.setPassword(user.getUsername() + "@M" + last4Char);
		}

		log.debug("User {} ", user);
		return user;
	}

	static {
		mapperList.add(new ColumnFieldMapper("id", "id", "String"));
		mapperList.add(new ColumnFieldMapper("username", "userName", "String"));
		mapperList.add(new ColumnFieldMapper("picture", "picture", "String"));
		mapperList.add(new ColumnFieldMapper("displayname", "displayName", "String"));
		mapperList.add(new ColumnFieldMapper("nickname", "nickName", "String"));
		mapperList.add(new ColumnFieldMapper("mobile", "mobile", "String"));
		mapperList.add(new ColumnFieldMapper("email", "email", "String"));
		mapperList.add(new ColumnFieldMapper("birthdate", "birthDate", "String"));
		mapperList.add(new ColumnFieldMapper("usertype", "userType", "String"));
		mapperList.add(new ColumnFieldMapper("userstate", "userState", "String"));
		mapperList.add(new ColumnFieldMapper("windowsaccount", "windowsAccount", "String"));
		mapperList.add(new ColumnFieldMapper("givenname", "givenName", "String"));
		mapperList.add(new ColumnFieldMapper("middlename", "middleName", "String"));
		mapperList.add(new ColumnFieldMapper("married", "married", "Int"));
		mapperList.add(new ColumnFieldMapper("gender", "gender", "Int"));
		mapperList.add(new ColumnFieldMapper("idtype", "idType", "Int"));
		mapperList.add(new ColumnFieldMapper("idcardno", "idCardNo", "String"));
		mapperList.add(new ColumnFieldMapper("website", "webSite", "String"));
		mapperList.add(new ColumnFieldMapper("startworkdate", "startWorkDate", "String"));
		// work
		mapperList.add(new ColumnFieldMapper("workcountry", "workCountry", "String"));
		mapperList.add(new ColumnFieldMapper("workregion", "workRegion", "String"));
		mapperList.add(new ColumnFieldMapper("worklocality", "workLocality", "String"));
		mapperList.add(new ColumnFieldMapper("workstreetaddress", "workStreetAddress", "String"));
		mapperList.add(new ColumnFieldMapper("workaddressformatted", "workAddressFormatted", "String"));
		mapperList.add(new ColumnFieldMapper("workemail", "workEmail", "String"));
		mapperList.add(new ColumnFieldMapper("workphonenumber", "workPhoneNumber", "String"));
		mapperList.add(new ColumnFieldMapper("workpostalcode", "workPostalCode", "String"));
		mapperList.add(new ColumnFieldMapper("workfax", "workFax", "String"));
		mapperList.add(new ColumnFieldMapper("workofficename", "workOfficeName", "String"));
		// home
		mapperList.add(new ColumnFieldMapper("homecountry", "homeCountry", "String"));
		mapperList.add(new ColumnFieldMapper("homeregion", "homeRegion", "String"));
		mapperList.add(new ColumnFieldMapper("homelocality", "homeLocality", "String"));
		mapperList.add(new ColumnFieldMapper("homestreetaddress", "homeStreetAddress", "String"));
		mapperList.add(new ColumnFieldMapper("homeaddressformatted", "homeAddressFormatted", "String"));
		mapperList.add(new ColumnFieldMapper("homeemail", "homeEmail", "String"));
		mapperList.add(new ColumnFieldMapper("homephonenumber", "homePhonenumber", "String"));
		mapperList.add(new ColumnFieldMapper("homepostalcode", "homePostalCode", "String"));
		mapperList.add(new ColumnFieldMapper("homefax", "homeFax", "String"));
		// company
		mapperList.add(new ColumnFieldMapper("employeenumber", "employeeNumber", "String"));
		mapperList.add(new ColumnFieldMapper("costcenter", "costCenter", "String"));
		mapperList.add(new ColumnFieldMapper("organization", "organization", "String"));
		mapperList.add(new ColumnFieldMapper("division", "division", "String"));
		mapperList.add(new ColumnFieldMapper("departmentid", "departmentId", "String"));
		mapperList.add(new ColumnFieldMapper("department", "department", "String"));
		mapperList.add(new ColumnFieldMapper("jobtitle", "jobTitle", "String"));
		mapperList.add(new ColumnFieldMapper("joblevel", "jobLevel", "String"));
		mapperList.add(new ColumnFieldMapper("managerid", "managerId", "String"));
		mapperList.add(new ColumnFieldMapper("manager", "manager", "String"));
		mapperList.add(new ColumnFieldMapper("assistantid", "assistantId", "String"));
		mapperList.add(new ColumnFieldMapper("assistant", "assistant", "String"));
		mapperList.add(new ColumnFieldMapper("entrydate", "entrydate", "String"));
		mapperList.add(new ColumnFieldMapper("quitdate", "quitdate", "String"));
		mapperList.add(new ColumnFieldMapper("ldapdn", "ldapDn", "String"));

		mapperList.add(new ColumnFieldMapper("description", "description", "String"));
		mapperList.add(new ColumnFieldMapper("status", "status", "String"));
	}
}
