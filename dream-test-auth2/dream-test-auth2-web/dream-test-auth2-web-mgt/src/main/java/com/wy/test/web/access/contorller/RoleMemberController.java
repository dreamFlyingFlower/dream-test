package com.wy.test.web.access.contorller;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.RoleMemberEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.RoleMemberService;
import com.wy.test.persistence.service.RoleService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/access/rolemembers" })
@Slf4j
public class RoleMemberController {

	@Autowired
	RoleMemberService roleMemberService;

	@Autowired
	RoleService rolesService;

	@Autowired
	UserService userInfoService;

	@Autowired
	// HistorySystemLogsService systemLog;

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute RoleMemberEntity roleMember, @CurrentUser UserEntity currentUser) {
		log.debug("fetch " + roleMember);
		roleMember.setInstId(currentUser.getInstId());
		return new Message<>(roleMemberService.list(roleMember)).buildResponse();
	}

	@GetMapping(value = { "/memberInRole" })
	@ResponseBody
	public ResponseEntity<?> memberInRole(@ModelAttribute RoleMemberEntity roleMember,
			@CurrentUser UserEntity currentUser) {
		log.debug("roleMember : " + roleMember);
		roleMember.setInstId(currentUser.getInstId());
		return new Message<>(roleMemberService.fetchPageResults("memberInRole", roleMember)).buildResponse();
	}

	@GetMapping(value = { "/memberNotInRole" })
	@ResponseBody
	public ResponseEntity<?> memberNotInRole(@ModelAttribute RoleMemberEntity roleMember,
			@CurrentUser UserEntity currentUser) {
		roleMember.setInstId(currentUser.getInstId());
		return new Message<>(roleMemberService.fetchPageResults("memberNotInRole", roleMember)).buildResponse();
	}

	@GetMapping(value = { "/rolesNoMember" })
	@ResponseBody
	public ResponseEntity<?> rolesNoMember(@ModelAttribute RoleMemberEntity roleMember,
			@CurrentUser UserEntity currentUser) {
		roleMember.setInstId(currentUser.getInstId());
		return new Message<>(roleMemberService.rolesNoMember(roleMember)).buildResponse();
	}

	/**
	 * Members add to the Role
	 * 
	 * @param roleMember
	 * @param currentUser
	 * @return
	 */
	@PostMapping(value = { "/add" })
	@ResponseBody
	public ResponseEntity<?> addRoleMember(@RequestBody RoleMemberEntity roleMember,
			@CurrentUser UserEntity currentUser) {
		if (roleMember == null || roleMember.getRoleId() == null) {
			return new Message<RoleMemberEntity>(Message.FAIL).buildResponse();
		}
		String roleId = roleMember.getRoleId();

		boolean result = true;
		String memberIds = roleMember.getMemberId();
		String memberNames = roleMember.getMemberName();
		if (memberIds != null) {
			String[] arrMemberIds = memberIds.split(",");
			String[] arrMemberNames = memberNames.split(",");
			// set default as USER
			if (StrHelper.isBlank(roleMember.getType())) {
				roleMember.setType("USER");
			}
			for (int i = 0; i < arrMemberIds.length; i++) {
				RoleMemberEntity newRoleMember = new RoleMemberEntity(roleId, roleMember.getRoleName(), arrMemberIds[i],
						arrMemberNames[i], roleMember.getType(), currentUser.getInstId());
				newRoleMember.setId(WebContext.genId());
				result = roleMemberService.insert(newRoleMember);
			}
			if (result) {
				return new Message<RoleMemberEntity>(Message.SUCCESS).buildResponse();
			}
		}
		return new Message<RoleMemberEntity>(Message.FAIL).buildResponse();
	}

	/**
	 * Member add to Roles
	 * 
	 * @param roleMember
	 * @param currentUser
	 * @return
	 */
	@PostMapping(value = { "/addMember2Roles" })
	@ResponseBody
	public ResponseEntity<?> addMember2Roles(@RequestBody RoleMemberEntity roleMember,
			@CurrentUser UserEntity currentUser) {
		if (roleMember == null || StrHelper.isBlank(roleMember.getUsername())) {
			return new Message<RoleMemberEntity>(Message.FAIL).buildResponse();
		}
		UserEntity userInfo = userInfoService.findByUsername(roleMember.getUsername());

		boolean result = true;
		String roleIds = roleMember.getRoleId();
		String roleNames = roleMember.getRoleName();
		if (roleIds != null && userInfo != null) {
			String[] arrRoleIds = roleIds.split(",");
			String[] arrRoleNames = roleNames.split(",");

			for (int i = 0; i < arrRoleIds.length; i++) {
				RoleMemberEntity newRoleMember = new RoleMemberEntity(arrRoleIds[i], arrRoleNames[i], userInfo.getId(),
						userInfo.getDisplayName(), "USER", currentUser.getInstId());
				newRoleMember.setId(WebContext.genId());
				result = roleMemberService.insert(newRoleMember);
			}
			if (result) {
				return new Message<RoleMemberEntity>(Message.SUCCESS).buildResponse();
			}
		}
		return new Message<RoleMemberEntity>(Message.FAIL).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete ids : {}", ids);
		if (roleMemberService.deleteBatch(ids)) {
			return new Message<RoleMemberEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<RoleMemberEntity>(Message.FAIL).buildResponse();
		}
	}
}