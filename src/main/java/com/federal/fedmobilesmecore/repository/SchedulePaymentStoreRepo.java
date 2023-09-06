/**
 * 
 */
package com.federal.fedmobilesmecore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ScheduledPaymentStore;

/**
 * @author Vikas
 *
 */
@Repository
public interface SchedulePaymentStoreRepo extends JpaRepository<ScheduledPaymentStore, Long> {

	Optional<ScheduledPaymentStore> findByTransactionId(String transactionId);

}
