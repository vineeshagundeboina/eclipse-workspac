package com.federal.fedmobilesmecore.controller;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.model.ApproveTranRespModel;
import com.federal.fedmobilesmecore.model.ApproveTransactionReqModel;
import com.federal.fedmobilesmecore.model.InitTransactionReqModel;
import com.federal.fedmobilesmecore.model.InitTransactionRespModel;
import com.federal.fedmobilesmecore.model.PerformTranRespModel;
import com.federal.fedmobilesmecore.model.PerformTransactionReqModel;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.service.FundTransferService;
import com.federal.fedmobilesmecore.service.FundTransferService.PERIODTYPE;

@RestController
@RequestMapping(path = "/core/initiatetransfer")
public class FundTransferController {
	private static final Logger logger = LogManager.getLogger(FundTransferController.class);
	@Autowired
	FundTransferService fundTransferService;

	@Autowired
	EnterprisesRepository companyrepo;

	@PostMapping(path = "/inittransaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Fund Transfer", description = "Fund Transfer")
	public ResponseEntity<?> initializeTransaction(@RequestBody InitTransactionReqModel initTransactionReqModel) {
		RecordLog.writeLogFile("FundTransferController /inittransaction api is calling. API request: "+initTransactionReqModel);
		InitTransactionRespModel initTransactionRespModel = null;
		try {
		String remarks=initTransactionReqModel.getRemarks();
		remarks=remarks.replaceAll("[^a-zA-Z0-9]", " ");
		RecordLog.writeLogFile("remarks for mobile "+initTransactionReqModel.getMobileNo() +remarks);
		initTransactionReqModel.setRemarks(remarks);
		initTransactionRespModel = fundTransferService.initiateTransaction(initTransactionReqModel);
		}catch(Exception e) {
			RecordLog.writeLogFile("Exception occured at for mobile "+initTransactionReqModel.getMobileNo()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			initTransactionRespModel = new InitTransactionRespModel();
			initTransactionRespModel.setStatus(false);
			initTransactionRespModel.setDescription("Failed to initiate the transaction");
			initTransactionRespModel.setTransactionId(null);
		}
		RecordLog.writeLogFile("FundTransferController /inittransaction api completed. API response: foor mobile "+initTransactionReqModel.getMobileNo()+"   "+initTransactionRespModel);
		return ResponseEntity.ok(initTransactionRespModel);
	}

	@PostMapping(path = "/performtransaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Fund Transfer", description = "Fund Transfer")
	public ResponseEntity<?> performTransaction(@RequestBody PerformTransactionReqModel performTransactionReqModel) {
		RecordLog.writeLogFile("FundTransferController /performtransaction api is calling. API request: "+performTransactionReqModel);
		PerformTranRespModel performTranRespModel = null;
//		logger.info("Perform Transaction Input Response " + performTransactionReqModel.toString());
try {
		if (StringUtils.isNotEmpty(performTransactionReqModel.getMobile())
				&& StringUtils.isNotEmpty(performTransactionReqModel.getTransactionId())
				&& StringUtils.isNotEmpty(performTransactionReqModel.getAuthToken())
				&& StringUtils.isNotEmpty(performTransactionReqModel.getPrefCorp())) {
			performTranRespModel = fundTransferService.performTransaction(performTransactionReqModel);
			RecordLog.writeLogFile("FundTransferController response from performtransaction method: "+performTransactionReqModel.getPrefCorp());
		} else {
			performTranRespModel = new PerformTranRespModel();
			performTranRespModel.setStatus(false);
			performTranRespModel.setDescription("There are one or more field are missing");
			performTranRespModel.setMessage("Request With incomplete Fiels");
		}
		
}catch(Exception e) {
	RecordLog.writeLogFile("Exception occured at for mobile "+performTransactionReqModel.getPrefCorp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
	e.printStackTrace();
	performTranRespModel = new PerformTranRespModel();
	performTranRespModel.setStatus(false);
	performTranRespModel.setDescription("Exception occured while performing the transaction");
	performTranRespModel.setMessage("Exception occured while performing the transaction");
}
		RecordLog.writeLogFile("FundTransferController /performtransaction api completed. API response:for  "+performTransactionReqModel.getTransactionId()+"  "+performTranRespModel);
		return ResponseEntity.ok(performTranRespModel);
	}

	@PostMapping(path = "/approvetransaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@io.swagger.v3.oas.annotations.Operation(summary = "Fund Transfer", description = "Fund Transfer")
	public ResponseEntity<?> approveTransaction(@RequestBody ApproveTransactionReqModel approveTransactionReqModel) {
		ApproveTranRespModel performTranRespModel = null;
		RecordLog.writeLogFile("FundTransferController /approvetransaction api is calling. API request: "+approveTransactionReqModel);
//		logger.info("Approve Transaction Input Response " + approveTransactionReqModel.toString());

		if (StringUtils.isNotEmpty(approveTransactionReqModel.getRefNo())
				&& StringUtils.isNotEmpty(approveTransactionReqModel.getAuthToken())
				&& StringUtils.isNotEmpty(approveTransactionReqModel.getPrefCorp())) {
			performTranRespModel = fundTransferService.approveTransaction(approveTransactionReqModel);
		} else {
			performTranRespModel = new ApproveTranRespModel();
			performTranRespModel.setStatus(false);
			performTranRespModel.setDescription("There are one or more field are missing");
			performTranRespModel.setMessage("Request With incomplete Fiels");
		}
		RecordLog.writeLogFile("FundTransferController /approvetransaction api completed. API response:for prefcorp "+approveTransactionReqModel.getPrefCorp()+performTranRespModel);
		return ResponseEntity.ok(performTranRespModel);
	}

	@GetMapping(path = "/getamounts")
	@io.swagger.v3.oas.annotations.Operation(summary = "Fund Transfer", description = "Fund Transfer")
	public ResponseEntity<?> getAmounts(@RequestParam Long enterprise_id) {
		RecordLog.writeLogFile("FundTransferController /approvetransaction api is calling. API request: "+enterprise_id);
		Enterprises company = companyrepo.findById(enterprise_id).orElse(null);
		logger.info("company:" + company.getBranch());
		BigDecimal totalamountmontly = fundTransferService.getTotalConsumedAmount(PERIODTYPE.MONTH, company);
		BigDecimal totalamountdaily = fundTransferService.getTotalConsumedAmount(PERIODTYPE.DAY, company);
		RecordLog.writeLogFile("FundTransferController /performtransaction api completed. API response: "+"Success: daily:" + totalamountdaily + ",Monthly:" + totalamountmontly);
		return ResponseEntity.ok("Success: daily:" + totalamountdaily + ",Monthly:" + totalamountmontly);
	}
	
	@PostMapping(path = "/iftcallback")
	public ResponseEntity<?> iftCallBack(@RequestBody String response){
//		RecordLog.writeLogFile("Final Response-->>> "+response);
		//TODO Save to DB
		return ResponseEntity.ok("RECIVED SUCCESSFULLY");
		
	}

}