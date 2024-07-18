package com.wy.test.web.permissions.contorller;

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstsEntryType;
import com.wy.test.core.constants.ConstsOperateAction;
import com.wy.test.core.constants.ConstsOperateResult;
import com.wy.test.core.entity.Resources;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.web.component.TreeAttributes;
import com.wy.test.core.web.component.TreeNode;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.ResourcesService;

@Controller
@RequestMapping(value = { "/permissions/resources" })
public class ResourcesController {

	final static Logger _logger = LoggerFactory.getLogger(ResourcesController.class);

	@Autowired
	ResourcesService resourcesService;

	@Autowired
	HistorySystemLogsService systemLog;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute Resources resource, @CurrentUser UserInfo currentUser) {
		_logger.debug("fetch {}", resource);
		resource.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<Resources>>(resourcesService.fetchPageResults(resource)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute Resources resource, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  {}", resource);
		resource.setInstId(currentUser.getInstId());
		List<Resources> resourceList = resourcesService.query(resource);
		if (resourceList != null) {
			return new Message<List<Resources>>(Message.SUCCESS, resourceList).buildResponse();
		} else {
			return new Message<List<Resources>>(Message.FAIL).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Resources resource = resourcesService.get(id);
		return new Message<Resources>(resource).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody Resources resource, @CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + resource);
		resource.setInstId(currentUser.getInstId());
		if (resourcesService.insert(resource)) {
			systemLog.insert(ConstsEntryType.RESOURCE, resource, ConstsOperateAction.CREATE,
					ConstsOperateResult.SUCCESS, currentUser);
			return new Message<Resources>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Resources>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody Resources resource, @CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + resource);
		resource.setInstId(currentUser.getInstId());
		if (resourcesService.update(resource)) {
			systemLog.insert(ConstsEntryType.RESOURCE, resource, ConstsOperateAction.UPDATE,
					ConstsOperateResult.SUCCESS, currentUser);
			return new Message<Resources>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Resources>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (resourcesService.deleteBatch(ids)) {
			systemLog.insert(ConstsEntryType.RESOURCE, ids, ConstsOperateAction.DELETE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Resources>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Resources>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/tree" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> tree(@ModelAttribute Resources resource, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  {}", resource);
		resource.setInstId(currentUser.getInstId());
		List<Resources> resourceList = resourcesService.query(resource);
		if (resourceList != null) {
			TreeAttributes treeAttributes = new TreeAttributes();
			int nodeCount = 0;
			for (Resources r : resourceList) {
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