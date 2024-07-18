package com.wy.test.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MXK_FILE_UPLOAD")
public class FileUpload extends JpaEntity implements Serializable {

	private static final long serialVersionUID = -4338400992411166457L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	byte[] uploaded;

	@JsonIgnore
	MultipartFile uploadFile;

	@Column
	String fileName;

	@Column
	String contentType;

	@Column
	long contentSize;

	@Column
	String createdBy;

	String createdDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getUploaded() {
		return uploaded;
	}

	public void setUploaded(byte[] uploaded) {
		this.uploaded = uploaded;
	}

	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getContentSize() {
		return contentSize;
	}

	public void setContentSize(long contentSize) {
		this.contentSize = contentSize;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileUpload [id=");
		builder.append(id);
		builder.append(", uploadFile=");
		builder.append(uploadFile);
		builder.append(", createdBy=");
		builder.append(this.createdBy);
		builder.append("]");
		return builder.toString();
	}

}
