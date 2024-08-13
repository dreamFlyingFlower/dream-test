package com.wy.test.web.apis.identity.rest;

import java.io.IOException;

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

import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.query.OrgQuery;
import com.wy.test.persistence.service.OrgService;

import dream.flying.flower.lang.StrHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/api/idm/Organization" })
@Slf4j
@AllArgsConstructor
public class RestOrganizationController {

	OrgService orgService;

	@GetMapping(value = "/{id}")
	public OrgEntity getUser(@PathVariable String id, @RequestParam(required = false) String attributes) {
		log.debug("Organizations id {} , attributes {}", id, attributes);
		OrgEntity org = orgService.getById(id);
		return org;
	}

	@PostMapping
	public OrgEntity create(@RequestBody OrgEntity org, @RequestParam(required = false) String attributes,
			UriComponentsBuilder builder) throws IOException {
		log.debug("Organizations content {} , attributes {}", org, attributes);
		OrgEntity loadOrg = orgService.getById(org.getId());
		if (loadOrg == null) {
			orgService.insert(org);
		} else {
			orgService.update(org);
		}
		return org;
	}

	@PutMapping(value = "/{id}")
	public OrgEntity replace(@PathVariable String id, @RequestBody OrgEntity org,
			@RequestParam(required = false) String attributes) throws IOException {
		log.debug("Organizations id {} , content {} , attributes {}", id, org, attributes);
		OrgEntity loadOrg = orgService.getById(id);
		if (loadOrg == null) {
			orgService.insert(org);
		} else {
			orgService.update(org);
		}

		return org;
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable final String id) {
		log.debug("Organizations id {} ", id);
		orgService.removeById(id);
	}

	@GetMapping(value = { "/.search" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute OrgQuery org) {
		if (StrHelper.isBlank(org.getInstId())) {
			org.setInstId("1");
		}
		return new Message<>(orgService.listPage(org)).buildResponse();
	}
}