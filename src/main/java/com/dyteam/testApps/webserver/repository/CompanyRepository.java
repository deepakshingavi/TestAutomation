package com.dyteam.testApps.webserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dyteam.testApps.webserver.entity.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

	@Query("select c.companyName from Company c where c.companyId = :companyId")
	public String getName(Long companyId);
	
	@Query("select new com.dyteam.testApps.webserver.entity.Company(companyId,companyName, userId, email, domain," + 
			" contactLandline, contact, address, isUrlAdd, status, passStatus) from Company c where c.companyId = :companyId")
	public Optional<Company> basicDetailsById(Long companyId);

	@Query(value = "SELECT HEX(AES_ENCRYPT(:password,:key))", nativeQuery = true)
	public abstract String getEncodedPassword(@Param("password") String password,@Param("key") String key);

	@Query(value = "SELECT AES_DECRYPT(UNHEX(:password),:key)", nativeQuery = true)
	public abstract String getDecodePassword(@Param("password") String password, @Param("key") String key);

	@Query(value = "SELECT AES_DECRYPT(UNHEX('4E56E73DF470BAF28D4C4F123F04DE5C'),'theKey')", nativeQuery = true)
	public abstract String getPassword();

	@Query(value = "SELECT new com.dyteam.testApps.webserver.entity.Company(hostName,port,email,username,secuirityProtocol,password,companyId) from Company where companyId = :companyId")
	public Company emailSettingById(@Param("companyId")Long companyId);
	
	@Query(value = "SELECT new com.dyteam.testApps.webserver.entity.Company(companyId,seleniumHome,testDataHome,screenShotsHome,batchFileHome,logsHome) from Company where companyId = :companyId")
	public Company applicationPathById(@Param("companyId") Long companyId);

	@Modifying
	@Transactional
	@Query(value = "update Company set hostName=:hostName,port=:port,password=:password,email=:email,username=:username,"
			+ "secuirityProtocol=:secuirityProtocol where companyId = :companyId ")
	public Integer saveEmailSettingById(@Param("hostName")String hostName,@Param("port")String port,@Param("email")String email,
			@Param("username")String username,@Param("secuirityProtocol")String secuirityProtocol,@Param("password")String password,
			@Param("companyId")Long companyId);
	
	@Modifying
	@Transactional
	@Query(value = "update Company set seleniumHome=:seleniumHome,testDataHome=:testDataHome,screenShotsHome=:screenShotsHome,batchFileHome=:batchFileHome,"
			+ "logsHome=:logsHome where companyId = :companyId ")
	public Integer saveApplicationPathById(@Param("seleniumHome")String seleniumHome,@Param("testDataHome")String testDataHome,@Param("screenShotsHome")String screenShotsHome,
			@Param("batchFileHome")String batchFileHome,@Param("logsHome")String logsHome, @Param("companyId")Long companyId);

	@Modifying
	@Transactional
	@Query(value = "update Company set contact=:contact,address=:address where companyId = :companyId ")
	public Integer update(String contact, String address, Long companyId);

	@Query(value = "SELECT new Company(c.companyName,c.seleniumHome) from Company c where c.companyId=:companyId")
	public Company getCompanyInfoForFolder(Long companyId);

	@Query(value = "SELECT seleniumHome from Company c where c.companyId=:companyId")
	public String getSeleniumHome(Long companyId);

}
