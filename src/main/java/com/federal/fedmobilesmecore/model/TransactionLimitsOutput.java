package com.federal.fedmobilesmecore.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TransactionLimitsOutput extends SMEMessage {

	private Double daily;
	private Double daily_used;
	private Double daily_rem;
	private Double monthly;
	private Double monthly_used;
	private Double monthly_rem;
	private Double per_transaction;

	public Double getDaily() {
		return daily;
	}

	public void setDaily(Double daily) {
		this.daily = daily;
	}

	public Double getDaily_used() {
		return daily_used;
	}

	public void setDaily_used(Double daily_used) {
		this.daily_used = daily_used;
	}

	public Double getDaily_rem() {
		return daily_rem;
	}

	public void setDaily_rem(Double daily_rem) {
		this.daily_rem = daily_rem;
	}

	public Double getMonthly() {
		return monthly;
	}

	public void setMonthly(Double monthly) {
		this.monthly = monthly;
	}

	public Double getMonthly_used() {
		return monthly_used;
	}

	public void setMonthly_used(Double monthly_used) {
		this.monthly_used = monthly_used;
	}

	public Double getMonthly_rem() {
		return monthly_rem;
	}

	public void setMonthly_rem(Double monthly_rem) {
		this.monthly_rem = monthly_rem;
	}

	public Double getPer_transaction() {
		return per_transaction;
	}

	public void setPer_transaction(Double per_transaction) {
		this.per_transaction = per_transaction;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);

	}
}
