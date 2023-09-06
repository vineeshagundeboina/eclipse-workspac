package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

/**
 * @author Debasish_Splenta created on - 14-03-2020
 */
public class ExternalUserCreationReqModel {
	public String prefNo;
	private String userName;
	private String mobile;
	private String password;
	private String role;
	private String exFlag;
	public String authToken;
	public String prefCorp;
	public Timestamp timeStamp;

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getExFlag() {
		return exFlag;
	}

	public void setExFlag(String exFlag) {
		this.exFlag = exFlag;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authToken == null) ? 0 : authToken.hashCode());
		result = prime * result + ((exFlag == null) ? 0 : exFlag.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((prefCorp == null) ? 0 : prefCorp.hashCode());
		result = prime * result + ((prefNo == null) ? 0 : prefNo.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExternalUserCreationReqModel other = (ExternalUserCreationReqModel) obj;
		if (authToken == null) {
			if (other.authToken != null)
				return false;
		} else if (!authToken.equals(other.authToken))
			return false;
		if (exFlag == null) {
			if (other.exFlag != null)
				return false;
		} else if (!exFlag.equals(other.exFlag))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (prefCorp == null) {
			if (other.prefCorp != null)
				return false;
		} else if (!prefCorp.equals(other.prefCorp))
			return false;
		if (prefNo == null) {
			if (other.prefNo != null)
				return false;
		} else if (!prefNo.equals(other.prefNo))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExternalUserCreationReqModel [prefNo=" + prefNo + ", userName=" + userName + ", mobile=" + mobile
				+ ", password=" + password + ", role=" + role + ", exFlag=" + exFlag + ", authToken=" + authToken
				+ ", prefCorp=" + prefCorp + ", timeStamp=" + timeStamp + "]";
	}
}
