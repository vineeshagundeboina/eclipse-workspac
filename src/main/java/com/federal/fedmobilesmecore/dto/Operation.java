package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;


import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "OPERATIONS")
public class Operation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_OPER_SEQ")
	@SequenceGenerator(sequenceName = "OPERATIONS_SEQ", allocationSize = 1, name = "SME_OPER_SEQ")
	private long id;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "CURRENT_SIGNATURE_COUNT")
	private String currentSignatureCount;

	@Column(name = "OPERATION_NAME")
	private String operationName;

	@Column(name = "REQUIRED_SIGNATURE_COUNT")
	private String requiredSignatureCount;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "USER_ID")
	private String userId;

	// bi-directional many-to-one association to BulkUpload
	@OneToMany(mappedBy = "operation")
	private List<BulkUpload> bulkUploads;

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

	public String getCurrentSignatureCount() {
		return currentSignatureCount;
	}

	public void setCurrentSignatureCount(String currentSignatureCount) {
		this.currentSignatureCount = currentSignatureCount;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getRequiredSignatureCount() {
		return requiredSignatureCount;
	}

	public void setRequiredSignatureCount(String requiredSignatureCount) {
		this.requiredSignatureCount = requiredSignatureCount;
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

	public List<BulkUpload> getBulkUploads() {
		return bulkUploads;
	}

	public void setBulkUploads(List<BulkUpload> bulkUploads) {
		this.bulkUploads = bulkUploads;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}