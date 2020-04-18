package com.dyteam.testApps.webserver.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "scheduled_execution")
@EntityListeners(AuditingEntityListener.class)
public class ScheduledExecution{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="scheduled_execution_id")
	private Long scheduledExecutionId;
	
	@Column(name="scheduled_execution_name")
	@NotNull
	private String scheduledExecutionName;
	
	@Column(name="scheduled_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledDate;
	
	@Column(name="browser_id")
	@NotNull
	private Long browserId;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "environment_id")
	private Environment environment;
	
	@Column(name="scheduled_by_id")
	@NotNull
	private Long scheduledById;
	
	@Column(name="company_id")
	@NotNull
	private Long companyId;
	
	
	@Column(name="execution_user_id")
	@NotNull
	private Long executionUserId;
	
	@Column(name="runner_id")
	private Long runnerId;
	
	@Column(name="status")
	@NotNull
	private Boolean status;
	
	@Column(name="created_at",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "test_cases_scheduled", 
	joinColumns = @JoinColumn(name = "scheduled_execution_id", referencedColumnName = "scheduled_execution_id"), 
	inverseJoinColumns = @JoinColumn(name = "testcase_id", referencedColumnName = "testcase_id"))
	private List<Testcases> testCaseList;

	public Long getScheduledExecutionId() {
		return scheduledExecutionId;
	}

	public void setScheduledExecutionId(Long scheduledExecutionId) {
		this.scheduledExecutionId = scheduledExecutionId;
	}

	public String getScheduledExecutionName() {
		return scheduledExecutionName;
	}

	public void setScheduledExecutionName(String scheduledExecutionName) {
		this.scheduledExecutionName = scheduledExecutionName;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Long getBrowserId() {
		return browserId;
	}

	public void setBrowserId(Long browserId) {
		this.browserId = browserId;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Long getScheduledById() {
		return scheduledById;
	}

	public void setScheduledById(Long scheduledById) {
		this.scheduledById = scheduledById;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<Testcases> getTestCaseList() {
		return testCaseList;
	}

	public void setTestCaseList(List<Testcases> testCaseList) {
		this.testCaseList = testCaseList;
	}
	
	public Long getExecutionUserId() {
		return executionUserId;
	}
	
	public void setExecutionUserId(Long executionUserId) {
		this.executionUserId = executionUserId;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ScheduledExecution [scheduledExecutionId=" + scheduledExecutionId + ", scheduledExecutionName="
				+ scheduledExecutionName + ", scheduledDate=" + scheduledDate + ", browserId=" + browserId
				+ ", environment=" + environment + ", scheduledById=" + scheduledById + ", companyId=" + companyId+ ", executionUserId=" + executionUserId
				+ ", runnerId=" + runnerId + ", createdAt=" + createdAt +  "]";
	}
	
	public Long getRunnerId() {
		return runnerId;
	}
	
	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}
	

}
