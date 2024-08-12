package com.wy.test.web.core.contorller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.SocialAssociateService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/socialsignon" })
@Slf4j
public class SocialSignOnListController {

	@Autowired
	protected SocialAssociateService socialsAssociatesService;

	@GetMapping(value = { "/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@CurrentUser UserEntity currentUser) {

		List<SocialAssociateEntity> listSocialsAssociate = socialsAssociatesService.queryByUser(currentUser);

		return new Message<List<SocialAssociateEntity>>(listSocialsAssociate).buildResponse();
	}

	@ResponseBody
	@GetMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (socialsAssociatesService.removeById(ids)) {
			return new Message<AppEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppEntity>(Message.FAIL).buildResponse();
		}
	}
}