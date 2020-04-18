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
@Table(name = "environment")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(Include.NON_NULL)
public class Environment {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="environment_id")
	private Long environmentId;
	
	@Column(name="environment_name")
	@NotNull
	private String environmentName;
	
	@Column(name="company_id")
	@NotNull
	private Long companyId;
	
	@Column(name="user_id")
	@NotNull
	private Long userId;
	
	@Column(name="added_by")
	@NotNull
	private Long addedBy;
	
	@Column(name="status")
	@NotNull
	private Integer status;
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public Environment(Long environmentId, String environmentName) {
		this.environmentId=environmentId;
		this.environmentName=environmentName;
	}

	public Environment() {
	}

	public Environment(long environmentId) {
		this.environmentId=environmentId;
	}

	public Long getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Long environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
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

	public Long getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Long addedBy) {
		this.addedBy = addedBy;
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

	@Override
	public String toString() {
		return "Environment [environmentId=" + environmentId + ", environmentName=" + environmentName + ", companyId="
				+ companyId + ", userId=" + userId + ", addedBy=" + addedBy + ", status=" + status + ", createdAt="
				+ createdAt + "]";
	}

	
	
	
}
