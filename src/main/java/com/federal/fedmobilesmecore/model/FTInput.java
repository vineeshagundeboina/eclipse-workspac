package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.ChannelFlag;

import io.swagger.v3.oas.annotations.media.Schema;

public class FTInput {

	@Schema(description = "Required for all requests", type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMyIiwiZXhwIjoxNjIzNzQ1NTQyLCJpYXQiOjE2MjM3NDE5NDJ9.txI3p-lPFTU4YXJhkHamu1HNlNlX7Nb97R09nT3IU97AhOa333PG8XV1QuawIp_EENmvdQpzX1Do6S1Q3Oi2QA")
	private String auth_token;

	@Schema(description = "Required for all requests", type = "string", example = "00a4a92d")
	private String pref_corp;

	@Schema(description = "Required for /pendingtrans", type = "string", example = "FundTransfer")
	private String transfer_type;

	@Schema(description = "Required for /pendingtrans_specific. count=Number of records required", type = "string", example = "10")
	private Integer count;

	@Schema(description = "Reference Number required for /reject", type = "string", example = "SMEFB19171gc7pu")
	private String ref_no;
	
	private String ipAddress;
	private String geoLocation;
	private ChannelFlag channelFlag;

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

	public String getPref_corp() {
		return pref_corp;
	}

	public void setPref_corp(String pref_corp) {
		this.pref_corp = pref_corp;
	}

	public String getTransfer_type() {
		return transfer_type;
	}

	public void setTransfer_type(String transfer_type) {
		this.transfer_type = transfer_type;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getRef_no() {
		return ref_no;
	}

	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
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
		return "FTInput [auth_token=" + auth_token + ", pref_corp=" + pref_corp + ", transfer_type=" + transfer_type
				+ ", count=" + count + ", ref_no=" + ref_no + ", ipAddress=" + ipAddress + ", geoLocation="
				+ geoLocation + ", channelFlag=" + channelFlag + "]";
	}
	
	
}
