package com.federal.fedmobilesmecore.model;

public class RefreshTokenmodel {
	
	private String refreshToken;
	private String pref_no;
	
	

	public String getPref_no() {
		return pref_no;
	}

	public void setPref_no(String pref_no) {
		this.pref_no = pref_no;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "RefreshTokenmodel [refreshToken=" + refreshToken + ", pref_no=" + pref_no + "]";
	}

}
