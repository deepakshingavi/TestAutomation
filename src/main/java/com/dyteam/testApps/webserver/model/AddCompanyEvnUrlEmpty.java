package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;

import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.Environment;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddCompanyEvnUrlEmpty implements Serializable{

	private static final long serialVersionUID = -6532989807660870311L;
	private Long userId;
	private List<Environment> environments;
	private List<Application> applications;

	public AddCompanyEvnUrlEmpty(@JsonProperty("userId") Long userId, 
			@JsonProperty("applications") List<Application> applications, 
			@JsonProperty("environments") List<Environment> environments) {
		this.userId=userId;
		this.environments=environments;
		this.applications=applications;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<Environment> environments) {
		this.environments = environments;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	
	

}
