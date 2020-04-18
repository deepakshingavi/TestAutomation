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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "testcases")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(Include.NON_NULL)
public class Testcases {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="testcase_id")
	private Long testcasesId;
	
	@Column(name="testcase_name")
	@NotNull
	private String testcaseName;
	
	@Column(name="application_id")
	@NotNull
	private Long applicationId;
	
	@Column(name="company_id")
	@NotNull
	private Long companyId;
	
	@Column(name="user_id")
	@NotNull
	private Long userId;
	
	@Column(name="status_type")
	@NotNull
	private Integer statusType;

	@Column(name="class_name")
	@NotNull
	private String className;
	
	@Column(name="description")
	@NotNull
	private String description;
	
	@Column(name="is_availbale")
	@NotNull
	private Integer isAvailbale;
	
	@Column(name="status")
	@NotNull
	private Integer status;
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="is_perf_suite")
	@NotNull
	private Integer isPerfSuite;

	public Testcases(long testcasesId) {
		this.testcasesId=testcasesId;
	}
	
	public Testcases(long testcasesId,String testcaseName,String className) {
		this.testcasesId=testcasesId;
		this.testcaseName=testcaseName;
		this.className=className;
	}

	public Testcases() {
	}

	public Long getTestcasesId() {
		return testcasesId;
	}

	public void setTestcasesId(Long testcasesId) {
		this.testcasesId = testcasesId;
	}

	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getStatusType() {
		return statusType;
	}

	public void setStatusType(Integer statusType) {
		this.statusType = statusType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsAvailbale() {
		return isAvailbale;
	}

	public void setIsAvailbale(Integer isAvailbale) {
		this.isAvailbale = isAvailbale;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Integer getIsPerfSuite() {
		return isPerfSuite;
	}
	public void setIsPerfSuite(Integer isPerfSuite) {
		this.isPerfSuite = isPerfSuite;
	}
	
}
