package com.wy.test.persistence.service.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wy.test.core.convert.FileUploadConvert;
import com.wy.test.core.entity.FileUploadEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.FileUploadQuery;
import com.wy.test.core.vo.FileUploadVO;
import com.wy.test.persistence.mapper.FileUploadMapper;
import com.wy.test.persistence.service.FileUploadService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class FileUploadServiceImpl extends
		AbstractServiceImpl<FileUploadEntity, FileUploadVO, FileUploadQuery, FileUploadConvert, FileUploadMapper>
		implements FileUploadService {

	@Override
	public ResponseEntity<?> upload(HttpServletRequest request, HttpServletResponse response, FileUploadVO fileUpload,
			UserEntity currentUser) {
		log.debug("FileUpload");

		FileUploadEntity fileUploadEntity = baseConvert.convert(fileUpload);

		fileUploadEntity.setContentType(fileUpload.getUploadFile().getContentType());
		fileUploadEntity.setFileName(fileUpload.getUploadFile().getOriginalFilename());
		fileUploadEntity.setContentSize(fileUpload.getUploadFile().getSize());
		fileUploadEntity.setCreateUser(Long.parseLong(currentUser.getId()));
		if (null != fileUpload.getUploadFile() && !fileUpload.getUploadFile().isEmpty()) {
			try {
				fileUploadEntity.setUploaded(fileUpload.getUploadFile().getBytes());
				save(fileUploadEntity);
				log.trace("FileUpload SUCCESS");
			} catch (IOException e) {
				log.error("FileUpload IOException", e);
			}
		}
		return new Message<Object>(Message.SUCCESS, (Object) fileUpload.getId()).buildResponse();
	}
}