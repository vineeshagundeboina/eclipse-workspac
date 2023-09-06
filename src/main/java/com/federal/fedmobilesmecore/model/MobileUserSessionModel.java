package com.federal.fedmobilesmecore.model;

public class MobileUserSessionModel {
	private String prefNo;
	private String mpin;
	private String mobile;
	public String getPrefNo() {
		return prefNo;
	}
	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}
	public String getMpin() {
		return mpin;
	}
	public void setMpin(String mpin) {
		this.mpin = mpin;
	}
	public MobileUserSessionModel() {
		super();
	}

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Override
	public String toString() {
		return "MobileUserSessionModel [prefNo=" + prefNo + ", mpin=" + mpin + ", mobile=" + mobile + "]";
	}

}
