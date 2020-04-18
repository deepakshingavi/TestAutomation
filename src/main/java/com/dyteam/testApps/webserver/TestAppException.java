package com.dyteam.testApps.webserver;

public class TestAppException extends Exception{
	
	private static final long serialVersionUID = 517992661454511751L;
	
	private Integer errorCode;
	private String errorMessage;
	
	public TestAppException(String errorMessage,int errorCode) {
		super(errorMessage);
		this.errorMessage=errorMessage;
		this.errorCode=errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}

}
