package com.federal.fedmobilesmecore.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.IfscRespModel;
import com.federal.fedmobilesmecore.model.IfscSearchByCodeReqModel;
import com.federal.fedmobilesmecore.model.IfscSearchRequest;
import com.federal.fedmobilesmecore.service.IfscService;

/**
 * @author Debasish_Splenta
 *
 */
@RestController
@RequestMapping(path = "/core/ifsc")
public class IfscController {
	private static final Logger log4j = LogManager.getLogger(IfscController.class);

	@Autowired
	IfscService ifscService;

	@Autowired
	GlobalProperties properties;

	// FDRL0001014
	@PostMapping(path = "/byifsccode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "API for searching Bank details based on IfscCode", description = "IfscCode value is mandatory & should be of 11 digits")
	public ResponseEntity<?> ifscSearchByIfscCode(@RequestBody IfscSearchByCodeReqModel ifscSearchByCodeReqModel) {
		RecordLog.writeLogFile("IfscController /byifsccode api is calling. API request: "+ifscSearchByCodeReqModel);
		String getIfscCode = ifscSearchByCodeReqModel.getIfscCode();
		IfscRespModel getResult = null;
		if (getIfscCode.length() == 11) {
			getResult = ifscService.searchByIfscCode(getIfscCode);
		} else {
			getResult = new IfscRespModel();
			getResult.setDescription("IFSCCODE length must be of 11 digits");
			getResult.setStatus(false);
			getResult.setResult(null);
		}
		RecordLog.writeLogFile("IfscController /byifsccode api completed. API response: "+getResult);
		return ResponseEntity.ok(getResult);
	}

	@PostMapping(path = "/bybankname", produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "POST API for searching Banks", description = "POST API - no req required to be passed")
	public ResponseEntity<?> ifscSearchByBankName() {
		RecordLog.writeLogFile("IfscController /bybankname api is calling.");
		IfscRespModel getResult = null;
		getResult = ifscService.getAllBankName();
		RecordLog.writeLogFile("IfscController /bybankname api completed. API response: "+getResult);
		return ResponseEntity.ok(getResult);
	}

	@PostMapping(path = "/bybankname_state", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "POST API for searching state details based on bankName")
	public ResponseEntity<?> ifscSearchByBankName_state(@RequestBody IfscSearchRequest searchRequest) {
		RecordLog.writeLogFile("IfscController /bybankname_state api is calling. API request: "+searchRequest);
		IfscRespModel getResult = null;
		if (searchRequest.getBankName() != null && !searchRequest.getBankName().equals("")) {
			getResult = ifscService.getStateByBankName(searchRequest.getBankName());
		} else {
			getResult = new IfscRespModel();
			getResult.setDescription("bankname parameter is empty | null");
			getResult.setStatus(false);
			getResult.setResult(null);
		}
		RecordLog.writeLogFile("IfscController /bybankname_state api completed. API response: "+getResult);
		return ResponseEntity.ok(getResult);
	}

	@PostMapping(path = "/bybanknamestate_district", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "POST API for searching district details based on bankName , state")
	public ResponseEntity<?> ifscSearchByBankName_State_District(@RequestBody IfscSearchRequest searchRequest) {
		RecordLog.writeLogFile("IfscController /bybanknamestate_district api is calling. API request: "+searchRequest);
		IfscRespModel getResult = null;
		if (searchRequest.getBankName() != null && !searchRequest.getBankName().equals("")) {
			getResult = ifscService.getDistrictByBankNameAndState(searchRequest.getBankName(),
					searchRequest.getState());
		} else {
			getResult = new IfscRespModel();
			getResult.setDescription("bankname , state parameter is empty | null");
			getResult.setStatus(false);
			getResult.setResult(null);
		}
		RecordLog.writeLogFile("IfscController /bybanknamestate_district api completed. API response: "+getResult);
		return ResponseEntity.ok(getResult);
	}

	@PostMapping(path = "/bybanknamestatedistrict_city", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "POST API for searching city details based on bank name , state , district")
	public ResponseEntity<?> ifscSearchByBankName_State_District_City(@RequestBody IfscSearchRequest searchRequest) {
		RecordLog.writeLogFile("IfscController /bybanknamestatedistrict_city api is calling. API request: "+searchRequest);
		IfscRespModel getResult = null;
		if (searchRequest.getBankName() != null && !searchRequest.getBankName().equals("")) {
			getResult = ifscService.getCityByBankNameAndStateAndDistrict(searchRequest.getBankName(),
					searchRequest.getState(), searchRequest.getDistrict());
		} else {
			getResult = new IfscRespModel();
			getResult.setDescription("bankname , state , district parameter is empty | null");
			getResult.setStatus(false);
			getResult.setResult(null);
		}
		RecordLog.writeLogFile("IfscController /bybanknamestatedistrict_city api completed. API response: "+getResult);
		return ResponseEntity.ok(getResult);
	}

	@PostMapping(path = "/bybanknamestatedistrictcity_branch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "POST API for searching branch details based on bankName , state , district , city")
	public ResponseEntity<?> ifscSearchByBankName_State_District_City_Branch(
			@RequestBody IfscSearchRequest searchRequest) {
		RecordLog.writeLogFile("IfscController /bybanknamestatedistrictcity_branch api is calling. API request: "+searchRequest);
		IfscRespModel getResult = null;
		if (searchRequest.getBankName() != null && !searchRequest.getBankName().equals("")) {
			getResult = ifscService.getBranchByBankNameAndStateAndDistrictAndCity(searchRequest.getBankName(),
					searchRequest.getState(), searchRequest.getDistrict(), searchRequest.getCity());
		} else {
			getResult = new IfscRespModel();
			getResult.setDescription("bankname , state , district , city parameter is empty | null");
			getResult.setStatus(false);
			getResult.setResult(null);
		}
		RecordLog.writeLogFile("IfscController /bybanknamestatedistrictcity_branch api completed. API response: "+getResult);
		return ResponseEntity.ok(getResult);
	}
	
	@PostMapping(path = "/bybranch_ifsccode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "POST API for searching ifscCode based on branch")
	public ResponseEntity<?> getIfscCodeBy_Branch(@RequestBody IfscSearchRequest searchRequest) {
		RecordLog.writeLogFile("IfscController /bybranch_ifsccode api is calling. API request: "+searchRequest);
		IfscRespModel getResult = null;
		if (searchRequest.getBranch() != null && !searchRequest.getBranch().equals("")) {
			getResult = ifscService.getIfscCodeByBranchName(searchRequest.getBankName(),
					searchRequest.getState(), searchRequest.getDistrict(), searchRequest.getCity(),searchRequest.getBranch());
			System.out.println();
		} else {
			getResult = new IfscRespModel();
			getResult.setDescription("branch parameter is empty | null");
			getResult.setStatus(false);
			getResult.setResult(null);
		}
		RecordLog.writeLogFile("IfscController /bybranch_ifsccode api completed. API response: "+getResult);
		return ResponseEntity.ok(getResult);
	}
}
