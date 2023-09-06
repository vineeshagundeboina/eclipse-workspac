package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "USER_PINS")
public class UserPin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_UPS_SEQ")
	@SequenceGenerator(sequenceName = "USER_PINS_SEQ", allocationSize = 1, name = "SME_UPS_SEQ")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(precision = 1)
	private BigDecimal active;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "EXPIRED_AT")
	private Timestamp expiredAt;

	@Column(length = 255)
	private String pin;

	@Column(name = "TYPE", length = 255)
	private String type;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	@Column(name = "USER_ID", precision = 38)
	private BigDecimal userId;

	public UserPin() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getActive() {
		return active;
	}

	public void setActive(BigDecimal active) {
		this.active = active;
	}

	public String getPin() {
		return this.pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Timestamp expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public BigDecimal getUserId() {
		return this.userId;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

}