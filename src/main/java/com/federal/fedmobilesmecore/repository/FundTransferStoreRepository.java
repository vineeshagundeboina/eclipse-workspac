package com.federal.fedmobilesmecore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.FundTransferStore;

@Repository
public interface FundTransferStoreRepository extends JpaRepository<FundTransferStore, Long> {

	Optional<FundTransferStore> findByTransactionId(String transactionId);

}
