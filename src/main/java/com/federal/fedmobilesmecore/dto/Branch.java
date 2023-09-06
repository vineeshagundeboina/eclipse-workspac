package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the BRANCHES database table.
 * 
 */
@Entity
@Table(name = "BRANCHES")
public class Branch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_BRANCH_SEQ")
	@SequenceGenerator(sequenceName = "BRANCHES_SEQ", allocationSize = 1, name = "SME_BRANCH_SEQ")
	private long id;

	@Column(name = "BRANCH_CODE")
	private String branchCode;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "REG_CD")
	private String regCd;

	@Column(name = "SOL_ID")
	private String solId;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "ZONE_CODE")
	private String zoneCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getRegCd() {
		return regCd;
	}

	public void setRegCd(String regCd) {
		this.regCd = regCd;
	}

	public String getSolId() {
		return solId;
	}

	public void setSolId(String solId) {
		this.solId = solId;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}