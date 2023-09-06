package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Enterprises;
@Repository
public interface EnterprisesRepository extends JpaRepository<Enterprises, Long> {

	public Enterprises findByActiveAndPrefCorp(boolean active, String prefCorp);

	public Enterprises findByPrefCorp(String prefCorp);

	public Enterprises findByPrefCorpAndActive(String pref_corp, boolean active);

	public Enterprises findByAccNo(String accNo);

	public Enterprises findByAccNoAndActive(String accNo, boolean active);
	
	public Enterprises findFirstByAccNoAndActiveOrderByCreatedAtDesc(String accNo, boolean active);

	public List<Enterprises> findByApplicationFormId(BigDecimal application_id);

	public List<Enterprises> findByActiveAndApplicationFormId(boolean active, Long appformid);

	public Enterprises findByIdAndPrefCorp(long entid, String pref_corp);

	public Enterprises findByApplicationFormId(long formApplicationId);

	public Enterprises findByIdAndActive(long id, boolean active);

}