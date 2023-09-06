package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

import com.federal.fedmobilesmecore.dto.ChannelFlag;

public class ApproveTransactionReqModel {
	private String refNo;
	private String authToken;
	private String prefCorp;
	public Timestamp timestamp;
	
	private String ipAddress;
	private String geoLocation;
	private ChannelFlag channelFlag;

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
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
		return "ApproveTransactionReqModel [refNo=" + refNo + ", authToken=" + authToken + ", prefCorp=" + prefCorp
				+ ", timestamp=" + timestamp + ", ipAddress=" + ipAddress + ", geoLocation=" + geoLocation
				+ ", channelFlag=" + channelFlag + "]";
	}

}
