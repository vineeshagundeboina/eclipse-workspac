package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

import com.federal.fedmobilesmecore.dto.ChannelFlag;

public class PerformTransactionReqModel {
	public String transactionId;
	public String mobile;
	public String authToken;
	public String prefCorp;
	public Timestamp timestamp;
	private String ipAddress;
	private String geoLocation;
	private ChannelFlag channelFlag;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public ChannelFlag getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(ChannelFlag channelFlag) {
		this.channelFlag = channelFlag;
	}

	@Override
	public String toString() {
		return "PerformTransactionReqModel [transactionId=" + transactionId + ", mobile=" + mobile + ", authToken="
				+ authToken + ", prefCorp=" + prefCorp + ", timestamp=" + timestamp + ", ipAddress=" + ipAddress
				+ ", geoLocation=" + geoLocation + ", channelFlag=" + channelFlag + "]";
	}

	
}
