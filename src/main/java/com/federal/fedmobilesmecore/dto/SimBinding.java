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
 * The persistent class for the SIM_BINDINGS database table.
 * 
 */
@Entity
@Table(name = "SIM_BINDINGS")
//@NamedQuery(name="SimBinding.findAll", query="SELECT s FROM SimBinding s")
public class SimBinding implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_SIMB_SEQ")
	@SequenceGenerator(sequenceName = "SIM_BINDINGS_SEQ", allocationSize = 1, name = "SME_SIMB_SEQ")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(length = 255)
	private String mobile;

	@Column(name = "SIM_HASH", length = 255)
	private String simHash;

	@Column(name = "SIM_RANDOM_NO", length = 255)
	private String simRandomNo;

	@Column(length = 255)
	private String status;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSimHash() {
		return simHash;
	}

	public void setSimHash(String simHash) {
		this.simHash = simHash;
	}

	public String getSimRandomNo() {
		return simRandomNo;
	}

	public void setSimRandomNo(String simRandomNo) {
		this.simRandomNo = simRandomNo;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}