package com.federal.fedmobilesmecore.httpservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.CheckcustdetlsReq;

@Component
public class FedSmeGatewayApiCall {
	private final RestTemplate restTemplate;

	public FedSmeGatewayApiCall(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	//@HystrixCommand(defaultFallback = "customerServiceFallback")
	public ResponseEntity<?> getCustomerDetailsServiceCall(String accntNo) {
		CheckcustdetlsReq checkcustdetlsReq = new CheckcustdetlsReq();
		checkcustdetlsReq.setAcctNo(accntNo);
		
		ResponseEntity<?> response = restTemplate.postForEntity(
				"http://10.251.111.27:8057/core/getcustdetls_v1/checkcustdetls", checkcustdetlsReq, String.class);
		RecordLog.writeLogFile("response " + response);
		return response;
	}

	public ResponseEntity<?> customerServiceFallback() {
		Map<String, Object> ifFailed = new HashMap<String, Object>();
		ifFailed.put("status", false);
		ifFailed.put("statusCode", "01");
		RecordLog.writeLogFile("response _ client " + ifFailed);
		return ResponseEntity.ok(ifFailed);
	}

}
