package com.dyteam.testApps.webserver.entity;

import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "execution_results")
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = "ExecutionResults.testcaseExeDetailList",
attributeNodes = @NamedAttributeNode("testcaseExeDetailList"))
public class ExecutionResults {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "execution_results_id")
	private Long executionResultsId;

	@Column(name = "runner_id")
	@NotNull
	private Long runnerId;

	@Column(name = "execution_name")
	@NotNull
	private String executionName;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "testcase_id")
	private Testcases testcases;

	@Column(name = "execution_start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionStartDate;

	@Column(name = "execution_end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionEndDate;

	@Column(name = "result")
	private String result;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executed_by_id",referencedColumnName="user_id")
	private User executedBy;
	
	@Transient
	@javax.persistence.Transient
	private Long elapsedTime;

	@Column(name = "reason")
	private String reason;

	@Column(name = "added_date", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date addedDate;

	@Column(name = "output")
	private String output;

	@Column(name = "company_id")
	private Long companyId;

	@Column(name = "browser_id")
	private Long browserId;

	@Column(name = "environment_id")
	private Long environmentId;
	
	@Column(name = "execution_user_id")
	private Long executionUserId;
	
	@Column(name = "is_scheduled")
	private Boolean scheduled;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "email_sent")
	private Boolean emailSent;
	
	@OneToMany(mappedBy="executionResults",fetch=FetchType.LAZY)
    private List<TestcaseExeDetail> testcaseExeDetailList = new ArrayList<>();

	public ExecutionResults() {
		
	}
	
	public ExecutionResults(ExecutionResults er, List<TestcaseExeDetail> testcaseExeDetailList) {
		super();
		this.executionResultsId = er.executionResultsId;
		this.runnerId = er.runnerId;
		this.executionName = er.executionName;
		this.testcases = er.testcases;
		this.executionStartDate = er.executionStartDate;
		this.executionEndDate = er.executionEndDate;
		this.result = er.result;
		this.executedBy = er.executedBy;
		this.elapsedTime = er.elapsedTime;
		this.reason = er.reason;
		this.addedDate = er.addedDate;
		this.output = er.output;
		this.companyId = er.companyId;
		this.browserId = er.browserId;
		this.environmentId = er.environmentId;
		this.testcaseExeDetailList = testcaseExeDetailList;
	}



	public ExecutionResults(long executionResultsId) {
		this.executionResultsId=executionResultsId;
	}

	public Long getRunnerId() {
		return runnerId;
	}

	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getExecutionResultsId() {
		return executionResultsId;
	}

	public void setExecutionResultsId(Long executionResultsId) {
		this.executionResultsId = executionResultsId;
	}

	public String getExecutionName() {
		return executionName;
	}

	public void setExecutionName(String executionName) {
		this.executionName = executionName;
	}

	public Date getExecutionStartDate() {
		return executionStartDate;
	}

	public void setExecutionStartDate(Date executionStartDate) {
		this.executionStartDate = executionStartDate;
	}

	public Date getExecutionEndDate() {
		return executionEndDate;
	}

	public void setExecutionEndDate(Date executionEndDate) {
		this.executionEndDate = executionEndDate;
	}

	public Long getBrowserId() {
		return browserId;
	}

	public Long getEnvironmentId() {
		return environmentId;
	}

	public void setBrowserId(Long browserId) {
		this.browserId = browserId;
	}

	public void setEnvironmentId(Long environmentId) {
		this.environmentId = environmentId;
	}

	public Testcases getTestcases() {
		return testcases;
	}

	public void setTestcases(Testcases testcases) {
		this.testcases = testcases;
	}

	public void setExecutedBy(User executedBy) {
		this.executedBy=executedBy;
	}
	
	public User getExecutedBy() {
		return executedBy;
	}
	
	public Long getElapsedTime() {
		 elapsedTime = 0l;
		if(null!=getExecutionStartDate() && null!=getExecutionEndDate()) {
			elapsedTime = (getExecutionEndDate().getTime() - getExecutionStartDate().getTime())/1000l;
        }
		
		return elapsedTime;
	}
	public void setElapsedTime(Integer elapsedTime) {
//		this.elapsedTime = elapsedTime;
	}
	
	public List<TestcaseExeDetail> getTestcaseExeDetailList() {
		return testcaseExeDetailList;
	}
	
	public void setTestcaseExeDetailList(List<TestcaseExeDetail> testcaseExeDetailList) {
		this.testcaseExeDetailList = testcaseExeDetailList;
	}
	
	public Boolean getScheduled() {
		return scheduled;
	}
	
	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	@Override
	public String toString() {
		return "ExecutionResults [executionResultsId=" + executionResultsId + ", runnerId=" + runnerId
				+ ", executionName=" + executionName + ", testcases=" + testcases + ", executionStartDate="
				+ executionStartDate + ", executionEndDate=" + executionEndDate + ", result=" + result + ", executedBy="
				+ executedBy + ", elapsedTime=" + elapsedTime + ", reason=" + reason + ", addedDate=" + addedDate
				+ ", output=" + output + ", companyId=" + companyId + ", browserId=" + browserId + ", environmentId="
				+ environmentId + ", testcaseExeDetailList=" + testcaseExeDetailList + "]";
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
	
	public void setEmailSent(Boolean emailSent) {
		this.emailSent = emailSent;
	}
	public Boolean getEmailSent() {
		return emailSent;
	}
	

}
