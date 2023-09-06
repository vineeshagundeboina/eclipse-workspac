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
@Table(name = "BANK_IFSCS")
public class BankIfsc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 38)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_IFSC_SEQ")
	@SequenceGenerator(sequenceName = "BANK_IFSCS_SEQ", allocationSize = 1, name = "SME_IFSC_SEQ")
	private long id;

	@Column(length = 255)
	private String address;

	@Column(name = "BANK_NAME", length = 255)
	private String bankName;

	@Column(name = "BRANCH_NAME", length = 255)
	private String branchName;

	@Column(name = "CITY_NAME", length = 255)
	private String cityName;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(length = 255)
	private String district;

	@Column(name = "IFSC_CODE", length = 255)
	private String ifscCode;

	@Column(name = "STATE", length = 255)
	private String state;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
