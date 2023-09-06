package com.federal.fedmobilesmecore.model;

/**
 * @author Debasish_Splenta
 *
 */

public class GetExternalUserModelElsResp {
	private boolean status;
	private String description;
	private String message;
	private RecordIdExtUser_els recordId;

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

	public RecordIdExtUser_els getRecordId() {
		return recordId;
	}

	public void setRecordId(RecordIdExtUser_els recordId) {
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return "GetExternalUserModelElsResp [status=" + status + ", description=" + description + ", message=" + message
				+ ", recordId=" + recordId + "]";
	}

}