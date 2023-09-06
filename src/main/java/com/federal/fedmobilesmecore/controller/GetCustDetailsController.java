package com.federal.fedmobilesmecore.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.getcust.pojo.CustDetails;
import com.federal.fedmobilesmecore.getcust.pojo.GetCustomerDetailsResp;
import com.federal.fedmobilesmecore.model.CreateCustDetailsReqModel;
import com.federal.fedmobilesmecore.model.GetCustDetails;
import com.federal.fedmobilesmecore.service.GetCustDetailsService;

/**
 * @author Syed_Splenta
 *
 */

@RestController
@RequestMapping(path = "/core/getcustdetls_v1")
public class GetCustDetailsController {

	private static final Logger log4j = LogManager.getLogger(GetCustDetailsController.class);

	@Autowired
	GetCustDetailsService custDetailsService;
	@Autowired
	GlobalProperties messages;
	@Autowired
	GetCustDetails GetCustinfo;

	@io.swagger.v3.oas.annotations.Operation(summary = "Get Customer Details API.")
	@PostMapping(path = "checkcustdetls", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> checkCustDetls(@RequestBody CreateCustDetailsReqModel createCustDetailsReq) {
		RecordLog.writeLogFile("GetCustDetailsController checkcustdetls api is calling. API request: "+createCustDetailsReq);
		Map<String, Object> mapping = null;
		String acctno = createCustDetailsReq.getAcctNo();
		mapping = custDetailsService.checkIfAcctExist(acctno);
		RecordLog.writeLogFile("GetCustDetailsController checkcustdetls api completed. API response: "+mapping);
		return ResponseEntity.ok(mapping);
		// return ResponseEntity.status(HttpStatus.OK).body(mapping.toString());
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Get Customer Details Gateway API.")
	@PostMapping(path = "getcustomerinfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> getcustinfo(@RequestBody CreateCustDetailsReqModel createCustDetailsReq)
			throws IOException {
		RecordLog.writeLogFile("GetCustDetailsController getcustomerinfo api is calling. API request: "+createCustDetailsReq);
		String mapping = null;
		String acctno = createCustDetailsReq.getAcctNo();
		mapping = GetCustinfo.getData(acctno);
		RecordLog.writeLogFile("GetCustDetailsController getcustomerinfo api completed. API response: "+mapping);
		return ResponseEntity.ok(mapping);
		// return ResponseEntity.status(HttpStatus.OK).body(mapping.toString());
	}

//	@io.swagger.v3.oas.annotations.Operation(summary = "Get Customer Account Details Gateway API.")
//	@PostMapping(path = "getcustacctinfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getcustacctinfo(@RequestBody CreateCustDetailsReqModel createCustDetailsReq) throws IOException {
//		String mapping = null;
//		Map<String, Object> retMap = new HashMap<String, Object>();
//		String acctno = createCustDetailsReq.getAcctNo();
//		mapping = GetCustinfo.getData(acctno);
//		JSONObject json = XML.toJSONObject(mapping);		
//		if (json != JSONObject.NULL) {
//			retMap = GetCustDetailsService.toMap(json);
//		}	
//		RecordLog.writeLogFile("mapping---->>> "+retMap);
//		return ResponseEntity.ok(retMap);
//		//return ResponseEntity.status(HttpStatus.OK).body(mapping.toString());
//	}

	/**
	 * Gives the CustDetails response as a json.
	 * 
	 * @param createCustDetailsReq
	 * @return
	 * @throws IOException
	 */
	@io.swagger.v3.oas.annotations.Operation(summary = "Get Customer Account Details With Json Reponse.")
	@PostMapping(path = "getcustacctinfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getcustacctinfo(@RequestBody CreateCustDetailsReqModel createCustDetailsReq)
			throws IOException {
		RecordLog.writeLogFile("GetCustDetailsController getcustacctinfo api is calling. API request: "+createCustDetailsReq);
		CustDetails message = new CustDetails();
		String acctno = createCustDetailsReq.getAcctNo();
		RecordLog.writeLogFile("Account no is:"+acctno);
		if (StringUtils.isNotBlank(acctno)) {
			RecordLog.writeLogFile("inside if Account no is:"+acctno);
			String mapping = GetCustinfo.getData(acctno);
			JSONObject json = XML.toJSONObject(mapping);
			RecordLog.writeLogFile("Customer info: " + json);
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			jsonMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
			GetCustomerDetailsResp tempcustdetails = jsonMapper.readValue(json.toString(), GetCustomerDetailsResp.class);
			GetCustomerDetailsResp custdetails=custDetailsService.filterAccounts(tempcustdetails, createCustDetailsReq.getPrefno(),createCustDetailsReq.getMobile());
			if (custdetails != null) {
				message.setStatus(true);
				message.setResponse(custdetails);
			} else {
				message.setStatus(false);
				message.setMessage("Unable to fetch the customer details.");
			}
		} else {
			message.setStatus(false);
			message.setMessage("Account number cannot be empty");
		}
		RecordLog.writeLogFile("GetCustDetailsController getcustacctinfo api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

}
