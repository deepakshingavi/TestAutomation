package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.Company;

public class CompanyAppMap implements Serializable{

	private static final long serialVersionUID = -5446739781038330474L;

	private Iterable<Company> companies;
	
	private Map<Long,List<Application>> map;
	
	public CompanyAppMap() {
	}
	
	
	public CompanyAppMap(Iterable<Company> companies, Map<Long, List<Application>> map) {
		super();
		this.companies = companies;
		this.map = map;
	}
	
	public Map<Long, List<Application>> getMap() {
		return map;
	}
	
	public Iterable<Company> getCompanies() {
		return companies;
	}
	public void setCompanies(Iterable<Company> companies) {
		this.companies = companies;
	}
	public void setMap(Map<Long, List<Application>> map) {
		this.map = map;
	}
	
}
