package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Signature;

public class MakerCheckerRejecedList {

	private Beneficiaries beneficiaries;
	private Action maker;
	private Signature checker;
	private boolean approvalPermission;

	public Beneficiaries getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(Beneficiaries beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	public Action getMaker() {
		return maker;
	}

	public void setMaker(Action maker) {
		this.maker = maker;
	}

	public Signature getChecker() {
		return checker;
	}

	public void setChecker(Signature checker) {
		this.checker = checker;
	}

	public boolean isApprovalPermission() {
		return approvalPermission;
	}

	public void setApprovalPermission(boolean approvalPermission) {
		this.approvalPermission = approvalPermission;
	}

	@Override
	public String toString() {
		return "MakerCheckerRejecedList [beneficiaries=" + beneficiaries + ", maker=" + maker + ", checker=" + checker
				+ ", approvalPermission=" + approvalPermission + "]";
	}

}
