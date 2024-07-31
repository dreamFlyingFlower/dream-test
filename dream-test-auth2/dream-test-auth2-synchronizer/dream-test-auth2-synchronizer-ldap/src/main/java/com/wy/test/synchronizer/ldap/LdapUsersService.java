package com.wy.test.synchronizer.ldap;

import java.util.HashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.wy.test.core.constants.ldap.InetOrgPerson;
import com.wy.test.core.entity.HistorySynchronizer;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.SynchroRelated;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.persistence.ldap.LdapHelpers;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import dream.flying.flower.digest.DigestHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LdapUsersService extends AbstractSynchronizerService implements ISynchronizerService {

	LdapHelpers ldapUtils;

	@Override
	public void sync() {
		log.info("Sync Ldap Users ...");
		loadOrgsByInstId(this.synchronizer.getInstId(), Organizations.ROOT_ORG_ID);
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(ldapUtils.getSearchScope());
			String filter =
					StringUtils.isNotBlank(this.getSynchronizer().getUserFilters()) ? getSynchronizer().getUserFilters()
							: "(&(objectClass=inetOrgPerson))";
			log.debug(" User filter {} ", filter);
			NamingEnumeration<SearchResult> results =
					ldapUtils.getConnection().search(ldapUtils.getBaseDN(), filter, constraints);

			long recordCount = 0;
			while (null != results && results.hasMoreElements()) {
				Object obj = results.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult sr = (SearchResult) obj;
					log.debug("Sync User {} , name [{}] , NameInNamespace [{}]", (++recordCount), sr.getName(),
							sr.getNameInNamespace());

					HashMap<String, Attribute> attributeMap = new HashMap<String, Attribute>();
					NamingEnumeration<? extends Attribute> attrs = sr.getAttributes().getAll();
					while (null != attrs && attrs.hasMoreElements()) {
						Attribute objAttrs = attrs.nextElement();
						log.trace("attribute {} : {}", objAttrs.getID(), LdapHelpers.getAttrStringValue(objAttrs));
						attributeMap.put(objAttrs.getID(), objAttrs);
					}
					String originId = DigestHelper.md5Hex(sr.getNameInNamespace());
					UserInfo userInfo = buildUserInfo(attributeMap, sr.getName(), sr.getNameInNamespace());
					userInfo.setPassword(userInfo.getUsername() + UserInfo.DEFAULT_PASSWORD_SUFFIX);
					userInfoService.saveOrUpdate(userInfo);
					SynchroRelated synchroRelated =
							new SynchroRelated(userInfo.getId(), userInfo.getUsername(), userInfo.getDisplayName(),
									UserInfo.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), originId,
									userInfo.getDisplayName(), "", "", synchronizer.getInstId());

					synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated, UserInfo.CLASS_TYPE);
					log.info("userInfo " + userInfo);
				}
			}

			// ldapUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	public void postSync(UserInfo userInfo) {

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
		userInfo.setDepartment(deptOrg.getOrgName());
		userInfo.setDepartmentId(deptOrg.getId());

		try {
			userInfo.setId(userInfo.generateId());
			String cn = LdapHelpers.getAttributeStringValue(InetOrgPerson.CN, attributeMap);
			String uid = LdapHelpers.getAttributeStringValue(InetOrgPerson.UID, attributeMap);
			String sn = LdapHelpers.getAttributeStringValue(InetOrgPerson.SN, attributeMap);
			String givenName = LdapHelpers.getAttributeStringValue(InetOrgPerson.GIVENNAME, attributeMap);
			String initials = LdapHelpers.getAttributeStringValue(InetOrgPerson.INITIALS, attributeMap);
			String displayName = LdapHelpers.getAttributeStringValue(InetOrgPerson.DISPLAYNAME, attributeMap);
			userInfo.setFormattedName(sn + givenName);
			if (StringUtils.isBlank(uid)) {
				userInfo.setUsername(cn);
				userInfo.setWindowsAccount(cn);
			} else {
				userInfo.setUsername(uid);
				userInfo.setWindowsAccount(uid);
			}
			userInfo.setFamilyName(sn);
			userInfo.setGivenName(givenName);
			if (StringUtils.isBlank(initials)) {
				userInfo.setNickName(sn + givenName);
				userInfo.setNameZhShortSpell(sn + givenName);
			} else {
				userInfo.setNickName(initials);
				userInfo.setNameZhShortSpell(initials);
			}
			if (StringUtils.isBlank(displayName)) {
				userInfo.setDisplayName(sn + givenName);
			} else {
				userInfo.setDisplayName(displayName);
			}

			userInfo.setEmployeeNumber(LdapHelpers.getAttributeStringValue(InetOrgPerson.EMPLOYEENUMBER, attributeMap));
			// userInfo.setDepartment(LdapUtils.getAttributeStringValue(InetOrgPerson.OU,attributeMap));
			// userInfo.setDepartmentId(LdapUtils.getAttributeStringValue(InetOrgPerson.DEPARTMENTNUMBER,attributeMap));
			userInfo.setJobTitle(LdapHelpers.getAttributeStringValue(InetOrgPerson.TITLE, attributeMap));
			userInfo.setWorkOfficeName(
					LdapHelpers.getAttributeStringValue(InetOrgPerson.PHYSICALDELIVERYOFFICENAME, attributeMap));
			userInfo.setWorkEmail(LdapHelpers.getAttributeStringValue(InetOrgPerson.MAIL, attributeMap));
			userInfo.setWorkRegion(LdapHelpers.getAttributeStringValue(InetOrgPerson.ST, attributeMap));
			userInfo.setWorkLocality(LdapHelpers.getAttributeStringValue(InetOrgPerson.L, attributeMap));
			userInfo.setWorkStreetAddress(LdapHelpers.getAttributeStringValue(InetOrgPerson.STREET, attributeMap));
			userInfo.setWorkPostalCode(LdapHelpers.getAttributeStringValue(InetOrgPerson.POSTALCODE, attributeMap));
			userInfo.setWorkAddressFormatted(
					LdapHelpers.getAttributeStringValue(InetOrgPerson.POSTOFFICEBOX, attributeMap));
			userInfo.setWorkFax(
					LdapHelpers.getAttributeStringValue(InetOrgPerson.FACSIMILETELEPHONENUMBER, attributeMap));

			userInfo.setHomePhoneNumber(LdapHelpers.getAttributeStringValue(InetOrgPerson.HOMEPHONE, attributeMap));
			userInfo.setHomeAddressFormatted(
					LdapHelpers.getAttributeStringValue(InetOrgPerson.HOMEPOSTALADDRESS, attributeMap));

			if (LdapHelpers.getAttributeStringValue(InetOrgPerson.MOBILE, attributeMap).equals("")) {
				userInfo.setMobile(userInfo.getId());
			} else {
				userInfo.setMobile(LdapHelpers.getAttributeStringValue(InetOrgPerson.MOBILE, attributeMap));
			}

			userInfo.setPreferredLanguage(
					LdapHelpers.getAttributeStringValue(InetOrgPerson.PREFERREDLANGUAGE, attributeMap));

			userInfo.setDescription(LdapHelpers.getAttributeStringValue(InetOrgPerson.DESCRIPTION, attributeMap));
			userInfo.setUserState("RESIDENT");
			userInfo.setUserType("EMPLOYEE");
			userInfo.setTimeZone("Asia/Shanghai");
			userInfo.setStatus(1);
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

	public LdapHelpers getLdapUtils() {
		return ldapUtils;
	}

	public void setLdapUtils(LdapHelpers ldapUtils) {
		this.ldapUtils = ldapUtils;
	}

}
