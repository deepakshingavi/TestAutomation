package com.dyteam.testApps.webserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.LogicalGroup;

@Repository
public interface LogicalGroupRepository extends CrudRepository<LogicalGroup, Long>{
	

	@Query("select lg, u.userName,lg.environment.environmentName,eu.name,eu.role,eu.executionUserId from LogicalGroup lg left outer join User u on lg.userId=u.userId "
			+ " left outer join ExecutionUser eu on eu.executionUserId=lg.executionUserId"
			+ " where lg.companyId = :companyId")
	public Iterable<Object[]> findAllByCompanyId(@Param("companyId")Long companyId);

	@Query(value="select a.application_name, t.testcase_name, eu.name,eu.role "
			+ " from test_cases_logical_group tclg inner join testcases t on tclg.testcase_id = t.testcase_id "
			+ " inner join application a on t.application_id=a.application_id "
			+ " inner join logical_group lg on lg.logical_group_id = tclg.logical_group_id "
			+ " inner join execution_user eu on lg.execution_user_id = eu.execution_user_id"
			+ " where lg.logical_group_id = :logicalGroupId",nativeQuery=true)
	public Iterable<Object[]> getDetails(@Param("logicalGroupId")Long logicalGroupId);

	public void deleteByCompanyId(Long companyId);
	
	
	@Query("select count(lg)>0 from LogicalGroup lg where lg.companyId=:companyId and lg.logicalGroupName=:logicalGroupName")
	public boolean nameExists(@Param("logicalGroupName")String logicalGroupName,@Param("companyId")Long companyId);
	
	
	/*@Query("select new com.dyteam.testApps.webserver.model.LogicalGroupView(t.testcaseName, a.application_name) "
			+ " from LogicalGroup as lg inner join Testcases t on lg.testCaseList.testcasesId=t.testcasesId inner join Application a on "
			+ " t.applicationId=a.applicationId"
			+ " where lg.logicalGroupId = :logicalGroupId")
	public Iterable<LogicalGroup> getDetails(@Param("logicalGroupId")Long logicalGroupId);*/
}
