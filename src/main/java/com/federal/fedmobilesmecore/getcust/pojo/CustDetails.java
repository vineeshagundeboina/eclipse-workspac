package com.federal.fedmobilesmecore.getcust.pojo;

import com.federal.fedmobilesmecore.model.SMEMessage;

public class CustDetails extends SMEMessage {

	private GetCustomerDetailsResp response;

	public GetCustomerDetailsResp getResponse() {
		return response;
	}

	public void setResponse(GetCustomerDetailsResp response) {
		this.response = response;
	}

}
