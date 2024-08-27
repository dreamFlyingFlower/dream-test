package com.wy.test.web.api.scim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.RoleMemberService;
import com.wy.test.persistence.service.RoleService;
import com.wy.test.web.api.scim.resources.ScimGroup;
import com.wy.test.web.api.scim.resources.ScimMemberRef;
import com.wy.test.web.api.scim.resources.ScimMeta;
import com.wy.test.web.api.scim.resources.ScimParameters;
import com.wy.test.web.api.scim.resources.ScimSearchResult;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api/idm/SCIM/v2/Groups")
public class ScimGroupController {

	@Autowired
	RoleService roleService;

	@Autowired
	RoleMemberService roleMemberService;

	@GetMapping(value = "/{id}")
	public MappingJacksonValue get(@PathVariable String id, @RequestParam(required = false) String attributes) {
		RoleEntity role = roleService.getById(id);
		ScimGroup scimGroup = role2ScimGroup(role);
		List<UserEntity> userList = roleMemberService.queryMemberByRoleId(id);
		if (userList != null && userList.size() > 0) {
			Set<ScimMemberRef> members = new HashSet<ScimMemberRef>();
			for (UserEntity user : userList) {
				members.add(new ScimMemberRef(user.getDisplayName(), user.getId()));
			}
			scimGroup.setMembers(members);
		}
		return new MappingJacksonValue(scimGroup);
	}

	@PostMapping
	public MappingJacksonValue create(@RequestBody ScimGroup scimGroup,
			@RequestParam(required = false) String attributes, UriComponentsBuilder builder) throws IOException {
		RoleEntity role = scimGroup2Role(scimGroup);
		roleService.save(role);
		return get(role.getId(), attributes);
	}

	@PutMapping(value = "/{id}")
	public MappingJacksonValue replace(@PathVariable String id, @RequestBody ScimGroup scimGroup,
			@RequestParam(required = false) String attributes) throws IOException {
		RoleEntity role = scimGroup2Role(scimGroup);
		roleService.updateById(role);
		return get(role.getId(), attributes);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		roleService.removeById(id);
	}

	@GetMapping
	public MappingJacksonValue searchWithGet(ScimParameters requestParameters) {
		return searchWithPost(requestParameters);
	}

	@PostMapping(value = "/.search")
	public MappingJacksonValue searchWithPost(ScimParameters requestParameters) {
		requestParameters.parse();
		log.debug("requestParameters {} ", requestParameters);

		Page<RoleEntity> orgResults = roleService
				.page(new Page<RoleEntity>(requestParameters.getStartIndex(), requestParameters.getCount()), null);

		List<ScimGroup> resultList = new ArrayList<ScimGroup>();
		for (RoleEntity group : orgResults.getRecords()) {
			resultList.add(role2ScimGroup(group));
		}
		ScimSearchResult<ScimGroup> scimSearchResult = new ScimSearchResult<ScimGroup>(resultList,
				orgResults.getPages(), requestParameters.getCount(), requestParameters.getStartIndex());
		return new MappingJacksonValue(scimSearchResult);
	}

	public ScimGroup role2ScimGroup(RoleEntity group) {
		ScimGroup scimGroup = new ScimGroup();
		scimGroup.setId(group.getId());
		scimGroup.setExternalId(group.getId());
		scimGroup.setDisplayName(group.getRoleName());

		ScimMeta meta = new ScimMeta("Group");
		if (null != group.getCreateTime()) {
			meta.setCreated(group.getCreateTime());
		}
		if (null != group.getUpdateTime()) {
			meta.setLastModified(group.getUpdateTime());
		}
		scimGroup.setMeta(meta);
		return scimGroup;
	}

	public RoleEntity scimGroup2Role(ScimGroup scimGroup) {
		RoleEntity role = new RoleEntity();
		role.setId(scimGroup.getId());
		role.setRoleName(scimGroup.getDisplayName());
		return role;
	}
}