package com.federal.fedmobilesmecore.model;

public class ApplicationFormReqModel {
	private String accountNo;
	private String mobileNo;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Override
	public String toString() {
		return "ApplicationFormReqModel [accountNo=" + accountNo + ", mobileNo=" + mobileNo + "]";
	}

}