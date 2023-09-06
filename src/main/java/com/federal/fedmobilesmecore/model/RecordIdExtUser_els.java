package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.EnterpriseUser;
public class RecordIdExtUser_els {
	private String status;
	private String refNo;
	private EnterpriseUser externalUser;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public EnterpriseUser getExternalUser() {
		return externalUser;
	}

	public void setExternalUser(EnterpriseUser externalUser) {
		this.externalUser = externalUser;
	}

	@Override
	public String toString() {
		return "RecordIdExtUser_els [status=" + status + ", refNo=" + refNo + ", externalUser=" + externalUser + "]";
	}

}