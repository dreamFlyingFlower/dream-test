package dream.flying.flower.framework.web.easyexcel;

import java.util.Date;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import dream.flying.flower.enums.DateEnum;
import dream.flying.flower.helper.DateHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * 日期转换
 *
 * @author 飞花梦影
 * @date 2023-08-08 09:28:29
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class DateConverter implements Converter<Date> {

	@Override
	public Class<Date> supportJavaTypeKey() {
		return Date.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Date convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		String dateString = cellData.getStringValue();
		return StrHelper.isBlank(dateString) ? null : DateHelper.parse(dateString, DateEnum.DATETIME);
	}

	@Override
	public WriteCellData<Date> convertToExcelData(Date value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return new WriteCellData<>(DateHelper.formatDateTime(value));
	}
}