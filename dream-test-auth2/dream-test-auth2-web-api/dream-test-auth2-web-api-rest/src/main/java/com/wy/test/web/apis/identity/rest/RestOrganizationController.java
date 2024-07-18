package com.wy.test.web.apis.identity.rest;

import java.io.IOException;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.wy.test.core.entity.Organizations;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.OrganizationsService;
import com.wy.test.util.StringUtils;

@RestController
@RequestMapping(value = { "/api/idm/Organization" })
public class RestOrganizationController {

	@Autowired
	OrganizationsService organizationsService;

	@GetMapping(value = "/{id}")
	public Organizations getUser(@PathVariable String id, @RequestParam(required = false) String attributes) {
		Organizations org = organizationsService.get(id);
		return org;
	}

	@PostMapping
	public Organizations create(@RequestBody Organizations org, @RequestParam(required = false) String attributes,
			UriComponentsBuilder builder) throws IOException {
		Organizations loadOrg = organizationsService.get(org.getId());
		if (loadOrg == null) {
			organizationsService.insert(org);
		} else {
			organizationsService.update(org);
		}
		return org;
	}

	@PutMapping(value = "/{id}")
	public Organizations replace(@PathVariable String id, @RequestBody Organizations org,
			@RequestParam(required = false) String attributes) throws IOException {
		Organizations loadOrg = organizationsService.get(id);
		if (loadOrg == null) {
			organizationsService.insert(org);
		} else {
			organizationsService.update(org);
		}

		return org;
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		organizationsService.remove(id);
	}

	@GetMapping(value = { "/.search" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute Organizations org) {
		if (StringUtils.isBlank(org.getInstId())) {
			org.setInstId("1");
		}
		return new Message<JpaPageResults<Organizations>>(organizationsService.fetchPageResults(org)).buildResponse();
	}
}