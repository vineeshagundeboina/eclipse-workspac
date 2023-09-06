package com.federal.fedmobilesmecore.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SchedulePaymentCreate {

	private String transaction_id;
	private String mobile;
	private String auth_token;
	private String pref_corp;

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
