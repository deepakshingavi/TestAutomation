package com.dyteam.testApps.webserver.model;

import java.io.Serializable;
import java.util.Date;

import com.dyteam.testApps.webserver.entity.LogicalGroup;
import com.dyteam.testApps.webserver.entity.ScheduledExecution;
import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;

public class ExecutionUiBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String executedBy;
	private Date executedDate;
	private Boolean scheduled;
	private String environmentName;
	private String executionUserName;
	private String executionUserRole;
	private Long runnerId;
	private float progess;
	private Date createdAt;
	private Long executionUserId;
	
	public ExecutionUiBean() {
		
	}
	
	public ExecutionUiBean(ScheduledExecution se,String userName) {
		id=se.getScheduledExecutionId();
		name=se.getScheduledExecutionName();
		executedBy = userName;
		executedDate=se.getScheduledDate();
		runnerId=se.getRunnerId();
		this.createdAt = se.getCreatedAt();
	}
	
	public ExecutionUiBean(ScheduledExecutionBk se,String userName,Boolean scheduled) {
		id=se.getScheduledExecutionId();
		name=se.getScheduledExecutionName();
		executedBy = userName;
		executedDate=se.getScheduledDate();
		this.runnerId = se.getRunnerId();
		this.scheduled = scheduled;
		this.createdAt = se.getCreatedAt();
	}
	
	public ExecutionUiBean(ScheduledExecutionBk se,String userName,Boolean scheduled,float progess) {
		id=se.getScheduledExecutionId();
		name=se.getScheduledExecutionName();
		executedBy = userName;
		executedDate=se.getScheduledDate();
		this.runnerId = se.getRunnerId();
		this.scheduled = scheduled;
		this.progess=progess;
		this.createdAt = se.getCreatedAt();
	}
	
	public ExecutionUiBean(LogicalGroup lg,String userName, String environmentName, String executionUserName,String executionUserRole) {
		id=lg.getLogicalGroupId();
		name=lg.getLogicalGroupName();
		executedBy = userName;
		executedDate=lg.getCreatedAt();
		this.environmentName=environmentName;
		this.executionUserName=executionUserName;
		this.executionUserRole=executionUserRole;
		this.createdAt = lg.getCreatedAt();
	}
	
	public ExecutionUiBean(LogicalGroup lg,String userName, String environmentName, String executionUserName,String executionUserRole,
			Long executionUserId) {
		id=lg.getLogicalGroupId();
		name=lg.getLogicalGroupName();
		executedBy = userName;
		executedDate=lg.getCreatedAt();
		this.environmentName=environmentName;
		this.executionUserName=executionUserName;
		this.executionUserRole=executionUserRole;
		this.createdAt = lg.getCreatedAt();
		this.executionUserId=executionUserId;
	}

	public String getExecutedBy() {
		return executedBy;
	}
	public Date getExecutedDate() {
		return executedDate;
	}
	public String getName() {
		return name;
	}
	
	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}
	
	
	public void setExecutedDate(Date executedDate) {
		this.executedDate = executedDate;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getScheduled() {
		return scheduled;
	}
	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}
	
	public String getEnvironmentName() {
		return environmentName;
	}
	public String getExecutionUserName() {
		return executionUserName;
	}
	public String getExecutionUserRole() {
		return executionUserRole;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public void setExecutionUserName(String executionUserName) {
		this.executionUserName = executionUserName;
	}
	public void setExecutionUserRole(String executionUserRole) {
		this.executionUserRole = executionUserRole;
	}
	
	public Long getRunnerId() {
		return runnerId;
	}
	
	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}
	
	public float getProgess() {
		return progess;
	}
	
	public void setProgess(int progess) {
		this.progess = progess;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	 public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	 
	 public Long getExecutionUserId() {
		return executionUserId;
	}
	 
	 public void setExecutionUserId(Long executionUserId) {
		this.executionUserId = executionUserId;
	}
	 
	 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ExecutionUiBean other = (ExecutionUiBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExecutionUiBean [id=" + id + ", name=" + name + ", executedBy=" + executedBy + ", executedDate="
				+ executedDate + ", scheduled=" + scheduled + ", environmentName=" + environmentName
				+ ", executionUserName=" + executionUserName + ", executionUserRole=" + executionUserRole
				+ ", runnerId=" + runnerId + ", progess=" + progess + ", createdAt=" + createdAt + 
				", executionUserId=" + executionUserId +"]";
	}
	
	
	
}
