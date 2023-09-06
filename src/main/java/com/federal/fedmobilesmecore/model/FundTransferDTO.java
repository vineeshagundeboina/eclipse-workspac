package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;

public class FundTransferDTO {

	private long id;
	private String amount;
	private String benAccNo;
	private String benIfsc;
	private String benNickName;
	private Timestamp createdAt;
	private String enterpriseId;
	private boolean isDuplicate;
	private String mode;
	private String operationId;
	private String progress;
	private String refNo;
	private String remName;
	private String remarks;
	private String senderRefId;
	private String status;
	private Timestamp transactionDate;
	private String transferType;
	private Timestamp updatedAt;
	private String responseCode;
	private String responseReason;
	private String gatewayStatus;

	// FTSPECIFIC
	private String benAccType;
	private String benName;
	private String remAccNo;

	// IMPS SPECIFIC
	private String benAadhar;
	private String benCustName;
	private String benMmid;
	private String benMobNum;
	private String remAccNum;
	private String remCustId;
	private String remMmid;
	private String remMobNum;

	// TO Differentiate FT & IMPS
	@Schema(description = "FT/IMPS", type = "string", example = "FT")
	private String type;

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

	public String getBenIfsc() {
		return benIfsc;
	}

	public void setBenIfsc(String benIfsc) {
		this.benIfsc = benIfsc;
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

	public boolean isDuplicate() {
		return isDuplicate;
	}

	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getRemAccNo() {
		return remAccNo;
	}

	public void setRemAccNo(String remAccNo) {
		this.remAccNo = remAccNo;
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

	public String getBenAadhar() {
		return benAadhar;
	}

	public void setBenAadhar(String benAadhar) {
		this.benAadhar = benAadhar;
	}

	public String getBenCustName() {
		return benCustName;
	}

	public void setBenCustName(String benCustName) {
		this.benCustName = benCustName;
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

	public String getRemAccNum() {
		return remAccNum;
	}

	public void setRemAccNum(String remAccNum) {
		this.remAccNum = remAccNum;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getGatewayStatus() {
		return gatewayStatus;
	}

	public void setGatewayStatus(String gatewayStatus) {
		this.gatewayStatus = gatewayStatus;
	}

	@Override
	public String toString() {
		return "FundTransferDTO [id=" + id + ", amount=" + amount + ", benAccNo=" + benAccNo + ", benIfsc=" + benIfsc
				+ ", benNickName=" + benNickName + ", createdAt=" + createdAt + ", enterpriseId=" + enterpriseId
				+ ", isDuplicate=" + isDuplicate + ", mode=" + mode + ", operationId=" + operationId + ", progress="
				+ progress + ", refNo=" + refNo + ", remName=" + remName + ", remarks=" + remarks + ", senderRefId="
				+ senderRefId + ", status=" + status + ", transactionDate=" + transactionDate + ", transferType="
				+ transferType + ", updatedAt=" + updatedAt + ", responseCode=" + responseCode + ", responseReason="
				+ responseReason + ", gatewayStatus=" + gatewayStatus + ", benAccType=" + benAccType + ", benName="
				+ benName + ", remAccNo=" + remAccNo + ", benAadhar=" + benAadhar + ", benCustName=" + benCustName
				+ ", benMmid=" + benMmid + ", benMobNum=" + benMobNum + ", remAccNum=" + remAccNum + ", remCustId="
				+ remCustId + ", remMmid=" + remMmid + ", remMobNum=" + remMobNum + ", type=" + type + "]";
	}
}
