package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Branch;

@Repository
public interface GetCustomerBranchRepo extends JpaRepository<Branch, Long>{
	
	public Branch findFirstBySolId(String solid);

}
