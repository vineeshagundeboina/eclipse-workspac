package com.federal.fedmobilesmecore.model;




public class DebitcardVerificationPayload {
	private long card;
	private int pin;
	private ExpiryDatePayload expiryDate;
	private String app_token;
	public long getCard() {
		return card;
	}
	public void setCard(long card) {
		this.card = card;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public ExpiryDatePayload getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(ExpiryDatePayload expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getApp_token() {
		return app_token;
	}
	public void setApp_token(String app_token) {
		this.app_token = app_token;
	}
	@Override
	public String toString() {
		return "DebitcardVerificationPayload [card=" + card + ", pin=" + pin + ", expiryDate=" + expiryDate
				+ ", app_token=" + app_token + "]";
	}
}
