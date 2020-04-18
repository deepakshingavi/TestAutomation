package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class PerfUiBean implements Serializable{

	private static final long serialVersionUID = 5372416417329597534L;
	
	private String runnerName;
	private Long runnerId;
	private Collection<TestcaseRunInfo> testcaseRunInfoList;
	private Date startDate;
	private Date endDate;
	
	
	
	
	public String getRunnerName() {
		return runnerName;
	}
	public void setRunnerName(String runnerName) {
		this.runnerName = runnerName;
	}
	public Long getRunnerId() {
		return runnerId;
	}
	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setElapsedTime(Long elapsedTime) {
		
	}
	
	public Long getElapsedTime() {
		if(null!=getEndDate() && null!=getStartDate()) {
			return (getEndDate().getTime() - getStartDate().getTime())/1000l;
        }
		return 0l;
	}
	
	public PerfUiBean(String runnerName, Long runnerId, Collection<TestcaseRunInfo> testcaseRunInfoList, Date startDate, Date endDate) {
		super();
		this.runnerName = runnerName;
		this.runnerId = runnerId;
		this.testcaseRunInfoList = testcaseRunInfoList;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "PerfUiBean [runnerName=" + runnerName + ", runnerId=" + runnerId + ", testcaseRunInfoList=" + testcaseRunInfoList
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	public Collection<TestcaseRunInfo> getTestcaseRunInfoList() {
		return testcaseRunInfoList;
	}
	
	public void setTestcaseRunInfoList(Collection<TestcaseRunInfo> testcaseRunInfoList) {
		this.testcaseRunInfoList = testcaseRunInfoList;
	}
	
	
}
