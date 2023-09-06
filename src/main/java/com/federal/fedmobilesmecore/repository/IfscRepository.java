package com.federal.fedmobilesmecore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.BankIfsc;

@Repository
public interface IfscRepository extends JpaRepository<BankIfsc, Long> {
	public Optional<BankIfsc> findByIfscCode(String ifscCode);

	@Query(value = "select Distinct bi.bankName from BankIfsc bi")
	public List<String> getAllBankName();

	@Query(value = "select DISTINCT(bi.state) as state from BankIfsc bi where bi.bankName = ?1")
	public List<String> findStateByBankName(String bankName);

	@Query(value = "select DISTINCT(bi.district) as district from BankIfsc bi where bi.bankName = ?1 and bi.state = ?2")
	public List<String> findDistrictByBankNameAndState(String bankName, String state);

	@Query(value = "select DISTINCT(bi.cityName) as cityName from BankIfsc bi where bi.bankName = ?1 and bi.state = ?2 and bi.district = ?3")
	public List<String> findCityByBankNameAndStateAndDistrict(String bankName, String state, String district);

	@Query(value = "select bi.branchName from BankIfsc bi where bi.bankName = ?1 and bi.state = ?2 and bi.district = ?3 and bi.cityName = ?4")
	public List<String> findBranchByBankNameAndStateAndDistrictAndCity(String bankName, String state, String district,
			String cityName);
	@Query(value = "select bi.ifscCode from BankIfsc bi where bi.bankName = ?1 and bi.state = ?2 and bi.district = ?3 and bi.cityName = ?4 and bi.branchName = ?5")
	public List<String> findIfscCodeByBranch(String bankName,String state, String district, String city,String branchName);}