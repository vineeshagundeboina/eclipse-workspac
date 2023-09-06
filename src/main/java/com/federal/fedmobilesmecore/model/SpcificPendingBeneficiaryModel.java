package com.federal.fedmobilesmecore.model;


public class SpcificPendingBeneficiaryModel {
	private String auth_token;
	private String pref_corp;
	private int count;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "SpcificPendingBeneficiaryModel [auth_token=" + auth_token + ", pref_corp=" + pref_corp + ", count="
				+ count + "]";
	}	
}
