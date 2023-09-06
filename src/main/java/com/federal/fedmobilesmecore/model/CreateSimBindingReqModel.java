package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

/**
 * @author Debasish_Splenta
 *
 */

public class CreateSimBindingReqModel {
	private String mobile;
	private String simHash;
	private String simRandomNo;
	private String prefCorp;
	private Timestamp timeStamp;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSimHash() {
		return simHash;
	}

	public void setSimHash(String simHash) {
		this.simHash = simHash;
	}

	public String getSimRandomNo() {
		return simRandomNo;
	}

	public void setSimRandomNo(String simRandomNo) {
		this.simRandomNo = simRandomNo;
	}

	public String getPrefCorp() {
		return prefCorp;
	}

	public void setPrefCorp(String prefCorp) {
		this.prefCorp = prefCorp;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((prefCorp == null) ? 0 : prefCorp.hashCode());
		result = prime * result + ((simHash == null) ? 0 : simHash.hashCode());
		result = prime * result + ((simRandomNo == null) ? 0 : simRandomNo.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		CreateSimBindingReqModel other = (CreateSimBindingReqModel) obj;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (prefCorp == null) {
			if (other.prefCorp != null)
				return false;
		} else if (!prefCorp.equals(other.prefCorp))
			return false;
		if (simHash == null) {
			if (other.simHash != null)
				return false;
		} else if (!simHash.equals(other.simHash))
			return false;
		if (simRandomNo == null) {
			if (other.simRandomNo != null)
				return false;
		} else if (!simRandomNo.equals(other.simRandomNo))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CreateSimBindingReqModel [mobile=" + mobile + ", simHash=" + simHash + ", simRandomNo=" + simRandomNo
				+ ", prefCorp=" + prefCorp + ", timeStamp=" + timeStamp + "]";
	}
}
