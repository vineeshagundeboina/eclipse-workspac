package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Enterprises;


/**
 * @author Sandeep Dash
 *
 */
@Repository
public interface EnterprisesRepository_V1 extends JpaRepository<Enterprises, Long> {

	public Enterprises findByActiveAndPrefCorp(boolean active, String prefCorp);

	public Enterprises findByPrefCorpAndActive(String pref_corp, boolean active);

	public Enterprises findByAccNo(String accNo);

	public Enterprises findByAccNoAndActive(String accNo, boolean active);

	public List<Enterprises> findByApplicationFormId(BigDecimal application_id);

	public List<Enterprises> findByActiveAndApplicationFormId(boolean active, Long appformid);

	public Enterprises findByIdAndPrefCorp(long entid, String pref_corp);

	public Enterprises findByApplicationFormId(long formApplicationId);

	
	Optional<Enterprises> findByPrefCorp(String pref_corp);

	Enterprises findByActiveAndId(boolean active, Long enterpriseId);
	

}