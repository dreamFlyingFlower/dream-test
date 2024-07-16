package com.wy.test.web.contorller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.wy.test.constants.ConstsEntryType;
import com.wy.test.constants.ConstsOperateAction;
import com.wy.test.constants.ConstsOperateResult;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.entity.ExcelImport;
import com.wy.test.entity.Message;
import com.wy.test.entity.Organizations;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.OrganizationsService;
import com.wy.test.util.ExcelUtils;
import com.wy.test.web.component.TreeAttributes;
import com.wy.test.web.component.TreeNode;

@Controller
@RequestMapping({ "/orgs" })
public class OrganizationsController {

	static final Logger _logger = LoggerFactory.getLogger(OrganizationsController.class);

	@Autowired
	OrganizationsService organizationsService;

	@Autowired
	HistorySystemLogsService systemLog;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute Organizations org, @CurrentUser UserInfo currentUser) {
		_logger.debug("fetch {}", org);
		org.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<Organizations>>(organizationsService.fetchPageResults(org)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute Organizations org, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  {}", org);
		org.setInstId(currentUser.getInstId());
		List<Organizations> orgList = organizationsService.query(org);
		if (orgList != null) {
			return new Message<List<Organizations>>(Message.SUCCESS, orgList).buildResponse();
		} else {
			return new Message<List<Organizations>>(Message.FAIL).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Organizations org = organizationsService.get(id);
		return new Message<Organizations>(org).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody Organizations org, @CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + org);
		org.setInstId(currentUser.getInstId());
		if (organizationsService.insert(org)) {
			systemLog.insert(ConstsEntryType.ORGANIZATION, org, ConstsOperateAction.CREATE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Organizations>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Organizations>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody Organizations org, @CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + org);
		org.setInstId(currentUser.getInstId());
		if (organizationsService.update(org)) {
			systemLog.insert(ConstsEntryType.ORGANIZATION, org, ConstsOperateAction.UPDATE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Organizations>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Organizations>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (organizationsService.deleteBatch(ids)) {
			systemLog.insert(ConstsEntryType.ORGANIZATION, ids, ConstsOperateAction.DELETE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Organizations>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Organizations>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/tree" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> tree(@ModelAttribute Organizations organization, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  {}", organization);
		organization.setInstId(currentUser.getInstId());
		List<Organizations> orgList = organizationsService.query(organization);
		if (orgList != null) {
			TreeAttributes treeAttributes = new TreeAttributes();
			int nodeCount = 0;
			for (Organizations org : orgList) {
				TreeNode treeNode = new TreeNode(org.getId(), org.getOrgName());
				treeNode.setCode(org.getOrgCode());
				treeNode.setCodePath(org.getCodePath());
				treeNode.setNamePath(org.getNamePath());
				treeNode.setParentKey(org.getParentId());
				treeNode.setParentTitle(org.getParentName());
				treeNode.setParentCode(org.getParentCode());
				treeNode.setAttrs(org);
				treeNode.setLeaf(true);
				treeAttributes.addNode(treeNode);
				nodeCount++;
				// root organization node,parentId is null or parentId = -1 or parentId = 0 or
				// id = instId or id = parentId
				if (org.getParentId() == null || org.getParentId().equalsIgnoreCase("0")
						|| org.getParentId().equalsIgnoreCase("-1")
						|| org.getId().equalsIgnoreCase(currentUser.getInstId())
						|| org.getId().equalsIgnoreCase(org.getParentId())) {
					treeNode.setExpanded(true);
					treeNode.setLeaf(false);
					treeAttributes.setRootNode(treeNode);
				}
			}
			treeAttributes.setNodeCount(nodeCount);
			return new Message<TreeAttributes>(Message.SUCCESS, treeAttributes).buildResponse();
		} else {
			return new Message<TreeAttributes>(Message.FAIL).buildResponse();
		}
	}

	@PostMapping(value = "/import")
	public ResponseEntity<?> importingOrganizations(@ModelAttribute("excelImportFile") ExcelImport excelImportFile,
			@CurrentUser UserInfo currentUser) {
		if (excelImportFile.isExcelNotEmpty()) {
			try {
				List<Organizations> orgsList = Lists.newArrayList();
				Workbook workbook = excelImportFile.biuldWorkbook();
				int sheetSize = workbook.getNumberOfSheets();
				// 遍历sheet页
				for (int i = 0; i < sheetSize; i++) {
					Sheet sheet = workbook.getSheetAt(i);
					int rowSize = sheet.getLastRowNum() + 1;
					for (int j = 1; j < rowSize; j++) {// 遍历行
						Row row = sheet.getRow(j);
						if (row == null || j < 3) {// 略过空行和前3行
							continue;
						} else {// 其他行是数据行
							orgsList.add(buildOrganizationsFromSheetRow(row, currentUser));
						}
					}
				}
				// 数据去重
				if (!CollectionUtils.isEmpty(orgsList)) {
					orgsList = orgsList.stream()
							.collect(Collectors.collectingAndThen(
									Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getId()))),
									ArrayList::new));
					if (organizationsService.insertBatch(orgsList)) {
						return new Message<Organizations>(Message.SUCCESS).buildResponse();
					} else {
						return new Message<Organizations>(Message.FAIL).buildResponse();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				excelImportFile.closeWorkbook();
			}
		}

		return new Message<Organizations>(Message.FAIL).buildResponse();

	}

	public Organizations buildOrganizationsFromSheetRow(Row row, UserInfo currentUser) {
		Organizations organization = new Organizations();
		// 上级编码
		organization.setParentId(ExcelUtils.getValue(row, 0));
		// 上级名称
		organization.setParentName(ExcelUtils.getValue(row, 1));
		// 组织编码
		organization.setId(ExcelUtils.getValue(row, 2));
		// 组织名称
		organization.setOrgName(ExcelUtils.getValue(row, 3));
		// 组织全称
		organization.setFullName(ExcelUtils.getValue(row, 4));
		// 编码路径
		organization.setCodePath(ExcelUtils.getValue(row, 5));
		// 名称路径
		organization.setNamePath(ExcelUtils.getValue(row, 6));
		// 组织类型
		organization.setType(ExcelUtils.getValue(row, 7));
		// 所属分支机构
		organization.setDivision(ExcelUtils.getValue(row, 8));
		// 级别
		String level = ExcelUtils.getValue(row, 9);
		organization.setLevel(level.equals("") ? 1 : Integer.parseInt(level));
		// 排序
		String sortIndex = ExcelUtils.getValue(row, 10);
		organization.setSortIndex(sortIndex.equals("") ? 1 : Integer.parseInt(sortIndex));
		// 联系人
		organization.setContact(ExcelUtils.getValue(row, 11));
		// 联系电话
		organization.setPhone(ExcelUtils.getValue(row, 12));
		// 邮箱
		organization.setEmail(ExcelUtils.getValue(row, 13));
		// 传真
		organization.setFax(ExcelUtils.getValue(row, 14));
		// 工作-国家
		organization.setCountry(ExcelUtils.getValue(row, 15));
		// 工作-省
		organization.setRegion(ExcelUtils.getValue(row, 16));
		// 工作-城市
		organization.setLocality(ExcelUtils.getValue(row, 17));
		// 工作-地址
		organization.setLocality(ExcelUtils.getValue(row, 18));
		// 邮编
		organization.setPostalCode(ExcelUtils.getValue(row, 19));
		// 详细描述
		organization.setDescription(ExcelUtils.getValue(row, 20));
		organization.setStatus(1);

		organization.setInstId(currentUser.getInstId());
		return organization;
	}
}
