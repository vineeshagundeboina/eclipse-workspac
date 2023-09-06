package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the PASSWORD_HISTORIES database table.
 * 
 */
@Entity
@Table(name = "PASSWORD_HISTORIES")
@NamedQuery(name = "PasswordHistory.findAll", query = "SELECT p FROM PasswordHistory p")
public class PasswordHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_PWD_SEQ")
	@SequenceGenerator(sequenceName = "PASSWORD_HISTORIES_SEQ", allocationSize = 1, name = "SME_PWD_SEQ")
	private long id;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	private String mpin;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "USER_ID")
	private long userId;

	public PasswordHistory() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getMpin() {
		return this.mpin;
	}

	public void setMpin(String mpin) {
		this.mpin = mpin;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}