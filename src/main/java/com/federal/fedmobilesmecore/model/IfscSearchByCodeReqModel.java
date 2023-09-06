package com.federal.fedmobilesmecore.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class IfscSearchByCodeReqModel {
	@NotBlank
	@Size(min = 11,max = 11,message = "pass the IfscCode")
	private String ifscCode;

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	@Override
	public String toString() {
		return "IfscSearchByCodeReqModel [ifscCode=" + ifscCode + "]";
	}

}