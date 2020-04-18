package com.dyteam.testApps.webserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dyteam.testApps.webserver.entity.ExecutionResults;
import com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo;
import com.dyteam.testApps.webserver.model.LatestExecutionExtraSummary;
import com.dyteam.testApps.webserver.model.LatestExecutionInfo;

@Repository
public interface ExecutionResultsRepository extends CrudRepository<ExecutionResults, Long>{
	
	
	@Query("select new com.dyteam.testApps.webserver.model.LatestExecutionInfo("
			+ " er.executionName,u.userName,er.executionStartDate,count(er.executionResultsId),er.result ) "
			+ " from ExecutionResults er inner join User u on er.executedBy.userId = u.userId "
			+ " where  er.runnerId = "
			+ " (select max(er1.runnerId) from ExecutionResults er1 where er1.companyId = :companyId and er1.status = true)"
			+ " group by er.result")
	public List<LatestExecutionInfo> getLatestTestCaseExecutionSummary(@Param("companyId")Long companyId);
	
	/*@Query("select new com.dyteam.testApps.webserver.model.LatestExecutionInfo("
			+ " er.executionName,u.userName,er.executionStartDate,count(er.executionResultsId),er.result ) "
			+ " from ExecutionResults er inner join User u on er.executedBy.userId = u.userId "
			+ " where  er.runnerId = "
			+ " (select max(er1.runnerId) from ExecutionResults er1 where er1.companyId = :companyId and er.status = true and"
			+ "  er1.runnerId not in (select er2.runnerId from ExecutionResults er2 group by er2.runnerId,er2.testcases.status having er2.testcases.status=0) ) "
			+ " group by er.result")
	public List<LatestExecutionInfo> getLatestTestCaseExecutionSummary(@Param("companyId")Long companyId);*/
	
	@Query("select new com.dyteam.testApps.webserver.model.LatestExecutionInfo("
			+ " er.executionName,u.userName,er.executionStartDate,count(er.executionResultsId),er.result ) "
			+ " from ExecutionResults er inner join User u on er.executedBy.userId = u.userId "
			+ " where  er.executionStartDate = "
			+ " (select max(er1.executionStartDate) from ExecutionResults er1) and er.status = true "
			+ " group by er.runnerId,er.result")
	public List<LatestExecutionInfo> getSuperLatestTestCaseExecutionSummary();

	@Query(nativeQuery=true,value="select sum(case when er.result = 'Queued' then er.tot_count else 0 end)/sum(er.tot_count) from "
			+ " ( select result as result ,count(1) as tot_count from execution_results where runner_id = :runnerId and er.status = true group by result  ) er")
	public Double executionResultProgress(@Param("runnerId") Long runnerId);

	
	@Query("select new com.dyteam.testApps.webserver.model.LatestExecutionInfo("
			+ " er.executionName,u.userName,er.executionStartDate,count(er.executionResultsId),er.result ) "
			+ " from ExecutionResults er inner join User u on er.executedBy.userId = u.userId "
			+ " where er.companyId = :companyId and er.status = true and er.executionStartDate = "
			+ " (select max(er1.executionStartDate) from ExecutionResults er1) "
			+ " group by er.runnerId,er.result")
	public Iterable<ExecutionResults> findAll(@Param("companyId")Long companyId);

	
	@Query("select new com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo(runnerId,executionName,executedBy.email,executionStartDate,count(1),result) "
			+ " from ExecutionResults er "
			+ " where companyId = :companyId and er.scheduled=false and er.status = true group by runnerId,result order by addedDate desc")
	public Iterable<ExecutionResultRunnerInfo> findAllRunner(@Param("companyId")Long companyId);
	
	@Query("select new com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo(runnerId,executionName,executedBy.email,executionStartDate,count(1),result) "
			+ " from ExecutionResults er "
			+ " where companyId = :companyId and er.scheduled=true and er.status = true group by runnerId,result order by addedDate desc")
	public Iterable<ExecutionResultRunnerInfo> findAllScheduleRunner(@Param("companyId")Long companyId);
	
	@Query("select new com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo(runnerId,count(1),result) "
			+ " from ExecutionResults er "
			+ " where companyId = :companyId and er.status = true group by runnerId,result")
	public Iterable<ExecutionResultRunnerInfo> findAllRunnerStatus(@Param("companyId")Long companyId);

	public Iterable<ExecutionResults> findAllByRunnerId(Long runnerId);
	
	@EntityGraph(value = "ExecutionResults.testcaseExeDetailList", type = EntityGraphType.LOAD)
	public Iterable<ExecutionResults> findAllDetailsByRunnerId(@Param("runnerId")Long runnerId);

	@Modifying
    @Transactional
    @Query("update ExecutionResults set status = false where runnerId=:runnerId")
	public void deleteByRunnerId(@Param("runnerId")Long runnerId);

