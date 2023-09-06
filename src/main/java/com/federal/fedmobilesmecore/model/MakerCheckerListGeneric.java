package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.ScheduledPayment;
import com.federal.fedmobilesmecore.dto.Signature;

public class MakerCheckerListGeneric {
	private ScheduledPayment payment;
	private FundTransferDTO ft;

	private Action maker;
	private Signature checker;
	private boolean approvalPermission;
	private boolean isUserNull;
	private boolean isOperationNull;

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

	public boolean isUserNull() {
		return isUserNull;
	}

	public void setUserNull(boolean isUserNull) {
		this.isUserNull = isUserNull;
	}

	public boolean isOperationNull() {
		return isOperationNull;
	}

	public void setOperationNull(boolean isOperationNull) {
		this.isOperationNull = isOperationNull;
	}

	public ScheduledPayment getPayment() {
		return payment;
	}

	public void setPayment(ScheduledPayment payment) {
		this.payment = payment;
	}

	public FundTransferDTO getFt() {
		return ft;
	}

	public void setFt(FundTransferDTO ft) {
		this.ft = ft;
	}

	@Override
	public String toString() {
		return "MakerCheckerListGeneric [payment=" + payment + ", ft=" + ft + ", maker=" + maker + ", checker="
				+ checker + ", approvalPermission=" + approvalPermission + ", isUserNull=" + isUserNull
				+ ", isOperationNull=" + isOperationNull + "]";
	}

}
