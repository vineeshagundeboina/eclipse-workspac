package com.federal.fedmobilesmecore.model;

import java.util.Objects;

public class CommonResetOtpWebUsersReqModel {
	private String applicationFormId;
	private String authToken;
	private String otp;
	private String viewPassword;
	private String newPassword;
	private String confirmPassword;
	private String exFlag;
	private String password;
	private String prefNo;
	private String activationToken;
	private String mobile;
	private String forgotPin;
	private String prefCorp;
	private String checkSum;
	private String captcha;

	public String getMobile() {
		return mobile;
	}

	public String getForgotPin() {
		return forgotPin;
	}

	public void setForgotPin(String forgotPin) {
		this.forgotPin = forgotPin;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getExFlag() {
		return exFlag;
	}

	public void setExFlag(String exFlag) {
		this.exFlag = exFlag;
	}

	public String getApplicationFormId() {
		return applicationFormId;
	}

	public void setApplicationFormId(String applicationFormId) {
		this.applicationFormId = applicationFormId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	
	
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(activationToken, applicationFormId, authToken, captcha, checkSum,viewPassword, confirmPassword, exFlag,
				forgotPin, mobile, newPassword, otp, password, prefCorp, prefNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommonResetOtpWebUsersReqModel other = (CommonResetOtpWebUsersReqModel) obj;
		return Objects.equals(activationToken, other.activationToken)
				&& Objects.equals(applicationFormId, other.applicationFormId)
				&& Objects.equals(authToken, other.authToken) && Objects.equals(captcha, other.captcha)
				&& Objects.equals(checkSum, other.checkSum) && Objects.equals(confirmPassword, other.confirmPassword)
				&& Objects.equals(exFlag, other.exFlag) && Objects.equals(forgotPin, other.forgotPin)
				&& Objects.equals(mobile, other.mobile) && Objects.equals(newPassword, other.newPassword)
				&& Objects.equals(otp, other.otp) && Objects.equals(password, other.password)
				&& Objects.equals(prefCorp, other.prefCorp) && Objects.equals(prefNo, other.prefNo) && Objects.equals(viewPassword, other.viewPassword);
	}

	@Override
	public String toString() {
		return "CommonResetOtpWebUsersReqModel [applicationFormId=" + applicationFormId + ", authToken=" + authToken
				+ ", otp=" + otp + ", viewPassword=" + viewPassword +" newPassword=" + newPassword + ", confirmPassword=" + confirmPassword + ", exFlag="
				+ exFlag + ", password=" + password + ", prefNo=" + prefNo + ", activationToken=" + activationToken
				+ ", mobile=" + mobile + ", forgotPin=" + forgotPin + ", prefCorp=" + prefCorp + ", checkSum="
				+ checkSum + ", captcha=" + captcha + "]";
	}

	
	public String getViewPassword() {
		return viewPassword;
	}

	public void setViewPassword(String viewPassword) {
		this.viewPassword = viewPassword;
	}

}
