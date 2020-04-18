package com.dyteam.testApps.webserver.model;

public class AppUrlInfo {
	
	private String url;
	private String appName;
	public AppUrlInfo(String url, String appName) {
		super();
		this.url = url;
		this.appName = appName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	

}
