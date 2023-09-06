package com.federal.fedmobilesmecore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.User;

/**
 * @author Sandeep Dash
 *
 */
@Repository
public interface UserRepository_V1 extends JpaRepository<User, Long> {

	public Optional<User> findByMobileAndMarkAsEnabled(String mobileNumber, boolean isEnabled);

	public Optional<User> findByAuthToken(String authToken);

	public User findByMobileAndMarkAsEnabledAndEnterpriseIdNot(String mobileNumber, boolean isEnabled,
			String enterpriseId);

	public User findByRoleAndPrefNoAndMarkAsEnabled(String role, String prefNo, boolean isEnabled);

	public List<User> findByEnterpriseIdAndMarkAsEnabledAndMobile(String enterprise_id, boolean enabled,
			String mobileno);

	public User findByEnterpriseIdAndMobile(String enterprise_id, String mobileno);

	public User findFirstByEnterpriseIdAndMarkAsEnabledAndMobileOrderByCreatedAtDesc(String enterprise_id,
			boolean enabled, String mobileno);

	// findFirstBySolIdAndBranchCodeOrderByCreatedAtDesc

	public Optional<User> findByAuthTokenAndMarkAsEnabled(String auth_token, boolean isEnabled);

	public Optional<User> findByPrefNoAndMobileAndMarkAsEnabled(String prefNo,String mobile,boolean enabled);

	public Optional<User> findByAppTokenAndMarkAsEnabled(String apptoken, boolean markenable);

	// public int countByEnterpriseId(long id);

	public int countByEnterpriseId(String id);

	public Optional<User> findByAppToken(String refreshToken);

//	public Optional<User> findByPrefNoAndMarkAsEnabled(String prefNo, boolean isEnabled);
	
	
	public boolean existsByPrefNoAndMobileAndWebCheckStatusAndMarkAsEnabled(String prefNo,String mobile,String status,boolean enabled);
}