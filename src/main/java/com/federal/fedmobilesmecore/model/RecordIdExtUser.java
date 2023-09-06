package com.federal.fedmobilesmecore.model;

import java.util.List;

import com.federal.fedmobilesmecore.dto.EnterpriseUser;
import com.federal.fedmobilesmecore.dto.User;

public class RecordIdExtUser {
	private String status;
	private String refNo;
	private User externalUser;
	private EnterpriseUser externalEnterpriseUser;
	private List<MakerCheckerListModel> getMakerCheckerListModel;
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
	public User getExternalUser() {
		return externalUser;
	}
	public void setExternalUser(User externalUser) {
		this.externalUser = externalUser;
	}
	public EnterpriseUser getExternalEnterpriseUser() {
		return externalEnterpriseUser;
	}
	public void setExternalEnterpriseUser(EnterpriseUser externalEnterpriseUser) {
		this.externalEnterpriseUser = externalEnterpriseUser;
	}
	public List<MakerCheckerListModel> getGetMakerCheckerListModel() {
		return getMakerCheckerListModel;
	}
	public void setGetMakerCheckerListModel(List<MakerCheckerListModel> getMakerCheckerListModel) {
		this.getMakerCheckerListModel = getMakerCheckerListModel;
	}
	@Override
	public String toString() {
		return "RecordIdExtUser [status=" + status + ", refNo=" + refNo + ", externalUser=" + externalUser
				+ ", externalEnterpriseUser=" + externalEnterpriseUser + ", getMakerCheckerListModel="
				+ getMakerCheckerListModel + "]";
	}
	
}