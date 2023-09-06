
package com.federal.fedmobilesmecore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "app_hash", "mobile", "pref_corp", "otp", "app_form_id" })
public class IVRInput {

	@JsonProperty("app_hash")
	private String appHash;
	@JsonProperty("mobile")
	private String mobile;
	@JsonProperty("pref_corp")
	private String prefCorp;

	@JsonProperty("app_form_id")
	private String app_form_id;

	@JsonProperty("otp")
	private String otp;

	@JsonProperty("app_hash")
	public String getAppHash() {
		return appHash;
	}

	@JsonProperty("app_hash")
	public void setAppHash(String appHash) {
		this.appHash = appHash;
	}

	@JsonProperty("mobile")
	public String getMobile() {
		return mobile;
	}

	@JsonProperty("mobile")
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JsonProperty("pref_corp")
	public String getPrefCorp() {
		return prefCorp;
	}

	@JsonProperty("pref_corp")
	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	@JsonProperty("otp")
	public String getOtp() {
		return otp;
	}

	@JsonProperty("otp")
	public void setOtp(String otp) {
		this.otp = otp;
	}

	@JsonProperty("app_form_id")
	public String getApp_form_id() {
		return app_form_id;
	}

	@JsonProperty("app_form_id")
	public void setApp_form_id(String app_form_id) {
		this.app_form_id = app_form_id;
	}

	@Override
	public String toString() {
		return "IVRInput [appHash=" + appHash + ", mobile=" + mobile + ", prefCorp=" + prefCorp + ", app_form_id="
				+ app_form_id + ", otp=" + otp + "]";
	}

}