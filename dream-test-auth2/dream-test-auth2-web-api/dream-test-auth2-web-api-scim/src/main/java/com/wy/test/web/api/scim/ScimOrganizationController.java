package com.wy.test.web.api.scim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.persistence.service.OrgService;
import com.wy.test.web.api.scim.resources.ScimMeta;
import com.wy.test.web.api.scim.resources.ScimOrganization;
import com.wy.test.web.api.scim.resources.ScimParameters;
import com.wy.test.web.api.scim.resources.ScimSearchResult;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class ScimOrganizationController {

	@Autowired
	OrgService orgService;

	@GetMapping(value = "/{id}")
	public MappingJacksonValue get(@PathVariable String id, @RequestParam(required = false) String attributes) {
		OrgEntity org = orgService.getById(id);
		ScimOrganization scimOrg = org2ScimOrg(org);

		return new MappingJacksonValue(scimOrg);
	}

	@PostMapping
	public MappingJacksonValue create(@RequestBody ScimOrganization scimOrg,
			@RequestParam(required = false) String attributes, UriComponentsBuilder builder) throws IOException {
		OrgEntity createOrg = scimOrg2Org(scimOrg);
		orgService.insert(createOrg);
		return get(createOrg.getId(), attributes);
	}

	@PutMapping(value = "/{id}")
	public MappingJacksonValue replace(@PathVariable String id, @RequestBody ScimOrganization scimOrg,
			@RequestParam(required = false) String attributes) throws IOException {
		OrgEntity updateOrg = scimOrg2Org(scimOrg);
		orgService.update(updateOrg);
		return get(id, attributes);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		orgService.removeById(id);
	}

	@GetMapping
	public MappingJacksonValue searchWithGet(ScimParameters requestParameters) {
		return searchWithPost(requestParameters);
	}

	@PostMapping(value = "/.search")
	public MappingJacksonValue searchWithPost(ScimParameters requestParameters) {
		requestParameters.parse();
		log.debug("requestParameters {} ", requestParameters);
		Page<OrgEntity> orgResults = orgService
				.page(new Page<OrgEntity>(requestParameters.getStartIndex(), requestParameters.getCount()), null);
		List<ScimOrganization> resultList = new ArrayList<ScimOrganization>();
		for (OrgEntity org : orgResults.getRecords()) {
			resultList.add(org2ScimOrg(org));
		}
		ScimSearchResult<ScimOrganization> scimSearchResult = new ScimSearchResult<ScimOrganization>(resultList,
				orgResults.getPages(), requestParameters.getCount(), requestParameters.getStartIndex());

		return new MappingJacksonValue(scimSearchResult);
	}

	public ScimOrganization org2ScimOrg(OrgEntity org) {
		ScimOrganization scimOrg = new ScimOrganization();
		scimOrg.setId(org.getId());
		scimOrg.setCode(org.getOrgCode());
		scimOrg.setName(org.getOrgName());
		scimOrg.setDisplayName(org.getOrgName());
		scimOrg.setFullName(org.getFullName());
		scimOrg.setType(org.getType());
		scimOrg.setLevel(org.getLevel());
		scimOrg.setDivision(org.getDivision());
		scimOrg.setSortOrder(org.getSortIndex() + "");
		scimOrg.setCodePath(org.getCodePath());
		scimOrg.setNamePath(org.getNamePath());
		scimOrg.setDescription(org.getRemark());

		scimOrg.setParentId(org.getParentId());
		scimOrg.setParent(org.getParentId());
		// scimOrg.setParentCode(org.getParentId());
		scimOrg.setParentName(org.getParentName());

		scimOrg.setParentName(org.getParentName());
		if (null != org.getSortIndex()) {
			scimOrg.setOrder(org.getSortIndex());
		} else {
			scimOrg.setOrder(1);
		}
		scimOrg.setExternalId(org.getId());

		ScimMeta meta = new ScimMeta("Organization");

		if (null != org.getCreateTime()) {
			meta.setCreated(org.getCreateTime());
		}
		if (null != org.getUpdateTime()) {
			meta.setLastModified(org.getUpdateTime());
		}
		scimOrg.setMeta(meta);
		return scimOrg;
	}

	public OrgEntity scimOrg2Org(ScimOrganization scimOrg) {
		OrgEntity org = new OrgEntity();
		org.setId(scimOrg.getId());
		org.setOrgCode(scimOrg.getCode());
		org.setFullName(scimOrg.getFullName());
		org.setOrgName(StrHelper.isNotBlank(scimOrg.getName()) ? scimOrg.getName() : scimOrg.getDisplayName());
		org.setParentId(StrHelper.isNotBlank(scimOrg.getParentId()) ? scimOrg.getParentId() : scimOrg.getParent());
		org.setParentCode(scimOrg.getParentCode());
		org.setParentName(scimOrg.getParentName());
		org.setSortIndex(StrHelper.isNotBlank(scimOrg.getSortOrder()) ? Integer.parseInt(scimOrg.getSortOrder())
				: (int) scimOrg.getOrder());
		org.setLevel(scimOrg.getLevel());
		org.setType(scimOrg.getType());
		org.setDivision(scimOrg.getDivision());
		org.setRemark(scimOrg.getDescription());
		return org;
	}
}