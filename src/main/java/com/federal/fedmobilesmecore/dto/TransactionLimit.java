package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;


/**
 * The persistent class for the TRANSACTION_LIMITS database table.
 * 
 */
@Entity
@Table(name="TRANSACTION_LIMITS")
public class TransactionLimit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_TL_SEQ")
	@SequenceGenerator(sequenceName = "TRANSACTION_LIMITS_SEQ", allocationSize = 1, name = "SME_TL_SEQ")
	private long id;

	@Column(length=255)
	private String amount;

	@Column(name="CREATED_AT", nullable=false)
	private Timestamp createdAt;

	@Column(name="ENTERPRISE_ID", length=255)
	private String enterpriseId;

	@Column(name = "[MODE]", length = 255)
	private String mode;

	@Column(name="UPDATED_AT", nullable=false)
	private Timestamp updatedAt;

	public TransactionLimit() {
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

	public Object getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Object getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}