package com.federal.fedmobilesmecore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.EnterpriseUser;

@Repository
public interface EnterpriseUserRepository extends JpaRepository<EnterpriseUser, Long> {

	@Query(value = "select 1 as one from EnterpriseUser eu where (eu.status != 'deleted' OR eu.status != 'rejected') AND (eu.mobile = ?1) AND (eu.prefNo=?2)")
	String isEnterpriseUserExist(String mobileNumber,String prefNo);

	@Query(value = "SELECT 1 AS one FROM EnterpriseUser eu WHERE (eu.status != 'deleted' AND eu.status != 'rejected') AND eu.enterpriseId = ?1 AND eu.prefNo = ?2")
	String isEnterpriseUserExistForPrefNoVal(String enterpriseId, String prefNo);

	EnterpriseUser findByRefNo(String refNo);
	
	EnterpriseUser findByMobile(String refNo);

	List<EnterpriseUser> findByMobileAndStatusNotIn(String mobile,List<String> status);

	EnterpriseUser findByPrefNoAndStatusNotInAndEnterpriseId(String prefNo, List<String> status, String enterpriseId);

	List<EnterpriseUser> findByStatusAndEnterpriseId(String status, String enterpriseId);

	List<EnterpriseUser> findByStatusAndEnterpriseIdOrderByUpdatedAt(String status, String enterpriseId);
	
	List<EnterpriseUser> findByStatusAndEnterpriseIdOrderByCreatedAt(String status, String enterpriseId);

	@Query(value = "select count(*) from EnterpriseUser where enterpriseId = (select us.enterpriseId from User us where auth_token = ?1)")
	String usersCount(String authToken);
	
	EnterpriseUser findByRefNoAndStatusAndMobile(String refNo,String status,String mobile);	
	EnterpriseUser findByMobileAndPrefNoAndStatus(String mobile,String prefno,String status);
}
