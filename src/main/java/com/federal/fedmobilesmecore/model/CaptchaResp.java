package com.federal.fedmobilesmecore.model;

public class CaptchaResp {
	private boolean status;
	private String message;
	private String recordid;
	private String description;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((recordid == null) ? 0 : recordid.hashCode());
		result = prime * result + (status ? 1231 : 1237);
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
		CaptchaResp other = (CaptchaResp) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (recordid == null) {
			if (other.recordid != null)
				return false;
		} else if (!recordid.equals(other.recordid))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaptchaResp [status=" + status + ", message=" + message + ", recordid=" + recordid + ", description="
				+ description + "]";
	}

}
