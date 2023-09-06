package com.federal.fedmobilesmecore.model;

/**
 * @author Debasish_Splenta
 *
 */

public class GetSimBindingRespModel {
	private boolean status;
	private String description;
	private String message;
	private String appToken;

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

	public String getAppToken() {
		return appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appToken == null) ? 0 : appToken.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		GetSimBindingRespModel other = (GetSimBindingRespModel) obj;
		if (appToken == null) {
			if (other.appToken != null)
				return false;
		} else if (!appToken.equals(other.appToken))
			return false;
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
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GetSimBindingRespModel [status=" + status + ", description=" + description + ", message=" + message
				+ ", appToken=" + appToken + "]";
	}

}
