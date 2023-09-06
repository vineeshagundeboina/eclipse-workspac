package com.federal.fedmobilesmecore.model;

/**
 * @author Debasish_Splenta
 *
 */

public class GetValidateUserSaveFlowModelResp {
	private boolean status;
	private String description;
	private String message;
	private RecordIdResp recordId;

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

	public RecordIdResp getRecordId() {
		return recordId;
	}

	public void setRecordId(RecordIdResp recordId) {
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return "GetValidateUserSaveFlowModelResp [status=" + status + ", description=" + description + ", message="
				+ message + ", recordId=" + recordId + "]";
	}

}
