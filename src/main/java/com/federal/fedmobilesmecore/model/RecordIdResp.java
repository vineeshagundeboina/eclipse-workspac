package com.federal.fedmobilesmecore.model;

public class RecordIdResp {
	private String appFormId;
	private String enterpriseId;
	private String prefCorp;
	private String prefNo;
	private String longNumber;
	private String retryCount;

	public String getAppFormId() {
		return appFormId;
	}

	public void setAppFormId(String appFormId) {
		this.appFormId = appFormId;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	public String getLongNumber() {
		return longNumber;
	}

	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}

	public String getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(String retryCount) {
		this.retryCount = retryCount;
	}

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	@Override
	public String toString() {
		return "RecordIdResp [appFormId=" + appFormId + ", enterpriseId=" + enterpriseId + ", prefCorp=" + prefCorp
				+ ", prefNo=" + prefNo + ", longNumber=" + longNumber + ", retryCount=" + retryCount + "]";
	}

}