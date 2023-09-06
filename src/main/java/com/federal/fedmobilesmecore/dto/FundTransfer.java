package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the FUND_TRANSFERS database table.
 * 
 */
@Entity
@Table(name = "FUND_TRANSFERS")
//@NamedQuery(name="FundTransfer.findAll", query="SELECT f FROM FundTransfer f")
public class FundTransfer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_FT_SEQ")
	@SequenceGenerator(sequenceName = "FUND_TRANSFERS_SEQ", allocationSize = 1, name = "SME_FT_SEQ")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(length = 255)
	private String amount;

	@Column(name = "BEN_ACC_NO", length = 255)
	private String benAccNo;

	@Column(name = "BEN_ACC_TYPE", length = 255)
	private String benAccType;

	@Column(name = "BEN_IFSC", length = 255)
	private String benIfsc;

	@Column(name = "BEN_NAME", length = 255)
	private String benName;

	@Column(name = "BEN_NICK_NAME", length = 255)
	private String benNickName;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "ENTERPRISE_ID", length = 255)
	private String enterpriseId;

	@Column(name = "IS_DUPLICATE", precision = 1)
	private boolean isDuplicate;

	@Column(name = "[MODE]", length = 255)
	private String mode;

	@Column(name = "OPERATION_ID", length = 255)
	private String operationId;

	@Column(length = 255)
	private String progress;

	@Column(name = "REF_NO", length = 255)
	private String refNo;

	@Column(name = "REM_ACC_NO", length = 255)
	private String remAccNo;

	@Column(name = "REM_NAME", length = 255)
	private String remName;

	@Column(length = 255)
	private String remarks;

	@Column(name = "SENDER_REF_ID", length = 255)
	private String senderRefId;

	@Column(length = 255)
	private String status;

	@Column(name = "TRANSACTION_DATE")
	private Timestamp transactionDate;

	@Column(name = "TRANSFER_TYPE", length = 255)
	private String transferType;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	@Column(name = "RESPONSE_CODE")
	private String responseCode;

	@Column(name = "RESPONSE_REASON")
	private String responseReason;

	@Column(name = "GATEWAY_STATUS")
	private String gatewayStatus;
	
	@Column(name="IP_ADDRESS")
	private String ipAddress;
	
	@Column(name="GEO_LOCATION")
	private String geoLocation;
	
	@Column(name="CHANNEL_FLAG")
	@Enumerated(EnumType.STRING)
	private ChannelFlag channelFlag;

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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public ChannelFlag getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(ChannelFlag channelFlag) {
		this.channelFlag = channelFlag;
	}

}