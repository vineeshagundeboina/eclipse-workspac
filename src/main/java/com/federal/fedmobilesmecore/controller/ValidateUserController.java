package com.federal.fedmobilesmecore.controller;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.httpservice.FedSmeGatewayApiCall;
import com.federal.fedmobilesmecore.model.ApplicationFormReqModel;
import com.federal.fedmobilesmecore.model.GetValidateUserSaveFlowModelResp;
import com.federal.fedmobilesmecore.model.RecordIdResp;
import com.federal.fedmobilesmecore.service.ValidateUserService;

/**
 * @author Debasish_Splenta
 *
 */
@RestController
@RequestMapping(path = "/core/validateuser")
public class ValidateUserController {
	@Autowired
	ValidateUserService validateUserService;
	@Autowired
	FedSmeGatewayApiCall fedSmeGatewayApiCall;

	@PostMapping(path = "/applicationform", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateMobile(@RequestBody ApplicationFormReqModel formReqModel) {
		RecordLog.writeLogFile("ValidateUserController /applicationform api is calling.accountnum "+formReqModel.getAccountNo()+" API request: "+formReqModel);

		GetValidateUserSaveFlowModelResp serviceResp = null;
		// 17170200000554,919447512538
		if (formReqModel != null) {
			if (StringUtils.isNotEmpty(formReqModel.getAccountNo())
					&& StringUtils.isNotEmpty(formReqModel.getMobileNo())) {
				try {
					serviceResp = validateUserService.getAccDetailsIfExistResp(formReqModel);
					RecordLog.writeLogFile("ValidateUserController getAccDetailsIfExistResp method completed.accountnum "+formReqModel.getAccountNo()+" method response: "+serviceResp);
				} catch (Exception e) {
					RecordLog.writeLogFile("Exception occured at: account "+formReqModel.getAccountNo()+" "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
					e.printStackTrace();
					serviceResp = new GetValidateUserSaveFlowModelResp();
					serviceResp.setStatus(false);
					serviceResp.setMessage("Exception occured while validating the user.");
					serviceResp.setDescription("failed");
					serviceResp.setRecordId(null);
				}

			} else {
				serviceResp = new GetValidateUserSaveFlowModelResp();
				serviceResp.setStatus(false);
				serviceResp.setMessage("Mandatory parameters not passed.");
				serviceResp.setDescription("failed");
				serviceResp.setRecordId(null);
			}
		} else {
			serviceResp = new GetValidateUserSaveFlowModelResp();
			serviceResp.setStatus(false);
			serviceResp.setMessage("Empty body");
			serviceResp.setDescription("failed");
			serviceResp.setRecordId(null);
		}
		RecordLog.writeLogFile("ValidateUserController /applicationform api completed. accountnum "+formReqModel.getAccountNo()+" API response: "+serviceResp);
		return ResponseEntity.ok(serviceResp);
	}
}