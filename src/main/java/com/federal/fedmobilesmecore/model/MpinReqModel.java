package com.federal.fedmobilesmecore.model;

import org.springframework.stereotype.Service;

/**
 * @author Syed_Splenta
 *
 */

@Service
public class MpinReqModel {
	private String device_no;
	private String mpin_hash;
	private String app_token;
	private String push_token;
	public String getDevice_no() {
		return device_no;
	}
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}
	public String getMpin_hash() {
		return mpin_hash;
	}
	public void setMpin_hash(String mpin_hash) {
		this.mpin_hash = mpin_hash;
	}
	public String getApp_token() {
		return app_token;
	}
	public void setApp_token(String app_token) {
		this.app_token = app_token;
	}
	public String getPush_token() {
		return push_token;
	}
	public void setPush_token(String push_token) {
		this.push_token = push_token;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((app_token == null) ? 0 : app_token.hashCode());
		result = prime * result + ((device_no == null) ? 0 : device_no.hashCode());
		result = prime * result + ((mpin_hash == null) ? 0 : mpin_hash.hashCode());
		result = prime * result + ((push_token == null) ? 0 : push_token.hashCode());
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
		MpinReqModel other = (MpinReqModel) obj;
		if (app_token == null) {
			if (other.app_token != null)
				return false;
		} else if (!app_token.equals(other.app_token))
			return false;
		if (device_no == null) {
			if (other.device_no != null)
				return false;
		} else if (!device_no.equals(other.device_no))
			return false;
		if (mpin_hash == null) {
			if (other.mpin_hash != null)
				return false;
		} else if (!mpin_hash.equals(other.mpin_hash))
			return false;
		if (push_token == null) {
			if (other.push_token != null)
				return false;
		} else if (!push_token.equals(other.push_token))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MpinReqModel [device_no=" + device_no + ", mpin_hash=" + mpin_hash + ", app_token=" + app_token
				+ ", push_token=" + push_token + "]";
	}

	

}
