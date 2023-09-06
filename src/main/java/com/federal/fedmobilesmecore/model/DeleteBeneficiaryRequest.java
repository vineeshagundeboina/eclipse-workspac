package com.federal.fedmobilesmecore.model;


public class DeleteBeneficiaryRequest {
	
	private String ref_no;
	private String auth_token;
	
	public String getRef_no() {
		return ref_no;
	}
	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
	}
	public String getAuth_token() {
		return auth_token;
	}
	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}
	@Override
	public String toString() {
		return "DeleteBeneficiaryRequest [ref_no=" + ref_no + ", auth_token=" + auth_token + "]";
	}
}
