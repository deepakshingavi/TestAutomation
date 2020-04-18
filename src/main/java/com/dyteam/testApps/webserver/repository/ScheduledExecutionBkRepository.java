package com.dyteam.testApps.webserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dyteam.testApps.webserver.entity.ScheduledExecutionBk;

@Repository
public interface ScheduledExecutionBkRepository extends CrudRepository<ScheduledExecutionBk, Long>{

	@Query("select se, u.userName from ScheduledExecutionBk se left outer join User u on se.scheduledById=u.userId "
			+ " where se.companyId = :companyId and se.status = true order by se.createdAt desc")
	Iterable<Object[]> findAllByCompanyId(@Param("companyId")Long companyId);

	void deleteByCompanyId(Long companyId);
	
}
