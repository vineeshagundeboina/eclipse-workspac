package com.federal.fedmobilesmecore.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SoleActivateResp {
	
	private boolean status;
	private String description;
	private String message;
	private Map<String , String> recordId;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, String> getRecordId() {
		return recordId;
	}
	public void setRecordId(Map<String, String> recordId) {
		this.recordId = recordId;
	}
	public void setRecordId(Object object) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
