package com.wy.test.synchronizer.ldap;

import java.util.HashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wy.test.constants.ldap.InetOrgPerson;
import com.wy.test.crypto.DigestUtils;
import com.wy.test.entity.HistorySynchronizer;
import com.wy.test.entity.Organizations;
import com.wy.test.entity.SynchroRelated;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.ldap.LdapUtils;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

@Service
public class LdapUsersService extends AbstractSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(LdapUsersService.class);

	LdapUtils ldapUtils;

	@Override
	public void sync() {
		_logger.info("Sync Ldap Users ...");
		loadOrgsByInstId(this.synchronizer.getInstId(), Organizations.ROOT_ORG_ID);
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(ldapUtils.getSearchScope());
			String filter =
					StringUtils.isNotBlank(this.getSynchronizer().getUserFilters()) ? getSynchronizer().getUserFilters()
							: "(&(objectClass=inetOrgPerson))";
			_logger.debug(" User filter {} ", filter);
			NamingEnumeration<SearchResult> results =
					ldapUtils.getConnection().search(ldapUtils.getBaseDN(), filter, constraints);

			long recordCount = 0;
			while (null != results && results.hasMoreElements()) {
				Object obj = results.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult sr = (SearchResult) obj;
					_logger.debug("Sync User {} , name [{}] , NameInNamespace [{}]", (++recordCount), sr.getName(),
							sr.getNameInNamespace());

					HashMap<String, Attribute> attributeMap = new HashMap<String, Attribute>();
					NamingEnumeration<? extends Attribute> attrs = sr.getAttributes().getAll();
					while (null != attrs && attrs.hasMoreElements()) {
						Attribute objAttrs = attrs.nextElement();
						_logger.trace("attribute {} : {}", objAttrs.getID(), LdapUtils.getAttrStringValue(objAttrs));
						attributeMap.put(objAttrs.getID(), objAttrs);
					}
					String originId = DigestUtils.md5B64(sr.getNameInNamespace());
					UserInfo userInfo = buildUserInfo(attributeMap, sr.getName(), sr.getNameInNamespace());
					userInfo.setPassword(userInfo.getUsername() + UserInfo.DEFAULT_PASSWORD_SUFFIX);
					userInfoService.saveOrUpdate(userInfo);
					SynchroRelated synchroRelated =
							new SynchroRelated(userInfo.getId(), userInfo.getUsername(), userInfo.getDisplayName(),
									UserInfo.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), originId,
									userInfo.getDisplayName(), "", "", synchronizer.getInstId());

					synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated, UserInfo.CLASS_TYPE);
					_logger.info("userInfo " + userInfo);
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
		_logger.info("deptNamePath  " + deptNamePath);
		Organizations deptOrg = orgsNamePathMap.get(deptNamePath);
		userInfo.setDepartment(deptOrg.getOrgName());
		userInfo.setDepartmentId(deptOrg.getId());

		try {
			userInfo.setId(userInfo.generateId());
			String cn = LdapUtils.getAttributeStringValue(InetOrgPerson.CN, attributeMap);
			String uid = LdapUtils.getAttributeStringValue(InetOrgPerson.UID, attributeMap);
			String sn = LdapUtils.getAttributeStringValue(InetOrgPerson.SN, attributeMap);
			String givenName = LdapUtils.getAttributeStringValue(InetOrgPerson.GIVENNAME, attributeMap);
			String initials = LdapUtils.getAttributeStringValue(InetOrgPerson.INITIALS, attributeMap);
			String displayName = LdapUtils.getAttributeStringValue(InetOrgPerson.DISPLAYNAME, attributeMap);
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

			userInfo.setEmployeeNumber(LdapUtils.getAttributeStringValue(InetOrgPerson.EMPLOYEENUMBER, attributeMap));
			// userInfo.setDepartment(LdapUtils.getAttributeStringValue(InetOrgPerson.OU,attributeMap));
			// userInfo.setDepartmentId(LdapUtils.getAttributeStringValue(InetOrgPerson.DEPARTMENTNUMBER,attributeMap));
			userInfo.setJobTitle(LdapUtils.getAttributeStringValue(InetOrgPerson.TITLE, attributeMap));
			userInfo.setWorkOfficeName(
					LdapUtils.getAttributeStringValue(InetOrgPerson.PHYSICALDELIVERYOFFICENAME, attributeMap));
			userInfo.setWorkEmail(LdapUtils.getAttributeStringValue(InetOrgPerson.MAIL, attributeMap));
			userInfo.setWorkRegion(LdapUtils.getAttributeStringValue(InetOrgPerson.ST, attributeMap));
			userInfo.setWorkLocality(LdapUtils.getAttributeStringValue(InetOrgPerson.L, attributeMap));
			userInfo.setWorkStreetAddress(LdapUtils.getAttributeStringValue(InetOrgPerson.STREET, attributeMap));
			userInfo.setWorkPostalCode(LdapUtils.getAttributeStringValue(InetOrgPerson.POSTALCODE, attributeMap));
			userInfo.setWorkAddressFormatted(
					LdapUtils.getAttributeStringValue(InetOrgPerson.POSTOFFICEBOX, attributeMap));
			userInfo.setWorkFax(
					LdapUtils.getAttributeStringValue(InetOrgPerson.FACSIMILETELEPHONENUMBER, attributeMap));

			userInfo.setHomePhoneNumber(LdapUtils.getAttributeStringValue(InetOrgPerson.HOMEPHONE, attributeMap));
			userInfo.setHomeAddressFormatted(
					LdapUtils.getAttributeStringValue(InetOrgPerson.HOMEPOSTALADDRESS, attributeMap));

			if (LdapUtils.getAttributeStringValue(InetOrgPerson.MOBILE, attributeMap).equals("")) {
				userInfo.setMobile(userInfo.getId());
			} else {
				userInfo.setMobile(LdapUtils.getAttributeStringValue(InetOrgPerson.MOBILE, attributeMap));
			}

			userInfo.setPreferredLanguage(
					LdapUtils.getAttributeStringValue(InetOrgPerson.PREFERREDLANGUAGE, attributeMap));

			userInfo.setDescription(LdapUtils.getAttributeStringValue(InetOrgPerson.DESCRIPTION, attributeMap));
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

	public LdapUtils getLdapUtils() {
		return ldapUtils;
	}

	public void setLdapUtils(LdapUtils ldapUtils) {
		this.ldapUtils = ldapUtils;
	}

}
