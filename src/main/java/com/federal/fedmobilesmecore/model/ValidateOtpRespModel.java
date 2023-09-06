package com.federal.fedmobilesmecore.model;

public class ValidateOtpRespModel {
	private boolean status;
	private String description;
	private String message;
	private String authToken;
	private String prefCorp;
	private String prefNo;
	private String state;
	private Object messageResp;
	private boolean firstTimeWebLogin;
	private String webToken;

	private String temptoken;
	
	private String role;
	private boolean activity;

	// TODO remove it in actual UAT/LIVE
	private String otp;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean getFirstTimeWebLogin() {
		return firstTimeWebLogin;
	}

	public void setFirstTimeWebLogin(boolean firstTimeWebLogin) {
		this.firstTimeWebLogin = firstTimeWebLogin;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	public Object getMessageResp() {
		return messageResp;
	}

	public void setMessageResp(Object messageResp) {
		this.messageResp = messageResp;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getWebToken() {
		return webToken;
	}

	public void setWebToken(String webToken) {
		this.webToken = webToken;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getTemptoken() {
		return temptoken;
	}

	public void setTemptoken(String temptoken) {
		this.temptoken = temptoken;
	}
	
	

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActivity() {
		return activity;
	}

	public void setActivity(boolean activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return "ValidateOtpRespModel [status=" + status + ", description=" + description + ", message=" + message
				+ ", authToken=" + authToken + ", prefCorp=" + prefCorp + ", prefNo=" + prefNo + ", state=" + state
				+ ", messageResp=" + messageResp + ", firstTimeWebLogin=" + firstTimeWebLogin + ", webToken=" + webToken
				+ ", temptoken=" + temptoken + ", role=" + role + ", activity=" + activity + ", otp=" + otp + "]";
	}

}
