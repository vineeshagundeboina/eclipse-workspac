package com.federal.fedmobilesmecore.model;

public class IfscSearchRequest {
	private String bankName;
	private String state;
	private String district;
	private String ifscCode;
	private String city;
	private String branch;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		return "IfscSearchRequest [bankName=" + bankName + ", state=" + state + ", district=" + district + ", ifscCode="
				+ ifscCode + ", city=" + city + ", branch=" + branch + "]";
	}

}