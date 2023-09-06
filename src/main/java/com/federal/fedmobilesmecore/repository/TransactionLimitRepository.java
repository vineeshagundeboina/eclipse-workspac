package com.federal.fedmobilesmecore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.TransactionLimit;

@Repository
public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, Long> {

	Optional<TransactionLimit> findFirstByEnterpriseIdAndModeOrderByIdDesc(String id, String mode);

}