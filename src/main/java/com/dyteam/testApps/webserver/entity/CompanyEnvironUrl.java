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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "company_environ_url")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(Include.NON_NULL)
public class CompanyEnvironUrl {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="company_environ_url_id")
	private Long companyEnvironUrlId;
	
	@Column(name="env_url")
	@NotNull
	private String envUrl;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "environment_id")
	private Environment environment;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id")
    private Application application;
	
	@NotNull
	@Column(name="user_id")
	private Long userId;
	
	@NotNull
	@Column(name="company_id")
	private Long companyId;
	
	@Column(name="status")
	@NotNull
	private Integer status;
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	public CompanyEnvironUrl() {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCompanyEnvironUrlId() {
		return companyEnvironUrlId;
	}

	public void setCompanyEnvironUrlId(Long companyEnvironUrlId) {
		this.companyEnvironUrlId = companyEnvironUrlId;
	}

	public String getEnvUrl() {
		return envUrl;
	}

	public void setEnvUrl(String envUrl) {
		this.envUrl = envUrl;
	}

	public Application getApplication() {
		return application;
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyEnvironUrlId == null) ? 0 : companyEnvironUrlId.hashCode());
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
		CompanyEnvironUrl other = (CompanyEnvironUrl) obj;
		if (companyEnvironUrlId == null) {
			if (other.companyEnvironUrlId != null)
				return false;
		} else if (!companyEnvironUrlId.equals(other.companyEnvironUrlId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompanyEnvironUrl [companyEnvironUrlId=" + companyEnvironUrlId + ", envUrl=" + envUrl + ", environment="
				+ environment + ", application=" + application + ", userId=" + userId + ", status=" + status
				+ ", createdAt=" + createdAt + "]";
	}

	

}
