package com.dyteam.testApps.webserver.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "testcase_result")
@EntityListeners(AuditingEntityListener.class)
public class TestcaseResult {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="testcase_result_id")
	private Long testcaseResultId;
	
	@Column(name="runname")
	@NotNull
	private String runname;
	
	@Column(name="testcase_id")
	@NotNull
	private Long testcaseId;
	
	@Column(name="testcase_name")
	@NotNull
	private String testcaseName;

	@Column(name="user_id")
	@NotNull
	private Long userId;
	
	@Column(name="application_id")
	@NotNull
	private Long applicationId;
	
	@Column(name="compnay_id")
	@NotNull
	private Long compnayId;
	
	@Column(name="browser_id")
	@NotNull
	private Long browserId;
	
	@Column(name="email")
	@NotNull
	private String email;
	
	@Column(name="execution_date")
	@NotNull
	private String executionDate;
	
	@Column(name="elapse_time")
	@NotNull
	private String elapseTime;
	
	@Column(name="result")
	@NotNull
	private Integer result;
	
	@Column(name="reason")
	@NotNull
	private String reason;
	
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;


	public Long getTestcaseResultId() {
		return testcaseResultId;
	}


	public void setTestcaseResultId(Long testcaseResultId) {
		this.testcaseResultId = testcaseResultId;
	}


	public String getRunname() {
		return runname;
	}


	public void setRunname(String runname) {
		this.runname = runname;
	}


	public Long getTestcaseId() {
		return testcaseId;
	}


	public void setTestcaseId(Long testcaseId) {
		this.testcaseId = testcaseId;
	}


	public String getTestcaseName() {
		return testcaseName;
	}


	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Long getApplicationId() {
		return applicationId;
	}


	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}


	public Long getCompnayId() {
		return compnayId;
	}


	public void setCompnayId(Long compnayId) {
		this.compnayId = compnayId;
	}


	public Long getBrowserId() {
		return browserId;
	}


	public void setBrowserId(Long browserId) {
		this.browserId = browserId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getExecutionDate() {
		return executionDate;
	}


	public void setExecutionDate(String executionDate) {
		this.executionDate = executionDate;
	}


	public String getElapseTime() {
		return elapseTime;
	}


	public void setElapseTime(String elapseTime) {
		this.elapseTime = elapseTime;
	}


	public Integer getResult() {
		return result;
	}


	public void setResult(Integer result) {
		this.result = result;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
