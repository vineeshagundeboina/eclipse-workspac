package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * The persistent class for the FUND_TRANSFER_STORES database table.
 * 
 */
@Entity
@Table(name = "FUND_TRANSFER_STORES")
public class FundTransferStore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_FTS_SEQ")
	@SequenceGenerator(sequenceName = "FUND_TRANSFER_STORES_SEQ", allocationSize = 1, name = "SME_FTS_SEQ")
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

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "ENTERPRISE_ID", length = 255)
	private String enterpriseId;

	@Column(name = "[MODE]", length = 255)
	private String mode;

	@Column(name = "REM_ACC_NO", length = 255)
	private String remAccNo;

	@Column(name = "REM_CUST_ID", length = 255)
	private String remCustId;

	@Column(name = "REM_MMID", length = 255)
	private String remMmid;

	@Column(name = "REM_MOB_NUM", length = 255)
	private String remMobNum;

	@Column(name = "REM_NAME", length = 255)
	private String remName;

	@Column(length = 255)
	private String remarks;

	@Column(length = 255)
	private String signature;

	@Column(length = 255)
	private String status;

	@Column(name = "TRANSACTION_DATE")
	private Timestamp transactionDate;

	@Column(name = "TRANSACTION_ID", length = 255)
	private String transactionId;

	@Column(name = "TRANSFER_TYPE", length = 255)
	private String transferType;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	public FundTransferStore() {
	}

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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}