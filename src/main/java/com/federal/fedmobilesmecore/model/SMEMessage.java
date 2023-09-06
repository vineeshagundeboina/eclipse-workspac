package com.federal.fedmobilesmecore.model;

public class SMEMessage {

	public boolean status;
	public String message;
	public String recordid;
	public String description;

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

	public String getRecordid() {
		return recordid;
	}

	public void setRecordid(String recordid) {
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
		return "SMEMessage [status=" + status + ", message=" + message + ", recordid=" + recordid + ", description="
				+ description + "]";
	}
}
