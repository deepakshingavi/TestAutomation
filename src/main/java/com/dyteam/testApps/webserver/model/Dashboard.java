package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;

public class Dashboard implements Serializable{

	private static final long serialVersionUID = -5072562143032766088L;

	private List<ApplicationTestcaseInfo> appTestCaseInfoList;

	private LatestExecutionSummary latestExecutionSummary;

	public Dashboard() {
	}
	
	public Dashboard(List<ApplicationTestcaseInfo> appTestCaseInfoList, 
			LatestExecutionSummary latestExecutionSummary) {
		this.appTestCaseInfoList=appTestCaseInfoList;
		this.latestExecutionSummary=latestExecutionSummary;
	}

	public List<ApplicationTestcaseInfo> getAppTestCaseInfoList() {
		return appTestCaseInfoList;
	}

	public void setAppTestCaseInfoList(List<ApplicationTestcaseInfo> appTestCaseInfoList) {
		this.appTestCaseInfoList = appTestCaseInfoList;
	}

	public LatestExecutionSummary getLatestExecutionSummary() {
		return latestExecutionSummary;
	}

	public void setLatestExecutionSummary(LatestExecutionSummary latestExecutionSummary) {
		this.latestExecutionSummary = latestExecutionSummary;
	}

	@Override
	public String toString() {
		return "Dashboard [appTestCaseInfoList=" + appTestCaseInfoList + ", latestExecutionSummary="
				+ latestExecutionSummary + "]";
	}

	


}
