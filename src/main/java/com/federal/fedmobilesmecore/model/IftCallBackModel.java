package com.federal.fedmobilesmecore.model;

public class IftCallBackModel {

	private String referenceId;
	private String utr;
	private String remitterAccountNo;
	private String benificiaryAccountNo;
	private String tranAmount;
	private String tranDate;
	private String responseCode;
	private String responseReason;
	private String responseAction;

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getUtr() {
		return utr;
	}

	public void setUtr(String utr) {
		this.utr = utr;
	}

	public String getRemitterAccountNo() {
		return remitterAccountNo;
	}

	public void setRemitterAccountNo(String remitterAccountNo) {
		this.remitterAccountNo = remitterAccountNo;
	}

	public String getBenificiaryAccountNo() {
		return benificiaryAccountNo;
	}

	public void setBenificiaryAccountNo(String benificiaryAccountNo) {
		this.benificiaryAccountNo = benificiaryAccountNo;
	}

	public String getTranAmount() {
		return tranAmount;
	}

	public void setTranAmount(String tranAmount) {
		this.tranAmount = tranAmount;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseReason() {
		return responseReason;
	}

	public void setResponseReason(String responseReason) {
		this.responseReason = responseReason;
	}

	public String getResponseAction() {
		return responseAction;
	}

	public void setResponseAction(String responseAction) {
		this.responseAction = responseAction;
	}

	@Override
	public String toString() {
		return "IftCallBackModel [referenceId=" + referenceId + ", utr=" + utr + ", remitterAccountNo="
				+ remitterAccountNo + ", benificiaryAccountNo=" + benificiaryAccountNo + ", tranAmount=" + tranAmount
				+ ", tranDate=" + tranDate + ", responseCode=" + responseCode + ", responseReason=" + responseReason
				+ ", responseAction=" + responseAction + "]";
	}
}
