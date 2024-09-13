package com.wy.test.authentication.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.FileUploadVO;
import com.wy.test.persistence.service.FileUploadService;

@Controller
public class FileUploadEndpoint {

	@Autowired
	FileUploadService fileUploadService;

	@PostMapping(value = { "/file/upload/" })
	@ResponseBody
	public ResponseEntity<?> upload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam FileUploadVO fileUpload, @CurrentUser UserEntity currentUser) {
		return fileUploadService.upload(request, response, fileUpload, currentUser);
	}
}