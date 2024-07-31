package com.wy.test.synchronizer.activedirectory;

import java.util.ArrayList;
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

import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.constants.ldap.OrganizationalUnit;
import com.wy.test.core.entity.HistorySynchronizer;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.SynchroRelated;
import com.wy.test.core.persistence.ldap.ActiveDirectoryHelpers;
import com.wy.test.core.persistence.ldap.LdapHelpers;
import com.wy.test.synchronizer.core.synchronizer.AbstractSynchronizerService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import dream.flying.flower.helper.DateTimeHelper;

@Service
public class ActiveDirectoryOrganizationService extends AbstractSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(ActiveDirectoryOrganizationService.class);

	ActiveDirectoryHelpers ldapUtils;

	@Override
	public void sync() {
		loadOrgsByInstId(this.synchronizer.getInstId(), Organizations.ROOT_ORG_ID);
		_logger.info("Sync ActiveDirectory Organizations ...");
		try {
			ArrayList<Organizations> orgsList = queryActiveDirectory();
			int maxLevel = 0;
			for (Organizations organization : orgsList) {
				maxLevel = (maxLevel < organization.getLevel()) ? organization.getLevel() : maxLevel;
			}

			for (int level = 2; level <= maxLevel; level++) {
				for (Organizations organization : orgsList) {
					if (organization.getLevel() == level) {
						String parentNamePath =
								organization.getNamePath().substring(0, organization.getNamePath().lastIndexOf("/"));

						if (orgsNamePathMap.get(organization.getNamePath()) != null) {
							_logger.info("org  " + orgsNamePathMap.get(organization.getNamePath()).getNamePath()
									+ " exists.");
							continue;
						}

						Organizations parentOrg = orgsNamePathMap.get(parentNamePath);
						if (parentOrg == null) {
							parentOrg = rootOrganization;
						}
						organization.setParentId(parentOrg.getId());
						organization.setParentName(parentOrg.getOrgName());
						organization.setCodePath(parentOrg.getCodePath() + "/" + organization.getId());
						_logger.info("parentNamePath " + parentNamePath + " , namePah " + organization.getNamePath());

						// synchro Related
						SynchroRelated synchroRelated = synchroRelatedService.findByOriginId(this.synchronizer,
								organization.getLdapDn(), Organizations.CLASS_TYPE);
						if (synchroRelated == null) {
							organization.setId(organization.generateId());
							organizationsService.insert(organization);
							_logger.debug("Organizations : " + organization);

							synchroRelated = buildSynchroRelated(organization, organization.getLdapDn(),
									organization.getOrgName());
						} else {
							organization.setId(synchroRelated.getObjectId());
							organizationsService.update(organization);
						}

						synchroRelatedService.updateSynchroRelated(this.synchronizer, synchroRelated,
								Organizations.CLASS_TYPE);

						orgsNamePathMap.put(organization.getNamePath(), organization);

						HistorySynchronizer historySynchronizer = new HistorySynchronizer(synchronizer.generateId(),
								this.synchronizer.getId(), this.synchronizer.getName(), organization.getId(),
								organization.getOrgName(), Organizations.class.getSimpleName(),
								DateTimeHelper.formatDate(), "success", synchronizer.getInstId());
						this.historySynchronizerService.insert(historySynchronizer);
					}
				}
			}

			// ldapUtils.close();
		} catch (NamingException e) {
			_logger.error("NamingException ", e);
		}

	}

	private ArrayList<Organizations> queryActiveDirectory() throws NamingException {
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(ldapUtils.getSearchScope());
		String filter = "(&(objectClass=OrganizationalUnit))";
		if (StringUtils.isNotBlank(this.getSynchronizer().getOrgFilters())) {
			// filter = this.getSynchronizer().getFilters();
		}

		NamingEnumeration<SearchResult> results =
				ldapUtils.getConnection().search(ldapUtils.getBaseDN(), filter, constraints);

		ArrayList<Organizations> orgsList = new ArrayList<Organizations>();
		long recordCount = 0;
		while (null != results && results.hasMoreElements()) {
			Object obj = results.nextElement();
			if (obj instanceof SearchResult) {
				SearchResult sr = (SearchResult) obj;
				if (sr.getNameInNamespace().contains("OU=Domain Controllers") || StringUtils.isEmpty(sr.getName())) {
					_logger.info("Skip '' or 'OU=Domain Controllers' .");
					continue;
				}
				_logger.debug("Sync OrganizationalUnit {} , name [{}] , NameInNamespace [{}]", (++recordCount),
						sr.getName(), sr.getNameInNamespace());

				HashMap<String, Attribute> attributeMap = new HashMap<String, Attribute>();
				NamingEnumeration<? extends Attribute> attrs = sr.getAttributes().getAll();
				while (null != attrs && attrs.hasMoreElements()) {
					Attribute objAttrs = attrs.nextElement();
					_logger.trace("attribute {} : {}", objAttrs.getID(),
							ActiveDirectoryHelpers.getAttrStringValue(objAttrs));
					attributeMap.put(objAttrs.getID().toLowerCase(), objAttrs);
				}

				Organizations organization = buildOrganization(attributeMap, sr.getName(), sr.getNameInNamespace());
				if (organization != null) {
					orgsList.add(organization);
				}
			}
		}
		return orgsList;
	}

	public SynchroRelated buildSynchroRelated(Organizations organization, String ldapDN, String name) {
		return new SynchroRelated(organization.getId(), organization.getOrgName(), organization.getOrgName(),
				Organizations.CLASS_TYPE, synchronizer.getId(), synchronizer.getName(), ldapDN, name, "",
				organization.getParentId(), synchronizer.getInstId());
	}

	public Organizations buildOrganization(HashMap<String, Attribute> attributeMap, String name,
			String nameInNamespace) {
		try {
			Organizations org = new Organizations();
			org.setLdapDn(nameInNamespace);
			String[] namePaths = name.replaceAll(",OU=", "/").replaceAll("OU=", "/").replaceAll(",ou=", "/")
					.replaceAll("ou=", "/").split("/");
			String namePah = "/" + rootOrganization.getOrgName();
			for (int i = namePaths.length - 1; i >= 0; i--) {
				namePah = namePah + "/" + namePaths[i];
			}

			namePah = namePah.substring(0, namePah.length() - 1);

			org.setId(org.generateId());
			org.setOrgCode(org.getId());
			org.setNamePath(namePah);
			org.setLevel(namePaths.length);
			org.setOrgName(LdapHelpers.getAttributeStringValue(OrganizationalUnit.OU, attributeMap));
			org.setFullName(org.getOrgName());
			org.setType("department");
			org.setCountry(LdapHelpers.getAttributeStringValue(OrganizationalUnit.CO, attributeMap));
			org.setRegion(LdapHelpers.getAttributeStringValue(OrganizationalUnit.ST, attributeMap));
			org.setLocality(LdapHelpers.getAttributeStringValue(OrganizationalUnit.L, attributeMap));
			org.setStreet(LdapHelpers.getAttributeStringValue(OrganizationalUnit.STREET, attributeMap));
			org.setPostalCode(LdapHelpers.getAttributeStringValue(OrganizationalUnit.POSTALCODE, attributeMap));
			org.setDescription(LdapHelpers.getAttributeStringValue(OrganizationalUnit.DESCRIPTION, attributeMap));
			org.setInstId(this.synchronizer.getInstId());
			org.setStatus(ConstStatus.ACTIVE);

			_logger.debug("Organization " + org);
			return org;
		} catch (NamingException e) {
			_logger.error("NamingException ", e);
		}
		return null;
	}

	public ActiveDirectoryHelpers getLdapUtils() {
		return ldapUtils;
	}

	public void setLdapUtils(ActiveDirectoryHelpers ldapUtils) {
		this.ldapUtils = ldapUtils;
	}

}
