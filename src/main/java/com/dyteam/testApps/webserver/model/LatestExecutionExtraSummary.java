package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatestExecutionExtraSummary implements Serializable{
	
	private static final long serialVersionUID = -1609265635832503906L;
	
	@JsonProperty("runId")
	private Long runId;
	
	@JsonProperty("runName")
	private String runName;
	
	@JsonProperty("envName")
	private String envName;
	
	@JsonProperty("browserName")
	private String browserName;
	
	@JsonProperty("executedBy")
	private String executedBy;
	
	@JsonProperty("executedByEmail")
	private String executedByEmail;
	
	@JsonProperty("executedOn")
	private Date executedOn;
	
	@JsonProperty("companyId")
	private Long companyId;
	
	@JsonProperty("passedTestCasesCount")
	private Long passedTestCasesCount=0l;
	
	@JsonProperty("failedTestCasesCount")
	private Long failedTestCasesCount=0l;
	
	@JsonProperty("queuedTestCasesCount")
	private Long queuedTestCasesCount=0l;
	
	public LatestExecutionExtraSummary() {
		
	}
	
	public LatestExecutionExtraSummary(Long runId, String runName, String envName, String browserName,
			String executedBy, String executedByEmail, Date executedOn,Long companyId) {
		super();
		this.runId = runId;
		this.runName = runName;
		this.envName = envName;
		this.browserName = browserName;
		this.executedBy = executedBy;
		this.executedByEmail = executedByEmail;
		this.executedOn = executedOn;
		this.companyId=companyId;
	}
	
	public LatestExecutionExtraSummary(Long runId, String runName, String envName, String browserName,
			String fName, String executedByEmail, Date executedOn,Long companyId,String lName) {
		super();
		this.runId = runId;
		this.runName = runName;
		this.envName = envName;
		this.browserName = browserName;
		this.executedBy = fName+" "+lName;
		this.executedByEmail = executedByEmail;
		this.executedOn = executedOn;
		this.companyId=companyId;
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
	
	public Long getPassedTestCasesCount() {
		return passedTestCasesCount;
	}
	public void setPassedTestCasesCount(Long passedTestCasesCount) {
		this.passedTestCasesCount = passedTestCasesCount;
	}
	public Long getFailedTestCasesCount() {
		return failedTestCasesCount;
	}
	public void setFailedTestCasesCount(Long failedTestCasesCount) {
		this.failedTestCasesCount = failedTestCasesCount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setQueuedTestCasesCount(Long queuedTestCasesCount) {
		this.queuedTestCasesCount = queuedTestCasesCount;
	}
	
	public Long getQueuedTestCasesCount() {
		return queuedTestCasesCount;
	}
	
	public Long getRunId() {
		return runId;
	}
	
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public Long getTestCasesExecutedCount() {
		return Math.addExact(Math.addExact(passedTestCasesCount, failedTestCasesCount),queuedTestCasesCount);
	}
	public void setTestCasesExecutedCount(Integer testCasesExecutedCount) {
//		this.testCasesExecutedCount = testCasesExecutedCount;
	}
	
	public Float getProgess() {
		return  new Float(Math.floorDiv(Math.multiplyExact(Math.addExact(passedTestCasesCount, failedTestCasesCount),100),getTestCasesExecutedCount()));
	}
	
	public void setProgess(Float progess) {
//		this.progess = progess;
	}

	@Override
	public String toString() {
		return "LatestExecutionSummary [runId=" + runId + ", runName=" + runName + ", executedBy=" + executedBy
				+ ", executedOn=" + executedOn + ", passedTestCasesCount=" + passedTestCasesCount
				+ ", failedTestCasesCount=" + failedTestCasesCount + ", queuedTestCasesCount=" + queuedTestCasesCount
				+ ", progress=" + getProgess()
				+ "]";
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getExecutedByEmail() {
		return executedByEmail;
	}

	public void setExecutedByEmail(String executedByEmail) {
		this.executedByEmail = executedByEmail;
	}
	
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	

}
