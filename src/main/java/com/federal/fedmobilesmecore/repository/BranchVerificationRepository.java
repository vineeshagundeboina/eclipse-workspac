package com.federal.fedmobilesmecore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.BranchVerification;

@Repository
public interface BranchVerificationRepository extends JpaRepository<BranchVerification, Long> {

}
