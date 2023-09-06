package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "BULK_UPLOADS")
public class BulkUpload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_BULK_SEQ")
	@SequenceGenerator(sequenceName = "BULK_UPLOADS_SEQ", allocationSize = 1, name = "SME_BULK_SEQ")
	private long id;

	@Column(name = "ACCOUNT_NO")
	private String accountNo;

	private BigDecimal amount;

	private String attachment;

	@Column(name = "BATCH_NO")
	private BigDecimal batchNo;

	private String checksum;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "ENTERPRISE_ID")
	private BigDecimal enterpriseId;

	@Lob
	@Column(name = "ERROR_DESCRIPTION")
	private String errorDescription;

	@Column(name = "FILE_ID")
	private String fileId;

	@Column(name = "FILE_STATUS")
	private String fileStatus;

	@Column(name = "IS_JOB_ACTIVE")
	private BigDecimal isJobActive;

	@Column(name = "LOCK_VERSION")
	private BigDecimal lockVersion;

	@Column(name = "MAKER_ID")
	private BigDecimal makerId;

	@Column(name = "REF_NO")
	private String refNo;

	private String remarks;

	private String status;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	// bi-directional many-to-one association to Operation
	@ManyToOne(fetch = FetchType.LAZY)
	private Operation operation;

	// bi-directional many-to-one association to ParsedTransaction
	@OneToMany(mappedBy = "bulkUpload")
	private List<ParsedTransaction> parsedTransactions;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public BigDecimal getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(BigDecimal batchNo) {
		this.batchNo = batchNo;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public BigDecimal getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(BigDecimal enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
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

	public BigDecimal getMakerId() {
		return makerId;
	}

	public void setMakerId(BigDecimal makerId) {
		this.makerId = makerId;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public List<ParsedTransaction> getParsedTransactions() {
		return parsedTransactions;
	}

	public void setParsedTransactions(List<ParsedTransaction> parsedTransactions) {
		this.parsedTransactions = parsedTransactions;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}