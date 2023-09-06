package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;


@Entity
@Table(name="FUND_TRANSFER_LOGS")
public class FundTransferLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_FTL_SEQ")
	@SequenceGenerator(sequenceName = "FUND_TRANSFER_LOGS_SEQ", allocationSize = 1, name = "SME_FTL_SEQ")
	@Column(unique=true, nullable=false, precision=38)
	private long id;

	@Column(length=255)
	private String amount;

	@Column(name="APPROVED_BY", length=255)
	private String approvedBy;

	@Column(name="CREATED_AT", nullable=false)
	private Timestamp createdAt;

	@Column(name="CREATED_BY", length=255)
	private String createdBy;

	@Column(name="ENTERPRISE_NAME", length=255)
	private String enterpriseName;

	@Column(name="FINAL_APPROVAL_BY", length=255)
	private String finalApprovalBy;

	@Column(name="FROM_ACCOUNT", length=255)
	private String fromAccount;

	@Column(name="IMPS_DETAILS", length=255)
	private String impsDetails;

	@Column(name="OPERATION", length=255)
	private String operation;

	@Column(name="REF_NO", length=255)
	private String refNo;

	@Column(name="TO_ACCOUNT", length=255)
	private String toAccount;

	@Column(name="TRANSFER_RESPONSE", length=255)
	private String transferResponse;

	@Column(name="UPDATED_AT", nullable=false)
	private Timestamp updatedAt;

	public FundTransferLog() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getApprovedBy() {
		return this.approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Object getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEnterpriseName() {
		return this.enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getFinalApprovalBy() {
		return this.finalApprovalBy;
	}

	public void setFinalApprovalBy(String finalApprovalBy) {
		this.finalApprovalBy = finalApprovalBy;
	}

	public String getFromAccount() {
		return this.fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getImpsDetails() {
		return this.impsDetails;
	}

	public void setImpsDetails(String impsDetails) {
		this.impsDetails = impsDetails;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRefNo() {
		return this.refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getToAccount() {
		return this.toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getTransferResponse() {
		return this.transferResponse;
	}

	public void setTransferResponse(String transferResponse) {
		this.transferResponse = transferResponse;
	}

	public Object getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}