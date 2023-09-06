package com.federal.fedmobilesmecore.model;

public class UserTCAcceptanceRequest {
	private String userId;
	private String mobileNo;
	private String isAccepted;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}

	@Override
	public String toString() {
		return "UserTCAcceptanceRequest [userId=" + userId + ", mobileNo=" + mobileNo + ", isAccepted=" + isAccepted
				+ "]";
	}

}
