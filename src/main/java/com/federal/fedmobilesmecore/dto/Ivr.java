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

/**
 * The persistent class for the IVRS database table.
 * 
 */
@Entity
@Table(name = "IVRS")
public class Ivr implements Serializable {

	private static final long serialVersionUID = -6633712097898821172L;

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_IVR_SEQ")
	@SequenceGenerator(sequenceName = "IVRS_SEQ", allocationSize = 1, name = "SME_IVR_SEQ")
	private Long id;

	@Column(name = "APP_HASH")
	private String appHash;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	private String mobile;

	private String otp;

	private String status;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	public Ivr() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppHash() {
		return this.appHash;
	}

	public void setAppHash(String appHash) {
		this.appHash = appHash;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtp() {
		return this.otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}