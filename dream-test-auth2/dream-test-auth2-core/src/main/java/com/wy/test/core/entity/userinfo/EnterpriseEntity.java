package com.wy.test.core.entity.userinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseEntity {

	private String employeeNumber;

	private String costCenter;

	private String organization;

	private String division;

	private String departmentId;

	private String department;

	private String title;

	private String managerId;

	private String manager;

	private String assistantId;

	private String assistant;
}