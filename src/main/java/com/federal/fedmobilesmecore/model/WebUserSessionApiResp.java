package com.federal.fedmobilesmecore.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WebUserSessionApiResp {
	
	public boolean status;
	public String message;
	public Object recordid;
	public String description;
	public String isAccepted;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getRecordid() {
		return recordid;
	}
	public void setRecordid(Object recordid) {
		this.recordid = recordid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
	public String getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}
	
}
