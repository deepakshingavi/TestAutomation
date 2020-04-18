package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;

import com.dyteam.testApps.webserver.entity.CompanyEnvironUrl;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationEnvUrlInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long applicationId;
	private String applicationName;
	private List<CompanyEnvironUrl> companyEnvironUrlList;

	public ApplicationEnvUrlInfo(
			@JsonProperty("applicationName")String applicationName, 
			@JsonProperty("environmentList")List<CompanyEnvironUrl> companyEnvironUrlList) {
		this.applicationName=applicationName;
		this.companyEnvironUrlList=companyEnvironUrlList;
	}
	
	public ApplicationEnvUrlInfo(
			@JsonProperty("applicationId")Long applicationId, 
			@JsonProperty("applicationName")String applicationName,
			@JsonProperty("environmentList")List<CompanyEnvironUrl> companyEnvironUrlList) {
		this.applicationId=applicationId;
		this.applicationName=applicationName;
		this.companyEnvironUrlList=companyEnvironUrlList;
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	public List<CompanyEnvironUrl> getCompanyEnvironUrlList() {
		return companyEnvironUrlList;
	}

	public void add(CompanyEnvironUrl companyEnvironUrl) {
		companyEnvironUrlList.add(companyEnvironUrl);
	}
	
	public Long getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	
}
