package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctc.wstx.util.StringUtil;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.ApproveTranRespModel;
import com.federal.fedmobilesmecore.model.ApproveTransactionReqModel;
import com.federal.fedmobilesmecore.model.BenParams;
import com.federal.fedmobilesmecore.model.GetCustDetails;
import com.federal.fedmobilesmecore.model.InitTransactionReqModel;
import com.federal.fedmobilesmecore.model.InitTransactionRespModel;
import com.federal.fedmobilesmecore.model.PerformTranRespModel;
import com.federal.fedmobilesmecore.model.PerformTransactionReqModel;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.FundTransferRepository;
import com.federal.fedmobilesmecore.repository.ImpsTransferRepository;
import com.federal.fedmobilesmecore.repository.ScheduledPaymentRepo;
import com.federal.fedmobilesmecore.repository.UserRepository;

@Service
public class FundTransferService {
	private static final Logger logger = LogManager.getLogger(FundTransferService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	EnterprisesRepository enterprisesRepository;

	@Autowired
	GetCustDetails getCustDetails;

	@Autowired
	FundTransferRepository fundTransferRepository;

	@Autowired
	ImpsTransferRepository impsTransferRpository;

	@Autowired
	FundTransferInternalService fundTransferInternalService;

	@Autowired
	FundTransferRepository ftrepo;

	@Autowired
	ImpsTransferRepository impsrepo;

	@Autowired
	ScheduledPaymentRepo schpayrepo;
	
	@Autowired
	GlobalProperties globalProperties;

	public InitTransactionRespModel initiateTransaction(InitTransactionReqModel initTransactionReqModel) {
		InitTransactionRespModel transactionRespModel = null;
		User user = null;
		Enterprises enterprises = null;
		BenParams benParams = null;
		Optional<Beneficiaries> beneficiaries = null;

		String getCustDetailsResp = null;
		String errorCode = null;
		String errorMsg = null;
		String transferType = null;
		String mode = null;
		boolean isValidRemAccnt = false;

		JSONObject convertXml = null;
		JSONObject accountDetailsObj = null;
		JSONArray accountDetailsArray = null;

		user = userRepository
				.findFirstByMobileAndMarkAsEnabledOrderByCreatedAtDesc(initTransactionReqModel.getMobileNo(), true);
		if (user == null) {
			transactionRespModel = new InitTransactionRespModel();
			transactionRespModel.setStatus(false);
			transactionRespModel.setDescription("Invalid User");
			transactionRespModel.setTransactionId(null);
		} else {
			enterprises = enterprisesRepository.findByIdAndActive(Long.valueOf(user.getEnterpriseId()), true);
			if (enterprises == null) {
				transactionRespModel = new InitTransactionRespModel();
				transactionRespModel.setStatus(false);
				transactionRespModel.setDescription("Invalid enterprises");
				transactionRespModel.setTransactionId(null);
			} else {
				transactionRespModel=limitCheckOfTrancaction(initTransactionReqModel.getAmount(),initTransactionReqModel.getMode(),initTransactionReqModel.isQuickPay,user.getTransLimit());
				if(transactionRespModel==null) {
				getCustDetailsResp = getCustDetails.getData(enterprises.getAccNo());
				convertXml = XML.toJSONObject(getCustDetailsResp);
				RecordLog.writeLogFile("enterprise id " + user.getEnterpriseId());
//				RecordLog.writeLogFile("account no " + enterprises.getAccNo());
				RecordLog.writeLogFile("getCustDetailsResp to JSON for mobile "+initTransactionReqModel.getMobileNo() + convertXml);

				try {
					if (convertXml.getJSONObject("GetCustomerDetailsResp")
							.get("AccountDetails") instanceof JSONObject) {
						accountDetailsObj = convertXml.getJSONObject("GetCustomerDetailsResp")
								.getJSONObject("AccountDetails");

						errorCode = String.valueOf(convertXml.getJSONObject("GetCustomerDetailsResp")
								.getJSONObject("AccountDetails").get("ERRORCODE"));
						errorMsg = String.valueOf(convertXml.getJSONObject("GetCustomerDetailsResp")
								.getJSONObject("AccountDetails").get("ERRORMSG"));

						if (errorCode.equals("00")) {
							RecordLog.writeLogFile("remaccnt "+initTransactionReqModel.getMobileNo() + " requestmob "+initTransactionReqModel.getRemAccNo()+" getcustmob "+
							String.valueOf(accountDetailsObj.get("FORACID")));
							RecordLog.writeLogFile("remaccnt "+initTransactionReqModel.getMobileNo() + initTransactionReqModel.getRemAccNo().equals(String.valueOf(accountDetailsObj.get("FORACID"))));
							isValidRemAccnt = fundTransferInternalService.validRemitterAccountForAccountDetailsObj(
									accountDetailsObj, initTransactionReqModel.getRemAccNo());
//							RecordLog.writeLogFile("isValidRemAccnt " + isValidRemAccnt);

							transactionRespModel = fundTransferInternalService.modeCheck(initTransactionReqModel, user,
									isValidRemAccnt, enterprises);
							RecordLog.writeLogFile("initiate fund for mobile "+initTransactionReqModel.getMobileNo() + transactionRespModel);

						} else {
							transactionRespModel = new InitTransactionRespModel();
							transactionRespModel.setStatus(false);
							transactionRespModel.setDescription(errorMsg);
							transactionRespModel.setTransactionId(null);
						}

					} else if (convertXml.getJSONObject("GetCustomerDetailsResp")
							.get("AccountDetails") instanceof JSONArray) {
						accountDetailsArray = convertXml.getJSONObject("GetCustomerDetailsResp")
								.getJSONArray("AccountDetails");
						for (int i = 0; i < accountDetailsArray.length(); i++) {
							JSONObject objects = accountDetailsArray.getJSONObject(i);
							if (objects.get("ERRORCODE").equals("00")) {

//								RecordLog.writeLogFile("-----------");
								isValidRemAccnt = fundTransferInternalService.validRemitterAccountForAccountDetailsObj(
										objects, initTransactionReqModel.getRemAccNo());
//								RecordLog.writeLogFile("isValidRemAccnt " + isValidRemAccnt);
                               if(isValidRemAccnt) {
								transactionRespModel = fundTransferInternalService.modeCheck(initTransactionReqModel,
										user, isValidRemAccnt, enterprises);
								break;
                               }
							} else {
								transactionRespModel = new InitTransactionRespModel();
								transactionRespModel.setStatus(false);
								transactionRespModel.setDescription(objects.getString("ERRORMSG"));
								transactionRespModel.setTransactionId(null);
							}
						}
					}
				} catch (Exception e) {
					RecordLog.writeLogFile("Exception occured at: for mobile "+user.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
					e.printStackTrace();
					transactionRespModel = new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription("Unable to process the request , Try again later");
					transactionRespModel.setTransactionId(null);
				}
			}
			}
		}
		return transactionRespModel;
	}

	public PerformTranRespModel performTransaction(PerformTransactionReqModel performTransactionReqModel) {
		PerformTranRespModel performTranRespModel = null;
		User user = null;

		user = userRepository
				.findFirstByMobileAndMarkAsEnabledOrderByCreatedAtDesc(performTransactionReqModel.getMobile(), true);
		if (user == null) {
			performTranRespModel = new PerformTranRespModel();
			performTranRespModel.setStatus(false);
			performTranRespModel
					.setDescription("User with mobile" + performTransactionReqModel.getMobile() + " was not available");
			performTranRespModel.setMessage("Invalid User");
		} else {
			performTranRespModel = fundTransferInternalService.validateTransactionForUser(performTransactionReqModel);
		}
		return performTranRespModel;
	}

	public ApproveTranRespModel approveTransaction(ApproveTransactionReqModel approveTransactionReqModel) {
		ApproveTranRespModel approveTranRespModel = null;

		Optional<FundTransfer> fundTransfer = fundTransferRepository.findByRefNo(approveTransactionReqModel.getRefNo());
		Optional<ImpsTransfer> impsTransfer = impsTransferRpository.findByRefNo(approveTransactionReqModel.getRefNo());

		if (fundTransfer.isPresent()) {
			approveTranRespModel = fundTransferInternalService.approveIntrabankProcess(fundTransfer,
					approveTransactionReqModel);
		} else if (impsTransfer.isPresent()) {
			approveTranRespModel = fundTransferInternalService.approveImpsProcess(impsTransfer,
					approveTransactionReqModel);
		}

		return approveTranRespModel;
	}

	/**
	 * Returns total transfers happened in a certain period.
	 * 
	 * @param type
	 * @param enterprise
	 * @return
	 */
	public BigDecimal getTotalConsumedAmount(PERIODTYPE type, Enterprises enterprise) {
		if (enterprise != null) {
			final ZonedDateTime input = ZonedDateTime.now();
			String fed2fedcodes=globalProperties.getFed2fedstatuscode();
		    String impscodes=globalProperties.getImpsstatuscode();
			Timestamp fromdate = null;
			Timestamp todate = null;
			if (type == PERIODTYPE.DAY) {
				Date today = Date.from(input.withHour(0).withMinute(0).withSecond(0).toInstant());
				Date endday = Date.from(input.withHour(23).withMinute(59).withSecond(59).toInstant());
				logger.info("from:" + today + ",todate:" + endday);
				fromdate = new Timestamp(today.getTime());
				todate = new Timestamp(endday.getTime());
			} else if (type == PERIODTYPE.MONTH) {
				Date currentmonthstart = Date
						.from(input.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).toInstant());
				Date currentmonthend = Date.from(input.withHour(23).withMinute(59).withSecond(59).toInstant());
				logger.info("from:" + currentmonthstart + ",todate:" + currentmonthend);
				fromdate = new Timestamp(currentmonthstart.getTime());
				todate = new Timestamp(currentmonthend.getTime());
			}
			List<String> codes=Arrays.asList(fed2fedcodes.split(","));
			BigDecimal ftamount = ftrepo.getApprovedFTAmountBetweenDates(fromdate, todate, "" + enterprise.getId(),codes);
			List<String> impsCode=Arrays.asList(impscodes.split(","));
			BigDecimal impsamount = impsrepo.getApprovedFTAmountBetweenDates(fromdate, todate, "" + enterprise.getId(),impsCode);
			List<String> schCodes=Arrays.asList(globalProperties.getSchedulecode().split(","));
			BigDecimal schamount = schpayrepo.getApprovedSCPayAmountBetweenDates(fromdate, todate,
					"" + enterprise.getId(),schCodes);
			logger.info("ftamount:" + ftamount + ",impsamount:" + impsamount + ",schamount:" + schamount);
			BigDecimal totalsum = ftamount.add(impsamount).add(schamount);
			return totalsum;
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getTotalAmountQuickpay(PERIODTYPE type, Enterprises enterprise) {
		if (enterprise != null) {
			final ZonedDateTime input = ZonedDateTime.now();
			String fed2fedcodes=globalProperties.getFed2fedstatuscode();
		    String impscodes=globalProperties.getImpsstatuscode();
			Timestamp fromdate = null;
			Timestamp todate = null;
			if (type == PERIODTYPE.DAY) {
				Date today = Date.from(input.withHour(0).withMinute(0).withSecond(0).toInstant());
				Date endday = Date.from(input.withHour(23).withMinute(59).withSecond(59).toInstant());
				logger.info("from:" + today + ",todate:" + endday);
				fromdate = new Timestamp(today.getTime());
				todate = new Timestamp(endday.getTime());
			} else if (type == PERIODTYPE.MONTH) {
				Date currentmonthstart = Date
						.from(input.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).toInstant());
				Date currentmonthend = Date.from(input.withHour(23).withMinute(59).withSecond(59).toInstant());
				logger.info("from:" + currentmonthstart + ",todate:" + currentmonthend);
				fromdate = new Timestamp(currentmonthstart.getTime());
				todate = new Timestamp(currentmonthend.getTime());
			}List<String> codes=Arrays.asList(fed2fedcodes.split(","));
			BigDecimal ftamount = ftrepo.getApprovedFTAmountBetweenDates(fromdate, todate, "" + enterprise.getId(),codes);
			List<String> impsCode=Arrays.asList(impscodes.split(","));
			BigDecimal impsamount = impsrepo.getApprovedFTAmountBetweenDates(fromdate, todate, "" + enterprise.getId(),impsCode);
			logger.info("ftamount:" + ftamount + ",impsamount:" + impsamount);
			BigDecimal totalsum = ftamount.add(impsamount);
			return totalsum;
		}
		return BigDecimal.ZERO;
	}

	public enum PERIODTYPE {
		DAY, MONTH
	}
	
	public InitTransactionRespModel limitCheckOfTrancaction(BigDecimal amount,String mode,boolean isQuickPay,String transactionLimit){
		InitTransactionRespModel transactionRespModel=null;
		BigDecimal perTransactionLimit=new BigDecimal(globalProperties.getFedcorp_per_transaction_limit());
		BigDecimal userTranSactionLimit=null;
		int userResult=0;
//		RecordLog.writeLogFile(transactionLimit);
		if(transactionLimit!=null && StringUtils.isNoneEmpty(transactionLimit) && (!transactionLimit.equals("0"))) {
			userTranSactionLimit=new BigDecimal(transactionLimit);
			 userResult= userTranSactionLimit.compareTo(amount);
		}
		if(userResult==-1) {
			RecordLog.writeLogFile("user transaction limit-->"+userTranSactionLimit);
			transactionRespModel=new InitTransactionRespModel();
			transactionRespModel.setStatus(false);
			transactionRespModel.setDescription("User per transaction limit exceeded of Rs."+userTranSactionLimit);
			transactionRespModel.setTransactionId(null);
		}else if(mode.equals("p2a")) {
			 if(!isQuickPay){
					int impsLimit=globalProperties.getImps_per_transaction_limit().compareTo(amount);
					if(impsLimit==-1) {
						RecordLog.writeLogFile(globalProperties.getFundTransferLimitMSG()+ globalProperties.getImps_per_transaction_limit());
						transactionRespModel=new InitTransactionRespModel();
						transactionRespModel.setStatus(false);
						transactionRespModel.setDescription(globalProperties.getImpsLimitMessage()+"Rs."+globalProperties.getImps_per_transaction_limit());
						transactionRespModel.setTransactionId(null);
					}
				}else {
					int quickPayLimit=globalProperties.getQuickPay_per_transactionlimit().compareTo(amount);
					if(quickPayLimit==-1) {
						RecordLog.writeLogFile(" Quick Pay Imps Transaction limit---->"+ globalProperties.getQuickPay_per_transactionlimit());
						transactionRespModel=new InitTransactionRespModel();
						transactionRespModel.setStatus(false);
						transactionRespModel.setDescription(globalProperties.getQuickPayTransferLimitMSG()+"Rs."+globalProperties.getQuickPay_per_transactionlimit());
						transactionRespModel.setTransactionId(null);
					}
				else {
					int impsLimit=globalProperties.getImps_per_transaction_limit().compareTo(amount);
					if(impsLimit==-1) {
					RecordLog.writeLogFile("Quick Pay Imps Transaction limit---->"+ globalProperties.getImps_per_transaction_limit());
					transactionRespModel=new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription(globalProperties.getImpsLimitMessage()+"Rs."+globalProperties.getImps_per_transaction_limit());
					transactionRespModel.setTransactionId(null);
				}
				}
				}
		}else if(mode.equals("fed2fed")) {
			if(!isQuickPay) {
				int limitExceeded=perTransactionLimit.compareTo(amount);
				if(limitExceeded==-1) {
					RecordLog.writeLogFile("FundTransfer PerTransaction limit---->"+ perTransactionLimit);
					transactionRespModel=new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription(globalProperties.getFundTransferLimitMSG()+"Rs."+perTransactionLimit);
					transactionRespModel.setTransactionId(null);
				}
			}else {
				int limitExceeded=globalProperties.getQuickPay_per_transactionlimit().compareTo(amount);
				if(limitExceeded==-1) {
					RecordLog.writeLogFile("quickpay PerTransaction limit---->"+ globalProperties.getQuickPay_per_transactionlimit());
					transactionRespModel=new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription(globalProperties.getQuickPayTransferLimitMSG()+"Rs."+globalProperties.getQuickPay_per_transactionlimit());
					transactionRespModel.setTransactionId(null);
				}	else {
					int impsLimit=perTransactionLimit.compareTo(amount);
					if(impsLimit==-1) {
						RecordLog.writeLogFile("Federal bank Quick pay Per Transaction Limit Exceeded---->"+ perTransactionLimit);
						transactionRespModel=new InitTransactionRespModel();
						transactionRespModel.setStatus(false);
						transactionRespModel.setDescription(globalProperties.getQuickPayTransferLimitMSG()+"Rs."+perTransactionLimit);
						transactionRespModel.setTransactionId(null);
					}
				}
			}
		}else if(mode.equals("neft")) {
			if(!isQuickPay) {
				int limitExceeded=perTransactionLimit.compareTo(amount);
				if(limitExceeded==-1) {
					RecordLog.writeLogFile("neft PerTransaction limit---->"+ perTransactionLimit);
					transactionRespModel=new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription(globalProperties.getFundTransferLimitMSG()+"Rs."+perTransactionLimit);
					transactionRespModel.setTransactionId(null);
				}
			}else {
				int limit=globalProperties.getQuickPay_per_transactionlimit().compareTo(amount);
				if(limit==-1) {
					RecordLog.writeLogFile("Fund Transfer Transaction limit---->"+ perTransactionLimit);
					transactionRespModel=new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription(globalProperties.getQuickPayTransferLimitMSG()+"Rs."+globalProperties.getQuickPay_per_transactionlimit());
					transactionRespModel.setTransactionId(null);
				} else {
					int limitExceeded=perTransactionLimit.compareTo(amount);
					if(limitExceeded==-1) {
//						RecordLog.writeLogFile(" PerTransaction limit--->"+ perTransactionLimit);
						transactionRespModel=new InitTransactionRespModel();
						transactionRespModel.setStatus(false);
						transactionRespModel.setDescription(globalProperties.getQuickPayTransferLimitMSG()+"Rs."+perTransactionLimit);
						transactionRespModel.setTransactionId(null);
					}	
				}
			}
		}else if(mode.equals("p2p")) {
			if(!isQuickPay) {
				int limitExceeded=globalProperties.getMmid_per_transaction_limit().compareTo(amount);
				if(limitExceeded==-1) {
					RecordLog.writeLogFile("PerTransaction limit--->"+ globalProperties.getMmid_per_transaction_limit());
					transactionRespModel=new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription(globalProperties.getFundTransferLimitMSG()+"Rs."+globalProperties.getMmid_per_transaction_limit());
					transactionRespModel.setTransactionId(null);
				}
			}else {
				int limit=globalProperties.getQuickPay_per_transactionlimit().compareTo(amount);
				if(limit==-1) {
					RecordLog.writeLogFile("Fund Transfer Transaction limit---->"+ perTransactionLimit);
					transactionRespModel=new InitTransactionRespModel();
					transactionRespModel.setStatus(false);
					transactionRespModel.setDescription(globalProperties.getQuickPayTransferLimitMSG()+"Rs."+globalProperties.getQuickPay_per_transactionlimit());
					transactionRespModel.setTransactionId(null);
				}	else {
					
					int limitExceeded=globalProperties.getMmid_per_transaction_limit().compareTo(amount);
					if(limitExceeded==-1) {
						RecordLog.writeLogFile("PerTransaction limit--->"+ globalProperties.getMmid_per_transaction_limit());
						transactionRespModel=new InitTransactionRespModel();
						transactionRespModel.setStatus(false);
						transactionRespModel.setDescription(globalProperties.getQuickPayTransferLimitMSG()+"Rs."+globalProperties.getMmid_per_transaction_limit());
						transactionRespModel.setTransactionId(null);
					}	
				}
			}
		}
		return transactionRespModel;
	}

}