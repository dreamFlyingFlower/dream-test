package com.wy.test.web.apis.identity.scim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.wy.test.common.util.DateUtils;
import com.wy.test.core.entity.Organizations;
import com.wy.test.persistence.service.OrganizationsService;
import com.wy.test.web.apis.identity.scim.resources.ScimMeta;
import com.wy.test.web.apis.identity.scim.resources.ScimOrganization;
import com.wy.test.web.apis.identity.scim.resources.ScimParameters;
import com.wy.test.web.apis.identity.scim.resources.ScimSearchResult;

import dream.flying.flower.lang.StrHelper;

/**
 * This Controller is used to manage Organization
 * <p>
 * http://tools.ietf.org/html/draft-ietf-scim-core-schema-00#section-6
 * <p>
 * it is based on the SCIM 2.0 API Specification:
 * <p>
 * http://tools.ietf.org/html/draft-ietf-scim-api-00#section-3
 */
@RestController
@RequestMapping(value = "/api/idm/SCIM/v2/Organizations")
public class ScimOrganizationController {

	final static Logger _logger = LoggerFactory.getLogger(ScimOrganizationController.class);

	@Autowired
	OrganizationsService organizationsService;

	@GetMapping(value = "/{id}")
	public MappingJacksonValue get(@PathVariable String id, @RequestParam(required = false) String attributes) {
		Organizations org = organizationsService.get(id);
		ScimOrganization scimOrg = org2ScimOrg(org);

		return new MappingJacksonValue(scimOrg);
	}

	@PostMapping
	public MappingJacksonValue create(@RequestBody ScimOrganization scimOrg,
			@RequestParam(required = false) String attributes, UriComponentsBuilder builder) throws IOException {
		Organizations createOrg = scimOrg2Org(scimOrg);
		organizationsService.insert(createOrg);
		return get(createOrg.getId(), attributes);
	}

	@PutMapping(value = "/{id}")
	public MappingJacksonValue replace(@PathVariable String id, @RequestBody ScimOrganization scimOrg,
			@RequestParam(required = false) String attributes) throws IOException {
		Organizations updateOrg = scimOrg2Org(scimOrg);
		organizationsService.update(updateOrg);
		return get(id, attributes);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		organizationsService.remove(id);
	}

	@GetMapping
	public MappingJacksonValue searchWithGet(@ModelAttribute ScimParameters requestParameters) {
		return searchWithPost(requestParameters);
	}

	@PostMapping(value = "/.search")
	public MappingJacksonValue searchWithPost(@ModelAttribute ScimParameters requestParameters) {
		requestParameters.parse();
		_logger.debug("requestParameters {} ", requestParameters);
		Organizations queryModel = new Organizations();
		queryModel.setPageSize(requestParameters.getCount());
		queryModel.calculate(requestParameters.getStartIndex());

		JpaPageResults<Organizations> orgResults = organizationsService.fetchPageResults(queryModel);
		List<ScimOrganization> resultList = new ArrayList<ScimOrganization>();
		for (Organizations org : orgResults.getRows()) {
			resultList.add(org2ScimOrg(org));
		}
		ScimSearchResult<ScimOrganization> scimSearchResult = new ScimSearchResult<ScimOrganization>(resultList,
				orgResults.getRecords(), queryModel.getPageSize(), requestParameters.getStartIndex());

		return new MappingJacksonValue(scimSearchResult);
	}

	public ScimOrganization org2ScimOrg(Organizations org) {
		ScimOrganization scimOrg = new ScimOrganization();
		scimOrg.setId(org.getId());
		scimOrg.setCode(org.getOrgCode());
		scimOrg.setName(org.getOrgName());
		scimOrg.setDisplayName(org.getOrgName());
		scimOrg.setFullName(org.getFullName());
		scimOrg.setType(org.getType());
		scimOrg.setLevel(org.getLevel());
		scimOrg.setDivision(org.getDivision());
		scimOrg.setSortOrder(org.getSortOrder());
		scimOrg.setCodePath(org.getCodePath());
		scimOrg.setNamePath(org.getNamePath());
		scimOrg.setDescription(org.getDescription());

		scimOrg.setParentId(org.getParentId());
		scimOrg.setParent(org.getParentId());
		// scimOrg.setParentCode(org.getParentId());
		scimOrg.setParentName(org.getParentName());

		scimOrg.setParentName(org.getParentName());
		if (StrHelper.isNotBlank(org.getSortOrder())) {
			scimOrg.setOrder(Long.parseLong(org.getSortOrder()));
		} else {
			scimOrg.setOrder(1);
		}
		scimOrg.setExternalId(org.getId());

		ScimMeta meta = new ScimMeta("Organization");

		if (StrHelper.isNotBlank(org.getCreatedDate())) {
			meta.setCreated(DateUtils.parse(org.getCreatedDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS));
		}
		if (StrHelper.isNotBlank(org.getModifiedDate())) {
			meta.setLastModified(DateUtils.parse(org.getModifiedDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS));
		}
		scimOrg.setMeta(meta);
		return scimOrg;
	}

	public Organizations scimOrg2Org(ScimOrganization scimOrg) {
		Organizations org = new Organizations();
		org.setId(scimOrg.getId());
		org.setOrgCode(scimOrg.getCode());
		org.setFullName(scimOrg.getFullName());
		org.setOrgName(StrHelper.isNotBlank(scimOrg.getName()) ? scimOrg.getName() : scimOrg.getDisplayName());
		org.setParentId(StrHelper.isNotBlank(scimOrg.getParentId()) ? scimOrg.getParentId() : scimOrg.getParent());
		org.setParentCode(scimOrg.getParentCode());
		org.setParentName(scimOrg.getParentName());
		org.setSortOrder(
				StrHelper.isNotBlank(scimOrg.getSortOrder()) ? scimOrg.getSortOrder() : scimOrg.getOrder() + "");
		org.setLevel(scimOrg.getLevel());
		org.setType(scimOrg.getType());
		org.setDivision(scimOrg.getDivision());
		org.setDescription(scimOrg.getDescription());
		return org;
	}
}
