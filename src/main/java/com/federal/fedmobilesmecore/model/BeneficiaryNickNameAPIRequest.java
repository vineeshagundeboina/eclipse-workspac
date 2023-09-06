package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;


public class BeneficiaryNickNameAPIRequest {
	private String nick_name;
	private String pref_corp;
	private String auth_token;
	private Timestamp timeStamp;
	
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
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
		return "BeneficiaryNickNameAPIRequest [nick_name=" + nick_name + ", pref_corp=" + pref_corp + ", auth_token="
				+ auth_token + ", timeStamp=" + timeStamp + "]";
	}	

}
