package com.federal.fedmobilesmecore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.CreateSimBindingReqModel;
import com.federal.fedmobilesmecore.model.CreateSimBindingRespModel;
import com.federal.fedmobilesmecore.model.GetSimBindingReqModel;
import com.federal.fedmobilesmecore.model.GetSimBindingRespModel;
import com.federal.fedmobilesmecore.service.SimBindingService;

/**
 * @author Debasish_Splenta
 *
 */

@RestController
@RequestMapping(path = "/core/simbind")
public class SimBindingController {
	
	private String longCode=System.getenv("longcode");
	@Autowired
	SimBindingService simBindingService;

	@Autowired
	GlobalProperties globalProperties;

	@Autowired
	GlobalProperties messages;

	@PostMapping(path = "createbinding", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSimBinding(@RequestBody CreateSimBindingReqModel createSimBindingReqModel) {
		RecordLog.writeLogFile("SimBindingController createbinding api is calling. API request: "+createSimBindingReqModel);
		CreateSimBindingRespModel bindingRespModel = null;
		bindingRespModel = simBindingService.createSimService(createSimBindingReqModel);
		bindingRespModel.setReceipientMobile(longCode);
		RecordLog.writeLogFile("SimBindingController createbinding api completed. API response: "+bindingRespModel);
		return ResponseEntity.ok(bindingRespModel);
	}

	@PostMapping(path = "getsimbinding", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> showSimBinding(@RequestBody GetSimBindingReqModel simBindingReqModel) {
		RecordLog.writeLogFile("SimBindingController getsimbinding api is calling. API request: "+simBindingReqModel);
		GetSimBindingRespModel simBindingRespModel = null;
		simBindingRespModel = simBindingService.getSimBinding(simBindingReqModel);
		RecordLog.writeLogFile("SimBindingController getsimbinding api completed. API response: "+simBindingRespModel);
		return ResponseEntity.ok(simBindingRespModel);
	}
	
	//new version2.4 started  getsimbinding
	
	@PostMapping(path = "getsimbindingv2_4", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> showSimBindingV24(@RequestBody GetSimBindingReqModel simBindingReqModel) {
		RecordLog.writeLogFile("SimBindingController getsimbindingv2_4 api is calling. API request: "+simBindingReqModel);
		GetSimBindingRespModel simBindingRespModel = null;
		simBindingRespModel = simBindingService.getSimBindingV24(simBindingReqModel);
		RecordLog.writeLogFile("SimBindingController getsimbindingv2_4 api completed. API response: "+simBindingRespModel);
		return ResponseEntity.ok(simBindingRespModel);
	}
	
	//new version2.4 ended   getsimbinding
	

	@PostMapping(path = "updatesimbinding", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateSimBinding() {
		// select from sim binding --> update simbinding ?
		return ResponseEntity.ok("");
	}

	@PostMapping(path = "validatemobno", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateMobileNumber() {
		return ResponseEntity.ok("");
	}
}
