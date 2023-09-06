package com.federal.fedmobilesmecore.model;

public class BlockModeResponse {
	private boolean status;
	private String message;
	private String description;
	private Boolean blockFlag;

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

	public Boolean isBlockFlag() {
		return blockFlag;
	}

	public void setBlockFlag(Boolean blockFlag) {
		this.blockFlag = blockFlag;
	}

	@Override
	public String toString() {
		return "BlockModeResponse [status=" + status + ", message=" + message + ", description=" + description
				+ ", blockFlag=" + blockFlag + "]";
	}

}
