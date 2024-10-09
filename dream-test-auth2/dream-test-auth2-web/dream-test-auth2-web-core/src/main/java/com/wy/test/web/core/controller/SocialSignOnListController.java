package com.wy.test.web.core.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.SocialAssociateEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.SocialAssociateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "1-9 社交相关API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = { "/config/socialsignon" })
public class SocialSignOnListController {

	protected final SocialAssociateService socialsAssociatesService;

	@Operation(summary = "获取社交信息", description = "获取社交信息", method = "GET")
	@GetMapping(value = { "/fetch" })
	public ResponseEntity<?> fetch(@CurrentUser UserEntity currentUser) {
		List<SocialAssociateEntity> listSocialsAssociate = socialsAssociatesService.queryByUser(currentUser);
		return new ResultResponse<List<SocialAssociateEntity>>(listSocialsAssociate).buildResponse();
	}

	@Operation(summary = "删除社交信息", description = "删除社交信息", method = "GET")
	@GetMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (socialsAssociatesService.removeById(ids)) {
			return new ResultResponse<AppEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AppEntity>(ResultResponse.FAIL).buildResponse();
		}
	}
}