package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.ChannelFlag;

public class LoginDetailsRequest {
	
	private String appVersion;
	
	private String osVersion;
	
	private String osType;
	
	private String ipAddress;
	
	private String geoLocation;
	
	private String mobile;

	private ChannelFlag channelFlag;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public ChannelFlag getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(ChannelFlag channelFlag) {
		this.channelFlag = channelFlag;
	}

	@Override
	public String toString() {
		return "LoginDetailsRequest [appVersion=" + appVersion + ", osVersion=" + osVersion + ", osType=" + osType
				+ ", ipAddress=" + ipAddress + ", geoLocation=" + geoLocation + ", mobile=" + mobile + ", channelFlag="
				+ channelFlag + "]";
	}

}
