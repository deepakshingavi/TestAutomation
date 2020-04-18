package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;

import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.CompanyEnvironUrl;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddCompanyEvnUrl implements Serializable{

	private static final long serialVersionUID = -5136462584410367611L;
	
	private Application application;
	
	private List<CompanyEnvironUrl> companyEnvironUrls;
	
	public List<CompanyEnvironUrl> getCompanyEnvironUrls() {
		return companyEnvironUrls;
	}
	public void setCompanyEnvironUrls(List<CompanyEnvironUrl> companyEnvironUrls) {
		this.companyEnvironUrls = companyEnvironUrls;
	}
	public AddCompanyEvnUrl(@JsonProperty("companyEnvironUrls") List<CompanyEnvironUrl> companyEnvironUrls,
			@JsonProperty("application") Application application) {
		super();
		this.companyEnvironUrls = companyEnvironUrls;
		this.application = application;
	}
	
	public Application getApplication() {
		return application;
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}
	
	
	@Override
	public String toString() {
		return "AddCompanyEvnUrl [application=" + application + ", companyEnvironUrls=" + companyEnvironUrls + "]";
	}
	
	
}
