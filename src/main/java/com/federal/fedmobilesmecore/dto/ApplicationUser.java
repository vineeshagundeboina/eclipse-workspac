package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "APPLICATION_USERS")
public class ApplicationUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 38)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_AP_USER_SEQ")
	@SequenceGenerator(sequenceName = "APPLICATION_USERS_SEQ", allocationSize = 1, name = "SME_AP_USER_SEQ")
	private long id;

	@Column(name = "ACC_NO", length = 255)
	private String accNo;

	@Column(precision = 1)
	private boolean active;

	@Column(name = "APPLICATION_ENTERPRISE_ID", length = 255)
	private String applicationEnterpriseId;

	@Column(name = "AUTHORIZED_SIGNATORY", precision = 1)
	private Long authorizedSignatory;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "CUST_NO", length = 255)
	private String custNo;

	@Column(length = 255)
	private String email;

	@Column(length = 255)
	private String mobile;

	@Column(length = 255)
	private String otp;

	@Column(name = "OTP_EXPIRED_AT")
	private Timestamp otpExpiredAt;

	@Column(name = "PREF_NO", length = 255)
	private String prefNo;

	@Column(name = "ROLE", length = 255)
	private String role;

	@Column(name = "TRANS_LIMIT", length = 255)
	private String transLimit;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	@Column(name = "USER_NAME", length = 255)
	private String userName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public Long getAuthorizedSignatory() {
		return authorizedSignatory;
	}

	public void setAuthorizedSignatory(Long authorizedSignatory) {
		this.authorizedSignatory = authorizedSignatory;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getApplicationEnterpriseId() {
		return applicationEnterpriseId;
	}

	public void setApplicationEnterpriseId(String applicationEnterpriseId) {
		this.applicationEnterpriseId = applicationEnterpriseId;
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

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Timestamp getOtpExpiredAt() {
		return otpExpiredAt;
	}

	public void setOtpExpiredAt(Timestamp otpExpiredAt) {
		this.otpExpiredAt = otpExpiredAt;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTransLimit() {
		return transLimit;
	}

	public void setTransLimit(String transLimit) {
		this.transLimit = transLimit;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
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