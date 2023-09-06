package com.federal.fedmobilesmecore.model;

import java.util.List;

import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;

public class RecordId1 {
	private List<FundTransfer> fundTransfer;
	private List<ImpsTransfer> impsTransfer;

	public List<FundTransfer> getFundTransfer() {
		return fundTransfer;
	}

	public void setFundTransfer(List<FundTransfer> fundTransfer) {
		this.fundTransfer = fundTransfer;
	}

	public List<ImpsTransfer> getImpsTransfer() {
		return impsTransfer;
	}

	public void setImpsTransfer(List<ImpsTransfer> impsTransfer) {
		this.impsTransfer = impsTransfer;
	}

	@Override
	public String toString() {
		return "RecordId1 [fundTransfer=" + fundTransfer + ", impsTransfer=" + impsTransfer + "]";
	}

}
