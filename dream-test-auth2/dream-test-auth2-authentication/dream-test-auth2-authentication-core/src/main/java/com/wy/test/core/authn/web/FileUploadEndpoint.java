package com.wy.test.core.authn.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.FileUpload;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.service.FileUploadService;

@Controller
public class FileUploadEndpoint {

	private static Logger _logger = LoggerFactory.getLogger(FileUploadEndpoint.class);

	@Autowired
	FileUploadService fileUploadService;

	@PostMapping(value = { "/file/upload/" })
	@ResponseBody
	public ResponseEntity<?> upload(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute FileUpload fileUpload, @CurrentUser UserInfo currentUser) {
		_logger.debug("FileUpload");
		fileUpload.setId(fileUpload.generateId());
		fileUpload.setContentType(fileUpload.getUploadFile().getContentType());
		fileUpload.setFileName(fileUpload.getUploadFile().getOriginalFilename());
		fileUpload.setContentSize(fileUpload.getUploadFile().getSize());
		fileUpload.setCreatedBy(currentUser.getUsername());
		/*
		 * upload UploadFile MultipartFile to Uploaded Bytes
		 */
		if (null != fileUpload.getUploadFile() && !fileUpload.getUploadFile().isEmpty()) {
			try {
				fileUpload.setUploaded(fileUpload.getUploadFile().getBytes());
				fileUploadService.insert(fileUpload);
				_logger.trace("FileUpload SUCCESS");
			} catch (IOException e) {
				_logger.error("FileUpload IOException", e);
			}
		}
		return new Message<Object>(Message.SUCCESS, (Object) fileUpload.getId()).buildResponse();
	}

}
