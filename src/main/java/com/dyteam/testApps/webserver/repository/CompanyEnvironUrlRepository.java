package com.dyteam.testApps.webserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dyteam.testApps.webserver.entity.CompanyEnvironUrl;
import com.dyteam.testApps.webserver.model.ApplicationCompanyUrl;

@Repository
public interface CompanyEnvironUrlRepository extends CrudRepository<CompanyEnvironUrl, Long>{
	
	@Query("select new com.dyteam.testApps.webserver.model.ApplicationCompanyUrl("
			+ "ceu.companyEnvironUrlId,"
			+ "ceu.envUrl,"
			+ "e.environmentId,"
			+ "e.environmentName,"
			+ "ceu.application.applicationId,"
			+ "ceu.application.applicationName) "
			+ " from CompanyEnvironUrl ceu right join Environment e on ( ceu.environment.environmentId=e.environmentId or ceu is null) "
			+ " where e.companyId = :companyId")
	public List<ApplicationCompanyUrl> findAllByUserId(@Param("companyId") Long companyId);
	
	@Query("select ceu,e "
			+ " from CompanyEnvironUrl ceu inner join Environment e on ( ceu.environment.environmentId=e.environmentId) "
			+ " where e.companyId = :companyId and ceu.application.applicationId = :applicationId")
	public Iterable<Object[]> findAllByUserId1(@Param("companyId") Long companyId,@Param("applicationId") Long applicationId);

	@Query("select ceu from CompanyEnvironUrl ceu where ceu.companyId = :companyId")
	public Iterable<CompanyEnvironUrl> findAll(@Param("companyId") Long companyId);

	@Modifying
    @Transactional
	@Query("delete from CompanyEnvironUrl ceu where ceu.application.applicationId = :applicationId")
	public void deleteByApplicationId(@Param("applicationId")Long applicationId);

	public void deleteByCompanyId(Long companyId);

	
}
