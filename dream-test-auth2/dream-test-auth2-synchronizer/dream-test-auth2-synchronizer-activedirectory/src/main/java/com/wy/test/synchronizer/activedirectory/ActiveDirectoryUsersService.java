package com.wy.test.synchronizer.activedirectory;

import java.util.HashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.constants.ldap.ActiveDirectoryUser;
import com.wy.test.core.entity.HistorySyncEntity;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.persistence.ldap.ActiveDirectoryHelpers;
import com.wy.test.core.persistence.ldap.LdapHelpers;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActiveDirectoryUsersService extends AbstractSynchronizerService implements ISynchronizerService {

	ActiveDirectoryHelpers ldapUtils;

	@Override
	public void sync() {
		log.info("Sync ActiveDirectory Users...");
		loadOrgsByInstId(this.synchronizer.getInstId(), OrgEntity.ROOT_ORG_ID);
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(ldapUtils.getSearchScope());
			String filter =
					StringUtils.isNotBlank(this.getSynchronizer().getUserFilters()) ? getSynchronizer().getUserFilters()
							: "(&(objectClass=User))";
			NamingEnumeration<SearchResult> results =
					ldapUtils.getConnection().search(ldapUtils.getBaseDN(), filter, constraints);

			long recordCount = 0;
			while (null != results && results.hasMoreElements()) {
				Object obj = results.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult sr = (SearchResult) obj;
					if (sr.getNameInNamespace().contains("CN=Users,")
							|| sr.getNameInNamespace().contains("OU=Domain Controllers,")) {
						log.trace("Skip 'CN=Users' or 'OU=Domain Controllers' . ");
						continue;
					}
					log.debug("Sync User {} , name [{}] , NameInNamespace [{}]", (++recordCount), sr.getName(),
							sr.getNameInNamespace());

					HashMap<String, Attribute> attributeMap = new HashMap<String, Attribute>();
					NamingEnumeration<? extends Attribute> attrs = sr.getAttributes().getAll();
					while (null != attrs && attrs.hasMoreElements()) {
						Attribute objAttrs = attrs.nextElement();
						log.trace("attribute {} : {}", objAttrs.getID(),
								ActiveDirectoryHelpers.getAttrStringValue(objAttrs));
						attributeMap.put(objAttrs.getID().toLowerCase(), objAttrs);
					}

					String originId = DigestHelper.md5Hex(sr.getNameInNamespace());

					UserEntity userInfo = buildUserInfo(attributeMap, sr.getName(), sr.getNameInNamespace());
					if (userInfo != null) {
						userInfo.setPassword(userInfo.getUsername() + UserEntity.DEFAULT_PASSWORD_SUFFIX);
						userInfoService.saveOrUpdate(userInfo);
						log.info("userInfo " + userInfo);

						SyncRelatedEntity synchroRelated = new SyncRelatedEntity(userInfo.getId(),
								userInfo.getUsername(), userInfo.getDisplayName(), UserEntity.CLASS_TYPE,
								synchronizer.getId(), synchronizer.getName(), originId, userInfo.getDisplayName(), "",
								"", synchronizer.getInstId());

						synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated,
								UserEntity.CLASS_TYPE);
					}
				}
			}

			// ldapUtils.close();
		} catch (NamingException e) {
			log.error("NamingException ", e);
		}
	}

	public UserEntity buildUserInfo(HashMap<String, Attribute> attributeMap, String name, String nameInNamespace) {

		UserEntity userInfo = new UserEntity();
		userInfo.setLdapDn(nameInNamespace);
		String[] namePaths = name.replaceAll(",OU=", "/").replaceAll("OU=", "/").replaceAll(",ou=", "/")
				.replaceAll("ou=", "/").split("/");

		String namePah = "/" + rootOrganization.getOrgName();
		for (int i = namePaths.length - 1; i >= 0; i--) {
			namePah = namePah + "/" + namePaths[i];
		}

		// namePah = namePah.substring(0, namePah.length());
		String deptNamePath = namePah.substring(0, namePah.lastIndexOf("/"));
		log.info("deptNamePath  " + deptNamePath);
		OrgEntity deptOrg = orgsNamePathMap.get(deptNamePath);
		if (deptOrg == null) {
			deptOrg = rootOrganization;
		}
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();

		userInfo.setDepartment(deptOrg.getOrgName());
		userInfo.setDepartmentId(deptOrg.getId());
		try {
			userInfo.setId(generatorStrategyContext.generate());
			userInfo.setFormattedName(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.CN, attributeMap));// cn
			//
			userInfo.setUsername(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.SAMACCOUNTNAME, attributeMap));// WindowsAccount
			userInfo.setWindowsAccount(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.SAMACCOUNTNAME, attributeMap));
			// userInfo.setWindowsAccount(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.USERPRINCIPALNAME,attributeMap));//

			//
			userInfo.setFamilyName(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.SN, attributeMap));// Last
																												// Name/SurName
			userInfo.setGivenName(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.GIVENNAME, attributeMap));// First
																													// Name
			userInfo.setNickName(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.INITIALS, attributeMap));// Initials
			userInfo.setNameZhShortSpell(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.INITIALS, attributeMap));// Initials
			userInfo.setDisplayName(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.DISPLAYNAME, attributeMap));//
			userInfo.setRemark(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.DESCRIPTION, attributeMap));//
			userInfo.setWorkPhoneNumber(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.TELEPHONENUMBER, attributeMap));//
			userInfo.setWorkOfficeName(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.PHYSICALDELIVERYOFFICENAME, attributeMap));//
			userInfo.setWorkEmail(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.MAIL, attributeMap));//
			userInfo.setWebSite(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.WWWHOMEPAGE, attributeMap));//
			//
			userInfo.setWorkCountry(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.CO, attributeMap));//
			userInfo.setWorkRegion(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.ST, attributeMap));//
			userInfo.setWorkLocality(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.L, attributeMap));//
			userInfo.setWorkStreetAddress(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.STREETADDRESS, attributeMap));//
			userInfo.setWorkPostalCode(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.POSTALCODE, attributeMap));//
			userInfo.setWorkAddressFormatted(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.POSTOFFICEBOX, attributeMap));//

			if (LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.MOBILE, attributeMap).equals("")) {
				userInfo.setMobile(userInfo.getId());
			} else {
				userInfo.setMobile(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.MOBILE, attributeMap));//
			}
			userInfo.setHomePhoneNumber(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.HOMEPHONE, attributeMap));//
			userInfo.setWorkFax(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.FACSIMILETELEPHONENUMBER, attributeMap));//
			userInfo.setHomeAddressFormatted(
					LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.INFO, attributeMap));//

			userInfo.setDivision(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.COMPANY, attributeMap)); //
			// userInfo.setDepartment(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.DEPARTMENT,attributeMap));
			// //
			// userInfo.setDepartmentId(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.DEPARTMENT,attributeMap));
			// //
			userInfo.setJobTitle(LdapHelpers.getAttributeStringValue(ActiveDirectoryUser.TITLE, attributeMap));//
			userInfo.setUserStatus("RESIDENT");
			userInfo.setUserType("EMPLOYEE");
			userInfo.setTimeZone("Asia/Shanghai");
			userInfo.setStatus(ConstStatus.ACTIVE);
			userInfo.setInstId(this.synchronizer.getInstId());

			HistorySyncEntity historySynchronizer = new HistorySyncEntity();
			historySynchronizer.setId(generatorStrategyContext.generate());
			historySynchronizer.setSyncId(this.synchronizer.getId());
			historySynchronizer.setSyncName(this.synchronizer.getName());
			historySynchronizer.setObjectId(userInfo.getId());
			historySynchronizer.setObjectName(userInfo.getUsername());
			historySynchronizer.setObjectType(OrgEntity.class.getSimpleName());
			historySynchronizer.setInstId(synchronizer.getInstId());
			historySynchronizer.setResult("success");
			this.historySynchronizerService.save(historySynchronizer);

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return userInfo;
	}

	public ActiveDirectoryHelpers getLdapUtils() {
		return ldapUtils;
	}

	public void setLdapUtils(ActiveDirectoryHelpers ldapUtils) {
		this.ldapUtils = ldapUtils;
	}

}
