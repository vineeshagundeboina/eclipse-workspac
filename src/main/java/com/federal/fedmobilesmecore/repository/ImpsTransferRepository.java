package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.ImpsTransfer;

@Repository
public interface ImpsTransferRepository extends JpaRepository<ImpsTransfer, Long> {

	/*
	 * @Query("SELECT I FROM ImpsTransfer I" + "   WHERE" +
	 * "   I.status = 'pending' AND I.enterpriseId =?1 ")
	 */
	public List<ImpsTransfer> findByStatusAndEnterpriseId(String status, String enterprisesId);

//	@Query(value = "SELECT * FROM SMEDBA.IMPS_TRANSFERS WHERE IMPS_MODE = ?1 AND ENTERPRISE_ID = ?2 AND status != ?3 AND (CAST(CURRENT_TIMESTAMP AS DATE) - CAST(TRANSACTION_DATE AS DATE)) * 24 * 60 < 5 ORDER BY updated_at DESC", nativeQuery = true)
	@Query(value = "select it from ImpsTransfer it where it.mode = ?1 and it.enterpriseId = ?2 and it.status <> ?3 and it.transactionDate between ?4 and ?5 ORDER BY updatedAt DESC")
	public List<ImpsTransfer> findDuplicateIMPSRecordsLessThan5Min(String ftMode, String enterpriseId, String status,
			Timestamp fromdate, Timestamp todate);

	@Query(value = "SELECT * FROM SMEDBA.IMPS_TRANSFERS WHERE enterprise_id = ?1 AND status != ?2 AND TRANSFER_TYPE= ?3 AND transaction_date BETWEEN (cast(trunc(sysdate) AS TIMESTAMP(9))) AND (cast(trunc(sysdate + 1) - 1 / (24*60*60)AS TIMESTAMP(9)))", nativeQuery = true)
	public List<ImpsTransfer> currentApprovedImpsTransfers(String enterpriseId, String status, String transferType);

	@Query("select ft from ImpsTransfer ft where ft.status <> 'error'  and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 and ft.transactionDate between ?4 and ?5 order by ft.updatedAt desc")
	public List<ImpsTransfer> getApprovedListTime(String enterpriseId, String status, String transferType,Timestamp start,Timestamp end);
	
	
//	@Query("select  coalesce(sum(ft.amount),0) from ImpsTransfer ft where ft.status <> 'error' and ft.status='approved' and ft.transactionDate between ?1 and ?2 and ft.enterpriseId=?3")
//	public BigDecimal getApprovedFTAmountBetweenDates(Timestamp fromdate, Timestamp todate, String enterpriseId);

	@Query("select  coalesce(sum(ft.amount),0) from ImpsTransfer ft where ft.status <> 'error' and ft.status='approved' and ft.transactionDate between ?1 and ?2 and ft.enterpriseId=?3 and ft.responseCode in (?4)")
	public BigDecimal getApprovedFTAmountBetweenDates(Timestamp fromdate, Timestamp todate, String enterpriseId,List<String> codes);
	
	@Query("select ft from ImpsTransfer ft where ft.status <> 'error'  and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 order by ft.updatedAt desc")
	public List<ImpsTransfer> getPendingList(String enterpriseId, String status, String transferType);

	@Query("select ft from ImpsTransfer ft where ft.status <> 'error'  and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 order by ft.updatedAt desc")
	public Page<ImpsTransfer> getPendingList(String enterpriseId, String status, String transferType,
			Pageable pageable);

	@Query("select count(ft) from ImpsTransfer ft where ft.status <> 'error'  and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 order by ft.updatedAt desc")
	public Long getCountPendingList(String enterpriseId, String status, String transferType);

	public Optional<ImpsTransfer> findByRefNo(String ref_no);
	
	public boolean existsByAmountAndBenAccNoAndRemAccNumAndEnterpriseIdAndCreatedAtBetween(String amount,String benAcc,String remAcc,String entId,Timestamp start,Timestamp end);

	public boolean existsByAmountAndBenMobNumAndRemAccNumAndEnterpriseIdAndCreatedAtBetween(String amount,String benMobNum,String remAcc,String entId,Timestamp start,Timestamp end);
	
	public boolean existsByStatusAndAmountAndBenAccNoAndRemAccNumAndEnterpriseIdAndCreatedAtBetween(String status,String amount, String benAccNo, String remAccNum, String enterpriseId, Timestamp past, Timestamp current);

	public boolean existsByStatusAndAmountAndBenMobNumAndRemAccNumAndEnterpriseIdAndCreatedAtBetween(String string,String amount, String benMobNum, String remAccNum, String enterpriseId, Timestamp past, Timestamp current);
}