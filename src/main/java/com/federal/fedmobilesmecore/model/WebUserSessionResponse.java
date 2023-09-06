package com.federal.fedmobilesmecore.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.federal.fedmobilesmecore.dto.User;

public class WebUserSessionResponse {

	private String accNo;
	private String role;
	private String userName;
	private String prefno;
	private String customerName;
	private String mobile;
	private String favouriteAccount;
	private boolean firstTimelogin;
	private String authToken;
	private String enterpriseCorpId;
	private String companyName;
	private String custid;

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

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

	public String getPrefno() {
		return prefno;
	}

	public void setPrefno(String prefno) {
		this.prefno = prefno;
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

	public String getFavouriteAccount() {
		return favouriteAccount;
	}

	public void setFavouriteAccount(String favouriteAccount) {
		this.favouriteAccount = favouriteAccount;
	}

	public boolean getFirstTimelogin() {
		return firstTimelogin;
	}

	public void setFirstTimelogin(boolean firstTimelogin) {
		this.firstTimelogin = firstTimelogin;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getEnterpriseCorpId() {
		return enterpriseCorpId;
	}

	public void setEnterpriseCorpId(String enterpriseCorpId) {
		this.enterpriseCorpId = enterpriseCorpId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
