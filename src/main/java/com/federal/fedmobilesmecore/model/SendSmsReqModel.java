package com.federal.fedmobilesmecore.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Debasish_Splenta
 *
 */

public class SendSmsReqModel {
	private String mobileno;
	private String messageText;
	private String shortCode;

	public String getMobileNo() {
		return mobileno;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileno = mobileNo;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageText == null) ? 0 : messageText.hashCode());
		result = prime * result + ((mobileno == null) ? 0 : mobileno.hashCode());
		result = prime * result + ((shortCode == null) ? 0 : shortCode.hashCode());
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
		SendSmsReqModel other = (SendSmsReqModel) obj;
		if (messageText == null) {
			if (other.messageText != null)
				return false;
		} else if (!messageText.equals(other.messageText))
			return false;
		if (mobileno == null) {
			if (other.mobileno != null)
				return false;
		} else if (!mobileno.equals(other.mobileno))
			return false;
		if (shortCode == null) {
			if (other.shortCode != null)
				return false;
		} else if (!shortCode.equals(other.shortCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
