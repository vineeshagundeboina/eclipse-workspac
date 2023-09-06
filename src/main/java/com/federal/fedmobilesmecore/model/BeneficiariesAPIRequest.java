package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;


public class BeneficiariesAPIRequest {
	
	private String pref_corp;
	private String auth_token;
	private Timestamp timeStamp;
	public String getPref_corp() {
		return pref_corp;
	}
	public void setPref_corp(String pref_corp) {
		this.pref_corp = pref_corp;
	}
	public String getAuth_token() {
		return auth_token;
	}
	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		return "BeneficiariesAPIRequest [pref_corp=" + pref_corp + ", auth_token=" + auth_token + ", timeStamp="
				+ timeStamp + "]";
	}
}
