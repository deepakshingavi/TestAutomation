package com.dyteam.testApps.webserver.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationTestcaseInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2570881576060293887L;
	private Long testCaseCount;
	private String applicationName;

	public ApplicationTestcaseInfo(@JsonProperty("testCaseCount") Long testCaseCount,
			@JsonProperty("applicationName") String applicationName) {
		this.testCaseCount=testCaseCount;
		this.applicationName=applicationName;
	}
	
	public ApplicationTestcaseInfo() {
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	public Long getTestCaseCount() {
		return testCaseCount;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public void setTestCaseCount(Long testCaseCount) {
		this.testCaseCount = testCaseCount;
	}

	@Override
	public String toString() {
		return "ApplicationTestcaseInfo [testCaseCount=" + testCaseCount + ", applicationName=" + applicationName + "]";
	}
	
	
	
}
