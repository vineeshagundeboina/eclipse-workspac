package com.federal.fedmobilesmecore.model;

public class TCUserAcceptanceResp {

	private boolean status;
	private String message;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "TCUserAcceptanceResp [status=" + status + ", message=" + message + ", description=" + description + "]";
	}

}
