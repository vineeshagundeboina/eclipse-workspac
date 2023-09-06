package com.federal.fedmobilesmecore.model;

public class LastLoginDetailsRequest {

	private String acc_no;
	private String auth_token;

	public String getAcc_no() {
		return acc_no;
	}

	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acc_no == null) ? 0 : acc_no.hashCode());
		result = prime * result + ((auth_token == null) ? 0 : auth_token.hashCode());
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
		LastLoginDetailsRequest other = (LastLoginDetailsRequest) obj;
		if (acc_no == null) {
			if (other.acc_no != null)
				return false;
		} else if (!acc_no.equals(other.acc_no))
			return false;
		if (auth_token == null) {
			if (other.auth_token != null)
				return false;
		} else if (!auth_token.equals(other.auth_token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LastLoginDetailsRequest [acc_no=" + acc_no + ", auth_token=" + auth_token + "]";
	}

}
