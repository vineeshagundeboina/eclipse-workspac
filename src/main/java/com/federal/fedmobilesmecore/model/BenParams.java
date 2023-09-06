package com.federal.fedmobilesmecore.model;

public class BenParams {
	public String benName;
	public String benNickName;

	public String getBenName() {
		return benName;
	}

	public void setBenName(String benName) {
		this.benName = benName;
	}

	public String getBenNickName() {
		return benNickName;
	}

	public void setBenNickName(String benNickName) {
		this.benNickName = benNickName;
	}

	@Override
	public String toString() {
		return "BenParams [benName=" + benName + ", benNickName=" + benNickName + "]";
	}

}
