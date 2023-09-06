package com.federal.fedmobilesmecore.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.federal.fedmobilesmecore.dto.FundTransfer;

@Repository
public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {

	/*
	 * @Query("SELECT F FROM FundTransfer F" + "    WHERE" +
	 * "   F.status != 'error' AND F.status = 'pending' AND F.enterpriseId = ?1")
	 */
	public List<FundTransfer> findByStatusIsNotAndStatusAndEnterpriseId(String nstatus, String status,
			String enterprisesId);

//	@Query(value = "SELECT * FROM FUND_TRANSFERS WHERE ft_mode = ?1 AND enterprise_id = ?2 AND status != ?3 AND (CAST(CURRENT_TIMESTAMP AS DATE) - CAST(TRANSACTION_DATE AS DATE)) * 24 * 60 < 5 ORDER BY updated_at DESC", nativeQuery = true)
	@Query(value = "select ft from FundTransfer ft where ft.mode = ?1 and ft.enterpriseId = ?2 and ft.status <> ?3 and ft.transactionDate between ?4 and ?5 ORDER BY updatedAt DESC")
	public List<FundTransfer> findDuplicateFTRecordsLessThan5Min(String ftMode, String enterpriseId, String status,
			Timestamp fromdate, Timestamp todate);

	public Optional<FundTransfer> findByRefNo(String refno);
	
	@Query("select ft from FundTransfer ft where ft.status <> 'error' and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 and ft.transactionDate between ?4 and ?5 order by ft.updatedAt desc")
	public List<FundTransfer> getPendingListWithTime(String enterpriseId, String status, String transferType,Timestamp start,Timestamp end);

	@Query(value = "SELECT * FROM FUND_TRANSFERS WHERE enterprise_id = ?1 AND status != ?2 AND TRANSFER_TYPE= ?3 AND transaction_date BETWEEN (cast(trunc(sysdate) AS TIMESTAMP(9))) AND (cast(trunc(sysdate + 1) - 1 / (24*60*60)AS TIMESTAMP(9)))", nativeQuery = true)
	public List<FundTransfer> currentApprovedFundTransfers(String enterpriseId, String status, String transferType);

//	@Query("select coalesce(sum(ft.amount),0) from FundTransfer ft where ft.status <> 'error' and ft.status='approved' and ft.transactionDate between ?1 and ?2 and ft.enterpriseId=?3")
//	public BigDecimal getApprovedFTAmountBetweenDates(Timestamp fromdate, Timestamp todate, String enterpriseId);

	@Query("SELECT coalesce(SUM(ft.amount), 0) FROM FundTransfer ft WHERE ft.status <> 'error' AND ft.status = 'approved' AND ft.transactionDate BETWEEN :fromdate AND :todate AND ft.enterpriseId = :enterpriseId AND ft.responseCode IN (:codes) ")
	public BigDecimal getApprovedFTAmountBetweenDates(@Param("fromdate") Timestamp fromdate, @Param("todate") Timestamp todate, @Param("enterpriseId") String enterpriseId, @Param("codes") List<String> codes);
	
	@Query("select ft from FundTransfer ft where ft.status <> 'error' and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 order by ft.updatedAt desc")
	public List<FundTransfer> getPendingList(String enterpriseId, String status, String transferType);

	@Query("select ft from FundTransfer ft where ft.status <> 'error'  and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 order by ft.updatedAt desc")
	public Page<FundTransfer> getPendingList(String enterpriseId, String status, String transferType,
			Pageable pageable);

	@Query("select count(ft) from FundTransfer ft where ft.status <> 'error'  and ft.enterpriseId=?1 and ft.status=?2 and ft.transferType=?3 order by ft.updatedAt desc")
	public Long getCountPendingList(String enterpriseId, String status, String transferType);

	public boolean existsByAmountAndBenAccNoAndRemAccNoAndEnterpriseIdAndCreatedAtBetween(String amount,String benAcc,String remAcc,String entId,Timestamp start,Timestamp end);
	
	public boolean existsByStatusAndAmountAndBenAccNoAndRemAccNoAndEnterpriseIdAndCreatedAtBetween(String status,String amount,String benAcc,String remAcc,String entId,Timestamp start,Timestamp end);
	
}