	@Query("select new com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo(runnerId,count(1),result) "
			+ " from ExecutionResults er "
			+ " where companyId = :companyId and er.status = true and runnerId IN (:executionResultIds)group by runnerId,result")
	public List<ExecutionResultRunnerInfo> getInPogressRunnerStatusByCompany(@Param("executionResultIds")List<Long> executionResultIds,
			@Param("companyId")Long companyId);
	
	@Query("select new com.dyteam.testApps.webserver.model.ExecutionResultRunnerInfo(runnerId,count(1),result) "
			+ " from ExecutionResults er "
			+ " where emailSent != true and er.status = true group by runnerId,result")
	public Iterable<ExecutionResultRunnerInfo> findAllNonEmailedRunnerStatus();

	
	//runnerId,count(1),result
	
	@Query("select new com.dyteam.testApps.webserver.model.LatestExecutionExtraSummary(er.runnerId, er.executionName, e.environmentName, b.browserName, "
			+ " er.executedBy.fName, er.executedBy.email, er.executionStartDate,er.executedBy.companyId,er.executedBy.lName) "
			+ " from ExecutionResults er left outer join Environment e on er.environmentId=e.environmentId"
			+ " left outer join Browser b on er.browserId=b.browserId"
			+ " where er.runnerId=:runnerId group by er.runnerId")
	public LatestExecutionExtraSummary getExpandedER(@Param("runnerId")Long runnerId);
	
	@Query("select er.testcases.testcaseName,er.result from ExecutionResults er where er.runnerId=:runnerId ")
	public Iterable<Object[]> getTestCaseNameResult(@Param("runnerId")Long runnerId);

	@Modifying
    @Transactional
    @Query("update ExecutionResults set emailSent=true where runnerId=:runnerId")
	public void updateEmailSent(@Param("runnerId")Long runnerId);

	@Modifying
    @Transactional
    @Query("update ExecutionResults set status=false where companyId=:companyId")
	public void inactivateByCompanyId(@Param("companyId")Long companyId);

	@Query("select count(er)>0 from ExecutionResults er where er.companyId=:companyId and er.executionName=:executionName")
	public boolean nameExists(@Param("executionName")String executionName,@Param("companyId")Long companyId);

	@Query("select count(er)>0 from ExecutionResults er where er.environmentId=:environmentId and status=true")
	public boolean environmentExists(Long environmentId);

	@Query("select er1 from ExecutionResults er1 JOIN FETCH er1.testcaseExeDetailList where er1.status=true and er1.runnerId IN ( select er.runnerId from ExecutionResults er where er.companyId =:companyId and "
			+ " result !='QUEUED' and er.testcases.isPerfSuite=1 and er.testcases.status=1 group by er.runnerId)")
	public Iterable<ExecutionResults> getPerfRecords(@Param("companyId")Long companyId);
	
	@Query(value="Select testcases.testcase_name as TestName, Failure.FailureRate from testcases "
			+ "  inner join "
			+ "  (select testcase_id,Count(*) as FailureRate from execution_results "
			+ "   inner join "
			+ "   ( SELECT DISTINCT runner_id FROM execution_results where status=1 order by added_date desc LIMIT 5) TOP5 "
			+ "  on execution_results.runner_id=TOP5.runner_id where execution_results.Result=:result "
			+ "   group by testcase_id order by FailureRate desc Limit 10) as Failure "
			+ "   ON testcases.testcase_id=Failure.testcase_id and testcases.status=1 and testcases.company_id=:companyId",nativeQuery=true)
	public Iterable<Object[]> topTenFailedTestCase(@Param("companyId")Long companyId,@Param("result")String result);

	@Query(value="Select testcases.testcase_name as TestName, Failure.FailureRate from testcases "
			+ "  inner join "
			+ "  (select testcase_id,Count(*) as FailureRate from execution_results "
			+ "   inner join "
			+ "   (SELECT DISTINCT runner_id FROM execution_results where status=1 order by added_date desc LIMIT 5) TOP5 "
			+ "  on execution_results.runner_id=TOP5.runner_id where execution_results.Result=:result "
			+ "   group by testcase_id order by FailureRate desc Limit 10) as Failure "
			+ "   ON testcases.testcase_id=Failure.testcase_id and testcases.status=1",nativeQuery=true)
	public Iterable<Object[]> topTenFailedTestCase(@Param("result")String result);

	@Query(value="Select er from ExecutionResults er where er.runnerId=:runnerId and result=:result")
	public Iterable<ExecutionResults> findAllFailedByRunnerId(@Param("runnerId")Long runnerId,@Param("result")String result);

	@Transactional
	@Modifying
	@Query("update ExecutionResults set result=:result where executionResultsId IN (:executionResultIds)")
	public void updateResetResult(@Param("executionResultIds")List<Long> executionResultIds,@Param("result")String result);
	
}
