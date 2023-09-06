package com.federal.fedmobilesmecore.model;

public class CaptchaReq {
	private String reponse;

	public String getReponse() {
		return reponse;
	}

	public void setReponse(String reponse) {
		this.reponse = reponse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reponse == null) ? 0 : reponse.hashCode());
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
		CaptchaReq other = (CaptchaReq) obj;
		if (reponse == null) {
			if (other.reponse != null)
				return false;
		} else if (!reponse.equals(other.reponse))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaptchaReq [reponse=" + reponse + "]";
	}

}
