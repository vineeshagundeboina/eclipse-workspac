package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class FundTransferResponse {

	private boolean status;
	private String description;
	private String message;
	private RecordId recordId;

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

	@JsonRawValue
	public RecordId getRecordId() {
		return recordId;
	}

	public void setRecordId(RecordId recordId) {
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return "FundTransferResponse [status=" + status + ", description=" + description + ", message=" + message
				+ ", recordId=" + recordId + "]";
	}

	private class RecordId {
		private String ReferenceId;
		private Timestamp tranTimeStamp;
		private String responseCode;
		private String reason;

		public String getReferenceId() {
			return ReferenceId;
		}

		public void setReferenceId(String referenceId) {
			ReferenceId = referenceId;
		}

		public Timestamp getTranTimeStamp() {
			return tranTimeStamp;
		}

		public void setTranTimeStamp(Timestamp tranTimeStamp) {
			this.tranTimeStamp = tranTimeStamp;
		}

		public String getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(String responseCode) {
			this.responseCode = responseCode;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		@Override
		public String toString() {
			return "RecordId [ReferenceId=" + ReferenceId + ", tranTimeStamp=" + tranTimeStamp + ", responseCode="
					+ responseCode + ", reason=" + reason + "]";
		}

	}
}
