package com.wy.test.mgt.web.config.contorller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.LdapContextEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.persistence.ldap.ActiveDirectoryHelpers;
import com.wy.test.core.persistence.ldap.LdapHelpers;
import com.wy.test.persistence.service.LdapContextService;

@Controller
@RequestMapping(value = { "/config/ldapcontext" })
public class LdapContextController {

	final static Logger _logger = LoggerFactory.getLogger(LdapContextController.class);

	@Autowired
	private LdapContextService ldapContextService;

	@GetMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@CurrentUser UserEntity currentUser) {
		LdapContextEntity ldapContext = ldapContextService.getById(currentUser.getInstId());
		if (ldapContext != null && StringUtils.isNoneBlank(ldapContext.getCredentials())) {
			ldapContext.setCredentials(PasswordReciprocal.getInstance().decoder(ldapContext.getCredentials()));
		}
		return new Message<LdapContextEntity>(ldapContext).buildResponse();
	}

	@PostMapping(value = { "/update" })
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody LdapContextEntity ldapContext, @CurrentUser UserEntity currentUser,
			BindingResult result) {
		_logger.debug("update ldapContext : " + ldapContext);
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
			return new Message<LdapContextEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<LdapContextEntity>(Message.FAIL).buildResponse();
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
			return new Message<LdapContextEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<LdapContextEntity>(Message.FAIL).buildResponse();
		}
	}
}