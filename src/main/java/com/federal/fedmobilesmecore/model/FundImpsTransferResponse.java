package com.federal.fedmobilesmecore.model;

public class FundImpsTransferResponse {
	private boolean status;
	private String description;
	private String message;
	RecordId1 recordId;

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

	public RecordId1 getRecordId() {
		return recordId;
	}

	public void setRecordId(RecordId1 recordId) {
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return "FundImpsTransferResponse [status=" + status + ", description=" + description + ", message=" + message
				+ ", recordId=" + recordId + "]";
	}

}
