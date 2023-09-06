package com.federal.fedmobilesmecore.model;

import java.util.List;

import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Signature;

public class MakerCheckerBeneficiaryModel {

	private Beneficiaries beneficiaries;
	private Action maker;
	private List<Signature> checker;

	private Long coolingPeriod;

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

	public List<Signature> getChecker() {
		return checker;
	}

	public void setChecker(List<Signature> checker) {
		this.checker = checker;
	}

	public boolean isApprovalPermission() {
		return approvalPermission;
	}

	public void setApprovalPermission(boolean approvalPermission) {
		this.approvalPermission = approvalPermission;
	}

	public Long getCoolingPeriod() {
		return coolingPeriod;
	}

	public void setCoolingPeriod(Long coolingPeriod) {
		this.coolingPeriod = coolingPeriod;
	}

	@Override
	public String toString() {
		return "MakerCheckerBeneficiaryModel [beneficiaries=" + beneficiaries + ", maker=" + maker + ", checker="
				+ checker + ", coolingPeriod=" + coolingPeriod + ", approvalPermission=" + approvalPermission + "]";
	}

}
