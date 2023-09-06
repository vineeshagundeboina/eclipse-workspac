package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "APPLICATION_FORMS")
public class ApplicationForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 38)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_FORM_SEQ")
	@SequenceGenerator(sequenceName = "APPLICATION_FORMS_SEQ", allocationSize = 1, name = "SME_FORM_SEQ")
	private long id;

	@Column(name = "APPROVED_AT")
	private Timestamp approvedAt;

	@Column(name = "APPROVED_BY", length = 255)
	private String approvedBy;

	@Column(name = "CREATED_AT", nullable = false)
	private Timestamp createdAt;

	@Column(name = "CREATED_BY", length = 255)
	private String createdBy;

	@Column(name = "DECLARATION", length = 255)
	private String declaration;

	@Column(name = "FORWARDED_AT")
	private Timestamp forwardedAt;

	@Column(name = "FORWARDED_BY", length = 255)
	private String forwardedBy;

	@Column(name = "MODIFIED_AT")
	private Timestamp modifiedAt;

	@Column(name = "MODIFIED_BY", length = 255)
	private String modifiedBy;

	@Column(name = "REF_NO", length = 255)
	private String refNo;

	@Column(name = "REFERENCE_ID", length = 255)
	private String referenceId;

	@Column(name = "REJECTED_AT")
	private Timestamp rejectedAt;

	@Column(name = "REJECTED_BY", length = 255)
	private String rejectedBy;

	@Column(name = "RESUBMITTED_AT")
	private Timestamp resubmittedAt;

	@Column(name = "RESUBMITTED_BY", length = 255)
	private String resubmittedBy;

	@Column(length = 255)
	private String status;

	@Column(name = "STATUS_DESC", length = 255)
	private String statusDesc;

	@Column(name = "UPDATED_AT", nullable = false)
	private Timestamp updatedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Timestamp approvedAt) {
		this.approvedAt = approvedAt;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public Timestamp getForwardedAt() {
		return forwardedAt;
	}

	public void setForwardedAt(Timestamp forwardedAt) {
		this.forwardedAt = forwardedAt;
	}

	public String getForwardedBy() {
		return forwardedBy;
	}

	public void setForwardedBy(String forwardedBy) {
		this.forwardedBy = forwardedBy;
	}

	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Timestamp getRejectedAt() {
		return rejectedAt;
	}

	public void setRejectedAt(Timestamp rejectedAt) {
		this.rejectedAt = rejectedAt;
	}

	public String getRejectedBy() {
		return rejectedBy;
	}

	public void setRejectedBy(String rejectedBy) {
		this.rejectedBy = rejectedBy;
	}

	public Timestamp getResubmittedAt() {
		return resubmittedAt;
	}

	public void setResubmittedAt(Timestamp resubmittedAt) {
		this.resubmittedAt = resubmittedAt;
	}

	public String getResubmittedBy() {
		return resubmittedBy;
	}

	public void setResubmittedBy(String resubmittedBy) {
		this.resubmittedBy = resubmittedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}