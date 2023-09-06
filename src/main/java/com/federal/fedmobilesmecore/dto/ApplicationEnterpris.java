package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "APPLICATION_ENTERPRISES")
public class ApplicationEnterpris implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "idSequence")
	@SequenceGenerator(schema = "SMEDBA", name = "idSequence", sequenceName = "APPLICATION_ENTERPRISES_SEQ", allocationSize = 1)
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(name = "ACC_NAME", length = 255)
	private String accName;

	@Column(name = "ACC_NO", length = 255)
	private String accNo;

	@Column(name = "ACC_TYPE", length = 255)
	private String accType;

	@Column(precision = 1)
	private boolean active;

	@Column(length = 255)
	private String address;

	@Column(name = "APPLICATION_FORM_ID", precision = 38)
	private Long applicationFormId;

	@Column(name = "AUTH_BEN", length = 255)
	private String authBen;

	@Column(name = "AUTH_EXT", length = 255)
	private String authExt;

	@Column(name = "AUTH_FUND", length = 255)
	private String authFund;

	@Column(name = "BRANCH_ID", precision = 38)
	private Long branchId;

	@Column(length = 255)
	private String constitution;

	@Column(name = "COOLING_PERIOD", precision = 38)
	private Long coolingPeriod;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "CUST_NO", length = 255)
	private String custNo;

	@Column(name = "DAILY_LIMIT", precision = 38)
	private Long dailyLimit;

	@Column(length = 255)
	private String email;

	@Column(length = 255)
	private String mobile;

	@Column(name = "MONTHLY_LIMIT", precision = 38)
	private Long monthlyLimit;

	@Column(length = 255)
	private String pin;

	@Column(name = "PREF_CORP", length = 255)
	private String prefCorp;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getApplicationFormId() {
		return applicationFormId;
	}

	public void setApplicationFormId(Long applicationFormId) {
		this.applicationFormId = applicationFormId;
	}

	public String getAuthBen() {
		return authBen;
	}

	public void setAuthBen(String authBen) {
		this.authBen = authBen;
	}

	public String getAuthExt() {
		return authExt;
	}

	public void setAuthExt(String authExt) {
		this.authExt = authExt;
	}

	public String getAuthFund() {
		return authFund;
	}

	public void setAuthFund(String authFund) {
		this.authFund = authFund;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getConstitution() {
		return constitution;
	}

	public void setConstitution(String constitution) {
		this.constitution = constitution;
	}

	public Long getCoolingPeriod() {
		return coolingPeriod;
	}

	public void setCoolingPeriod(Long coolingPeriod) {
		this.coolingPeriod = coolingPeriod;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public Long getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(Long dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(Long monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
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