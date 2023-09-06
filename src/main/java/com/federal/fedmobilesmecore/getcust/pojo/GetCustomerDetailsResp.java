
package com.federal.fedmobilesmecore.getcust.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "GetCustomerDetailsResp" })
public class GetCustomerDetailsResp implements Serializable {

	@JsonProperty("GetCustomerDetailsResp")
	private GetCustomerDetailsResp__1 getCustomerDetailsResp;
	private final static long serialVersionUID = 128409022528354468L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public GetCustomerDetailsResp() {
	}

	/**
	 * 
	 * @param getCustomerDetailsResp
	 */
	public GetCustomerDetailsResp(GetCustomerDetailsResp__1 getCustomerDetailsResp) {
		super();
		this.getCustomerDetailsResp = getCustomerDetailsResp;
	}

	@JsonProperty("GetCustomerDetailsResp")
	public GetCustomerDetailsResp__1 getGetCustomerDetailsResp() {
		return getCustomerDetailsResp;
	}

	@JsonProperty("GetCustomerDetailsResp")
	public void setGetCustomerDetailsResp(GetCustomerDetailsResp__1 getCustomerDetailsResp) {
		this.getCustomerDetailsResp = getCustomerDetailsResp;
	}

}
