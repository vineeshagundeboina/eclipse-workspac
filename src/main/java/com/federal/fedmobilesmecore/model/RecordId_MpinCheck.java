package com.federal.fedmobilesmecore.model;

import java.util.List;

import com.federal.fedmobilesmecore.dto.User;

public class RecordId_MpinCheck {
	
	private List<User> user;
	private String Authtoken;
	private String State;
	private String Prefcorp;
	private String Favouriteaccount;

	public List<User> getUser() {
		return user;
	}
	public void setUser(List<User> user) {
		this.user = user;
	}
	public String getAuthtoken() {
		return Authtoken;
	}
	public void setAuthtoken(String authtoken) {
		Authtoken = authtoken;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getPrefcorp() {
		return Prefcorp;
	}
	public void setPrefcorp(String prefcorp) {
		Prefcorp = prefcorp;
	}
	public String getFavouriteaccount() {
		return Favouriteaccount;
	}
	public void setFavouriteaccount(String favouriteaccount) {
		Favouriteaccount = favouriteaccount;
	}
	@Override
	public String toString() {
		return "RecordId_MpinCheck [user=" + user + ", Authtoken=" + Authtoken + ", State=" + State + ", Prefcorp="
				+ Prefcorp + ", Favouriteaccount=" + Favouriteaccount + "]";
	}
	
	
}
