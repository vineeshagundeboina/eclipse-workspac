package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

public class CreateBenificiaryApprovedEnterpriseReq {
	
	private createBenificiary benificiary;
	private String auth_token;
	private String pref_corp;
	private Timestamp timestamp;
	
	
	
	public createBenificiary getBenificiary() {
		return benificiary;
	}
	public void setBenificiary(createBenificiary benificiary) {
		this.benificiary = benificiary;
	}
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
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "CreateBenificiaryApprovedEnterpriseReq [benificiary=" + benificiary + ", auth_token=" + auth_token
				+ ", pref_corp=" + pref_corp + ", timestamp=" + timestamp + "]";
	}
}
