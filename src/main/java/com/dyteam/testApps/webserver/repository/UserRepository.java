package com.dyteam.testApps.webserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dyteam.testApps.webserver.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
//	Optional<User> findByLoginId(String loginId);
	
	@Modifying
	@Transactional
	@Query("update User u set u.password = :password where u.userId = :userId")
	Integer updatePassword(@Param("password") String password,@Param("userId") Long userId);
	
	@Modifying
	@Transactional
	@Query("update User u set u.userName = :userName,u.contact = :contact,"
			+ " u.address = :address"
			+ " where u.userId = :userId")
	Integer updateProfile(@Param("userName") String userName,@Param("contact")String contact,
			@Param("address")String address, @Param("userId") Long userId);

	@Modifying
	@Transactional
	@Query("update User u set u.fName = :fName,u.lName = :lName,u.contact = :contact,u.userType = :userType,u.email = :email,u.address = :address where u.userId = :userId")
	Integer update(String fName, String lName, String contact, Integer userType, String email, String address, Long userId);

	Optional<User> findByUserName(String loginId);

	@Query("select u from User u where u.companyId = :companyId")
	Iterable<User> findAll(Long companyId);

	@Modifying
	@Transactional
	void deleteByCompanyId(Long companyId);
	
}
