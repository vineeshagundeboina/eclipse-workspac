package com.federal.fedmobilesmecore.model;

import com.federal.fedmobilesmecore.dto.SimBinding;

/**
 * @author Debasish_Splenta
 *
 */

public class CreateSimBindingRespModel {
	private boolean status;
	private String description;
	private String message;
	private SimBinding simBinding;
	private String receipientMobile;

	public String getReceipientMobile() {
		return receipientMobile;
	}

	public void setReceipientMobile(String receipientMobile) {
		this.receipientMobile = receipientMobile;
	}

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

	public SimBinding getSimBinding() {
		return simBinding;
	}

	public void setSimBinding(SimBinding simBinding) {
		this.simBinding = simBinding;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((simBinding == null) ? 0 : simBinding.hashCode());
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
		CreateSimBindingRespModel other = (CreateSimBindingRespModel) obj;
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
		if (simBinding == null) {
			if (other.simBinding != null)
				return false;
		} else if (!simBinding.equals(other.simBinding))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CreateSimBindingRespModel [status=" + status + ", description=" + description + ", message=" + message
				+ ", simBinding=" + simBinding + ", receipientMobile=" + receipientMobile + "]";
	}

}
