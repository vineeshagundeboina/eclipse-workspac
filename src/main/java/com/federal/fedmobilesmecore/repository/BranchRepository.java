package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
	Branch findFirstBySolIdAndBranchCodeOrderByCreatedAtDesc(String solId, String branchName);
	Branch findFirstByBranchCodeOrderByCreatedAtDesc(String branchName);
}