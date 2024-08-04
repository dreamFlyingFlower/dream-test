package com.wy.test.core.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImport implements Serializable {

	private static final long serialVersionUID = 4665009770629818479L;

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	@JsonIgnore
	protected MultipartFile excelFile;

	private String updateExist;

	private InputStream inputStream;

	private Workbook workbook;

	public boolean isExcelNotEmpty() {
		return excelFile != null && !excelFile.isEmpty();
	}

	public Workbook biuldWorkbook() throws IOException {
		workbook = null;
		inputStream = excelFile.getInputStream();
		if (excelFile.getOriginalFilename().toLowerCase().endsWith(".xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else if (excelFile.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else {
			throw new RuntimeException("Excel suffix error.");
		}
		return workbook;
	}

	public void closeWorkbook() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (workbook != null) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}