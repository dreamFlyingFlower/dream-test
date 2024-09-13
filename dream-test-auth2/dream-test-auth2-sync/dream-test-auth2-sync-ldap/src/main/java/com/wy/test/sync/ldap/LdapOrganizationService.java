package com.wy.test.sync.ldap;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.wy.test.core.constant.ConstCommon;
import com.wy.test.core.constant.ConstOrg;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.constant.OrganizationalUnit;
import com.wy.test.core.entity.HistorySyncEntity;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.SyncRelatedEntity;
import com.wy.test.core.ldap.LdapHelpers;
import com.wy.test.sync.core.synchronizer.AbstractSyncProcessor;
import com.wy.test.sync.core.synchronizer.SyncProcessor;

import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LdapOrganizationService extends AbstractSyncProcessor implements SyncProcessor {

	LdapHelpers ldapUtils;

	@Override
	public void sync() {
		log.info("Sync Ldap Organizations ...");
		loadOrgsByInstId(this.syncEntity.getInstId(), ConstCommon.ROOT_ORG_ID);
		try {
			ArrayList<OrgEntity> orgsList = queryLdap();
			int maxLevel = 0;
			for (OrgEntity organization : orgsList) {
				maxLevel = (maxLevel < organization.getLevel()) ? organization.getLevel() : maxLevel;
			}

			for (int level = 2; level <= maxLevel; level++) {
				GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
				for (OrgEntity organization : orgsList) {
					if (organization.getLevel() == level) {
						String parentNamePath =
								organization.getNamePath().substring(0, organization.getNamePath().lastIndexOf("/"));

						if (orgsNamePathMap.get(organization.getNamePath()) != null) {
							log.info("org  " + orgsNamePathMap.get(organization.getNamePath()).getNamePath()
									+ " exists.");
							continue;
						}

						OrgEntity parentOrg = orgsNamePathMap.get(parentNamePath);
						if (parentOrg == null) {
							parentOrg = rootOrganization;
						}
						organization.setParentId(parentOrg.getId());
						organization.setParentName(parentOrg.getOrgName());
						organization.setCodePath(parentOrg.getCodePath() + "/" + organization.getId());
						log.info("parentNamePath " + parentNamePath + " , namePah " + organization.getNamePath());

						// synchro Related
						SyncRelatedEntity synchroRelated = synchroRelatedService.findByOriginId(this.syncEntity,
								organization.getLdapDn(), ConstOrg.CLASS_TYPE);
						if (synchroRelated == null) {

							organization.setId(generatorStrategyContext.generate());
							organizationsService.insert(organization);
							log.debug("Organizations : " + organization);

							synchroRelated = buildSynchroRelated(organization, organization.getLdapDn(),
									organization.getOrgName());
						} else {
							organization.setId(synchroRelated.getObjectId());
							organizationsService.update(organization);
						}

						synchroRelatedService.updateSynchroRelated(this.syncEntity, synchroRelated,
								ConstOrg.CLASS_TYPE);

						orgsNamePathMap.put(organization.getNamePath(), organization);

						log.info("Organizations " + organization);
						HistorySyncEntity historySynchronizer = new HistorySyncEntity();
						historySynchronizer.setId(generatorStrategyContext.generate());
						historySynchronizer.setSyncId(this.syncEntity.getId());
						historySynchronizer.setSyncName(this.syncEntity.getName());
						historySynchronizer.setObjectId(organization.getId());
						historySynchronizer.setObjectName(organization.getOrgName());
						historySynchronizer.setObjectType(OrgEntity.class.getSimpleName());
						historySynchronizer.setInstId(syncEntity.getInstId());
						historySynchronizer.setResult("success");
						this.historySynchronizerService.save(historySynchronizer);
					}
				}
			}
			// ldapUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<OrgEntity> queryLdap() throws NamingException {
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(ldapUtils.getSearchScope());
		String filter = "(&(objectClass=OrganizationalUnit))";
		if (StringUtils.isNotBlank(this.getSyncEntity().getOrgFilters())) {
			filter = this.getSyncEntity().getOrgFilters();
		}
		NamingEnumeration<SearchResult> results =
				ldapUtils.getConnection().search(ldapUtils.getBaseDN(), filter, constraints);

		ArrayList<OrgEntity> orgsList = new ArrayList<OrgEntity>();

		long recordCount = 0;
		while (null != results && results.hasMoreElements()) {
			Object obj = results.nextElement();
			if (obj instanceof SearchResult) {
				SearchResult sr = (SearchResult) obj;
				log.debug("Sync OrganizationalUnit {} , name [{}] , NameInNamespace [{}]", (++recordCount),
						sr.getName(), sr.getNameInNamespace());

				HashMap<String, Attribute> attributeMap = new HashMap<String, Attribute>();
				NamingEnumeration<? extends Attribute> attrs = sr.getAttributes().getAll();
				while (null != attrs && attrs.hasMoreElements()) {
					Attribute objAttrs = attrs.nextElement();
					log.trace("attribute {} : {}", objAttrs.getID(), LdapHelpers.getAttrStringValue(objAttrs));
					attributeMap.put(objAttrs.getID().toLowerCase(), objAttrs);
				}

				OrgEntity organization = buildOrganization(attributeMap, sr.getName(), sr.getNameInNamespace());
				if (organization != null) {
					orgsList.add(organization);
				}
			}
		}
		return orgsList;
	}

	public SyncRelatedEntity buildSynchroRelated(OrgEntity organization, String ldapDN, String name) {
		return new SyncRelatedEntity(organization.getId(), organization.getOrgName(), organization.getOrgName(),
				ConstOrg.CLASS_TYPE, syncEntity.getId(), syncEntity.getName(), ldapDN, name, "",
				organization.getParentId(), syncEntity.getInstId());
	}

	public OrgEntity buildOrganization(HashMap<String, Attribute> attributeMap, String name, String nameInNamespace) {
		try {
			OrgEntity org = new OrgEntity();
			org.setLdapDn(nameInNamespace);
			String[] namePaths = name.replaceAll(",OU=", "/").replaceAll("OU=", "/").replaceAll(",ou=", "/")
					.replaceAll("ou=", "/").split("/");

			String namePah = "/" + rootOrganization.getOrgName();
			for (int i = namePaths.length - 1; i >= 0; i--) {
				namePah = namePah + "/" + namePaths[i];
			}

			namePah = namePah.substring(0, namePah.length() - 1);
			GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();

			org.setId(generatorStrategyContext.generate());
			org.setOrgCode(org.getId());
			org.setNamePath(namePah);
			org.setLevel(namePaths.length);
			org.setOrgName(LdapHelpers.getAttributeStringValue(OrganizationalUnit.OU, attributeMap));
			org.setFullName(org.getOrgName());
			org.setType("department");
			// org.setCountry(LdapUtils.getAttributeStringValue(OrganizationalUnit.CO,attributeMap));
			org.setRegion(LdapHelpers.getAttributeStringValue(OrganizationalUnit.ST, attributeMap));
			org.setLocality(LdapHelpers.getAttributeStringValue(OrganizationalUnit.L, attributeMap));
			org.setStreet(LdapHelpers.getAttributeStringValue(OrganizationalUnit.STREET, attributeMap));
			org.setPostalCode(LdapHelpers.getAttributeStringValue(OrganizationalUnit.POSTALCODE, attributeMap));
			org.setAddress(LdapHelpers.getAttributeStringValue(OrganizationalUnit.POSTALADDRESS, attributeMap));
			org.setPhone(LdapHelpers.getAttributeStringValue(OrganizationalUnit.TELEPHONENUMBER, attributeMap));
			org.setFax(LdapHelpers.getAttributeStringValue(OrganizationalUnit.FACSIMILETELEPHONENUMBER, attributeMap));
			org.setRemark(LdapHelpers.getAttributeStringValue(OrganizationalUnit.DESCRIPTION, attributeMap));
			org.setInstId(this.syncEntity.getInstId());
			org.setStatus(ConstStatus.ACTIVE);
			log.info("org " + org);
			return org;
		} catch (NamingException e) {
			log.error("NamingException ", e);
		}
		return null;
	}

	public LdapHelpers getLdapUtils() {
		return ldapUtils;
	}

	public void setLdapUtils(LdapHelpers ldapUtils) {
		this.ldapUtils = ldapUtils;
	}
}