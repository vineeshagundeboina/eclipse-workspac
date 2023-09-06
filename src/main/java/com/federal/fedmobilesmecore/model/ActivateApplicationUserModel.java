package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

public class ActivateApplicationUserModel {
	
	private String activation_token;
	private String app_token;
	private String auth_token;
	private String pref_corp;
	private Timestamp timeStamp;
	public String getActivation_token() {
		return activation_token;
	}
	public void setActivation_token(String activation_token) {
		this.activation_token = activation_token;
	}
	public String getApp_token() {
		return app_token;
	}
	public void setApp_token(String app_token) {
		this.app_token = app_token;
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
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		return "ActivateApplicationUserModel [activation_token=" + activation_token + ", app_token=" + app_token
				+ ", auth_token=" + auth_token + ", pref_corp=" + pref_corp + ", timeStamp=" + timeStamp + "]";
	}

}
