package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Map;

public class AppCompanyEnvWrapper implements Serializable{
	
	private static final long serialVersionUID = 1192359980875606937L;
	
	
//	@JsonSerialize(keyUsing = ApplicationSerializer.class)
	private Map<Long, ApplicationEnvUrlInfo> map=null;
	
	public AppCompanyEnvWrapper(Map<Long, ApplicationEnvUrlInfo> map2) {
		this.map=map2;
	}
	
	public Map<Long, ApplicationEnvUrlInfo> getMap() {
		return map;
	}
	public void setMap(Map<Long, ApplicationEnvUrlInfo> map) {
		this.map = map;
	}
	
	
}
