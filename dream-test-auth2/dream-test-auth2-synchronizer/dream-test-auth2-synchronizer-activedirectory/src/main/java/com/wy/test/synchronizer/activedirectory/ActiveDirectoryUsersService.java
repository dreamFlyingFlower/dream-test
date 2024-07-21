package com.wy.test.synchronizer.activedirectory;

import java.util.HashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.constants.ldap.ActiveDirectoryUser;
import com.wy.test.core.entity.HistorySynchronizer;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.SynchroRelated;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.persistence.ldap.ActiveDirectoryUtils;
import com.wy.test.core.persistence.ldap.LdapUtils;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import dream.flying.flower.digest.DigestHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActiveDirectoryUsersService extends AbstractSynchronizerService implements ISynchronizerService {

	ActiveDirectoryUtils ldapUtils;

	@Override
	public void sync() {
		log.info("Sync ActiveDirectory Users...");
		loadOrgsByInstId(this.synchronizer.getInstId(), Organizations.ROOT_ORG_ID);
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
								ActiveDirectoryUtils.getAttrStringValue(objAttrs));
						attributeMap.put(objAttrs.getID().toLowerCase(), objAttrs);
					}

					String originId = DigestHelper.md5Hex(sr.getNameInNamespace());

					UserInfo userInfo = buildUserInfo(attributeMap, sr.getName(), sr.getNameInNamespace());
					if (userInfo != null) {
						userInfo.setPassword(userInfo.getUsername() + UserInfo.DEFAULT_PASSWORD_SUFFIX);
						userInfoService.saveOrUpdate(userInfo);
						log.info("userInfo " + userInfo);

						SynchroRelated synchroRelated =
								new SynchroRelated(userInfo.getId(), userInfo.getUsername(), userInfo.getDisplayName(),
										UserInfo.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), originId,
										userInfo.getDisplayName(), "", "", synchronizer.getInstId());

						synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated,
								UserInfo.CLASS_TYPE);
					}
				}
			}

			// ldapUtils.close();
		} catch (NamingException e) {
			log.error("NamingException ", e);
		}
	}

	public UserInfo buildUserInfo(HashMap<String, Attribute> attributeMap, String name, String nameInNamespace) {

		UserInfo userInfo = new UserInfo();
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
		Organizations deptOrg = orgsNamePathMap.get(deptNamePath);
		if (deptOrg == null) {
			deptOrg = rootOrganization;
		}

		userInfo.setDepartment(deptOrg.getOrgName());
		userInfo.setDepartmentId(deptOrg.getId());
		try {
			userInfo.setId(userInfo.generateId());
			userInfo.setFormattedName(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.CN, attributeMap));// cn
			//
			userInfo.setUsername(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.SAMACCOUNTNAME, attributeMap));// WindowsAccount
			userInfo.setWindowsAccount(
					LdapUtils.getAttributeStringValue(ActiveDirectoryUser.SAMACCOUNTNAME, attributeMap));
			// userInfo.setWindowsAccount(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.USERPRINCIPALNAME,attributeMap));//

			//
			userInfo.setFamilyName(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.SN, attributeMap));// Last
																											// Name/SurName
			userInfo.setGivenName(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.GIVENNAME, attributeMap));// First
																													// Name
			userInfo.setNickName(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.INITIALS, attributeMap));// Initials
			userInfo.setNameZhShortSpell(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.INITIALS, attributeMap));// Initials
			userInfo.setDisplayName(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.DISPLAYNAME, attributeMap));//
			userInfo.setDescription(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.DESCRIPTION, attributeMap));//
			userInfo.setWorkPhoneNumber(
					LdapUtils.getAttributeStringValue(ActiveDirectoryUser.TELEPHONENUMBER, attributeMap));//
			userInfo.setWorkOfficeName(
					LdapUtils.getAttributeStringValue(ActiveDirectoryUser.PHYSICALDELIVERYOFFICENAME, attributeMap));//
			userInfo.setWorkEmail(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.MAIL, attributeMap));//
			userInfo.setWebSite(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.WWWHOMEPAGE, attributeMap));//
			//
			userInfo.setWorkCountry(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.CO, attributeMap));//
			userInfo.setWorkRegion(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.ST, attributeMap));//
			userInfo.setWorkLocality(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.L, attributeMap));//
			userInfo.setWorkStreetAddress(
					LdapUtils.getAttributeStringValue(ActiveDirectoryUser.STREETADDRESS, attributeMap));//
			userInfo.setWorkPostalCode(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.POSTALCODE, attributeMap));//
			userInfo.setWorkAddressFormatted(
					LdapUtils.getAttributeStringValue(ActiveDirectoryUser.POSTOFFICEBOX, attributeMap));//

			if (LdapUtils.getAttributeStringValue(ActiveDirectoryUser.MOBILE, attributeMap).equals("")) {
				userInfo.setMobile(userInfo.getId());
			} else {
				userInfo.setMobile(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.MOBILE, attributeMap));//
			}
			userInfo.setHomePhoneNumber(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.HOMEPHONE, attributeMap));//
			userInfo.setWorkFax(
					LdapUtils.getAttributeStringValue(ActiveDirectoryUser.FACSIMILETELEPHONENUMBER, attributeMap));//
			userInfo.setHomeAddressFormatted(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.INFO, attributeMap));//

			userInfo.setDivision(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.COMPANY, attributeMap)); //
			// userInfo.setDepartment(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.DEPARTMENT,attributeMap));
			// //
			// userInfo.setDepartmentId(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.DEPARTMENT,attributeMap));
			// //
			userInfo.setJobTitle(LdapUtils.getAttributeStringValue(ActiveDirectoryUser.TITLE, attributeMap));//
			userInfo.setUserState("RESIDENT");
			userInfo.setUserType("EMPLOYEE");
			userInfo.setTimeZone("Asia/Shanghai");
			userInfo.setStatus(ConstsStatus.ACTIVE);
			userInfo.setInstId(this.synchronizer.getInstId());

			HistorySynchronizer historySynchronizer = new HistorySynchronizer();
			historySynchronizer.setId(historySynchronizer.generateId());
			historySynchronizer.setSyncId(this.synchronizer.getId());
			historySynchronizer.setSyncName(this.synchronizer.getName());
			historySynchronizer.setObjectId(userInfo.getId());
			historySynchronizer.setObjectName(userInfo.getUsername());
			historySynchronizer.setObjectType(Organizations.class.getSimpleName());
			historySynchronizer.setInstId(synchronizer.getInstId());
			historySynchronizer.setResult("success");
			this.historySynchronizerService.insert(historySynchronizer);

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return userInfo;
	}

	public ActiveDirectoryUtils getLdapUtils() {
		return ldapUtils;
	}

	public void setLdapUtils(ActiveDirectoryUtils ldapUtils) {
		this.ldapUtils = ldapUtils;
	}

}
