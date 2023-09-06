package com.federal.fedmobilesmecore.model;

import java.util.Objects;

public class MessageResp {
	private String role;
	private String userName;
	private String prefNo;
	private String customerName;
	private String mobile;
	private String favoriteAcc;
	private String firstTimeLogin;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFavoriteAcc() {
		return favoriteAcc;
	}

	public void setFavoriteAcc(String favoriteAcc) {
		this.favoriteAcc = favoriteAcc;
	}

	public String getFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(String firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerName, favoriteAcc, firstTimeLogin, mobile, prefNo, role, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageResp other = (MessageResp) obj;
		return Objects.equals(customerName, other.customerName) && Objects.equals(favoriteAcc, other.favoriteAcc)
				&& Objects.equals(firstTimeLogin, other.firstTimeLogin) && Objects.equals(mobile, other.mobile)
				&& Objects.equals(prefNo, other.prefNo) && Objects.equals(role, other.role)
				&& Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "MessageResp [role=" + role + ", userName=" + userName + ", prefNo=" + prefNo + ", customerName="
				+ customerName + ", mobile=" + mobile + ", favoriteAcc=" + favoriteAcc + ", firstTimeLogin="
				+ firstTimeLogin + "]";
	}

}
