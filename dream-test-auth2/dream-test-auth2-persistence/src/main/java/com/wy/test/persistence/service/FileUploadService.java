package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.FileUpload;
import com.wy.test.persistence.mapper.FileUploadMapper;

@Repository
public class FileUploadService extends JpaService<FileUpload> {

	public FileUploadService() {
		super(FileUploadMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public FileUploadMapper getMapper() {
		return (FileUploadMapper) super.getMapper();
	}
}
