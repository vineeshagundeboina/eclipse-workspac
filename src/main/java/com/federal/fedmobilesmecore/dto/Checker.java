package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "CHECKERS")
public class Checker implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_CHECK_SEQ")
	@SequenceGenerator(sequenceName = "CHECKERS_SEQ", allocationSize = 1, name = "SME_CHECK_SEQ")
	private long id;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "SIGNATURE_ID")
	private String signatureId;

	private String status;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "USER_ID")
	private String userId;

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

	public String getSignatureId() {
		return signatureId;
	}

	public void setSignatureId(String signatureId) {
		this.signatureId = signatureId;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}