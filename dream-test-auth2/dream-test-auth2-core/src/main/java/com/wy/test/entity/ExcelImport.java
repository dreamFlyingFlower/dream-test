package com.wy.test.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExcelImport extends JpaEntity implements Serializable {

	private static final long serialVersionUID = 4665009770629818479L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@JsonIgnore
	protected MultipartFile excelFile;

	String updateExist;

	InputStream inputStream = null;

	Workbook workbook = null;

	public ExcelImport() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpdateExist() {
		return updateExist;
	}

	public void setUpdateExist(String updateExist) {
		this.updateExist = updateExist;
	}

	public MultipartFile getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(MultipartFile excelFile) {
		this.excelFile = excelFile;
	}

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
