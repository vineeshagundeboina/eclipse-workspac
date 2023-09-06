package com.federal.fedmobilesmecore.model;

/**
 * @author Debasish_Splenta
 *
 */

public class GetSimBindingReqModel {
	private String mobile;
	private String simHash;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((simHash == null) ? 0 : simHash.hashCode());
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
		GetSimBindingReqModel other = (GetSimBindingReqModel) obj;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (simHash == null) {
			if (other.simHash != null)
				return false;
		} else if (!simHash.equals(other.simHash))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GetSimBindingReqModel [mobile=" + mobile + ", simHash=" + simHash + "]";
	}

}
