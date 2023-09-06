package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

/**
 * @author Debasish_Splenta created on - 14-03-2020
 */
public class ValidateExtMobileNoReqModel {
	public String mobile;
	public String authToken;
	public String prefCorp;
	public Timestamp timeStamp;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "ValidateExtMobileNoReqModel [mobile=" + mobile + ", authToken=" + authToken + ", prefCorp=" + prefCorp
				+ ", timeStamp=" + timeStamp + "]";
	}

}
