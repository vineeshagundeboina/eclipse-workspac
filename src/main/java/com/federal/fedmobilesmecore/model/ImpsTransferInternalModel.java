package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.ImpsTransfer;

public class ImpsTransferInternalModel {

	private boolean status;
	private String description;
	private String message;
	private ImpsTransfer impsTransfer;

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

	public ImpsTransfer getImpsTransfer() {
		return impsTransfer;
	}

	public void setImpsTransfer(ImpsTransfer impsTransfer) {
		this.impsTransfer = impsTransfer;
	}

	@Override
	public String toString() {
		return "ImpsTransferInternalModel [status=" + status + ", description=" + description + ", message=" + message
				+ ", impsTransfer=" + impsTransfer + "]";
	}

}
