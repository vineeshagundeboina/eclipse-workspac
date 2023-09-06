/**
 * 
 */
package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ScheduledPayment;

/**
 * @author Vikas
 *
 */
@Repository
public interface ScheduledPaymentRepo extends JpaRepository<ScheduledPayment, Long> {

//	@Query("select  coalesce(sum(st.amount),0) from ScheduledPayment sp, ScheduledTransaction st where st.scheduledPaymentId=sp.id and sp.enterpriseId=?3 and sp.status='approved' and st.status='approved' and st.transactionDate between ?1 and ?2")
//	public BigDecimal getApprovedSCPayAmountBetweenDates(Timestamp fromdate, Timestamp todate, String enterprise_id);

	@Query("select  coalesce(sum(st.amount),0) from ScheduledPayment sp, ScheduledTransaction st where st.scheduledPaymentId=sp.id and sp.enterpriseId=?3 and sp.status='approved' and st.status='approved' and st.transactionDate between ?1 and ?2 and st.responseCode in (?4)")
	public BigDecimal getApprovedSCPayAmountBetweenDates(Timestamp fromdate, Timestamp todate, String enterprise_id,List<String> codes);
	
	public List<ScheduledPayment> findByStatus(String status);

	//public List<ScheduledPayment> findByStatusAndEnterpriseIdOrderByCreatedDesc(String status, String enterprise_id);

	public List<ScheduledPayment> findByStatusAndEnterpriseIdAndIsCompletedAndIsCancelled(String status,
			String enterprise_id, BigDecimal iscompleted, BigDecimal iscancelled);

	public List<ScheduledPayment> findByStatusAndIsJobActiveAndIsCompletedAndIsCancelled(String status, BigDecimal jobactive,
			BigDecimal iscompleted, BigDecimal iscancelled);

	public List<ScheduledPayment> findByStatusAndEnterpriseIdOrderByCreatedAtDesc(String status, String enterprise_id);

}
