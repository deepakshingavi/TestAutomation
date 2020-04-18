package com.dyteam.testApps.webserver.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.Testcases;
import com.dyteam.testApps.webserver.model.ApplicationTestcaseInfo;

@Repository
public interface TestcasesRepository extends CrudRepository<Testcases, Long>{

	@Query("select new com.dyteam.testApps.webserver.model.ApplicationTestcaseInfo(count(tc.testcasesId) as testcasesCount,a.applicationName) from Testcases tc inner join "
			+ " Application a on tc.applicationId=a.applicationId"
			+ " where tc.status = 1 and tc.companyId = :companyId group by tc.applicationId order by testcasesCount desc")
	List<ApplicationTestcaseInfo> getDashboardDetails(@Param("companyId")Long companyId);
	
	@Query("select new com.dyteam.testApps.webserver.model.ApplicationTestcaseInfo(count(tc.testcasesId) as testcasesCount,a.applicationName) from Testcases tc inner join "
			+ " Application a on tc.applicationId=a.applicationId"
			+ " where tc.status = 1 group by tc.applicationId order by testcasesCount desc")
	List<ApplicationTestcaseInfo> getSuperDashboardDetails();

	@Query("select className from Testcases where testcasesId IN (:testcaseId)")
	List<String> getClassesForAll(@Param("testcaseId")Collection<Long> testcaseId);
	
	@Query("select new com.dyteam.testApps.webserver.entity.Testcases(testcasesId,testcaseName,className) from Testcases where testcasesId IN (:testcaseId)")
	List<Testcases> getIdNameClassForAll(@Param("testcaseId")Collection<Long> testcaseId);

	@Query("select t from Testcases t where status = 1 and companyId = :companyId")
	Iterable<Testcases> findActiveAll( @Param("companyId")Long companyId);

	void deleteByCompanyId(Long companyId);
}
