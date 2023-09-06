package com.federal.fedmobilesmecore.model;

import org.springframework.lang.Nullable;

/**
 * @author Syed_Splenta
 *
 */

public class CreateCustDetailsReqModel {
	private String acctNo;
	@Nullable
	private String prefno;
	private String mobile;

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getPrefno() {
		return prefno;
	}

	public void setPrefno(String prefno) {
		this.prefno = prefno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acctNo == null) ? 0 : acctNo.hashCode());
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
		CreateCustDetailsReqModel other = (CreateCustDetailsReqModel) obj;
		if (acctNo == null) {
			if (other.acctNo != null)
				return false;
		} else if (!acctNo.equals(other.acctNo))
			return false;
		return true;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "CreateCustDetailsReqModel [acctNo=" + acctNo + ", prefno=" + prefno + ", mobile=" + mobile + "]";
	}

}
