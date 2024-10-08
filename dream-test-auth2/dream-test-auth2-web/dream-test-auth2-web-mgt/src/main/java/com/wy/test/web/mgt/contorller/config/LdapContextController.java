package com.wy.test.web.mgt.contorller.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.LdapContextEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.ldap.ActiveDirectoryHelpers;
import com.wy.test.core.ldap.LdapHelpers;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.persistence.service.LdapContextService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/ldapcontext" })
@Slf4j
public class LdapContextController {

	@Autowired
	private LdapContextService ldapContextService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		LdapContextEntity ldapContext = ldapContextService.getById(currentUser.getInstId());
		if (ldapContext != null && StringUtils.isNoneBlank(ldapContext.getCredentials())) {
			ldapContext.setCredentials(PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()));
		}
		return new ResultResponse<LdapContextEntity>(ldapContext).buildResponse();
	}

	@PostMapping(value = { "/update" })
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody LdapContextEntity ldapContext, @CurrentUser UserEntity currentUser,
			BindingResult result) {
		log.debug("update ldapContext : " + ldapContext);
		ldapContext.setCredentials(PasswordReciprocal.getInstance().encode(ldapContext.getCredentials()));
		ldapContext.setInstId(currentUser.getInstId());
		boolean updateResult = false;
		if (StringUtils.isBlank(ldapContext.getId())) {
			ldapContext.setId(ldapContext.getInstId());
			updateResult = ldapContextService.save(ldapContext);
		} else {
			updateResult = ldapContextService.updateById(ldapContext);
		}
		if (updateResult) {
			return new ResultResponse<LdapContextEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<LdapContextEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@GetMapping(value = { "/test" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> test(@CurrentUser UserEntity currentUser) {
		LdapContextEntity ldapContext = ldapContextService.getById(currentUser.getInstId());
		if (ldapContext != null && StringUtils.isNoneBlank(ldapContext.getCredentials())) {
			ldapContext.setCredentials(PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()));
		}

		LdapHelpers ldapUtils = null;
		if (ldapContext.getProduct().equalsIgnoreCase(LdapHelpers.Product.ActiveDirectory)) {
			ldapUtils = new ActiveDirectoryHelpers(ldapContext.getProviderUrl(), ldapContext.getPrincipal(),
					ldapContext.getCredentials(), ldapContext.getBasedn(), ldapContext.getMsadDomain());
		} else if (ldapContext.getProduct().equalsIgnoreCase(LdapHelpers.Product.OpenLDAP)) {
			ldapUtils = new LdapHelpers(ldapContext.getProviderUrl(), ldapContext.getPrincipal(),
					ldapContext.getCredentials(), ldapContext.getBasedn());
		} else if (ldapContext.getProduct().equalsIgnoreCase(LdapHelpers.Product.StandardLDAP)) {
			ldapUtils = new LdapHelpers(ldapContext.getProviderUrl(), ldapContext.getPrincipal(),
					ldapContext.getCredentials(), ldapContext.getBasedn());
		}

		if (ldapUtils.openConnection() != null) {
			ldapUtils.close();
			return new ResultResponse<LdapContextEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<LdapContextEntity>(ResultResponse.FAIL).buildResponse();
		}
	}
}