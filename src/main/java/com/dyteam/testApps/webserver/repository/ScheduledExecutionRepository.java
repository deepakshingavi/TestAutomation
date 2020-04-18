package com.dyteam.testApps.webserver.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.ScheduledExecution;

@Repository
public interface ScheduledExecutionRepository extends CrudRepository<ScheduledExecution, Long>{

	Iterable<ScheduledExecution> findByScheduledDateBetween(Date from, Date to);

	@Query("select se, u.userName from ScheduledExecution se left outer join User u on se.scheduledById=u.userId "
			+ " where se.companyId = :companyId and se.status = true order by se.createdAt desc")
	Iterable<Object[]> findAllByCompanyId(@Param("companyId")Long companyId);

	Iterable<ScheduledExecution> findByScheduledDateBefore(Date to);

	void deleteByCompanyId(Long companyId);

    @Query("SELECT er FROM ScheduledExecution er JOIN FETCH er.testCaseList WHERE er.runnerId = (:runnerId)")
	Iterable<ScheduledExecution> findByRunnerIdAndTestCaseList(@Param("runnerId")Long runnerId);
	
}
