package com.federal.fedmobilesmecore.dto;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "BRANCH_VERIFICATIONS")
public class BranchVerification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 38)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SME_VERF_SEQ")
	@SequenceGenerator(sequenceName = "BRANCH_VERIFICATIONS_SEQ", allocationSize = 1, name = "SME_VERF_SEQ")
	private long id;

	@Column(name = "APPLICATION_FORM_ID", precision = 38)
	private Long applicationFormId;

	@Column(name = "BOARD_RESOLUTION_CONFIRMED", precision = 1)
	private Long boardResolutionConfirmed;

	@Column(name = "MODE_OF_OPERATION", length = 255)
	private String modeOfOperation;

	@Column(name = "MODE_OF_OPERATION_CONFIRMED", precision = 1)
	private Long modeOfOperationConfirmed;

	@Column(name = "USER_DETAILS_CONFIRMED", precision = 1)
	private Long userDetailsConfirmed;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getApplicationFormId() {
		return applicationFormId;
	}

	public void setApplicationFormId(Long applicationFormId) {
		this.applicationFormId = applicationFormId;
	}

	public Long getBoardResolutionConfirmed() {
		return boardResolutionConfirmed;
	}

	public void setBoardResolutionConfirmed(Long boardResolutionConfirmed) {
		this.boardResolutionConfirmed = boardResolutionConfirmed;
	}

	public String getModeOfOperation() {
		return modeOfOperation;
	}

	public void setModeOfOperation(String modeOfOperation) {
		this.modeOfOperation = modeOfOperation;
	}

	public Long getModeOfOperationConfirmed() {
		return modeOfOperationConfirmed;
	}

	public void setModeOfOperationConfirmed(Long modeOfOperationConfirmed) {
		this.modeOfOperationConfirmed = modeOfOperationConfirmed;
	}

	public Long getUserDetailsConfirmed() {
		return userDetailsConfirmed;
	}

	public void setUserDetailsConfirmed(Long userDetailsConfirmed) {
		this.userDetailsConfirmed = userDetailsConfirmed;
	}

}