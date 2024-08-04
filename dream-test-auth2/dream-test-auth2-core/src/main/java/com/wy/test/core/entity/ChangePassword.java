package com.wy.test.core.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword implements Serializable {

	private static final long serialVersionUID = -3819249984031363024L;

	private String id;

	private String userId;

	private String username;

	private String email;

	private String mobile;

	private String windowsAccount;

	private String employeeNumber;

	private String displayName;

	private String oldPassword;

	private String password;

	private String confirmPassword;

	private String decipherable;

	private String instId;

	private int passwordSetType;

	private Date passwordLastSetTime;

	public ChangePassword(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public ChangePassword(UserEntity userInfo) {
		this.setId(userInfo.getId());
		this.setUserId(userInfo.getId());
		this.setUsername(userInfo.getUsername());
		this.setWindowsAccount(userInfo.getWindowsAccount());
		this.setMobile(userInfo.getMobile());
		this.setEmail(userInfo.getEmail());
		this.setEmployeeNumber(userInfo.getEmployeeNumber());
		this.setDecipherable(userInfo.getDecipherable());
		this.setPassword(userInfo.getPassword());
		this.setInstId(userInfo.getInstId());
	}

	public void clearPassword() {
		this.password = "";
		this.decipherable = "";
	}
}