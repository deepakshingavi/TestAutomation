package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatestExecutionInfo implements Serializable{
	
	private static final long serialVersionUID = 4994703558686429416L;
	private String runName;
	private String executedBy;
	private Date executedOn;
	private Long testCasesExecutedCount;
	private String result;
	
	public LatestExecutionInfo() {
	}

	public String getRunName() {
		return runName;
	}

	public void setRunName(String runName) {
		this.runName = runName;
	}

	public String getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

	public Date getExecutedOn() {
		return executedOn;
	}

	public void setExecutedOn(Date executedOn) {
		this.executedOn = executedOn;
	}

	public Long getTestCasesExecutedCount() {
		return testCasesExecutedCount;
	}

	public void setTestCasesExecutedCount(Long testCasesExecutedCount) {
		this.testCasesExecutedCount = testCasesExecutedCount;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LatestExecutionInfo(
			@JsonProperty("runName")String runName,
			@JsonProperty("executedBy")String executedBy,
			@JsonProperty("executedOn")Date executedOn, 
			@JsonProperty("testCasesExecutedCount")Long testCasesExecutedCount,
			@JsonProperty("result")String result) {
		super();
		this.runName = runName;
		this.executedBy = executedBy;
		this.executedOn = executedOn;
		this.testCasesExecutedCount = testCasesExecutedCount;
		this.result = result;
	}

	@Override
	public String toString() {
		return "LatestExecutionInfo [runName=" + runName + ", executedBy=" + executedBy + ", executedOn=" + executedOn
				+ ", testCasesExecutedCount=" + testCasesExecutedCount + ", result=" + result + "]";
	}
	
	
	
	
	

}
