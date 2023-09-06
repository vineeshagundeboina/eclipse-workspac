package com.federal.fedmobilesmecore.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Beneficiaries;

/**
 * @author Sandeep Dash
 *
 */
@Repository
public interface BeneficiariesRepository extends JpaRepository<Beneficiaries, Long> {

	Beneficiaries findByEnterpriseId(String enterpriseId);

	Optional<Beneficiaries> findByAccNoAndEnterpriseId(String accNo, String enterpriseId);

	Optional<Beneficiaries> findByAccNoAndEnterpriseIdAndMode(String accNo, String enterpriseId, String mode);

	Optional<Beneficiaries> findByAccNoAndEnterpriseIdAndModeAndIsActive(String accNo, String enterpriseId, String mode,
			boolean active);

	Optional<Beneficiaries> findByAccNoAndEnterpriseIdAndIfsc(String accNo, String enterpriseId, String ifsc);

	Optional<Beneficiaries> findByMobCust(String mobileNo);

	Optional<Beneficiaries> findByMobile(String mobileNo);

	Optional<Beneficiaries> findByMobileAndMmidAndEnterpriseId(String mobileNo, String mmid, String enterpriseid);

	Optional<Beneficiaries> findByMobileAndMmidAndEnterpriseIdAndMode(String mobileNo, String mmid, String enterpriseid,
			String mode);

	long countByStatusAndEnterpriseId(String status, String enterpriseId);

	List<Beneficiaries> findByEnterpriseIdAndStatusIsNot(String enterpriseId, String status);

	List<Beneficiaries> findByStatusIsNot(String status);

	List<Beneficiaries> findAllByEnterpriseId(String enterpriseId);

	List<Beneficiaries> findAllByEnterpriseIdAndNickNameIgnoreCase(String enterpriseId, String nickname);

	List<Beneficiaries> findAllByEnterpriseIdAndMobile(String enterpriseId, String mobileno);

	List<Beneficiaries> findAllByEnterpriseIdAndAccNo(String enterpriseId, String accno);
	
	List<Beneficiaries> findAllByEnterpriseIdAndAccNoAndIsActive(String enterpriseId,String accno,boolean active);

	List<Beneficiaries> findByStatusAndEnterpriseIdOrderByNickName(String status, String id);

	List<Beneficiaries> findByStatusAndEnterpriseIdOrderByUpdatedAtDesc(String status, String id);
	
	List<Beneficiaries> findByStatusAndEnterpriseIdAndOperationIdNotNullOrderByUpdatedAtDesc(String status, String id);

	Optional<Beneficiaries> findByRefNo(String ref_no);

	List<Beneficiaries> findByStatusAndIsActiveAndIsJobActiveOrderByUpdatedAtAsc(String status, boolean active,
			long jobactive);

	/* Added by sahil 27/08/2021 */
	List<Beneficiaries> findByStatusAndIsActiveAndIsJobActiveAndEnterpriseIdOrderByUpdatedAtDesc(String status,
			boolean active, long jobactive, String enterpriseid);
	
	List<Beneficiaries> findByStatusAndIsActiveAndIsJobActiveAndEnterpriseIdOrderByApprovedAtDesc(String status,
			boolean active, long jobactive, String enterpriseid);

	List<Beneficiaries> findAllByEnterpriseIdAndNickNameIgnoreCaseAndStatusIsNot(String enterpriseId, String nickname,String status);

	@Transactional
	@Modifying
	void deleteByIdAndLockVersion(long id, long lockVersion);

}
