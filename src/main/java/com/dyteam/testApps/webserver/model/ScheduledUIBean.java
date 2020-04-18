package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;

import com.dyteam.testApps.webserver.entity.TestcaseExeDetail;

public class ScheduledUIBean implements Serializable{
	
	private static final long serialVersionUID = 1503851456727878916L;
	public String getTestcaseName() {
		return testcaseName;
	}
	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	private String testcaseName;
	private String envName;
	private String result;
	private Integer elapsedTime;
	
	private List<TestcaseExeDetail> testcaseExeDetailList;
	
	
	public ScheduledUIBean(String testcaseName, String envName, String result) {
		super();
		this.testcaseName = testcaseName;
		this.envName = envName;
		this.result = result;
	}
	
	public ScheduledUIBean() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((envName == null) ? 0 : envName.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + ((testcaseName == null) ? 0 : testcaseName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduledUIBean other = (ScheduledUIBean) obj;
		if (envName == null) {
			if (other.envName != null)
				return false;
		} else if (!envName.equals(other.envName))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (testcaseName == null) {
			if (other.testcaseName != null)
				return false;
		} else if (!testcaseName.equals(other.testcaseName))
			return false;
		return true;
	}

	public Integer getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Integer elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	public List<TestcaseExeDetail> getTestcaseExeDetailList() {
		return testcaseExeDetailList;
	}
	public void setTestcaseExeDetailList(List<TestcaseExeDetail> testcaseExeDetailList) {
		this.testcaseExeDetailList = testcaseExeDetailList;
	}
	
}
