package com.wy.test.core.base;

import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class ResultResponse<T> {

	public final static int SUCCESS = 0; // 成功

	public final static int ERROR = 1; // 错误

	public final static int FAIL = 2; // 失败

	public final static int INFO = 101; // 信息

	public final static int PROMPT = 102; // 提示

	public final static int WARNING = 103; // 警告

	int code;

	String message;

	T data;

	public ResultResponse() {
		this.code = SUCCESS;
	}

	public ResultResponse(int code) {
		this.code = code;
	}

	public ResultResponse(T data) {
		this.data = data;
	}

	public ResultResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResultResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public ResultResponse(int code, T data) {
		this.code = code;
		this.data = data;
	}

	public void setMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseEntity<?> buildResponse() {
		return ResponseEntity.ok(this);
	}
}