package com.federal.fedmobilesmecore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.APIResponse;
import com.federal.fedmobilesmecore.model.DebitcardVerificationPayload;
import com.federal.fedmobilesmecore.model.SoleActivateResp;
import com.federal.fedmobilesmecore.service.SolActivateService;

@RestController
@RequestMapping("/core/solactivate")
public class SolActivateController {

	@Autowired
	SolActivateService solActivateService;

	@PostMapping("/activate")
	@io.swagger.v3.oas.annotations.Operation(summary = "Sole Activation", description = "Sole Activation")
	public ResponseEntity<APIResponse> processForDebitCardVerfication(@RequestBody DebitcardVerificationPayload dc) {
		RecordLog.writeLogFile("SolActivateController /activate api is calling. API request: "+dc);
		APIResponse activateResponse = solActivateService.debitCardVerfication(dc);
		RecordLog.writeLogFile("SolActivateController /activate api completed. API response: "+activateResponse);
		return ResponseEntity.ok(activateResponse);

	}

}
