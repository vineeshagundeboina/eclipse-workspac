package com.federal.fedmobilesmecore.model;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class APIResponse {
	private boolean status;
	private String description;
	private String message;
	private String recordId;

	@Override
	public String toString() {
		return "{" + "\"Status\":" + isStatus() + ",\"Message\":\"" + getMessage() + ",\"Description\":\""
				+ getDescription() + "\", \"RecordId\":" + getRecordId() + '}';

	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		if (getClass() != other.getClass()) {
			return false;
		}
		APIResponse otherApiResponse = (APIResponse) other;

		return Objects.equals(status, otherApiResponse.status)
				&& Objects.equals(message, otherApiResponse.message)
				&& Objects.equals(recordId, otherApiResponse.recordId)
				&& Objects.equals(description, otherApiResponse.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, message, description, recordId);
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@JsonRawValue
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
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

}
