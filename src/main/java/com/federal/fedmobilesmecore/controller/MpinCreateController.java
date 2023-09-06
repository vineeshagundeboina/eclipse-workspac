package com.federal.fedmobilesmecore.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.model.CreateCustDetailsReqModel;
import com.federal.fedmobilesmecore.model.MpinCreateReqModel;
import com.federal.fedmobilesmecore.model.ValidateExtMobileNoReqModel;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.service.GetCustDetailsService;
import com.federal.fedmobilesmecore.service.MpinCreateService;
import com.federal.fedmobilesmecore.service.MpinCheckService;

/**
 * @author Syed_Splenta
 *
 */

@RestController
@RequestMapping(path = "/core/mpin")
public class MpinCreateController {

	@Autowired
	GlobalProperties messages;
	@Autowired
	MpinCreateService MpinCreateService;
	@Autowired
	MpinCheckService MpinCheckService;
	
	@Autowired
	EnterprisesRepository enterpriseRepo;


	@io.swagger.v3.oas.annotations.Operation(summary = "Mpin Create API.")
	@PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> checkCustDetls(@RequestBody MpinCreateReqModel mpinCreateReqModel) {
		RecordLog.writeLogFile("MpinCreateController create api is calling. API request: "+mpinCreateReqModel);
		Map<String, Object> mapping = null;
		String apptokenno = mpinCreateReqModel.getMpin().getApp_token();
		String mpinhash = mpinCreateReqModel.getMpin().getMpin_hash();
		mapping = MpinCreateService.MpinCreate(apptokenno,true,mpinhash);
		RecordLog.writeLogFile("MpinCreateController create api completed. API response: "+mapping);
		return ResponseEntity.ok(mapping);
		//return ResponseEntity.status(HttpStatus.OK).body(mapping.toString());
	}
	
	@io.swagger.v3.oas.annotations.Operation(summary = "Mpin Check API.")
	@PostMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> check(@RequestBody MpinCreateReqModel mpinCreateReqModel) throws IOException, JSONException	
	{		
		RecordLog.writeLogFile("MpinCreateController /check api is calling. API request: "+mpinCreateReqModel);
		    Map<String, Object> mapping = null;
			String apptokenno = mpinCreateReqModel.getMpin().getApp_token();
			String prefno=mpinCreateReqModel.getPref_corp();
			String mpinhash=mpinCreateReqModel.getMpin().getMpin_hash();
			mapping = MpinCheckService.getUserDetails(apptokenno,prefno,mpinhash);	
			RecordLog.writeLogFile("MpinCreateController /check api completed. API response: "+mapping);
		    return ResponseEntity.ok(mapping);
	}
	
	
	//new version2.4 started  checkv2_4
	
		@io.swagger.v3.oas.annotations.Operation(summary = "Mpin Check API V2.4.")
		@PostMapping(path = "/checkv2_4", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> checkV24(@RequestBody MpinCreateReqModel mpinCreateReqModel) throws IOException, JSONException	
		{		
			RecordLog.writeLogFile("MpinCreateController /checkv2_4 api is calling. API request:for prefcorp "+mpinCreateReqModel.getPref_corp()+"  "+mpinCreateReqModel);
			    Map<String, Object> mapping = null;
				String apptokenno = mpinCreateReqModel.getMpin().getApp_token();
				String prefno=mpinCreateReqModel.getPref_corp();
				String mpinhash=mpinCreateReqModel.getMpin().getMpin_hash();
				String appVersion=mpinCreateReqModel.getAppVersion();
				String osVersion=mpinCreateReqModel.getOsVersion();
				String osType=mpinCreateReqModel.getOsType();
				mapping = MpinCheckService.getUserDetailsV24(apptokenno,prefno,mpinhash,osVersion,appVersion,osType);	
				RecordLog.writeLogFile("MpinCreateController /checkv2_4 api completed. API response: "+mapping);
			    return ResponseEntity.ok(mapping);
		}
		
		//new version2.4 ended  checkv2_4
	
	@PostMapping(path="/check_android",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserDetailsForAndroid(@RequestBody String request){
			RecordLog.writeLogFile("MpinCreateController /check_android api is calling. API request: "+request);
			Map<String, Object> userDetails = new HashMap<>();
			JSONObject json=new JSONObject(request);
	         Enterprises enterprise=enterpriseRepo.findByPrefCorp(json.getString("prefCorp"));
	         if(enterprise!=null) {
	         userDetails.put("enterpriseId", enterprise.getId());
	         }else {
	         userDetails.put("message", "enterprise user not found");
	         }
	  RecordLog.writeLogFile("MpinCreateController /check_android api is completed. API response: "+userDetails);
	return ResponseEntity.ok(userDetails);
	}


}
