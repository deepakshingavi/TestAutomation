package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionResultRunnerInfo implements Serializable{
	
	private static final long serialVersionUID = 4994703558686429416L;
	private Long runnerId;
	private String runName;
	private String executedBy;
	private Date executedOn;
	private Long completionPercentage;
	private String result;
	
	public ExecutionResultRunnerInfo() {
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public Long getRunnerId() {
		return runnerId;
	}

	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}

	public Long getCompletionPercentage() {
		return completionPercentage;
	}

	public void setCompletionPercentage(Long completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	public ExecutionResultRunnerInfo(
			@JsonProperty("runnerId")Long runnerId,
			@JsonProperty("runName")String runName,
			@JsonProperty("executedBy")String executedBy,
			@JsonProperty("executedOn")Date executedOn, 
			@JsonProperty("completionPercentage")Long completionPercentage,
			@JsonProperty("result")String result) {
		super();
		this.runnerId = runnerId;
		this.runName = runName;
		this.executedBy = executedBy;
		this.executedOn = executedOn;
		this.completionPercentage = completionPercentage;
		this.result = result;
	}
	
	public ExecutionResultRunnerInfo(
			@JsonProperty("runnerId")Long runnerId,
			@JsonProperty("completionPercentage")Long completionPercentage,
			@JsonProperty("result")String result) {
		super();
		this.runnerId = runnerId;
		this.completionPercentage = completionPercentage;
		this.result = result;
	}

	@Override
	public String toString() {
		return "ExecutionResultRunnerInfo [runnerId=" + runnerId + ", runName=" + runName + ", executedBy=" + executedBy
				+ ", executedOn=" + executedOn + ", completionPercentage=" + completionPercentage + ", result=" + result
				+ "]";
	}

	
	
	
	
	
	

}
