package com.federal.fedmobilesmecore.repository;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;

@Repository
public interface ApplicationEnterprisRepository extends JpaRepository<ApplicationEnterpris, Long> {

	ApplicationEnterpris findByPrefCorp(String prefCorp);
	ApplicationEnterpris findByPrefCorpIgnoreCase(String prefCorp);
	ApplicationEnterpris findByApplicationFormIdAndPrefCorpIgnoreCase(Long applicationFormId,String prefCorp);


	List<ApplicationEnterpris> findByAccNoAndActive(String acctno, Boolean active);

	List<ApplicationEnterpris> findByAccNo(String accountNo);

	@Query(value = "select TO_CHAR(frm.ID) as ID,TO_CHAR(frm.REF_NO) as REF_NO ,frm.Status_desc,TO_CHAR(afm.application_form_id) as APPLICATION_FORM_ID from APPLICATION_FORMS frm\r\n"
			+ "inner join APPLICATION_ENTERPRISES afm on afm.application_form_id=frm.ID\r\n"
			+ "where frm.status<>'rejected' and afm.ACC_NO=?1 order by afm.CREATED_AT desc", nativeQuery = true)
	List<Tuple> getApplEnterpriseDetails(String accountNo);

	ApplicationEnterpris findByPrefCorpAndActive(String prefCorp,boolean active);
	
}
