package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Debasish_Splenta created on - 14-03-2020
 */
public class ValidateExtUserPrefNoReqModel {
	public String prefNo;
	public String authToken;
	public String prefCorp;
	public Timestamp timeStamp;

	public String getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(String prefNo) {
		this.prefNo = prefNo;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
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
		result = prime * result + ((authToken == null) ? 0 : authToken.hashCode());
		result = prime * result + ((prefCorp == null) ? 0 : prefCorp.hashCode());
		result = prime * result + ((prefNo == null) ? 0 : prefNo.hashCode());
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
		ValidateExtUserPrefNoReqModel other = (ValidateExtUserPrefNoReqModel) obj;
		if (authToken == null) {
			if (other.authToken != null)
				return false;
		} else if (!authToken.equals(other.authToken))
			return false;
		if (prefCorp == null) {
			if (other.prefCorp != null)
				return false;
		} else if (!prefCorp.equals(other.prefCorp))
			return false;
		if (prefNo == null) {
			if (other.prefNo != null)
				return false;
		} else if (!prefNo.equals(other.prefNo))
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
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
