package com.federal.fedmobilesmecore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ApplicationUser;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

	ApplicationUser findByprefNo(String prefNo);

	@Query(value = "select * from application_users where application_enterprise_id = (select id from application_enterprises where application_form_id = ?1)", nativeQuery = true)
	Optional<List<ApplicationUser>> findByApplicationFormId(String applicationFormId);

//	@Query(value = "select au from ApplicationUser au where applicationEnterpriseId = (select ae.id from ApplicationEnterpris ae where ae.applicationFormId = (select e.applicationFormId from Enterprises e where e.id = (select u.enterpriseId from User u where u.authToken = ?1))) and au.custNo = ?2")
//	Optional<ApplicationUser> getApplicationUser(String authToken, String customerNumber);

	@Query(value = "select * from application_users  where application_enterprise_id = (select id from application_enterprises  where application_form_id = (select application_form_id from Enterprises where id = (select enterprise_Id from Users  where auth_Token = ?1))) and cust_no = ?2", nativeQuery = true)
	Optional<ApplicationUser> getApplicationUserSQL(String authToken, String customerNumber);

	ApplicationUser findByCustNoAndPrefNo(String custNo,String prefNo);
}
