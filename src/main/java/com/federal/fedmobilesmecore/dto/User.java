package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USERS")
//@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_US_SEQ")
	@SequenceGenerator(sequenceName = "USERS_SEQ", allocationSize = 1, name = "SME_US_SEQ")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(name = "ACC_NO", length = 255)
	private String accNo;

	@Column(name = "ACTIVATION_STATUS", length = 255)
	private String activationStatus;

	@Column(name = "APP_TOKEN", length = 255)
	private String appToken;

	@Column(name = "AUTH_TOKEN", length = 255)
	private String authToken;

	@Column(name = "AUTHORIZED_SIGNATORY", precision = 1)
	private Long authorizedSignatory;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "CURRENT_SIGN_IN_AT")
	private Timestamp currentSignInAt;

	@Column(name = "CURRENT_SIGN_IN_IP", length = 255)
	private String currentSignInIp;

	@Column(name = "CUST_NO", length = 255)
	private String custNo;

	@Column(name = "CUSTOMER_NAME", length = 255)
	private String customerName;

	@Column(name = "DEVICE_TYPE", length = 255)
	private String deviceType;

	@Column(length = 255)
	private String email;

	@Column(name = "ENCRYPTED_PASSWORD", nullable = false, length = 255)
	private String encryptedPassword;

	@Column(name = "ENTERPRISE_ID", length = 255)
	private String enterpriseId;

	@Column(name = "FAVOURITE_ACCOUNT", length = 255)
	private String favouriteAccount;

	@Column(name = "LAST_ACTIVITY_AT")
	private Timestamp lastActivityAt;

	@Column(name = "LAST_SIGN_IN_AT")
	private Timestamp lastSignInAt;

	@Column(name = "LAST_SIGN_IN_IP", length = 255)
	private String lastSignInIp;

	@Column(name = "MARK_AS_ENABLED", precision = 1)
	private boolean markAsEnabled;

	@Column(length = 255)
	private String mobile;

	@Column(length = 255)
	private String mpin;

	@Column(name = "MPIN_CHECK_STATUS", length = 255)
	private String mpinCheckStatus;

	@Column(length = 255)
	private String otp;

	@Column(name = "OTP_EXPIRED_AT")
	private Timestamp otpExpiredAt;

	@Column(length = 255)
	private String password;

	@Column(name = "PASSWORD_CHANGED_AT")
	private Timestamp passwordChangedAt;

	@Column(name = "PREF_NO", length = 255)
	private String prefNo;

	@Column(name = "PUSH_TOKEN", length = 255)
	private String pushToken;

	@Column(name = "REMEMBER_CREATED_AT")
	private Timestamp rememberCreatedAt;

	@Column(name = "RESET_PASSWORD_SENT_AT")
	private Timestamp resetPasswordSentAt;

	@Column(name = "RESET_PASSWORD_TOKEN", length = 255)
	private String resetPasswordToken;

	@Column(name = "ROLE", length = 255)
	private String role;

	@Column(name = "SIGN_IN_COUNT", nullable = false, precision = 38)
	private int signInCount;

	@Column(name = "TRANS_LIMIT", length = 255)
	private String transLimit;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	@Column(name = "USER_NAME", length = 255)
	private String userName;

	@Column(name = "VIEW_PWD", length = 255)
	private String viewPwd;
	
	@Column(name = "VIEW_PWD_HASH", length = 250)
	private String viewPwdHash;

	@Column(name = "WEB_SIGN_IN_COUNT", precision = 38)
	private Long webSignInCount;

	@Column(name = "WRONG_ACTIVATION_TOKEN_COUNT", length = 255)
	private String wrongActivationTokenCount;

	@Column(name = "WRONG_MPIN_COUNT", length = 255)
	private String wrongMpinCount;
	
	
	@Column(name = "WRONG_WEB_PASSWORD_COUNT")
	private String wrongWebPasswordCount;

	@Column(name = "WEB_CHECK_STATUS")
	private String webCheckStatus;
	
	@Column(name="APP_VERSION",nullable = true)
	private String appVersion;
	
	@Column(name="OS_VERSION",nullable = true)
	private String osVersion;
	
	@Column(name="OS_TYPE",nullable = true)
	private String osType;
	
	@Column(name = "IS_ACCEPTED_DATE", nullable = false)
    private Timestamp isAcceptedDate;
	
	@Column(name = "IS_ACCEPTED", length = 1)
    private String isAccepted;
	

	// bi-directional many-to-one association to ManagedAccount
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<ManagedAccount> managedAccounts;

	// bi-directional many-to-one association to UserToken
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<UserToken> userTokens;

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

	public String getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

	public String getAppToken() {
		return appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Long getAuthorizedSignatory() {
		return authorizedSignatory;
	}

	public void setAuthorizedSignatory(Long authorizedSignatory) {
		this.authorizedSignatory = authorizedSignatory;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getCurrentSignInAt() {
		return currentSignInAt;
	}

	public void setCurrentSignInAt(Timestamp currentSignInAt) {
		this.currentSignInAt = currentSignInAt;
	}

	public String getCurrentSignInIp() {
		return currentSignInIp;
	}

	public void setCurrentSignInIp(String currentSignInIp) {
		this.currentSignInIp = currentSignInIp;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getFavouriteAccount() {
		return favouriteAccount;
	}

	public void setFavouriteAccount(String favouriteAccount) {
		this.favouriteAccount = favouriteAccount;
	}

	public Timestamp getLastActivityAt() {
		return lastActivityAt;
	}

	public void setLastActivityAt(Timestamp lastActivityAt) {
		this.lastActivityAt = lastActivityAt;
	}

	public Timestamp getLastSignInAt() {
		return lastSignInAt;
	}

	public void setLastSignInAt(Timestamp lastSignInAt) {
		this.lastSignInAt = lastSignInAt;
	}

	public String getLastSignInIp() {
		return lastSignInIp;
	}

	public void setLastSignInIp(String lastSignInIp) {
		this.lastSignInIp = lastSignInIp;
	}

	public boolean isMarkAsEnabled() {
		return markAsEnabled;
	}

	public void setMarkAsEnabled(boolean markAsEnabled) {
		this.markAsEnabled = markAsEnabled;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMpin() {
		return mpin;
	}

	public void setMpin(String mpin) {
		this.mpin = mpin;
	}

	public String getMpinCheckStatus() {
		return mpinCheckStatus;
	}

	public void setMpinCheckStatus(String mpinCheckStatus) {
		this.mpinCheckStatus = mpinCheckStatus;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getPasswordChangedAt() {
		return passwordChangedAt;
	}

	public void setPasswordChangedAt(Timestamp passwordChangedAt) {
		this.passwordChangedAt = passwordChangedAt;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public Timestamp getRememberCreatedAt() {
		return rememberCreatedAt;
	}

	public void setRememberCreatedAt(Timestamp rememberCreatedAt) {
		this.rememberCreatedAt = rememberCreatedAt;
	}

	public Timestamp getResetPasswordSentAt() {
		return resetPasswordSentAt;
	}

	public void setResetPasswordSentAt(Timestamp resetPasswordSentAt) {
		this.resetPasswordSentAt = resetPasswordSentAt;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getSignInCount() {
		return signInCount;
	}

	public void setSignInCount(int signInCount) {
		this.signInCount = signInCount;
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

	public String getViewPwd() {
		return viewPwd;
	}

	public void setViewPwd(String viewPwd) {
		this.viewPwd = viewPwd;
	}

	public Long getWebSignInCount() {
		return webSignInCount;
	}

	public void setWebSignInCount(Long webSignInCount) {
		this.webSignInCount = webSignInCount;
	}

	public String getWrongActivationTokenCount() {
		return wrongActivationTokenCount;
	}

	public void setWrongActivationTokenCount(String wrongActivationTokenCount) {
		this.wrongActivationTokenCount = wrongActivationTokenCount;
	}

	public String getWrongMpinCount() {
		return wrongMpinCount;
	}

	public void setWrongMpinCount(String wrongMpinCount) {
		this.wrongMpinCount = wrongMpinCount;
	}

	public List<ManagedAccount> getManagedAccounts() {
		return managedAccounts;
	}

	public void setManagedAccounts(List<ManagedAccount> managedAccounts) {
		this.managedAccounts = managedAccounts;
	}

	public List<UserToken> getUserTokens() {
		return userTokens;
	}

	public void setUserTokens(List<UserToken> userTokens) {
		this.userTokens = userTokens;
	}

	public String getWrongWebPasswordCount() {
		return wrongWebPasswordCount;
	}

	public void setWrongWebPasswordCount(String wrongWebPasswordCount) {
		this.wrongWebPasswordCount = wrongWebPasswordCount;
	}

	public String getWebCheckStatus() {
		return webCheckStatus;
	}

	public void setWebCheckStatus(String webCheckStatus) {
		this.webCheckStatus = webCheckStatus;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public Timestamp getIsAcceptedDate() {
		return isAcceptedDate;
	}

	public void setIsAcceptedDate(Timestamp isAcceptedDate) {
		this.isAcceptedDate = isAcceptedDate;
	}

	public String getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}

	public String getViewPwdHash() {
		return viewPwdHash;
	}

	public void setViewPwdHash(String viewPwdHash) {
		this.viewPwdHash = viewPwdHash;
	}
	
	
}
