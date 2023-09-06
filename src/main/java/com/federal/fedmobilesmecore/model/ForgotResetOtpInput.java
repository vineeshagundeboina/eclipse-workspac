package com.federal.fedmobilesmecore.model;

import javax.validation.constraints.NotBlank;

public class ForgotResetOtpInput {

	private String auth_token;
	private String pref_corp;
	@NotBlank(message = "Forgot pin should be mandatory")
	private String forgot_pin;
	private String app_token;

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

	public String getForgot_pin() {
		return forgot_pin;
	}

	public void setForgot_pin(String forgot_pin) {
		this.forgot_pin = forgot_pin;
	}

	public String getApp_token() {
		return app_token;
	}

	public void setApp_token(String app_token) {
		this.app_token = app_token;
	}

	@Override
	public String toString() {
		return "ForgotResetOtpInput [auth_token=" + auth_token + ", pref_corp=" + pref_corp + ", forgot_pin="
				+ forgot_pin + ", app_token=" + app_token + "]";
	}
}
