package com.wy.test.web.api.scim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.RoleService;
import com.wy.test.persistence.service.UserService;
import com.wy.test.web.api.scim.resources.ScimEnterprise;
import com.wy.test.web.api.scim.resources.ScimFormattedName;
import com.wy.test.web.api.scim.resources.ScimGroupRef;
import com.wy.test.web.api.scim.resources.ScimManager;
import com.wy.test.web.api.scim.resources.ScimMeta;
import com.wy.test.web.api.scim.resources.ScimOrganizationEmail.UserEmailType;
import com.wy.test.web.api.scim.resources.ScimOrganizationPhoneNumber.UserPhoneNumberType;
import com.wy.test.web.api.scim.resources.ScimParameters;
import com.wy.test.web.api.scim.resources.ScimSearchResult;
import com.wy.test.web.api.scim.resources.ScimUser;
import com.wy.test.web.api.scim.resources.ScimUserEmail;
import com.wy.test.web.api.scim.resources.ScimUserPhoneNumber;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * This Controller is used to manage User
 * <p>
 * http://tools.ietf.org/html/draft-ietf-scim-core-schema-00#section-6
 * <p>
 * it is based on the SCIM 2.0 API Specification:
 * <p>
 * http://tools.ietf.org/html/draft-ietf-scim-api-00#section-3
 */
@RestController
@RequestMapping(value = "/api/idm/SCIM/v2/Users")
@Slf4j
public class ScimUserController {

	@Autowired
	private UserService userService;

	@Autowired
	RoleService roleService;

	@GetMapping(value = "/{id}")
	public MappingJacksonValue get(@PathVariable String id, @RequestParam(required = false) String attributes) {
		UserEntity userInfo = userService.getById(id);
		ScimUser scimUser = userInfo2ScimUser(userInfo);
		return new MappingJacksonValue(scimUser);
	}

	@PostMapping
	public MappingJacksonValue create(@RequestBody ScimUser user, @RequestParam(required = false) String attributes,
			UriComponentsBuilder builder) throws IOException {
		UserEntity userInfo = scimUser2UserInfo(user);
		userService.insert(userInfo);
		return get(userInfo.getId(), attributes);
	}

	@PutMapping(value = "/{id}")
	public MappingJacksonValue replace(@PathVariable String id, @RequestBody ScimUser user,
			@RequestParam(required = false) String attributes) throws IOException {
		UserEntity userInfo = scimUser2UserInfo(user);
		userService.update(userInfo);
		return get(id, attributes);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		userService.removeById(id);
	}

	@GetMapping
	public MappingJacksonValue searchWithGet(@ModelAttribute ScimParameters requestParameters) {
		return searchWithPost(requestParameters);
	}

	@PostMapping(value = "/.search")
	public MappingJacksonValue searchWithPost(@ModelAttribute ScimParameters requestParameters) {
		requestParameters.parse();
		log.debug("requestParameters {} ", requestParameters);
		Page<UserEntity> orgResults = userService
				.page(new Page<UserEntity>(requestParameters.getStartIndex(), requestParameters.getCount()), null);
		List<ScimUser> resultList = new ArrayList<ScimUser>();
		for (UserEntity user : orgResults.getRecords()) {
			resultList.add(userInfo2ScimUser(user));
		}
		ScimSearchResult<ScimUser> scimSearchResult = new ScimSearchResult<>(resultList, orgResults.getPages(),
				requestParameters.getCount(), requestParameters.getStartIndex());
		return new MappingJacksonValue(scimSearchResult);
	}

