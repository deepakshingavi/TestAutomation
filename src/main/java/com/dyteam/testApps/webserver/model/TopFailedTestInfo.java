package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.math.BigInteger;

public class TopFailedTestInfo implements Serializable{

	private static final long serialVersionUID = -1277377962176729244L;
	public String testName;
	public BigInteger failedCount;
	public TopFailedTestInfo(String testName, BigInteger failedCount) {
		super();
		this.testName = testName;
		this.failedCount = failedCount;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public BigInteger getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(BigInteger failedCount) {
		this.failedCount = failedCount;
	}
	
	
	
}
