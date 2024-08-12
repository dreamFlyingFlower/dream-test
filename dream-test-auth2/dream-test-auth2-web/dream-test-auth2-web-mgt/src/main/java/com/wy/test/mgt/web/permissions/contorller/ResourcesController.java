package com.wy.test.mgt.web.permissions.contorller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstEntryType;
import com.wy.test.core.constant.ConstOperateAction;
import com.wy.test.core.constant.ConstOperateResult;
import com.wy.test.core.convert.ResourceConvert;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.ResourceEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.ResourceVO;
import com.wy.test.core.web.component.TreeAttributes;
import com.wy.test.core.web.component.TreeNode;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.ResourceService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/permissions/resources" })
@Slf4j
public class ResourcesController {

	@Autowired
	ResourceService resourcesService;

	@Autowired
	ResourceConvert resourceConvert;

	@Autowired
	HistorySysLogService systemLog;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute ResourceEntity resource, @CurrentUser UserEntity currentUser) {
		log.debug("fetch {}", resource);
		resource.setInstId(currentUser.getInstId());
		return new Message<>(resourcesService.list(new LambdaQueryWrapper<>(resource))).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute ResourceEntity resource, @CurrentUser UserEntity currentUser) {
		log.debug("-query  {}", resource);
		resource.setInstId(currentUser.getInstId());
		List<ResourceEntity> resourceList = resourcesService.list(new LambdaQueryWrapper<>(resource));
		if (resourceList != null) {
			return new Message<List<ResourceEntity>>(Message.SUCCESS, resourceList).buildResponse();
		} else {
			return new Message<List<ResourceEntity>>(Message.FAIL).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		ResourceEntity resource = resourcesService.getById(id);
		return new Message<ResourceEntity>(resource).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody ResourceEntity resource, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + resource);
		resource.setInstId(currentUser.getInstId());
		if (resourcesService.save(resource)) {
			systemLog.insert(ConstEntryType.RESOURCE, resource, ConstOperateAction.CREATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<ResourceEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ResourceEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody ResourceEntity resource, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + resource);
		resource.setInstId(currentUser.getInstId());
		if (resourcesService.updateById(resource)) {
			systemLog.insert(ConstEntryType.RESOURCE, resource, ConstOperateAction.UPDATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<ResourceEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ResourceEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (resourcesService.removeByIds(Arrays.asList(ids.split(",")))) {
			systemLog.insert(ConstEntryType.RESOURCE, ids, ConstOperateAction.DELETE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<ResourceEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ResourceEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/tree" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> tree(@ModelAttribute ResourceVO resource, @CurrentUser UserEntity currentUser) {
		log.debug("-query  {}", resource);
		resource.setInstId(currentUser.getInstId());
		List<ResourceEntity> resourceList =
				resourcesService.list(new LambdaQueryWrapper<>(resourceConvert.convert(resource)));
		if (resourceList != null) {
			TreeAttributes treeAttributes = new TreeAttributes();
			int nodeCount = 0;
			for (ResourceEntity r : resourceList) {
				TreeNode treeNode = new TreeNode(r.getId(), r.getResourceName());
				treeNode.setParentKey(r.getParentId());
				treeNode.setParentTitle(r.getParentName());
				treeNode.setAttrs(r);
				treeNode.setLeaf(true);
				treeAttributes.addNode(treeNode);
				nodeCount++;
				if (r.getId().equalsIgnoreCase(currentUser.getInstId())) {
					treeNode.setExpanded(true);
					treeNode.setLeaf(false);
					treeAttributes.setRootNode(treeNode);
				}
			}

			TreeNode rootNode = new TreeNode(resource.getAppId(), resource.getAppName());
			rootNode.setParentKey(resource.getAppId());
			rootNode.setExpanded(true);
			rootNode.setLeaf(false);
			treeAttributes.setRootNode(rootNode);

			treeAttributes.setNodeCount(nodeCount);
			return new Message<TreeAttributes>(Message.SUCCESS, treeAttributes).buildResponse();
		} else {
			return new Message<TreeAttributes>(Message.FAIL).buildResponse();
		}
	}
}