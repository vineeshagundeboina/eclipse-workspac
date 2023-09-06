package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "USER_TOKENS")
//@NamedQuery(name="UserToken.findAll", query="SELECT u FROM UserToken u")
public class UserToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_URS_SEQ")
	@SequenceGenerator(sequenceName = "USER_TOKENS_SEQ", allocationSize = 1, name = "SME_URS_SEQ")
	@Column(unique = true, nullable = false, precision = 38)
	private long id;

	@Column(precision = 1)
	private BigDecimal active;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "EXPIRED_AT")
	private Timestamp expiredAt;

	@Column(length = 255)
	private String token;

	@Column(name = "TYPE", length = 255)
	private String type;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	public long getId() {
		return id;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}