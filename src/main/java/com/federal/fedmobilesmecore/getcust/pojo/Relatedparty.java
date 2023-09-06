
package com.federal.fedmobilesmecore.getcust.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "DETAILS" })
public class Relatedparty implements Serializable {

	@JsonProperty("DETAILS")
	private List<Details> details;
	private final static long serialVersionUID = -7919533221201062665L;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Relatedparty() {
	}

	/**
	 * 
	 * @param details
	 */
	public Relatedparty(List<Details> details) {
		super();
		this.details = details;
	}

	@JsonProperty("DETAILS")
	public List<Details> getDetails() {
		return details;
	}

	@JsonProperty("DETAILS")
	public void setDetails(List<Details> details) {
		this.details = details;
	}

}
