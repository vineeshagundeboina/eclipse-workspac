package com.federal.fedmobilesmecore.model;

public class GetTermsAndConditionsReq {

	private String userId;

	private String mobileNumber;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "GetTermsAndConditionsReq [userId=" + userId + ", mobileNumber=" + mobileNumber + "]";
	}

}
