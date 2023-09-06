package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the SCHEDULED_PAYMENT_STORES database table.
 * 
 */
@Entity
@Table(name = "SCHEDULED_PAYMENT_STORES")
@NamedQuery(name = "ScheduledPaymentStore.findAll", query = "SELECT s FROM ScheduledPaymentStore s")
public class ScheduledPaymentStore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_SEQ_SPS")
	@SequenceGenerator(sequenceName = "SCHEDULED_PAYMENT_STORES_SEQ", allocationSize = 1, name = "SME_SEQ_SPS")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(length = 255)
	private String amount;

	@Column(name = "BEN_AADHAR", length = 255)
	private String benAadhar;

	@Column(name = "BEN_ACC_NO", length = 255)
	private String benAccNo;

	@Column(name = "BEN_ACC_TYPE", length = 255)
	private String benAccType;

	@Column(name = "BEN_CUST_NAME", length = 255)
	private String benCustName;

	@Column(name = "BEN_IFSC", length = 255)
	private String benIfsc;

	@Column(name = "BEN_MMID", length = 255)
	private String benMmid;

	@Column(name = "BEN_MOB_NUM", length = 255)
	private String benMobNum;

	@Column(name = "BEN_NAME", length = 255)
	private String benName;

	@Column(name = "BEN_NICK_NAME", length = 255)
	private String benNickName;

	@Column(name = "[COUNT]", precision = 38)
	private BigDecimal count;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "ENTERPRISE_ID", length = 255)
	private String enterpriseId;

	@Column(length = 255)
	private String frequency;

	@Column(name = "[MODE]", length = 255)
	private String mode;

	@Column(name = "NEXT_EXECUTION_DATE")
	private Date nextExecutionDate;

	@Column(name = "REM_ACC_NO", length = 255)
	private String remAccNo;

	@Column(name = "REM_CUST_ID", length = 255)
	private String remCustId;

	@Column(name = "REM_MMID", length = 255)
	private String remMmid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBenAadhar() {
		return benAadhar;
	}

	public void setBenAadhar(String benAadhar) {
		this.benAadhar = benAadhar;
	}

	public String getBenAccNo() {
		return benAccNo;
	}

	public void setBenAccNo(String benAccNo) {
		this.benAccNo = benAccNo;
	}

	public String getBenAccType() {
		return benAccType;
	}

	public void setBenAccType(String benAccType) {
		this.benAccType = benAccType;
	}

	public String getBenCustName() {
		return benCustName;
	}

	public void setBenCustName(String benCustName) {
		this.benCustName = benCustName;
	}

	public String getBenIfsc() {
		return benIfsc;
	}

	public void setBenIfsc(String benIfsc) {
		this.benIfsc = benIfsc;
	}

	public String getBenMmid() {
		return benMmid;
	}

	public void setBenMmid(String benMmid) {
		this.benMmid = benMmid;
	}

	public String getBenMobNum() {
		return benMobNum;
	}

	public void setBenMobNum(String benMobNum) {
		this.benMobNum = benMobNum;
	}

	public String getBenName() {
		return benName;
	}

	public void setBenName(String benName) {
		this.benName = benName;
	}

	public String getBenNickName() {
		return benNickName;
	}

	public void setBenNickName(String benNickName) {
		this.benNickName = benNickName;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Date getNextExecutionDate() {
		return nextExecutionDate;
	}

	public void setNextExecutionDate(Date nextExecutionDate) {
		this.nextExecutionDate = nextExecutionDate;
	}

	public String getRemAccNo() {
		return remAccNo;
	}

	public void setRemAccNo(String remAccNo) {
		this.remAccNo = remAccNo;
	}

	public String getRemCustId() {
		return remCustId;
	}

	public void setRemCustId(String remCustId) {
		this.remCustId = remCustId;
	}

	public String getRemMmid() {
		return remMmid;
	}

	public void setRemMmid(String remMmid) {
		this.remMmid = remMmid;
	}

	public String getRemMobNum() {
		return remMobNum;
	}

	public void setRemMobNum(String remMobNum) {
		this.remMobNum = remMobNum;
	}

	public String getRemName() {
		return remName;
	}

	public void setRemName(String remName) {
		this.remName = remName;
	}

	public BigDecimal getRemainingCount() {
		return remainingCount;
	}

	public void setRemainingCount(BigDecimal remainingCount) {
		this.remainingCount = remainingCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(name = "REM_MOB_NUM", length = 255)
	private String remMobNum;

	@Column(name = "REM_NAME", length = 255)
	private String remName;

	@Column(name = "REMAINING_COUNT", precision = 38)
	private BigDecimal remainingCount;

	@Column(length = 255)
	private String remarks;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(length = 255)
	private String status;

	@Column(name = "TRANSACTION_ID", length = 255)
	private String transactionId;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	public ScheduledPaymentStore() {
	}

}