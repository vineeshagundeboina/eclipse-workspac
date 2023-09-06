
package com.federal.fedmobilesmecore.getcust.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AccountDetails"
})
public class GetCustomerDetailsResp__1 implements Serializable
{

    @JsonProperty("AccountDetails")
    private List<AccountDetail> accountDetails = null;
    private final static long serialVersionUID = -2601304281327689954L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GetCustomerDetailsResp__1() {
    }

    /**
     * 
     * @param accountDetails
     */
    public GetCustomerDetailsResp__1(List<AccountDetail> accountDetails) {
        super();
        this.accountDetails = accountDetails;
    }

    @JsonProperty("AccountDetails")
    public List<AccountDetail> getAccountDetails() {
        return accountDetails;
    }

    @JsonProperty("AccountDetails")
    public void setAccountDetails(List<AccountDetail> accountDetails) {
        this.accountDetails = accountDetails;
    }

}
