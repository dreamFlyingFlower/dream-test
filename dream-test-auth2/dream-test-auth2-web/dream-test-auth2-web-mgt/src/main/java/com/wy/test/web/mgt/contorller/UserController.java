package com.wy.test.web.mgt.contorller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstEntryType;
import com.wy.test.core.constant.ConstOperateAction;
import com.wy.test.core.constant.ConstOperateResult;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.ExcelImport;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.PasswordSetType;
import com.wy.test.core.query.UserQuery;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.FileUploadService;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.framework.core.excel.ExcelContentHelpers;
import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.helper.DateTimeHelper;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/users" })
@Slf4j
public class UserController {

	@Autowired
	private UserService userInfoService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	HistorySysLogService systemLog;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute UserQuery userInfo, @CurrentUser UserEntity currentUser) {
		log.debug("" + userInfo);
		userInfo.setInstId(currentUser.getInstId());
		return new Message<>(userInfoService.listPage(userInfo)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute UserEntity userInfo, @CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + userInfo);
		if (CollectionUtils.isNotEmpty(userInfoService.list(userInfo))) {
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		UserVO userInfo = userInfoService.getInfo(id);
		userInfo.trans();
		return new Message<>(userInfo).buildResponse();
	}

	@GetMapping(value = { "/getByUsername/{username}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getByUsername(@PathVariable("username") String username) {
		UserEntity userInfo = userInfoService.findByUsername(username);
		UserVO userVO = UserConvert.INSTANCE.convertt(userInfo);
		userVO.trans();
		return new Message<>(userVO).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody UserVO userInfo, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + userInfo);
		userInfo.setId(WebContext.genId());
		userInfo.setInstId(currentUser.getInstId());
		if (StrHelper.isNotBlank(userInfo.getPictureId())) {
			userInfo.setPicture(fileUploadService.getById(userInfo.getPictureId()).getUploaded());
			fileUploadService.removeById(userInfo.getPictureId());
		}
		if (null != userInfoService.add(userInfo)) {
			systemLog.insert(ConstEntryType.USERINFO, userInfo, ConstOperateAction.CREATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<UserEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody UserVO userInfo, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + userInfo);
		log.info(userInfo.getExtraAttributeName());
		log.info(userInfo.getExtraAttributeValue());
		// userInfo.setNameZHShortSpell(StringUtils.hanYu2Pinyin(userInfo.getDisplayName(),
		// true));
		// userInfo.setNameZHSpell(StringUtils.hanYu2Pinyin(userInfo.getDisplayName(),
		// false));
		convertExtraAttribute(userInfo);
		log.info(userInfo.getExtraAttribute());
		userInfo.setInstId(currentUser.getInstId());
		if (StrHelper.isNotBlank(userInfo.getPictureId())) {
			userInfo.setPicture(fileUploadService.getById(userInfo.getPictureId()).getUploaded());
			fileUploadService.removeById(userInfo.getPictureId());
		}
		if (userInfoService.edit(userInfo)) {
			systemLog.insert(ConstEntryType.USERINFO, userInfo, ConstOperateAction.UPDATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<UserEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);

		if (userInfoService.removeByIds(Arrays.asList(ids.split(",")))) {
			systemLog.insert(ConstEntryType.USERINFO, ids, ConstOperateAction.DELETE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<UserEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = "/randomPassword", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> randomPassword() {
		return new Message<Object>(Message.SUCCESS, (Object) userInfoService.randomPassword()).buildResponse();
	}

	protected void convertExtraAttribute(UserVO userInfo) {
		if (userInfo.getExtraAttributeValue() != null) {
			String[] extraAttributeLabel = userInfo.getExtraAttributeName().split(",");
			String[] extraAttributeValue = userInfo.getExtraAttributeValue().split(",");
			Map<String, String> extraAttributeMap = new HashMap<String, String>();
			for (int i = 0; i < extraAttributeLabel.length; i++) {
				extraAttributeMap.put(extraAttributeLabel[i], extraAttributeValue[i]);
			}
			String extraAttribute = JsonHelpers.toString(extraAttributeMap);
			userInfo.setExtraAttribute(extraAttribute);
		}
	}

	@ResponseBody
	@PostMapping(value = "/changePassword", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword,
			@CurrentUser UserEntity currentUser) {
		log.debug("UserId {}", changePassword.getUserId());
		changePassword.setPasswordSetType(PasswordSetType.PASSWORD_NORMAL.ordinal());
		if (userInfoService.changePassword(changePassword, true)) {
			systemLog.insert(ConstEntryType.USERINFO, changePassword, ConstOperateAction.CHANGE_PASSWORD,
					ConstOperateResult.SUCCESS, currentUser);
			return new Message<UserEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserEntity>(Message.FAIL).buildResponse();
		}
	}

	@PostMapping(value = { "/updateStatus" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> updateStatus(@ModelAttribute UserEntity userInfo, @CurrentUser UserEntity currentUser) {
		log.debug("" + userInfo);
		UserEntity loadUserInfo = userInfoService.getById(userInfo.getId());
		userInfo.setInstId(currentUser.getInstId());
		userInfo.setUsername(loadUserInfo.getUsername());
		userInfo.setDisplayName(loadUserInfo.getDisplayName());
		if (userInfoService.updateStatus(userInfo)) {
			systemLog.insert(ConstEntryType.USERINFO, userInfo,
					ConstOperateAction.statusActon.get(userInfo.getStatus()), ConstOperateResult.SUCCESS, currentUser);
			return new Message<UserEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<UserEntity>(Message.FAIL).buildResponse();
		}
	}

	@PostMapping(value = "/import")
	public ResponseEntity<?> importingUsers(@ModelAttribute("excelImportFile") ExcelImport excelImportFile,
			@CurrentUser UserEntity currentUser) {
		if (excelImportFile.isExcelNotEmpty()) {
			try {
				List<UserVO> userInfoList = Lists.newArrayList();
				Workbook workbook = excelImportFile.biuldWorkbook();
				int recordCount = 0;
				int sheetSize = workbook.getNumberOfSheets();
				for (int i = 0; i < sheetSize; i++) {// 遍历sheet页
					Sheet sheet = workbook.getSheetAt(i);
					int rowSize = sheet.getLastRowNum() + 1;
					for (int j = 1; j < rowSize; j++) {// 遍历行
						Row row = sheet.getRow(j);
						if (row == null || j < 3) {// 略过空行和前3行
							continue;
						} else {// 其他行是数据行
							UserVO userInfo = buildUserFromSheetRow(row, currentUser);
							userInfoList.add(userInfo);
							recordCount++;
							log.debug("record {} user {} account {}", recordCount, userInfo.getDisplayName(),
									userInfo.getUsername());
						}
					}
				}
				// 数据去重
				if (!CollectionUtils.isEmpty(userInfoList)) {
					userInfoList = userInfoList.stream()
							.collect(Collectors.collectingAndThen(
									Collectors.toCollection(
											() -> new TreeSet<>(Comparator.comparing(o -> o.getUsername()))),
									ArrayList::new));
					if (null != userInfoService.adds(userInfoList)) {
						return new Message<UserEntity>(Message.SUCCESS).buildResponse();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				excelImportFile.closeWorkbook();
			}
		}
		return new Message<UserEntity>(Message.FAIL).buildResponse();
	}

	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String value) {
				if (StrHelper.isEmpty(value)) {
					setValue(null);
				} else {
					setValue(value);
				}
			}

		});
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	public UserVO buildUserFromSheetRow(Row row, UserEntity currentUser) {
		UserVO userInfo = new UserVO();
		userInfo.setCreateTime(new Date());
		// 登录账号
		userInfo.setUsername(ExcelContentHelpers.getValueString(row, 0));
		// 密码
		userInfo.setPassword(ExcelContentHelpers.getValueString(row, 1));
		// 用户显示
		userInfo.setDisplayName(ExcelContentHelpers.getValueString(row, 2));
		// 姓
		userInfo.setFamilyName(ExcelContentHelpers.getValueString(row, 3));
		// 名
		userInfo.setGivenName(ExcelContentHelpers.getValueString(row, 4));
		// 中间名
		userInfo.setMiddleName(ExcelContentHelpers.getValueString(row, 5));
		// 昵称
		userInfo.setNickName(ExcelContentHelpers.getValueString(row, 6));
		// 性别
		String gender = ExcelContentHelpers.getValueString(row, 7);
		userInfo.setGender(gender.equals("") ? 1 : Integer.valueOf(gender));
		// 语言偏好
		userInfo.setPreferredLanguage(ExcelContentHelpers.getValueString(row, 8));
		// 时区
		userInfo.setTimeZone(ExcelContentHelpers.getValueString(row, 9));
		// 用户类型
		userInfo.setUserType(ExcelContentHelpers.getValueString(row, 10));
		// 员工编码
		userInfo.setEmployeeNumber(ExcelContentHelpers.getValueString(row, 11));
		// AD域账号
		userInfo.setWindowsAccount(ExcelContentHelpers.getValueString(row, 12));
		// 所属机构
		userInfo.setOrganization(ExcelContentHelpers.getValueString(row, 13));
		// 分支机构
		userInfo.setDivision(ExcelContentHelpers.getValueString(row, 14));
		// 部门编号
		userInfo.setDepartmentId(ExcelContentHelpers.getValueString(row, 15));
		// 部门名称
		userInfo.setDepartment(ExcelContentHelpers.getValueString(row, 16));
		// 成本中心
		userInfo.setCostCenter(ExcelContentHelpers.getValueString(row, 17));
		// 职位
		userInfo.setJobTitle(ExcelContentHelpers.getValueString(row, 18));
		// 级别
		userInfo.setJobLevel(ExcelContentHelpers.getValueString(row, 19));
		// 上级经理
		userInfo.setManager(ExcelContentHelpers.getValueString(row, 20));
		// 助理
		userInfo.setAssistant(ExcelContentHelpers.getValueString(row, 21));
		// 入职时间
		userInfo.setEntryDate(DateTimeHelper.parseDate(ExcelContentHelpers.getValueString(row, 22)));
		// 离职时间
		userInfo.setQuitDate(DateTimeHelper.parseDate(ExcelContentHelpers.getValueString(row, 23)));
		// 工作-国家
		userInfo.setWorkCountry(ExcelContentHelpers.getValueString(row, 24));
		// 工作-省
		userInfo.setWorkRegion(ExcelContentHelpers.getValueString(row, 25));
		// 工作-城市
		userInfo.setTimeZone(ExcelContentHelpers.getValueString(row, 26));
		// 工作-地址
		userInfo.setWorkLocality(ExcelContentHelpers.getValueString(row, 27));
		// 邮编
		userInfo.setWorkPostalCode(ExcelContentHelpers.getValueString(row, 28));
		// 传真
		userInfo.setWorkFax(ExcelContentHelpers.getValueString(row, 29));
		// 工作电话
		userInfo.setWorkPhoneNumber(ExcelContentHelpers.getValueString(row, 30));
		// 工作邮件
		userInfo.setWorkEmail(ExcelContentHelpers.getValueString(row, 31));
		// 证件类型 todo 现在数据库中存储的是tinyint
		// userInfo.setIdType(ExcelUtils.getValue(row, 32));
		// 证件号码
		userInfo.setIdCardNo(ExcelContentHelpers.getValueString(row, 33));
		// 出生日期
		userInfo.setBirthDate(DateTimeHelper.parseDate(ExcelContentHelpers.getValueString(row, 34)));
		// 婚姻状态 todo 现在数据字段类型是 tinyint
		// userInfo.setMarried(ExcelUtils.getValue(row, 35));
		// 开始工作时间
		userInfo.setStartWorkDate(DateTimeHelper.parseDate(ExcelContentHelpers.getValueString(row, 36)));
		// 个人主页
		userInfo.setWebSite(ExcelContentHelpers.getValueString(row, 37));
		// 即时通讯
		userInfo.setDefineIm(ExcelContentHelpers.getValueString(row, 38));
		// 国家
		userInfo.setHomeCountry(ExcelContentHelpers.getValueString(row, 39));
		// 省
		userInfo.setHomeRegion(ExcelContentHelpers.getValueString(row, 40));
		// 城市
		userInfo.setHomeLocality(ExcelContentHelpers.getValueString(row, 41));
		// 家庭地址
		userInfo.setHomeStreetAddress(ExcelContentHelpers.getValueString(row, 42));
		// 家庭邮编
		userInfo.setHomePostalCode(ExcelContentHelpers.getValueString(row, 43));
		// 家庭传真
		userInfo.setHomeFax(ExcelContentHelpers.getValueString(row, 44));
		// 家庭电话
		userInfo.setHomePhoneNumber(ExcelContentHelpers.getValueString(row, 45));
		// 家庭邮箱
		userInfo.setHomeEmail(ExcelContentHelpers.getValueString(row, 46));
		userInfoService.passwordEncoder(userInfo);
		userInfo.setStatus(1);
		userInfo.setInstId(currentUser.getInstId());
		return userInfo;
	}
}