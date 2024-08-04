package com.wy.test.persistence.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.wy.test.core.entity.FileUploadEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.FileUploadQuery;
import com.wy.test.core.vo.FileUploadVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 文件上传
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface FileUploadService extends BaseServices<FileUploadEntity, FileUploadVO, FileUploadQuery> {

	ResponseEntity<?> upload(HttpServletRequest request, HttpServletResponse response, FileUploadVO fileUpload,
			UserEntity currentUser);
}