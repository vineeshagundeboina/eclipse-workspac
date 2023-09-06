package com.federal.fedmobilesmecore.model;

/**
 * @author Debasish_Splenta
 *
 */

public class GetExternalUserModelResp {
	private boolean status;
	private String description;
	private String message;
	private RecordIdExtUser recordId;

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

	public RecordIdExtUser getRecordId() {
		return recordId;
	}

	public void setRecordId(RecordIdExtUser recordId) {
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return "GetExternalUserModelResp [status=" + status + ", description=" + description + ", message=" + message
				+ ", recordId=" + recordId + "]";
	}

}
