package com.federal.fedmobilesmecore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.model.APIResponse;
import com.federal.fedmobilesmecore.model.AproveRejectAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiariesAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiaryAccountNoAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiaryMobileNumberAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiaryNickNameAPIRequest;
import com.federal.fedmobilesmecore.model.CreateBenificiaryApprovedEnterpriseReq;
import com.federal.fedmobilesmecore.model.DeleteBeneficiaryRequest;
import com.federal.fedmobilesmecore.model.SpcificPendingBeneficiaryModel;
import com.federal.fedmobilesmecore.service.BeneficiaryService;

@RestController
@RequestMapping("/core/beneficiary")
public class BeneficiaryController {
	
	//validate_nick_name
	
	@Autowired
	BeneficiaryService beneficiaryService;
	
	@PostMapping(path="/validate_nick_name" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Validate Nick Nmae", description = "Validate Nick Nmae")
	public ResponseEntity<APIResponse> validateNickName(@RequestBody BeneficiaryNickNameAPIRequest nickNameAPIRequest ){
		RecordLog.writeLogFile("BeneficiaryController /validate_nick_name api is calling. API request: "+nickNameAPIRequest);
		APIResponse apiResponse=beneficiaryService.validateNickName(nickNameAPIRequest);
		RecordLog.writeLogFile("BeneficiaryController /validate_nick_name api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	
	@PostMapping(path="/validate_acc_no" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Validate Account Number", description = "Validate Account Number")
	public ResponseEntity<APIResponse> validateAccountNumber(@RequestBody BeneficiaryAccountNoAPIRequest validateAPIRequest  ){
		RecordLog.writeLogFile("BeneficiaryController /validate_acc_no api is calling. API request: "+validateAPIRequest);
		APIResponse apiResponse=beneficiaryService.validateActiveAccountNumber(validateAPIRequest);
		RecordLog.writeLogFile("BeneficiaryController /validate_acc_no api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	
	@PostMapping(path="/validate_mobile_number" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Validate Mobile Number", description = "Validate Mobile Number")
	public ResponseEntity<APIResponse> validateMobileNumber(@RequestBody BeneficiaryMobileNumberAPIRequest mobileNumberAPIRequest ){
		RecordLog.writeLogFile("BeneficiaryController /validate_mobile_number api is calling. API request: "+mobileNumberAPIRequest);
		APIResponse apiResponse=beneficiaryService.validateMobileNumber(mobileNumberAPIRequest);
		RecordLog.writeLogFile("BeneficiaryController /validate_mobile_number api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	
	@PostMapping(path="/pending_count" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Pending Count", description = "Pending Count")
	public ResponseEntity<APIResponse> pendingBeneficiaryCount(@RequestBody BeneficiariesAPIRequest pendingBeneficiaryAPIRequest){
		RecordLog.writeLogFile("BeneficiaryController /pending_count api is calling. API request: "+pendingBeneficiaryAPIRequest);
		APIResponse apiResponse = beneficiaryService.pendingBeneficiaryCount(pendingBeneficiaryAPIRequest);
		RecordLog.writeLogFile("BeneficiaryController /pending_count api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	
	@PostMapping(path="/create_beneficiary" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Create Beneficiary", description = "Create Beneficiary")
	public ResponseEntity<APIResponse> createBeneficiary(@RequestBody CreateBenificiaryApprovedEnterpriseReq approvedEnterpriseReq){
		RecordLog.writeLogFile("BeneficiaryController /create_beneficiary api is calling. API request: prefcorp "+approvedEnterpriseReq.getPref_corp() +approvedEnterpriseReq);
		APIResponse apiResponse = beneficiaryService.createBenificiaryForApprovedEnterprise(approvedEnterpriseReq);
		RecordLog.writeLogFile("BeneficiaryController /create_beneficiary api completed. API response:prefcorp "+approvedEnterpriseReq.getPref_corp()+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	@PostMapping(path="/pending_beneficiary_specific" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Pending Beneficiary Specific", description = "Pending Beneficiary Specific")
	public ResponseEntity<APIResponse> getSpecificPendingBeneficiary(@RequestBody SpcificPendingBeneficiaryModel beneficiaryModel){
		RecordLog.writeLogFile("BeneficiaryController /pending_beneficiary_specific api is calling. API request: "+beneficiaryModel);
		APIResponse apiResponse = beneficiaryService.getSpcificPendingBeneficiary(beneficiaryModel);
		RecordLog.writeLogFile("BeneficiaryController /pending_beneficiary_specific api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	// approve with BeneficiaryId
	@PostMapping(path="/ref_no/approve" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Approve Beneficiary", description = "Approve Beneficiary with BeneficiaryId")
	public ResponseEntity<APIResponse> getapproveBeneficiary(@RequestBody AproveRejectAPIRequest beneficiaryRequest){
		RecordLog.writeLogFile("BeneficiaryController /ref_no/approve api is calling. API request: "+beneficiaryRequest);
		APIResponse apiResponse = beneficiaryService.approveBeneficiaryWithBeneficiryId(beneficiaryRequest);
		RecordLog.writeLogFile("BeneficiaryController /ref_no/approve api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	//Gives the list of pending beneficiaries
		@PostMapping(path="/pending_beneficiary" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@io.swagger.v3.oas.annotations.Operation(summary = "Pending Beneficiary", description = "Gives the list of pending beneficiaries")
		public ResponseEntity<APIResponse> listOfPendingBeneficies(@RequestBody BeneficiariesAPIRequest beneficiaryModel){
			RecordLog.writeLogFile("BeneficiaryController /pending_beneficiary api is calling. API request:prefcorp "+beneficiaryModel.getPref_corp()+beneficiaryModel);
			APIResponse apiResponse = beneficiaryService.listOfPendingBeneficies(beneficiaryModel);
			RecordLog.writeLogFile("BeneficiaryController /pending_beneficiary api completed. API response: prefcorp "+beneficiaryModel.getPref_corp()+apiResponse);
			return ResponseEntity.ok(apiResponse);
			
		}
	
	//Approved - Gives a list of approved beneficiaries.
	@PostMapping(path="/approved_beneficiary" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Approved Beneficiary", description = "Gives a list of approved beneficiaries")
	public ResponseEntity<APIResponse> listOfApprovedBeneficies(@RequestBody BeneficiariesAPIRequest beneficiaryModel){
		RecordLog.writeLogFile("BeneficiaryController /approved_beneficiary api is calling. API request:prefcorp "+beneficiaryModel.getPref_corp()+beneficiaryModel);
		APIResponse apiResponse = beneficiaryService.listOfApprovedbeneficiaries(beneficiaryModel);
		RecordLog.writeLogFile("BeneficiaryController /approved_beneficiary api completed. API response: prefcorp "+beneficiaryModel.getPref_corp()+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	//Reject Beneficiary
	@PostMapping(path="/ref_no/reject" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Reject Beneficiary", description = "Reject Beneficiary with BeneficiaryId")
	public ResponseEntity<APIResponse> rejectBeneficiary(@RequestBody AproveRejectAPIRequest reject){
		RecordLog.writeLogFile("BeneficiaryController /ref_no/reject api is calling. API request: "+reject);
		APIResponse apiResponse = beneficiaryService.rejectedBeneficiary(reject);
		RecordLog.writeLogFile("BeneficiaryController /ref_no/reject api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	
	//Rejected Beneficiary List
	@PostMapping(path="/rejected_list" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Rejected Beneficiary", description = "Gives a list of rejected beneficiaries")
	public ResponseEntity<APIResponse> rejectedListBeneficiary(@RequestBody BeneficiariesAPIRequest beneficiaryRequest){
		RecordLog.writeLogFile("BeneficiaryController /rejected_list api is calling. API request: "+beneficiaryRequest);
		APIResponse apiResponse = beneficiaryService.rejectedListBeneficiary(beneficiaryRequest);
		RecordLog.writeLogFile("BeneficiaryController /rejected_list api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}
	
	@PostMapping(path="/delete_beneficiary" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Delete Beneficiary", description = "Delete Beneficiary")
	public ResponseEntity<APIResponse> deleteBeneficiary(@RequestBody DeleteBeneficiaryRequest deleteBeneficiaryRequest){
		RecordLog.writeLogFile("BeneficiaryController /delete_beneficiary api is calling. API request: "+deleteBeneficiaryRequest);
		APIResponse apiResponse = beneficiaryService.deletePendingBeneficiary(deleteBeneficiaryRequest);
		RecordLog.writeLogFile("BeneficiaryController /delete_beneficiary api completed. API response: "+apiResponse);
		return ResponseEntity.ok(apiResponse);
		
	}

}
