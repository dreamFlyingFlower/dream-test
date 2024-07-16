package com.wy.test.web.apis.identity.scim.resources;

import java.io.Serializable;

public class ScimEnterprise implements Serializable {

	private static final long serialVersionUID = -204619629148409697L;

	private String employeeNumber;

	private String costCenter;

	private String organization;

	private String division;

	private String departmentId;

	private String department;

	private ScimManager manager;

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public ScimManager getManager() {
		return manager;
	}

	public void setManager(ScimManager manager) {
		this.manager = manager;
	}

	public ScimEnterprise() {
	}

}
