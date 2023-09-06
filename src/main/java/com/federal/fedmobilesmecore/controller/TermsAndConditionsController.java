package com.federal.fedmobilesmecore.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.TCUserAcceptanceResp;
import com.federal.fedmobilesmecore.model.UserTCAcceptanceRequest;
import com.federal.fedmobilesmecore.service.TermsAndConditionService;

@RestController
@RequestMapping(path = "/core/termsandconditions")
public class TermsAndConditionsController {
	private static final Logger log4j = LogManager.getLogger(TermsAndConditionsController.class);
	@Autowired
	TermsAndConditionService termsAndConditionService;

	@Autowired
	GlobalProperties globalProperties;

	@PostMapping(path = "/updateUserAcceptanceTC", consumes = "Application/json", produces = "Application/json")
	public ResponseEntity<?> updateUserTCAcceptance(@RequestBody UserTCAcceptanceRequest request) {
		RecordLog.writeLogFile("TermsAndConditionsController /updateUserAcceptanceTC api is calling. API request: "+request);
		TCUserAcceptanceResp response = null;
		log4j.info("User Acceptance Terms and Conditions API INPUT: " + request.toString());

		if ((StringUtils.isNotEmpty(request.getIsAccepted()))
				&& ((StringUtils.isNotEmpty(request.getUserId())) || (StringUtils.isNotEmpty(request.getMobileNo())))) {
			response = termsAndConditionService.updateUserTCAcceptance(request);
		}

		else {
			response = new TCUserAcceptanceResp();
			response.setDescription(globalProperties.getMandatoryNotPassed());
			response.setMessage(globalProperties.getFailuremsg());
			response.setStatus(false);

		}
		log4j.info("User Acceptance Terms and Conditions API OUTPUT: " + response.toString());
		RecordLog.writeLogFile("TermsAndConditionsController /updateUserAcceptanceTC api is completed. API response: "+response);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(path = "/getTC")
	public ResponseEntity<?> getTCFromFile(@RequestParam String lang) {
		RecordLog.writeLogFile("TermsAndConditionsController /getTC api is calling. API request: "+lang);

		log4j.info("Get Terms and Conditions from File API INPUT: " + lang);
		TCUserAcceptanceResp response = null;
		response = termsAndConditionService.getTermsAndConditionsFromFile(lang);
		log4j.info("Get Terms and Conditions from File API OUTPUT: " + response.toString());
		RecordLog.writeLogFile("TermsAndConditionsController /getTC api is completed. API response: "+response);

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
	
	
}
