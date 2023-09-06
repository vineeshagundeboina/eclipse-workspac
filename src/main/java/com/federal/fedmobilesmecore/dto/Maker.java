package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "MAKERS")
public class Maker implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_MAKER_SEQ")
	@SequenceGenerator(sequenceName = "MAKERS_SEQ", allocationSize = 1, name = "SME_MAKER_SEQ")
	private long id;

	@Column(name = "ACTION_ID")
	private String actionId;

	@Column(name = "CREATED_AT")
	private Timestamp createdAt;

	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

	@Column(name = "USER_ID")
	private String userId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}