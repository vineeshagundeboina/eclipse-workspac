package com.federal.fedmobilesmecore.model;

import java.sql.Timestamp;

public class FavouriteAccountRequest {

	private String favourite_account;
	private String auth_token;
	private String pref_corp;
	private Timestamp timeStamp;

	public String getFavourite_account() {
		return favourite_account;
	}

	public void setFavourite_account(String favourite_account) {
		this.favourite_account = favourite_account;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

	public String getPref_corp() {
		return pref_corp;
	}

	public void setPref_corp(String pref_corp) {
		this.pref_corp = pref_corp;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auth_token == null) ? 0 : auth_token.hashCode());
		result = prime * result + ((favourite_account == null) ? 0 : favourite_account.hashCode());
		result = prime * result + ((pref_corp == null) ? 0 : pref_corp.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		FavouriteAccountRequest other = (FavouriteAccountRequest) obj;
		if (auth_token == null) {
			if (other.auth_token != null)
				return false;
		} else if (!auth_token.equals(other.auth_token))
			return false;
		if (favourite_account == null) {
			if (other.favourite_account != null)
				return false;
		} else if (!favourite_account.equals(other.favourite_account))
			return false;
		if (pref_corp == null) {
			if (other.pref_corp != null)
				return false;
		} else if (!pref_corp.equals(other.pref_corp))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FavouriteAccountRequest [favourite_account=" + favourite_account + ", auth_token=" + auth_token
				+ ", pref_corp=" + pref_corp + ", timeStamp=" + timeStamp + "]";
	}
}
