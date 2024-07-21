package com.wy.test.common.entity;

import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class Message<T> {

	public final static int SUCCESS = 0; // 成功

	public final static int ERROR = 1; // 错误

	public final static int FAIL = 2; // 失败

	public final static int INFO = 101; // 信息

	public final static int PROMPT = 102; // 提示

	public final static int WARNING = 103; // 警告

	int code;

	String message;

	T data;

	public Message() {
		this.code = SUCCESS;
	}

	public Message(int code) {
		this.code = code;
	}

	public Message(T data) {
		this.data = data;
	}

	public Message(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Message(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public Message(int code, T data) {
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