package dream.flying.flower.framework.web.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;

import dream.flying.flower.framework.web.easyexcel.ExcelDataListener;
import dream.flying.flower.framework.web.easyexcel.ExcelFinishCallBack;
import dream.flying.flower.helper.UrlHelper;
import dream.flying.flower.lang.StrHelper;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Excel 工具类 {@link <a href="https://easyexcel.opensource.alibaba.com/"></a>}
 *
 * @author 飞花梦影
 * @date 2023-08-08 17:21:07
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class EasyExcelHelpers {

	/**
	 * 读取excel文件
	 *
	 * @param <T> 数据类型
	 * @param multipartFile excel文件
	 * @param head 列名
	 * @param callBack 回调 导入时传入定义好的回调接口,excel数据解析完毕之后监听器将数据传入回调函数
	 *        这样调用工具类时可以通过回调函数获取导入的数据,如果数据量过大可根据实际情况进行分配入库
	 */
	public static <T> void readAnalysis(MultipartFile multipartFile, Class<T> head, ExcelFinishCallBack<T> callBack) {
		try {
			EasyExcel.read(multipartFile.getInputStream(), head, new ExcelDataListener<>(callBack)).sheet().doRead();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取excel文件
	 *
	 * @param <T> 数据类型
	 * @param inputStream 文件流
	 * @param head 列名
	 * @param callBack 回调 导入时传入定义好的回调接口,excel数据解析完毕之后监听器将数据传入回调函数
	 *        这样调用工具类时可以通过回调函数获取导入的数据,如果数据量过大可根据实际情况进行分配入库
	 */
	public static <T> void readAnalysis(InputStream inputStream, Class<T> head, ExcelFinishCallBack<T> callBack) {
		EasyExcel.read(inputStream, head, new ExcelDataListener<>(callBack)).sheet().doRead();
	}

	/**
	 * 读取excel文件
	 *
	 * @param <T> 数据类型
	 * @param file excel文件
	 * @param head 列名
	 * @param callBack 回调 导入时传入定义好的回调接口，excel数据解析完毕之后监听器将数据传入回调函数
	 *        这样调用工具类时可以通过回调函数获取导入的数据，如果数据量过大可根据实际情况进行分配入库
	 */
	public static <T> void readAnalysis(File file, Class<T> head, ExcelFinishCallBack<T> callBack) {
		try {
			EasyExcel.read(new FileInputStream(file), head, new ExcelDataListener<>(callBack)).sheet().doRead();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取excel文件 同步
	 *
	 * @param <T> 数据类型
	 * @param file 文件
	 * @param clazz 模板类
	 * @return java.util.List list
	 */
	public static <T> List<T> readSync(File file, Class<T> clazz) {
		return readSync(file, clazz, 1, 0, ExcelTypeEnum.XLSX);
	}

	/**
	 * 读取excel文件 同步
	 *
	 * @param <T> 数据类型
	 * @param file 文件
	 * @param clazz 模板类
	 * @param rowNum 数据开始行 1
	 * @param sheetNo 第几张表
	 * @param excelType 数据表格式类型
	 * @return java.util.List list
	 */
	public static <T> List<T> readSync(File file, Class<T> clazz, Integer rowNum, Integer sheetNo,
			ExcelTypeEnum excelType) {
		return EasyExcel.read(file).headRowNumber(rowNum).excelType(excelType).head(clazz).sheet(sheetNo).doReadSync();
	}

	/**
	 * 导出数据到文件
	 *
	 * @param <T> 数据类型
	 * @param head 类名
	 * @param file 导入到文件
	 * @param data 数据
	 */
	public static <T> void excelExport(Class<T> head, File file, List<T> data) {
		excelExport(head, file, "sheet1", data);
	}

	/**
	 * 导出数据到文件
	 *
	 * @param <T> 写入格式
	 * @param head 类名
	 * @param file 写入到文件
	 * @param sheetName sheet名称
	 * @param data 数据列表
	 */
	public static <T> void excelExport(Class<T> head, File file, String sheetName, List<T> data) {
		try {
			EasyExcel.write(file, head).sheet(sheetName).doWrite(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 导出数据到web 文件下载（失败了会返回一个有部分数据的Excel）
	 *
	 * @param head 类名
	 * @param excelName excel名字
	 * @param sheetName sheet名称
	 * @param data 数据
	 */
	public static <T> void excelExport(Class<T> head, String excelName, String sheetName, List<T> data) {
		try {
			HttpServletResponse response = WebHelpers.getResponse();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setCharacterEncoding("utf-8");
			String fileName = UrlHelper.encode(excelName).replaceAll("\\+", "%20");
			response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
			EasyExcel.write(response.getOutputStream(), head).sheet(StrHelper.isBlank(sheetName) ? "sheet1" : sheetName)
					.doWrite(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解析字典数据到字段上 比如 T中有 genderLabel字段 为男 需要给 gender 字段自动设置为0
	 *
	 * @param dataList 需要被反向解析的数据
	 */
	// @SneakyThrows
	// public static <T extends TransPojo> void parseDict(List<T> dataList) {
	// // 没有数据就不需要初始化
	// if (ListTool.isEmpty(dataList)) {
	// return;
	// }
	// Class<? extends TransPojo> clazz = dataList.get(0).getClass();
	// // 拿到所有需要反向翻译的字段
	// List<Field> fields = ReflectTool.getFieldsListByAnnotation(clazz,
	// Trans.class);
	// // 过滤出字典翻译
	// fields = fields.stream().filter(field ->
	// TransType.DICTIONARY.equals(field.getAnnotation(Trans.class).type()))
	// .collect(Collectors.toList());
	// DictionaryTransService dictionaryTransService =
	// SpringContextHelper.getBean(DictionaryTransService.class);
	// for (T data : dataList) {
	// for (Field field : fields) {
	// Trans trans = field.getAnnotation(Trans.class);
	// // key不能为空并且ref不为空的才自动处理
	// if (StrTool.isNotBlank(trans.key(), trans.ref())) {
	// Field ref = ReflectTool.getField(clazz, trans.ref());
	// ref.setAccessible(true);
	// // 获取字典反向值
	// String value =
	// dictionaryTransService.getDictionaryTransMap().get(trans.key() + "_" +
	// ref.get(data));
	// if (StrTool.isBlank(value)) {
	// continue;
	// }
	// // 一般目标字段是int或者string字段 后面有添加单独抽离方法
	// if (Integer.class.equals(field.getType())) {
	// field.setAccessible(true);
	// field.set(data, Convert.toInt(value));
	// } else {
	// field.setAccessible(true);
	// field.set(data, Convert.toStr(value));
	// }
	// }
	// }
	// }
	// }

	public static void setMaxlength(XSSFWorkbook workbook, String typeName, int maxLength, int col) {
		// 设置类型为文本
		XSSFSheet sheet = workbook.getSheet(typeName);
		DataValidationHelper dvHelper = new XSSFDataValidationHelper(workbook.getSheet(typeName));
		XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createNumericConstraint(
				DataValidationConstraint.ValidationType.TEXT_LENGTH,
				DataValidationConstraint.OperatorType.LESS_OR_EQUAL, maxLength + "", maxLength + "");
		CellRangeAddressList addressList = new CellRangeAddressList(1, 65535, col, col);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
		validation.createErrorBox("错误提示", "长度不能超过" + maxLength);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
	}
}