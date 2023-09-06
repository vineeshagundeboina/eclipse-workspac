package com.federal.fedmobilesmecore.model;

public class BenDetails {
	public String ben;
	public String benAccNo;
	public String benMobNo;

	public String getBen() {
		return ben;
	}

	public void setBen(String ben) {
		this.ben = ben;
	}

	public String getBenAccNo() {
		return benAccNo;
	}

	public void setBenAccNo(String benAccNo) {
		this.benAccNo = benAccNo;
	}

	public String getBenMobNo() {
		return benMobNo;
	}

	public void setBenMobNo(String benMobNo) {
		this.benMobNo = benMobNo;
	}

	@Override
	public String toString() {
		return "BenDetails [ben=" + ben + ", benAccNo=" + benAccNo + ", benMobNo=" + benMobNo + "]";
	}
}