	public ScimUser userInfo2ScimUser(UserEntity userInfo) {
		ScimUser scimUser = new ScimUser();
		scimUser.setId(userInfo.getId());
		scimUser.setExternalId(userInfo.getId());
		scimUser.setDisplayName(userInfo.getDisplayName());
		scimUser.setUserName(userInfo.getUsername());
		scimUser.setName(
				new ScimFormattedName(userInfo.getFormattedName(), userInfo.getFamilyName(), userInfo.getGivenName(),
						userInfo.getMiddleName(), userInfo.getHonorificPrefix(), userInfo.getHonorificSuffix()));
		scimUser.setNickName(userInfo.getNickName());
		scimUser.setTitle(userInfo.getJobTitle());
		scimUser.setUserType(userInfo.getUserType());

		ScimEnterprise enterprise = new ScimEnterprise();
		enterprise.setDepartmentId(userInfo.getDepartmentId());
		enterprise.setDepartment(userInfo.getDepartment());
		enterprise.setCostCenter(userInfo.getCostCenter());
		enterprise.setManager(new ScimManager(userInfo.getManagerId(), userInfo.getManager()));
		enterprise.setDivision(userInfo.getDivision());
		enterprise.setEmployeeNumber(userInfo.getEmployeeNumber());
		scimUser.setEnterprise(enterprise);

		List<String> organizationsList = new ArrayList<String>();
		organizationsList.add(userInfo.getDepartmentId());
		scimUser.setOrganization(organizationsList);

		List<String> groupsList = new ArrayList<String>();
		List<ScimGroupRef> groups = new ArrayList<ScimGroupRef>();
		for (RoleEntity role : roleService.queryRolesByUserId(userInfo.getId())) {
			groupsList.add(role.getId());
			groups.add(new ScimGroupRef(role.getId(), role.getRoleName()));

		}
		scimUser.setGroup(groupsList);
		scimUser.setGroups(groups);

		scimUser.setTimezone(userInfo.getTimeZone());
		scimUser.setLocale(userInfo.getLocale());
		scimUser.setPreferredLanguage(userInfo.getPreferredLanguage());
		scimUser.setActive(userInfo.getStatus() == ConstStatus.ACTIVE);

		List<ScimUserEmail> emails = new ArrayList<ScimUserEmail>();
		if (StrHelper.isNotBlank(userInfo.getEmail())) {
			emails.add(new ScimUserEmail(userInfo.getEmail(), UserEmailType.OTHER, true));
		}
		if (StrHelper.isNotBlank(userInfo.getWorkEmail())) {
			emails.add(new ScimUserEmail(userInfo.getEmail(), UserEmailType.WORK, false));
		}
		if (StrHelper.isNotBlank(userInfo.getHomeEmail())) {
			emails.add(new ScimUserEmail(userInfo.getEmail(), UserEmailType.HOME, false));
		}

		if (emails.size() > 0) {
			scimUser.setEmails(emails);
		}

		List<ScimUserPhoneNumber> phoneNumbers = new ArrayList<ScimUserPhoneNumber>();
		if (StrHelper.isNotBlank(userInfo.getMobile())) {
			phoneNumbers.add(new ScimUserPhoneNumber(userInfo.getMobile(), UserPhoneNumberType.MOBILE, true));
		}
		if (StrHelper.isNotBlank(userInfo.getWorkPhoneNumber())) {
			phoneNumbers.add(new ScimUserPhoneNumber(userInfo.getWorkPhoneNumber(), UserPhoneNumberType.WORK, false));
		}

		if (StrHelper.isNotBlank(userInfo.getHomePhoneNumber())) {
			phoneNumbers.add(new ScimUserPhoneNumber(userInfo.getHomePhoneNumber(), UserPhoneNumberType.HOME, false));
		}

		if (phoneNumbers.size() > 0) {
			scimUser.setPhoneNumbers(phoneNumbers);
		}

		ScimMeta meta = new ScimMeta("User");
		if (null != userInfo.getCreateTime()) {
			meta.setCreated(userInfo.getCreateTime());
		}
		if (null != userInfo.getUpdateTime()) {
			meta.setLastModified(userInfo.getUpdateTime());
		}
		scimUser.setMeta(meta);
		return scimUser;
	}

	public UserEntity scimUser2UserInfo(ScimUser scimUser) {
		UserEntity userInfo = new UserEntity();
		userInfo.setId(scimUser.getId());
		userInfo.setUsername(scimUser.getUserName());
		return userInfo;
	}
}