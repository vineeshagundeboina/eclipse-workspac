package com.federal.fedmobilesmecore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.ApplicationUser;
import com.federal.fedmobilesmecore.dto.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByMobileAndMarkAsEnabled(String mobileNumber, boolean isEnabled);
	public User findFirstByMobileAndMarkAsEnabledOrderByCreatedAtDesc(String mobileNumber, boolean isEnabled);

	public User findByAuthToken(String authToken);

	public User findByMobileAndMarkAsEnabledAndEnterpriseIdNot(String mobileNumber, boolean isEnabled,
			String enterpriseId);

	public User findByRoleAndPrefNoAndMarkAsEnabled(String role, String prefNo, boolean isEnabled);
	
	public List<User> findByEnterpriseIdAndMarkAsEnabledAndMobile(String enterprise_id,boolean enabled,String mobileno);
	public User findByEnterpriseIdAndMobile(String enterprise_id,String mobileno);
	public User findFirstByEnterpriseIdAndMarkAsEnabledAndMobileOrderByCreatedAtDesc(String enterprise_id,boolean enabled,String mobileno);

	//findFirstBySolIdAndBranchCodeOrderByCreatedAtDesc
	
	public User findByAuthTokenAndMarkAsEnabled(String auth_token, boolean isEnabled);
	
//	public Optional<User> findByPrefNo(String prefNo);	
	public Optional<User> findByPrefNoAndMobileAndMarkAsEnabled(String prefNo,String mobile,boolean enabled);	

	//public Optional<User> findByAuthToken(String authToken);
	public List<User> findByAppTokenAndMarkAsEnabled(String apptoken, boolean markenable);
	
	public List<User> findByAppTokenAndEnterpriseId(String apptoken,String enterprise_id);
	
	public User findByMobile(String mobileNumber);
	public Optional<User> findByPrefNoAndMarkAsEnabled(String prefNo,boolean enabled);
	//@Query("SELECT u from User u inner join Enterprises e on u.enterpriseId=e.id where e.active=true and u.markAsEnabled=true and u.mobile in (?1)")
	@Query("select au from ApplicationEnterpris ae left outer join ApplicationForm af on ae.applicationFormId=af.id left outer join ApplicationUser au on au.applicationEnterpriseId=ae.id where  au.mobile in (?1) and af.status not in('rejected','deleted')")
	public List<ApplicationUser> findByMobileAndEnterprise(List<String> mobiles);

	@Query("select ae from ApplicationEnterpris ae inner join ApplicationForm af on ae.applicationFormId=af.id where  ae.accNo=?1 and af.status not in('rejected','deleted')")
	public List<ApplicationEnterpris> findByAccountInIntermediate(String account);

	   @Modifying
	    @Transactional
	    @Query(value = "UPDATE Users set IS_ACCEPTED = :IS_ACCEPTED , IS_ACCEPTED_DATE = :isAcceptedDate where mobile = :mobile AND MARK_AS_ENABLED = 1", nativeQuery = true)
	    public int updateUsersTCByMobileNo(@Param("IS_ACCEPTED") String IS_ACCEPTED, @Param("mobile") String mobile,
	            @Param("isAcceptedDate") Date isAcceptedDate);

	 

	    @Modifying
	    @Transactional
	    @Query(value = "UPDATE Users set IS_ACCEPTED = :IS_ACCEPTED , IS_ACCEPTED_DATE = :isAcceptedDate where CUST_NO = :userId AND MARK_AS_ENABLED = 1", nativeQuery = true)
	    public int updateUsersTCByUserId(@Param("IS_ACCEPTED") String IS_ACCEPTED, @Param("userId") String userId,
	            @Param("isAcceptedDate") Date isAcceptedDate);
}