package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Date;
import java.lang.*; 

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatestExecutionSummary implements Serializable{
	
	private static final long serialVersionUID = -1609265635832503906L;
	
	private Long runId;
	private String runName;
	private String executedBy;
	private Date executedOn;
	private Long passedTestCasesCount=0l;
	private Long failedTestCasesCount=0l;
	private Long queuedTestCasesCount=0l;
	
	public LatestExecutionSummary() {
	}
	
	public LatestExecutionSummary(
			@JsonProperty("runName")String runName, 
			@JsonProperty("executedBy")String executedBy, 
			@JsonProperty("executedOn")Date executedOn, 
			@JsonProperty("testCasesExecutedCount")Integer testCasesExecutedCount,
			@JsonProperty("passedTestCasesCount")Long passedTestCasesCount, 
			@JsonProperty("failedTestCasesCount")Long failedTestCasesCount,
			@JsonProperty("queuedTestCasesCount")Long queuedTestCasesCount) {
		super();
		this.runName = runName;
		this.executedBy = executedBy;
		this.executedOn = executedOn;
		this.passedTestCasesCount = passedTestCasesCount;
		this.failedTestCasesCount = failedTestCasesCount;
		this.queuedTestCasesCount=queuedTestCasesCount;
	}
	
	public LatestExecutionSummary(
			@JsonProperty("runId")Long runId, 
			@JsonProperty("runName")String runName, 
			@JsonProperty("executedBy")String executedBy, 
			@JsonProperty("executedOn")Date executedOn, 
			@JsonProperty("passedTestCasesCount")Long passedTestCasesCount, 
			@JsonProperty("failedTestCasesCount")Long failedTestCasesCount,
			@JsonProperty("queuedTestCasesCount")Long queuedTestCasesCount) {
		super();
		this.runId = runId;
		this.runName = runName;
		this.executedBy = executedBy;
		this.executedOn = executedOn;
		this.passedTestCasesCount = passedTestCasesCount;
		this.failedTestCasesCount = failedTestCasesCount;
		this.queuedTestCasesCount=queuedTestCasesCount;
	}
	
	public LatestExecutionSummary(
			@JsonProperty("runId")Long runId, 
			@JsonProperty("passedTestCasesCount")Long passedTestCasesCount, 
			@JsonProperty("failedTestCasesCount")Long failedTestCasesCount,
			@JsonProperty("queuedTestCasesCount")Long queuedTestCasesCount) {
		super();
		this.runId = runId;
		this.passedTestCasesCount = passedTestCasesCount;
		this.failedTestCasesCount = failedTestCasesCount;
		this.queuedTestCasesCount=queuedTestCasesCount;
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
	
	

}
