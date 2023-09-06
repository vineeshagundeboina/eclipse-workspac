package com.federal.fedmobilesmecore.httpservice;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.SendSmsReqModel;

@Component
public class FedExternalUserGatewayApiCall {
	@Value("${fblgateway.url.http}")
	private String fblgatewayurl;

	private final RestTemplate restTemplate;

	public FedExternalUserGatewayApiCall(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<?> getCustomerDetailsServiceCall(String gatewayMessage, String mobileNo) {

		SendSmsReqModel sendSmsReqModel = new SendSmsReqModel();
		sendSmsReqModel.setMessageText(gatewayMessage);
		sendSmsReqModel.setMobileNo(mobileNo);
		sendSmsReqModel.setShortCode(String.valueOf(new Random().nextInt(9000000) + 1000000));

		ResponseEntity<?> smsResponse = restTemplate.postForEntity(fblgatewayurl + "/fblgateway_v1/sendmsgtouser",
				sendSmsReqModel, String.class);
		RecordLog.writeLogFile("getStatusCodeValue " + smsResponse.getStatusCodeValue());
		RecordLog.writeLogFile("smsResponse body " + smsResponse.getBody().toString());
		RecordLog.writeLogFile("smsResponse body " + smsResponse.getStatusCode());
		RecordLog.writeLogFile("smsResponse body " + smsResponse.getHeaders());
		return smsResponse;
	}

}
