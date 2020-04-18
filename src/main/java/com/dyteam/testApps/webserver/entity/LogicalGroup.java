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
@Table(name = "logical_group")
@EntityListeners(AuditingEntityListener.class)
public class LogicalGroup{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="logical_group_id")
	private Long logicalGroupId;
	
	@Column(name="logical_group_name")
	@NotNull
	private String logicalGroupName;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "environment_id")
	private Environment environment;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "browser_id")
	private Browser browser;
	
	@ManyToMany()
	@JoinTable(name = "test_cases_logical_group", 
	joinColumns = @JoinColumn(name = "logical_group_id", referencedColumnName = "logical_group_id"), 
	inverseJoinColumns = @JoinColumn(name = "testcase_id", referencedColumnName = "testcase_id"))
	private List<Testcases> testCaseList;
	
	@Column(name="user_id")
	@NotNull
	private Long userId;
	
	@Column(name="company_id")
	@NotNull
	private Long companyId;
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "execution_user_id")
	private Long executionUserId;
	
	@Column(name = "status")
	private Boolean status;

	public Long getLogicalGroupId() {
		return logicalGroupId;
	}


	public void setLogicalGroupId(Long logicalGroupId) {
		this.logicalGroupId = logicalGroupId;
	}


	public String getLogicalGroupName() {
		return logicalGroupName;
	}


	public void setLogicalGroupName(String logicalGroupName) {
		this.logicalGroupName = logicalGroupName;
	}


	public Environment getEnvironment() {
		return environment;
	}


	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Long getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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
	
	public Browser getBrowser() {
		return browser;
	}
	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

}
