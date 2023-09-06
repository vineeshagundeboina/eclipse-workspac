package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

/**
 * @author Syed_Splenta
 *
 */

public class MpinCreateReqModel {
	private String pref_corp;
	private String timeStamp;
	
	private MpinReqModel mpin;
	private String appVersion;
	private String osVersion;
	private String osType;

	public String getPref_corp() {
		return pref_corp;
	}

	public void setPref_corp(String pref_corp) {
		this.pref_corp = pref_corp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public MpinReqModel getMpin() {
		return mpin;
	}

	public void setMpin(MpinReqModel mpin) {
		this.mpin = mpin;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	@Override
	public String toString() {
		return "MpinCreateReqModel [pref_corp=" + pref_corp + ", timeStamp=" + timeStamp + ", mpin=" + mpin
				+ ", appVersion=" + appVersion + ", osVersion=" + osVersion + ", osType=" + osType + "]";
	}
	
	
	
}
 