package com.federal.fedmobilesmecore.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class SchedulePaymentCreateInput {
	@Schema(description = "Required only for /create api", type = "string", example = "SCHEDULEPAY8e_210619120320")

	private String transaction_id;
	@Schema(description = "Required only for /create api", type = "string", example = "919066658022")
	private String mobile;
	@Schema(description = "Required for all requests", type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMyIiwiZXhwIjoxNjIzNzQ1NTQyLCJpYXQiOjE2MjM3NDE5NDJ9.txI3p-lPFTU4YXJhkHamu1HNlNlX7Nb97R09nT3IU97AhOa333PG8XV1QuawIp_EENmvdQpzX1Do6S1Q3Oi2QA")
	private String auth_token;

	@Schema(description = "Required for all requests", type = "string", example = "00a4a92d")
	private String pref_corp;

	@Schema(description = "Required for approve/reject/transactionlist", type = "string", example = "20290")
	private String payment_id;

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

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

	@Override
	public String toString() {
		return "SchedulePaymentCreateInput [transaction_id=" + transaction_id + ", mobile=" + mobile + ", auth_token="
				+ auth_token + ", pref_corp=" + pref_corp + ", payment_id=" + payment_id + "]";
	}
}
