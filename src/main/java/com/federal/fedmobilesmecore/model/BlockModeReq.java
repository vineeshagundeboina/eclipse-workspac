package com.federal.fedmobilesmecore.model;

public class BlockModeReq {
	private String custId;
	private String mobileNo;
	private String countryCode;
	private String succFailflg;
	private String errorCode;
	private String errorDesc;
	private String ipAddress;
	private String deviceId;
	private String emailId;
	private String channel;
	private String deviceType;
	private String hostId;

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getSuccFailflg() {
		return succFailflg;
	}

	public void setSuccFailflg(String succFailflg) {
		this.succFailflg = succFailflg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	@Override
	public String toString() {
		return "BlockModeReq [custId=" + custId + ", mobileNo=" + mobileNo + ", countryCode=" + countryCode
				+ ", succFailflg=" + succFailflg + ", errorCode=" + errorCode + ", errorDesc=" + errorDesc
				+ ", ipAddress=" + ipAddress + ", deviceId=" + deviceId + ", emailId=" + emailId + ", channel="
				+ channel + ", deviceType=" + deviceType + ", hostId=" + hostId + "]";
	}

}
