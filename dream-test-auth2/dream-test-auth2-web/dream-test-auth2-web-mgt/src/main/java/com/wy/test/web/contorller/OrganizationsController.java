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
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstEntryType;
import com.wy.test.core.constants.ConstOperateAction;
import com.wy.test.core.constants.ConstOperateResult;
import com.wy.test.core.entity.ExcelImport;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.component.TreeAttributes;
import com.wy.test.core.web.component.TreeNode;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.OrganizationsService;

import dream.flying.flower.framework.core.excel.ExcelContentHelpers;

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
	public ResponseEntity<?> fetch(@ModelAttribute OrgEntity org, @CurrentUser UserEntity currentUser) {
		_logger.debug("fetch {}", org);
		org.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<OrgEntity>>(organizationsService.fetchPageResults(org)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute OrgEntity org, @CurrentUser UserEntity currentUser) {
		_logger.debug("-query  {}", org);
		org.setInstId(currentUser.getInstId());
		List<OrgEntity> orgList = organizationsService.query(org);
		if (orgList != null) {
			return new Message<List<OrgEntity>>(Message.SUCCESS, orgList).buildResponse();
		} else {
			return new Message<List<OrgEntity>>(Message.FAIL).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		OrgEntity org = organizationsService.get(id);
		return new Message<OrgEntity>(org).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody OrgEntity org, @CurrentUser UserEntity currentUser) {
		_logger.debug("-Add  :" + org);
		org.setInstId(currentUser.getInstId());
		if (organizationsService.insert(org)) {
			systemLog.insert(ConstEntryType.ORGANIZATION, org, ConstOperateAction.CREATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<OrgEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<OrgEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody OrgEntity org, @CurrentUser UserEntity currentUser) {
		_logger.debug("-update  :" + org);
		org.setInstId(currentUser.getInstId());
		if (organizationsService.update(org)) {
			systemLog.insert(ConstEntryType.ORGANIZATION, org, ConstOperateAction.UPDATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<OrgEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<OrgEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (organizationsService.deleteBatch(ids)) {
			systemLog.insert(ConstEntryType.ORGANIZATION, ids, ConstOperateAction.DELETE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<OrgEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<OrgEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/tree" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> tree(@ModelAttribute OrgEntity organization, @CurrentUser UserEntity currentUser) {
		_logger.debug("-query  {}", organization);
		organization.setInstId(currentUser.getInstId());
		List<OrgEntity> orgList = organizationsService.query(organization);
		if (orgList != null) {
			TreeAttributes treeAttributes = new TreeAttributes();
			int nodeCount = 0;
			for (OrgEntity org : orgList) {
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
			@CurrentUser UserEntity currentUser) {
		if (excelImportFile.isExcelNotEmpty()) {
			try {
				List<OrgEntity> orgsList = Lists.newArrayList();
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
						return new Message<OrgEntity>(Message.SUCCESS).buildResponse();
					} else {
						return new Message<OrgEntity>(Message.FAIL).buildResponse();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				excelImportFile.closeWorkbook();
			}
		}

		return new Message<OrgEntity>(Message.FAIL).buildResponse();

	}

	public OrgEntity buildOrganizationsFromSheetRow(Row row, UserEntity currentUser) {
		OrgEntity organization = new OrgEntity();
		// 上级编码
		organization.setParentId(ExcelContentHelpers.getValueString(row, 0));
		// 上级名称
		organization.setParentName(ExcelContentHelpers.getValueString(row, 1));
		// 组织编码
		organization.setId(ExcelContentHelpers.getValueString(row, 2));
		// 组织名称
		organization.setOrgName(ExcelContentHelpers.getValueString(row, 3));
		// 组织全称
		organization.setFullName(ExcelContentHelpers.getValueString(row, 4));
		// 编码路径
		organization.setCodePath(ExcelContentHelpers.getValueString(row, 5));
		// 名称路径
		organization.setNamePath(ExcelContentHelpers.getValueString(row, 6));
		// 组织类型
		organization.setType(ExcelContentHelpers.getValueString(row, 7));
		// 所属分支机构
		organization.setDivision(ExcelContentHelpers.getValueString(row, 8));
		// 级别
		String level = ExcelContentHelpers.getValueString(row, 9);
		organization.setLevel(level.equals("") ? 1 : Integer.parseInt(level));
		// 排序
		String sortIndex = ExcelContentHelpers.getValueString(row, 10);
		organization.setSortIndex(sortIndex.equals("") ? 1 : Integer.parseInt(sortIndex));
		// 联系人
		organization.setContact(ExcelContentHelpers.getValueString(row, 11));
		// 联系电话
		organization.setPhone(ExcelContentHelpers.getValueString(row, 12));
		// 邮箱
		organization.setEmail(ExcelContentHelpers.getValueString(row, 13));
		// 传真
		organization.setFax(ExcelContentHelpers.getValueString(row, 14));
		// 工作-国家
		organization.setCountry(ExcelContentHelpers.getValueString(row, 15));
		// 工作-省
		organization.setRegion(ExcelContentHelpers.getValueString(row, 16));
		// 工作-城市
		organization.setLocality(ExcelContentHelpers.getValueString(row, 17));
		// 工作-地址
		organization.setLocality(ExcelContentHelpers.getValueString(row, 18));
		// 邮编
		organization.setPostalCode(ExcelContentHelpers.getValueString(row, 19));
		// 详细描述
		organization.setDescription(ExcelContentHelpers.getValueString(row, 20));
		organization.setStatus(1);

		organization.setInstId(currentUser.getInstId());
		return organization;
	}
}