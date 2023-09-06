package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ScheduledTransaction;

@Repository
public interface ScheduledTransactionRepo extends JpaRepository<ScheduledTransaction, Long> {

	List<ScheduledTransaction> findByScheduledPaymentIdAndStatusInOrderByIdDesc(String payment_id, List<String> status);

	Optional<ScheduledTransaction> findByIdAndLockVersion(long scheduleTranId, BigDecimal lockversion);

}
