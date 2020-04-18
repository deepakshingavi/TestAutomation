package com.dyteam.testApps.webserver.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "testcase_exe_detail")
@EntityListeners(AuditingEntityListener.class)
public class TestcaseExeDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "testcase_exe_detail_id")
	private Long testcaseExeDetailId;

	@ManyToOne
    @JoinColumn(name = "execution_results_id")
	private ExecutionResults executionResults;

	@Column(name = "execution_start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionStartDate;

	@Column(name = "execution_end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionEndDate;

	@Column(name = "result")
	private String result;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "output")
	private String output;

	@Transient
	@javax.persistence.Transient
	private Long elapsedTime;

	@Column(name = "created_at", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public ExecutionResults getExecutionResults() {
		return executionResults;
	}

	public void setExecutionResults(ExecutionResults executionResults) {
		this.executionResults = executionResults;
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

	public Long getElapsedTime() {
		 elapsedTime = 0l;
		if(null!=getExecutionStartDate() && null!=getExecutionEndDate()) {
			elapsedTime = (getExecutionEndDate().getTime() - getExecutionStartDate().getTime())/1000l;
        }
		
		return elapsedTime;
	}
	
	
	public Long getTestcaseExeDetailId() {
		return testcaseExeDetailId;
	}

	public void setTestcaseExeDetailId(Long testcaseExeDetailId) {
		this.testcaseExeDetailId = testcaseExeDetailId;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setElapsedTime(Long elapsedTime) {
//		this.elapsedTime = elapsedTime;
	}

	@Override
	public String toString() {
		return "TestcaseExeDetail [testcaseExeDetailId=" + testcaseExeDetailId + ", executionStartDate="
				+ executionStartDate + ", executionEndDate=" + executionEndDate + ", result=" + result + ", output="
				+ output + ", elapsedTime=" + elapsedTime + ", createdAt=" + createdAt + "]";
	}
	
	

}
