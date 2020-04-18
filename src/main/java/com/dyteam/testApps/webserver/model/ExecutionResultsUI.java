package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.List;

import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.Testcases;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExecutionResultsUI implements Serializable{
	
	private static final long serialVersionUID = -6263758732819879321L;
	
	@JsonProperty("executionName")
	private String executionName;
	
	@JsonProperty("environmentId")
	private Long environmentId;
	
	@JsonProperty("browserId")
	private Long browserId;
	
	@JsonProperty("testcaseIds")
	private List<Long> testcaseIds;
	
	@JsonProperty("testcaseNames")
	private List<String> testcaseNames;
	
	@JsonProperty("applications")
	private List<Application> applications;
	
	@JsonProperty("browserName")
	private String browserName;
	
	@JsonProperty("environmentName")
	private String environmentName;
	
	@JsonProperty("executionUserId")
	private Long executionUserId;
	
	@JsonProperty("scheduled")
	private Boolean scheduled;

	@JsonProperty("testcases")
	private List<Testcases> testcases;

	public ExecutionResultsUI() {
		
	}
	
	public ExecutionResultsUI(String executionName, 
			Long environmentId, 
			Long browserId, 
			List<Long> testcaseIds,
			String browserName,
			String environmentName,
			List<Application> applications,
			List<String> testcaseNames,
			Long executionUserId) {
		super();
		this.executionName = executionName;
		this.environmentId = environmentId;
		this.browserId = browserId;
		this.testcaseIds = testcaseIds;
		this.browserName=browserName;
		this.environmentName=environmentName;
		this.testcaseNames=testcaseNames;
		this.executionUserId=executionUserId;
		this.applications=applications;
	}
	
	
	
	public ExecutionResultsUI(String executionName, 
			Long environmentId, 
			Long browserId, 
			List<Long> testcaseIds,
			String browserName,
			String environmentName,
			List<Application> applications,
			List<String> testcaseNames,
			Long executionUserId,
			Boolean scheduled
			) {
		super();
		this.executionName = executionName;
		this.environmentId = environmentId;
		this.browserId = browserId;
		this.testcaseIds = testcaseIds;
		this.browserName=browserName;
		this.environmentName=environmentName;
		this.applications=applications;
		this.testcaseNames=testcaseNames;
		this.executionUserId=executionUserId;
		this.scheduled=scheduled;
	}
	
	public String getExecutionName() {
		return executionName;
	}
	public void setExecutionName(String executionName) {
		this.executionName = executionName;
	}
	public Long getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(Long environmentId) {
		this.environmentId = environmentId;
	}
	public Long getBrowserId() {
		return browserId;
	}
	public void setBrowserId(Long browserId) {
		this.browserId = browserId;
	}
	
	public List<Long> getTestcaseIds() {
		return testcaseIds;
	}
	public void setTestcaseIds(List<Long> testcaseIds) {
		this.testcaseIds = testcaseIds;
	}
	
	public String getBrowserName() {
		return browserName;
	}
	
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	
	public List<Application> getApplications() {
		return applications;
	}
	
	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	
	public List<String> getTestcaseNames() {
		return testcaseNames;
	}
	public void setTestcaseNames(List<String> testcaseNames) {
		this.testcaseNames = testcaseNames;
	}
	public Long getExecutionUserId() {
		return executionUserId;
	}
	
	public void setExecutionUserId(Long executionUserId) {
		this.executionUserId = executionUserId;
	}
	
	public Boolean getScheduled() {
		return scheduled;
	}
	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	public void setTestcases(List<Testcases> testcases) {
		this.testcases=testcases;
	}
	
	public List<Testcases> getTestcases() {
		return testcases;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applications == null) ? 0 : applications.hashCode());
		result = prime * result + ((browserId == null) ? 0 : browserId.hashCode());
		result = prime * result + ((browserName == null) ? 0 : browserName.hashCode());
		result = prime * result + ((environmentId == null) ? 0 : environmentId.hashCode());
		result = prime * result + ((environmentName == null) ? 0 : environmentName.hashCode());
		result = prime * result + ((executionName == null) ? 0 : executionName.hashCode());
		result = prime * result + ((executionUserId == null) ? 0 : executionUserId.hashCode());
		result = prime * result + ((scheduled == null) ? 0 : scheduled.hashCode());
		result = prime * result + ((testcaseIds == null) ? 0 : testcaseIds.hashCode());
		result = prime * result + ((testcaseNames == null) ? 0 : testcaseNames.hashCode());
		result = prime * result + ((testcases == null) ? 0 : testcases.hashCode());
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
		ExecutionResultsUI other = (ExecutionResultsUI) obj;
		if (applications == null) {
			if (other.applications != null)
				return false;
		} else if (!applications.equals(other.applications))
			return false;
		if (browserId == null) {
			if (other.browserId != null)
				return false;
		} else if (!browserId.equals(other.browserId))
			return false;
		if (browserName == null) {
			if (other.browserName != null)
				return false;
		} else if (!browserName.equals(other.browserName))
			return false;
		if (environmentId == null) {
			if (other.environmentId != null)
				return false;
		} else if (!environmentId.equals(other.environmentId))
			return false;
		if (environmentName == null) {
			if (other.environmentName != null)
				return false;
		} else if (!environmentName.equals(other.environmentName))
			return false;
		if (executionName == null) {
			if (other.executionName != null)
				return false;
		} else if (!executionName.equals(other.executionName))
			return false;
		if (executionUserId == null) {
			if (other.executionUserId != null)
				return false;
		} else if (!executionUserId.equals(other.executionUserId))
			return false;
		if (scheduled == null) {
			if (other.scheduled != null)
				return false;
		} else if (!scheduled.equals(other.scheduled))
			return false;
		if (testcaseIds == null) {
			if (other.testcaseIds != null)
				return false;
		} else if (!testcaseIds.equals(other.testcaseIds))
			return false;
		if (testcaseNames == null) {
			if (other.testcaseNames != null)
				return false;
		} else if (!testcaseNames.equals(other.testcaseNames))
			return false;
		if (testcases == null) {
			if (other.testcases != null)
				return false;
		} else if (!testcases.equals(other.testcases))
			return false;
		return true;
	}
	
	
	


}
