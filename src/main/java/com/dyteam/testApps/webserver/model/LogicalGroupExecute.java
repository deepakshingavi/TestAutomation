package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogicalGroupExecute implements Serializable{

	private static final long serialVersionUID = 5243192751721787485L;
	
	@JsonProperty("logicalGroupId")
	private List<Long> logicalGroupId;
	
	@JsonProperty("scheduledDate")
	private Date scheduledDate;
	
	@JsonProperty("name")
	@NotNull
	private String name;
	
	@JsonProperty("browserId")
	@NotNull
	private Long browserId;

	@JsonProperty("browserName")
	@NotNull
	private String browserName;
	
	public LogicalGroupExecute() {
	}
	
	public LogicalGroupExecute(List<Long> logicalGroupId) {
		this.logicalGroupId=logicalGroupId;
	}
	
	public List<Long> getLogicalGroupId() {
		return logicalGroupId;
	}
	public void setLogicalGroupId(List<Long> logicalGroupId) {
		this.logicalGroupId = logicalGroupId;
	}
	
	public Date getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getBrowserId() {
		return browserId;
	}
	
	public void setBrowserId(Long browserId) {
		this.browserId = browserId;
	}

	public String getBrowserName() {
		return this.browserName;
	}
	
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	
}
