package com.federal.fedmobilesmecore.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.model.BlockModeReq;
import com.federal.fedmobilesmecore.model.BlockModeResponse;

@Service
public class BlockModeService {
	private static final Logger log4j = LogManager.getLogger(BlockModeService.class);

	@Autowired
	@Qualifier("template")
	private RestTemplate restTemplate;

	@Value("${blockModeURL}")
	String blockModeURL;

	@Value("${blockMode}")
	String blockMode;

	@Autowired
	GlobalProperties globalProperties;

	public BlockModeResponse blockMode(BlockModeReq request) {
		BlockModeResponse apiResponse = new BlockModeResponse();
		JSONObject jsonReq = new JSONObject();
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dateText = df.format(date);

		jsonReq.put("sysTime", dateText);
		jsonReq.put("custId", request.getCustId());
		jsonReq.put("MobileNo", request.getMobileNo());
		jsonReq.put("countryCode", request.getCountryCode());
		jsonReq.put("succFailflg", request.getSuccFailflg());
		jsonReq.put("errorCode", request.getErrorCode());
		jsonReq.put("errorDesc", request.getErrorDesc());
		jsonReq.put("ipAddress", request.getIpAddress());
		jsonReq.put("deviceId", request.getDeviceId());
		jsonReq.put("emailId", request.getEmailId());
		jsonReq.put("channel", request.getChannel());
		jsonReq.put("deviceType", request.getDeviceType());
		jsonReq.put("hostId", request.getHostId());
		ResponseEntity<String> response = null;
		JSONObject jsonResponse = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(jsonReq.toString(), headers);
		log4j.info("Request to 3rd party call: " + jsonReq.toString(4));
		try {
			if (blockMode.equals("Y")) {
				response = restTemplate.exchange(blockModeURL, HttpMethod.POST, entity, String.class);
				System.out.println("Response from Clari5: " + response);
				jsonResponse = new JSONObject(response.getBody());
				if (jsonResponse != null) {
					System.out.println("Response from Clari5 in JSON format: " + jsonResponse.toString(4));
					log4j.info("Response from 3rd party call in JSON format: " + jsonResponse.toString(4));
					if (jsonResponse.get("StatusDes").toString().equals("Success")) {
						apiResponse.setStatus(true);
						apiResponse.setBlockFlag(jsonResponse.getBoolean("blockFlg"));
						apiResponse.setDescription(jsonResponse.get("Response").toString());
						apiResponse.setMessage(jsonResponse.get("StatusDes").toString());
					} else {
						apiResponse.setStatus(false);
						apiResponse.setDescription(jsonResponse.get("Response").toString());
						apiResponse.setMessage(jsonResponse.get("StatusDes").toString());
					}
				}
			} else {
				apiResponse.setStatus(true);
				apiResponse.setBlockFlag(false);
				apiResponse.setDescription("allow");
				apiResponse.setMessage(globalProperties.getSuccessmsg());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			apiResponse.setDescription(ex.getMessage());
			apiResponse.setMessage(globalProperties.getFailuremsg());
			apiResponse.setStatus(false);
		}
		return apiResponse;
	}

}
