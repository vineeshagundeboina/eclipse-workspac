package com.federal.fedmobilesmecore.model;

/**
 * @author Debasish_Splenta created on - 07-04-2021
 */
public class ExternalUserCommonReq {
	public String refNo;
	public String prefNo;
	public String prefCorpNo;
	public String authToken;

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getPrefCorpNo() {
		return prefCorpNo;
	}

	public void setPrefCorpNo(String prefCorpNo) {
		this.prefCorpNo = prefCorpNo;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public String toString() {
		return "ExternalUserCommonReq [refNo=" + refNo + ", prefNo=" + prefNo + ", prefCorpNo=" + prefCorpNo
				+ ", authToken=" + authToken + "]";
	}

}
