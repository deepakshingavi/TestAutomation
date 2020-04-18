package com.dyteam.testApps.webserver.model;

import java.io.Serializable;

public class LogicalGroupView implements Serializable{

	private static final long serialVersionUID = -4182251691269261440L;
	private String applicationName;
	private String testCaseName;
	private String executionUserName;
	private String executionUserRole;
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	
	public LogicalGroupView() {
	}
	
	public LogicalGroupView(String applicationName, String testCaseName, String executionUserName,
			String executionUserRole) {
		super();
		this.applicationName = applicationName;
		this.testCaseName = testCaseName;
		this.executionUserName = executionUserName;
		this.executionUserRole = executionUserRole;
	}
	public String getExecutionUserName() {
		return executionUserName;
	}
	public String getExecutionUserRole() {
		return executionUserRole;
	}
	public void setExecutionUserName(String executionUserName) {
		this.executionUserName = executionUserName;
	}
	public void setExecutionUserRole(String executionUserRole) {
		this.executionUserRole = executionUserRole;
	}
	
}
