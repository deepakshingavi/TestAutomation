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
@Table(name = "browser")
@EntityListeners(AuditingEntityListener.class)
public class Browser {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="browser_id")
	private Long browserId;
	
	@Column(name="browser_name")
	@NotNull
	private String browserName;
	
	@Column(name="company_id")
	@NotNull
	private Long companyId;
	
	@Column(name="status")
	@NotNull
	private Integer status;
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="user_id")
	@NotNull
	private Long userId;

	public Long getBrowserId() {
		return browserId;
	}

	public void setBrowserId(Long browserId) {
		this.browserId = browserId;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	
}
