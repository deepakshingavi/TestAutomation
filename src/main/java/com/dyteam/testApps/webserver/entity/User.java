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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(Include.NON_NULL)
public class User {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="user_type")
	@NotNull
	private Integer userType;
	
	@Column(name="company_id")
	@NotNull
	private Long companyId;

	@Column(name="fname")
	@NotNull
	private String fName;
	
	@Column(name="lname")
	@NotNull
	private String lName;
	
	@Column(name="email")
	@NotNull
	@NotEmpty
	private String email;
	
	@Column(name="password")
	@NotNull
	@NotEmpty
	private String password;
	
	@Column(name="user_name")
	@NotNull
	@NotEmpty
	private String userName;
	
	@Column(name="contact")
	@NotNull
	private String contact;
	
	@Column(name="address")
	@NotNull
	private String address;
	
	@Column(name="added_by")
	@NotNull
	private Long addedBy;
	
	@Column(name="ref_user_id")
	@NotNull
	private Long refUserId;
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="status")
	@NotNull
	private Integer status;
	
	public User() {
	}

	public User(String fName, String lName, String contact, Integer userType, String email, String address) {
		this.fName = fName;
		this.lName = lName;
		this.contact = contact;
		this.userType = userType;
		this.email = email;
		this.address = address;
	}

	public User(Long userId) {
		this.userId=userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Long addedBy) {
		this.addedBy = addedBy;
	}

	public Long getRefUserId() {
		return refUserId;
	}

	public void setRefUserId(Long refUserId) {
		this.refUserId = refUserId;
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
	
}
