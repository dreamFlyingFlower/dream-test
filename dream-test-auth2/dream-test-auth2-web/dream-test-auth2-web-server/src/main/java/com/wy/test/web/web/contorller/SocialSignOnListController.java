package com.wy.test.web.web.contorller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.common.entity.Message;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.SocialsAssociate;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.persistence.service.SocialsAssociatesService;

@Controller
@RequestMapping(value = { "/config/socialsignon" })
public class SocialSignOnListController {

	final static Logger _logger = LoggerFactory.getLogger(SocialSignOnListController.class);

	@Autowired
	protected SocialsAssociatesService socialsAssociatesService;

	@GetMapping(value = { "/fetch" })
	@ResponseBody
	public ResponseEntity<?> fetch(@CurrentUser UserInfo currentUser) {

		List<SocialsAssociate> listSocialsAssociate = socialsAssociatesService.queryByUser(currentUser);

		return new Message<List<SocialsAssociate>>(listSocialsAssociate).buildResponse();
	}

	@ResponseBody
	@GetMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (socialsAssociatesService.deleteBatch(ids)) {
			return new Message<Apps>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Apps>(Message.FAIL).buildResponse();
		}
	}
}