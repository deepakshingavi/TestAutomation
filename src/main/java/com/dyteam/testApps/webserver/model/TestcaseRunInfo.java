package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Date;

import com.dyteam.testApps.webserver.entity.Testcases;

public class TestcaseRunInfo implements Serializable{

	private static final long serialVersionUID = -2823732618622057737L;
	private Long testcasesId;
	private String testcaseName;
	private Date startDate;
	private Date endDate;

	public TestcaseRunInfo(Testcases tc, Date startDate, Date endDate) {
		this.testcasesId=tc.getTestcasesId();
		this.testcaseName=tc.getTestcaseName();
		this.startDate=startDate;
		this.endDate=endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setElapsedTime(Long elapsedTime) {

	}

	public Long getElapsedTime() {
		if (null != getEndDate() && null != getStartDate()) {
			return (getEndDate().getTime() - getStartDate().getTime()) / 1000l;
		}
		return 0l;
	}
	
	public String getTestcaseName() {
		return testcaseName;
	}
	
	public Long getTestcasesId() {
		return testcasesId;
	}
	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}
	public void setTestcasesId(Long testcasesId) {
		this.testcasesId = testcasesId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testcasesId == null) ? 0 : testcasesId.hashCode());
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
		TestcaseRunInfo other = (TestcaseRunInfo) obj;
		if (testcasesId == null) {
			if (other.testcasesId != null)
				return false;
		} else if (!testcasesId.equals(other.testcasesId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestcaseRunInfo [testcasesId=" + testcasesId + ", testcaseName=" + testcaseName + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}
	
	

}
