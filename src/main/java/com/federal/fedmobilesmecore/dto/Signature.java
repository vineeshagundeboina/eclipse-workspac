package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SIGNATURES")
public class Signature implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_SIG_SEQ")
	@SequenceGenerator(sequenceName = "SIGNATURES_SEQ", allocationSize = 1, name = "SME_SIG_SEQ")
	private long id;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "OPERATION_ID")
	private String operationId;

	@Column(name = "PREF_NO")
	private String prefNo;

	private String status;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_NAME")
	private String userName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}