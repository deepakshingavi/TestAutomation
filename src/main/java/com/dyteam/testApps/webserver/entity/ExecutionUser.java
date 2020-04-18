package com.dyteam.testApps.webserver.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "execution_user")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(Include.NON_NULL)
public class ExecutionUser {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="execution_user_id")
	private Long executionUserId;
	
	@Column(name="company_id")
	@NotNull
	private Long companyId;

	@Column(name="name")
	@NotNull
	private String name;
	
	@Column(name="role")
	@NotNull
	private String role;
	
	@Column(name="password")
	@NotNull
	private String password;
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "added_by")
	private User addedBy;
	
	@Column(name="created_at",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	public ExecutionUser() {
	}

	public Long getExecutionUserId() {
		return executionUserId;
	}

	public void setExecutionUserId(Long executionUserId) {
		this.executionUserId = executionUserId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
}
