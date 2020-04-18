package com.dyteam.testApps.webserver.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationCompanyUrl implements Serializable{

	private static final long serialVersionUID = 9195405415066603783L;
	private Long companyEnvironUrlId;
	private String envUrl;
	private String environmentName;
	private Long environmentId;
	private Long applicationId;
	private String applicationName;
	
	public ApplicationCompanyUrl() {
	}
	
	public ApplicationCompanyUrl(@JsonProperty("companyEnvironUrlId")Long companyEnvironUrlId, 
			@JsonProperty("envUrl")String envUrl, 
			@JsonProperty("environmentId")Long environmentId,
			@JsonProperty("environmentName")String environmentName,
			@JsonProperty("applicationId")Long applicationId,
			@JsonProperty("applicationName")String applicationName) {
		super();
		this.companyEnvironUrlId = companyEnvironUrlId;
		this.envUrl = envUrl;
		this.environmentId = environmentId;
		this.environmentName = environmentName;
		this.applicationId = applicationId;
		this.applicationName = applicationName;
	}
	public Long getCompanyEnvironUrlId() {
		return companyEnvironUrlId;
	}
	public void setCompanyEnvironUrlId(Long companyEnvironUrlId) {
		this.companyEnvironUrlId = companyEnvironUrlId;
	}
	public String getEnvUrl() {
		return envUrl;
	}
	public void setEnvUrl(String envUrl) {
		this.envUrl = envUrl;
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public Long getEnvironmentId() {
		return environmentId;
	}
	
	public void setEnvironmentId(Long environmentId) {
		this.environmentId = environmentId;
	}

	@Override
	public String toString() {
		return "ApplicationCompanyUrl [companyEnvironUrlId=" + companyEnvironUrlId + ", envUrl=" + envUrl
				+ ", environmentName=" + environmentName + ", environmentId=" + environmentId + ", applicationId="
				+ applicationId + ", applicationName=" + applicationName + "]";
	}
	
}
