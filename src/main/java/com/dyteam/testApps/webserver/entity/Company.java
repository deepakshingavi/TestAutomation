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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "company")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(Include.NON_NULL)
public class Company {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="company_id")
	private Long companyId;
	
	@Column(name="company_name")
	@NotNull
	private String companyName;
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="email")
	@NotNull
	private String email;
	
	@Column(name="password")
	@NotNull
	private String password;
	
	@Column(name="domain")
	private String domain;
	
	@Column(name="contact_landline")
	private String contactLandline;
	
	@Column(name="contact")
	@NotNull
	private String contact;
	
	@Column(name="address")
	@NotNull
	private String address;
	
	@Column(name="is_Url_add")
	private Integer isUrlAdd;
	
	@Column(name="pass_status")
	private Integer passStatus;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="selenium_home")
	private String seleniumHome;
	
	@Column(name="test_data_home")
	private String testDataHome;
	
	@Column(name="screen_shots_home")
	private String screenShotsHome;
	
	@Column(name="batch_file_home")
	private String batchFileHome;
	
	@Column(name="logs_home")
	private String logsHome;
	
	@Column(name="host_name")
	private String hostName;
	
	@Column(name="port")
	private String port;
	
	@Column(name="username")
	private String username;
	
	@Column(name="secuirity_protocol")
	private String secuirityProtocol;
	
	@Column(name="createdat",insertable=false,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Transient
	private String loginId;
	
	public Company() {
	}
	
	public Company(String companyName,String seleniumHome) {
		this.companyName=companyName;
		this.seleniumHome=seleniumHome;
	}
	
	public Company(Long companyId,String seleniumHome,String testDataHome,String screenShotsHome,String batchFileHome,String logsHome) {
		this.companyId=companyId;
		this.seleniumHome=seleniumHome;
		this.testDataHome=testDataHome;
		this.screenShotsHome=screenShotsHome;
		this.batchFileHome=batchFileHome;
		this.logsHome=logsHome;
	}
	
	public Company(String hostName,String port,String email,String username,String secuirityProtocol,String password,Long companyId) {
		this.companyId=companyId;
		this.email=email;
		this.hostName=hostName;
		this.port=port;
		this.username=username;
		this.secuirityProtocol=secuirityProtocol;
		this.password=password;
	}
	
	public Company(Long companyId,String companyName, Long userId,  String email,  String domain, 
			 String contactLandline,  String contact,  String address,  Integer isUrlAdd, 
			 Integer status,  Integer passStatus) {
		this.companyId=companyId;
		this.companyName=companyName;
		this.userId=userId;
		this.email=email;
		this.domain=domain;
		this.contactLandline=contactLandline;
		this.contact=contact;
		this.address=address;
		this.isUrlAdd=isUrlAdd;
		this.status=status;
		this.passStatus=passStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public Integer getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(Integer passStatus) {
		this.passStatus = passStatus;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getContactLandline() {
		return contactLandline;
	}

	public void setContactLandline(String contactLandline) {
		this.contactLandline = contactLandline;
	}

	public Integer getIsUrlAdd() {
		return isUrlAdd;
	}

	public void setIsUrlAdd(Integer isUrlAdd) {
		this.isUrlAdd = isUrlAdd;
	}

	public String getSeleniumHome() {
		return seleniumHome;
	}

	public void setSeleniumHome(String seleniumHome) {
		this.seleniumHome = seleniumHome;
	}

	public String getTestDataHome() {
		return testDataHome;
	}

	public void setTestDataHome(String testDataHome) {
		this.testDataHome = testDataHome;
	}

	public String getScreenShotsHome() {
		return screenShotsHome;
	}

	public void setScreenShotsHome(String screenShotsHome) {
		this.screenShotsHome = screenShotsHome;
	}

	public String getBatchFileHome() {
		return batchFileHome;
	}

	public void setBatchFileHome(String batchFileHome) {
		this.batchFileHome = batchFileHome;
	}

	public String getLogsHome() {
		return logsHome;
	}

	public void setLogsHome(String logsHome) {
		this.logsHome = logsHome;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSecuirityProtocol() {
		return secuirityProtocol;
	}

	public void setSecuirityProtocol(String secuirityProtocol) {
		this.secuirityProtocol = secuirityProtocol;
	}

	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	
}
