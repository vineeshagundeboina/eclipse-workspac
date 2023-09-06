package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "PARSED_TRANSACTIONS")
public class ParsedTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_PAR_SEQ")
	@SequenceGenerator(sequenceName = "PARSED_TRANSACTIONS_SEQ", allocationSize = 1, name = "SME_PAR_SEQ")
	private long id;

	private BigDecimal amount;

	@Column(name = "BENEFICIARY_ACCOUNT_NO")
	private String beneficiaryAccountNo;

	@Column(name = "BENEFICIARY_ADDRESS")
	private String beneficiaryAddress;

	@Column(name = "BENEFICIARY_EMAIL")
	private String beneficiaryEmail;

	@Column(name = "BENEFICIARY_ID")
	private String beneficiaryId;

	@Column(name = "BENEFICIARY_IFSC")
	private String beneficiaryIfsc;

	@Column(name = "BENEFICIARY_MMID")
	private String beneficiaryMmid;

	@Column(name = "BENEFICIARY_MOBILE")
	private String beneficiaryMobile;

	@Column(name = "BENEFICIARY_NAME")
	private String beneficiaryName;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Lob
	@Column(name = "ERROR_DESCRIPTION")
	private String errorDescription;

	@Column(name = "FILE_ID")
	private String fileId;

	@Column(name = "IS_JOB_ACTIVE")
	private BigDecimal isJobActive;

	@Column(name = "LOCK_VERSION")
	private BigDecimal lockVersion;

	@Column(name = "OFFICE_ID")
	private String officeId;

	@Column(name = "PAYMENT_DESCRIPTION")
	private String paymentDescription;

	private String remarks;

	@Column(name = "REMITTER_ACCOUNT_NO")
	private String remitterAccountNo;

	@Column(name = "REMITTER_NAME")
	private String remitterName;

	@Column(name = "RESPONSE_CODE")
	private String responseCode;

	@Lob
	@Column(name = "RESPONSE_REASON")
	private String responseReason;

	@Column(name = "SCHEDULE")
	private Timestamp schedule;

	@Column(name = "SENDER_REF_ID")
	private String senderRefId;

	private String status;

	@Column(name = "TRANSACTION_DATE")
	private Timestamp transactionDate;

	@Column(name = "TRANSACTION_ID")
	private String transactionId;

	@Column(name = "TRANSACTION_MODE")
	private String transactionMode;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BULK_UPLOAD_ID")
	private BulkUpload bulkUpload;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBeneficiaryAccountNo() {
		return beneficiaryAccountNo;
	}

	public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
		this.beneficiaryAccountNo = beneficiaryAccountNo;
	}

	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}

	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	public String getBeneficiaryEmail() {
		return beneficiaryEmail;
	}

	public void setBeneficiaryEmail(String beneficiaryEmail) {
		this.beneficiaryEmail = beneficiaryEmail;
	}

	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public String getBeneficiaryIfsc() {
		return beneficiaryIfsc;
	}

	public void setBeneficiaryIfsc(String beneficiaryIfsc) {
		this.beneficiaryIfsc = beneficiaryIfsc;
	}

	public String getBeneficiaryMmid() {
		return beneficiaryMmid;
	}

	public void setBeneficiaryMmid(String beneficiaryMmid) {
		this.beneficiaryMmid = beneficiaryMmid;
	}

	public String getBeneficiaryMobile() {
		return beneficiaryMobile;
	}

	public void setBeneficiaryMobile(String beneficiaryMobile) {
		this.beneficiaryMobile = beneficiaryMobile;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public BigDecimal getIsJobActive() {
		return isJobActive;
	}

	public void setIsJobActive(BigDecimal isJobActive) {
		this.isJobActive = isJobActive;
	}

	public BigDecimal getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(BigDecimal lockVersion) {
		this.lockVersion = lockVersion;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getPaymentDescription() {
		return paymentDescription;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemitterAccountNo() {
		return remitterAccountNo;
	}

	public void setRemitterAccountNo(String remitterAccountNo) {
		this.remitterAccountNo = remitterAccountNo;
	}

	public String getRemitterName() {
		return remitterName;
	}

	public void setRemitterName(String remitterName) {
		this.remitterName = remitterName;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseReason() {
		return responseReason;
	}

	public void setResponseReason(String responseReason) {
		this.responseReason = responseReason;
	}

	public Timestamp getSchedule() {
		return schedule;
	}

	public void setSchedule(Timestamp schedule) {
		this.schedule = schedule;
	}

	public String getSenderRefId() {
		return senderRefId;
	}

	public void setSenderRefId(String senderRefId) {
		this.senderRefId = senderRefId;
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

	public String getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public BulkUpload getBulkUpload() {
		return bulkUpload;
	}

	public void setBulkUpload(BulkUpload bulkUpload) {
		this.bulkUpload = bulkUpload;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}