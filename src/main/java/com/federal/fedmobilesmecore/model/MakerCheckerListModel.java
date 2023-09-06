package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.EnterpriseUser;
import com.federal.fedmobilesmecore.dto.Signature;

public class MakerCheckerListModel {
	private EnterpriseUser function;
	private Action maker;
	private Signature checker;
	private boolean approvalPermission;
	private boolean isUserNull;
	private boolean isOperationNull;
	private boolean unblockExtUser;
	private boolean unblockExternalPassword;


	public EnterpriseUser getFunction() {
		return function;
	}

	public void setFunction(EnterpriseUser function) {
		this.function = function;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (approvalPermission ? 1231 : 1237);
		result = prime * result + ((checker == null) ? 0 : checker.hashCode());
		result = prime * result + ((function == null) ? 0 : function.hashCode());
		result = prime * result + (isOperationNull ? 1231 : 1237);
		result = prime * result + (isUserNull ? 1231 : 1237);
		result = prime * result + ((maker == null) ? 0 : maker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MakerCheckerListModel other = (MakerCheckerListModel) obj;
		if (approvalPermission != other.approvalPermission)
			return false;
		if (checker == null) {
			if (other.checker != null)
				return false;
		} else if (!checker.equals(other.checker))
			return false;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (isOperationNull != other.isOperationNull)
			return false;
		if (isUserNull != other.isUserNull)
			return false;
		if (maker == null) {
			if (other.maker != null)
				return false;
		} else if (!maker.equals(other.maker))
			return false;
		return true;
	}

	public boolean isUnblockExtUser() {
		return unblockExtUser;
	}

	public void setUnblockExtUser(boolean unblockExtUser) {
		this.unblockExtUser = unblockExtUser;
	}

	public boolean isUnblockExternalPassword() {
		return unblockExternalPassword;
	}

	public void setUnblockExternalPassword(boolean unblockExternalPassword) {
		this.unblockExternalPassword = unblockExternalPassword;
	}

	@Override
	public String toString() {
		return "MakerCheckerListModel [function=" + function + ", maker=" + maker + ", checker=" + checker
				+ ", approvalPermission=" + approvalPermission + ", isUserNull=" + isUserNull + ", isOperationNull="
				+ isOperationNull + ", unblockExtUser=" + unblockExtUser + ", unblockExternalPassword="
				+ unblockExternalPassword + "]";
	}

	
}
