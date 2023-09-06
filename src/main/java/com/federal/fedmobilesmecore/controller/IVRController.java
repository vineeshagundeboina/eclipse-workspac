package com.federal.fedmobilesmecore.controller;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.IVRInput;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.service.IvrUrlBuild;

@RestController
@RequestMapping(path = "/core/public")
public class IVRController {

	private static final Logger log4j = LogManager.getLogger(IVRController.class);

	@Autowired
	GlobalProperties properties;

	@Autowired
	IvrUrlBuild ivrbuild;
	
	

	@io.swagger.v3.oas.annotations.Operation(summary = "IVR Create. It will take the input and create a new IVR Code and sent it to the External API and it will insert the same reference in the IVRS Table.")
	@PostMapping(path = "/createivr", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> IvrCreate(@RequestBody IVRInput input)
			throws JsonProcessingException, KeyManagementException, KeyStoreException {
		RecordLog.writeLogFile("IVRController /createivr api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		if (input != null) {
			if (StringUtils.isNotEmpty(input.getAppHash()) && StringUtils.isNotEmpty(input.getMobile())) {
				try {
					// Calling external micro service to do a IVR Check.
					SMEMessage messagestatus = ivrbuild.buildUrl(input.getMobile());
					if (messagestatus.isStatus() == true) {
						boolean status = ivrbuild.constructIVR(input, messagestatus.getRecordid());
						if (status == true) {
							messagestatus.setRecordid("");
							message = messagestatus;
						} else {
							message.setStatus(false);
							message.setMessage(properties.getIvrfailobjsave());
						}

					} else {
						message.setStatus(false);
						message.setMessage(messagestatus.getMessage());
					}

				} catch (NoSuchAlgorithmException e) {
					RecordLog.writeLogFile("Exception occured at: for mobile "+input.getMobile() +Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
					message.setStatus(false);
					message.setMessage("Error While encryption");
					e.printStackTrace();
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getIvrfailcheck());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Request cannot be empty");
		}
		RecordLog.writeLogFile("IVRController /createivr api completed. API response: for mobile "+input.getMobile() +message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "IVR Verify")
	@PostMapping(path = "/verifyivr", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> IvrVerify(@RequestBody IVRInput input) throws KeyManagementException, KeyStoreException {
		RecordLog.writeLogFile("IVRController /verifyivr api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		try {
			message = ivrbuild.ivrVerify(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			RecordLog.writeLogFile("Exception occured at: for mobile "+input.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			message.setStatus(false);
			message.setMessage("Error occured while verifying the IVR.");
		}
		RecordLog.writeLogFile("IVRController /verifyivr api completed. API response: for mobile "+input.getMobile()+message);
		return ResponseEntity.ok(message);
	}
	
	// new api v2_4
	@io.swagger.v3.oas.annotations.Operation(summary = "IVR Verifyv2_4")
	@PostMapping(path = "/verifyivrv2_4", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> IvrVerifyv24(@RequestBody IVRInput input) throws KeyManagementException, KeyStoreException {
		RecordLog.writeLogFile("IVRController /verifyivrv2_4 api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		try {
			message = ivrbuild.ivrVerifyv24(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			RecordLog.writeLogFile("Exception occured at: for mobile "+input.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			message.setStatus(false);
			message.setMessage("Error occured while verifying the IVR.");
		}
		RecordLog.writeLogFile("IVRController /verifyivrv2_4 api completed. API response: for mobile "+input.getMobile()+message);
		return ResponseEntity.ok(message);
	}

}
