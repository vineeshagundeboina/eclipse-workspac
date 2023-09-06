package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "AUDIT_LOGS")
public class AuditLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_AUDIT_SEQ")
	@SequenceGenerator(sequenceName = "AUDIT_LOGS_SEQ", allocationSize = 1, name = "SME_AUDIT_SEQ")
	private long id;

	@Column(name = "AMOUNT", length = 255)
	private String amount;

	@Column(name = "BENEFICIARY_ACC_NO", length = 255)
	private String beneficiaryAccNo;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "ENTERPRISE_ID", length = 255)
	private String enterpriseId;

	@Column(name = "[MODE]", length = 255)
	private String mode;

	@Column(name = "PREF_CORP", length = 255)
	private String prefCorp;

	@Column(name = "REF_NO", length = 255)
	private String refNo;

	@Column(name = "REMITTER_ACC_NO", length = 255)
	private String remitterAccNo;

	@Column(name = "RESPONSE_CODE", length = 255)
	private String responseCode;

	@Column(name = "SENDER_REF_NO", length = 255)
	private String senderRefNo;

	@Column(name = "TRANS_DATE", length = 255)
	private String transDate;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

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

	public String getBeneficiaryAccNo() {
		return beneficiaryAccNo;
	}

	public void setBeneficiaryAccNo(String beneficiaryAccNo) {
		this.beneficiaryAccNo = beneficiaryAccNo;
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

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getRemitterAccNo() {
		return remitterAccNo;
	}

	public void setRemitterAccNo(String remitterAccNo) {
		this.remitterAccNo = remitterAccNo;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getSenderRefNo() {
		return senderRefNo;
	}

	public void setSenderRefNo(String senderRefNo) {
		this.senderRefNo = senderRefNo;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}