package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;


public class BeneficiaryAccountNoAPIRequest {
	private String acc_no;
	private String pref_corp;
	private String auth_token;
	private Timestamp timeStamp;
	public String getAcc_no() {
		return acc_no;
	}
	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}
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
		return "BeneficiaryAccountNoAPIRequest [acc_no=" + acc_no + ", pref_corp=" + pref_corp + ", auth_token="
				+ auth_token + ", timeStamp=" + timeStamp + "]";
	}
}
