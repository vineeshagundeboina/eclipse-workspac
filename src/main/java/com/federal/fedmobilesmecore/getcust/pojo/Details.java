
package com.federal.fedmobilesmecore.getcust.pojo;

import java.io.Serializable;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "CONTACT_NO",
    "ACCOUNT_TYPE",
    "CUSTOMER_NAME",
    "ERRORMSG",
    "ERRORCODE",
    "CUSTOMER_ID",
    "TYPECD",
    "REMARKS",
    "EMAIL",
    "PAN"
})

public class Details implements Serializable
{

    @JsonProperty("CONTACT_NO")
    private Long contactNo;
    @JsonProperty("ACCOUNT_TYPE")
    private String accountType;
    @JsonProperty("CUSTOMER_NAME")
    private String customerName;
    @JsonProperty("ERRORMSG")
    private String errormsg;
    @JsonProperty("ERRORCODE")
    private String errorcode;
    @JsonProperty("CUSTOMER_ID")
    private Long customerId;
    @JsonProperty("TYPECD")
    private String typecd;
    @JsonProperty("REMARKS")
    private String remarks;
    @JsonProperty("EMAIL")
    private String email;
    @JsonProperty("PAN")
    private String pan;
    private final static long serialVersionUID = -8882607263867646504L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Details() {
    }

    /**
     * 
     * @param typecd
     * @param accountType
     * @param customerId
     * @param pan
     * @param customerName
     * @param errormsg
     * @param errorcode
     * @param remarks
     * @param email
     * @param contactNo
     */
    public Details(Long contactNo, String accountType, String customerName, String errormsg, String errorcode, Long customerId, String typecd, String remarks, String email, String pan) {
        super();
        this.contactNo = contactNo;
        this.accountType = accountType;
        this.customerName = customerName;
        this.errormsg = errormsg;
        this.errorcode = errorcode;
        this.customerId = customerId;
        this.typecd = typecd;
        this.remarks = remarks;
        this.email = email;
        this.pan = pan;
    }

    @JsonProperty("CONTACT_NO")
    public Long getContactNo() {
        return contactNo;
    }

    @JsonProperty("CONTACT_NO")
    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }

    @JsonProperty("ACCOUNT_TYPE")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("ACCOUNT_TYPE")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("CUSTOMER_NAME")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("CUSTOMER_NAME")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("ERRORMSG")
    public String getErrormsg() {
        return errormsg;
    }

    @JsonProperty("ERRORMSG")
    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    @JsonProperty("ERRORCODE")
    public String getErrorcode() {
        return errorcode;
    }

    @JsonProperty("ERRORCODE")
    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    @JsonProperty("CUSTOMER_ID")
    public Long getCustomerId() {
        return customerId;
    }

    @JsonProperty("CUSTOMER_ID")
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("TYPECD")
    public String getTypecd() {
        return typecd;
    }

    @JsonProperty("TYPECD")
    public void setTypecd(String typecd) {
        this.typecd = typecd;
    }

    @JsonProperty("REMARKS")
    public String getRemarks() {
        return remarks;
    }

    @JsonProperty("REMARKS")
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JsonProperty("EMAIL")
    public String getEmail() {
        return email;
    }

    @JsonProperty("EMAIL")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("PAN")
    public String getPan() {
        return pan;
    }

    @JsonProperty("PAN")
    public void setPan(String pan) {
        this.pan = pan;
    }

}
