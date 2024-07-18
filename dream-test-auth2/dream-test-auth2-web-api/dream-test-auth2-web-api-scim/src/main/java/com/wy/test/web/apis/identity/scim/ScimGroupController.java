package com.wy.test.web.apis.identity.scim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.wy.test.core.entity.Roles;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.service.RoleMemberService;
import com.wy.test.persistence.service.RolesService;
import com.wy.test.util.DateUtils;
import com.wy.test.util.StringUtils;
import com.wy.test.web.apis.identity.scim.resources.ScimGroup;
import com.wy.test.web.apis.identity.scim.resources.ScimMemberRef;
import com.wy.test.web.apis.identity.scim.resources.ScimMeta;
import com.wy.test.web.apis.identity.scim.resources.ScimParameters;
import com.wy.test.web.apis.identity.scim.resources.ScimSearchResult;

@RestController
@RequestMapping(value = "/api/idm/SCIM/v2/Groups")
public class ScimGroupController {

	final static Logger _logger = LoggerFactory.getLogger(ScimGroupController.class);

	@Autowired
	RolesService rolesService;

	@Autowired
	RoleMemberService roleMemberService;

	@GetMapping(value = "/{id}")
	public MappingJacksonValue get(@PathVariable String id, @RequestParam(required = false) String attributes) {
		Roles role = rolesService.get(id);
		ScimGroup scimGroup = role2ScimGroup(role);
		List<UserInfo> userList = roleMemberService.queryMemberByRoleId(id);
		if (userList != null && userList.size() > 0) {
			Set<ScimMemberRef> members = new HashSet<ScimMemberRef>();
			for (UserInfo user : userList) {
				members.add(new ScimMemberRef(user.getDisplayName(), user.getId()));
			}
			scimGroup.setMembers(members);
		}
		return new MappingJacksonValue(scimGroup);
	}

	@PostMapping
	public MappingJacksonValue create(@RequestBody ScimGroup scimGroup,
			@RequestParam(required = false) String attributes, UriComponentsBuilder builder) throws IOException {
		Roles role = scimGroup2Role(scimGroup);
		rolesService.insert(role);
		return get(role.getId(), attributes);
	}

	@PutMapping(value = "/{id}")
	public MappingJacksonValue replace(@PathVariable String id, @RequestBody ScimGroup scimGroup,
			@RequestParam(required = false) String attributes) throws IOException {
		Roles role = scimGroup2Role(scimGroup);
		rolesService.update(role);
		return get(role.getId(), attributes);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		rolesService.remove(id);
	}

	@GetMapping
	public MappingJacksonValue searchWithGet(@ModelAttribute ScimParameters requestParameters) {
		return searchWithPost(requestParameters);
	}

	@PostMapping(value = "/.search")
	public MappingJacksonValue searchWithPost(@ModelAttribute ScimParameters requestParameters) {
		requestParameters.parse();
		_logger.debug("requestParameters {} ", requestParameters);
		Roles queryModel = new Roles();
		queryModel.setPageSize(requestParameters.getCount());
		queryModel.calculate(requestParameters.getStartIndex());

		JpaPageResults<Roles> orgResults = rolesService.fetchPageResults(queryModel);
		List<ScimGroup> resultList = new ArrayList<ScimGroup>();
		for (Roles group : orgResults.getRows()) {
			resultList.add(role2ScimGroup(group));
		}
		ScimSearchResult<ScimGroup> scimSearchResult = new ScimSearchResult<ScimGroup>(resultList,
				orgResults.getRecords(), queryModel.getPageSize(), requestParameters.getStartIndex());
		return new MappingJacksonValue(scimSearchResult);
	}

	public ScimGroup role2ScimGroup(Roles group) {
		ScimGroup scimGroup = new ScimGroup();
		scimGroup.setId(group.getId());
		scimGroup.setExternalId(group.getId());
		scimGroup.setDisplayName(group.getRoleName());

		ScimMeta meta = new ScimMeta("Group");
		if (StringUtils.isNotBlank(group.getCreatedDate())) {
			meta.setCreated(DateUtils.parse(group.getCreatedDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS));
		}
		if (StringUtils.isNotBlank(group.getModifiedDate())) {
			meta.setLastModified(DateUtils.parse(group.getModifiedDate(), DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS));
		}
		scimGroup.setMeta(meta);

		return scimGroup;
	}

	public Roles scimGroup2Role(ScimGroup scimGroup) {
		Roles role = new Roles();
		role.setId(scimGroup.getId());
		role.setRoleName(scimGroup.getDisplayName());
		return role;
	}
}
