package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.FundTransferLog;
import com.federal.fedmobilesmecore.dto.FundTransferStore;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.TransactionLimit;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.ApproveTranRespModel;
import com.federal.fedmobilesmecore.model.ApproveTransactionReqModel;
import com.federal.fedmobilesmecore.model.BenDetails;
import com.federal.fedmobilesmecore.model.BenParams;
import com.federal.fedmobilesmecore.model.FundTransferInternalModel;
import com.federal.fedmobilesmecore.model.FundTransferReqModel;
import com.federal.fedmobilesmecore.model.InitTransactionReqModel;
import com.federal.fedmobilesmecore.model.InitTransactionRespModel;
import com.federal.fedmobilesmecore.model.NeftTransferReqModel;
import com.federal.fedmobilesmecore.model.P2ATransferReqModel;
import com.federal.fedmobilesmecore.model.P2PTransferReqModel;
import com.federal.fedmobilesmecore.model.PerformTranRespModel;
import com.federal.fedmobilesmecore.model.PerformTransactionReqModel;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.BeneficiariesRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.FundTransferRepository;
import com.federal.fedmobilesmecore.repository.FundTransferStoreRepository;
import com.federal.fedmobilesmecore.repository.ImpsTransferRepository;
import com.federal.fedmobilesmecore.repository.TransactionLimitRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;
import com.federal.fedmobilesmecore.security.SHA512hash;

@Component
public class FundTransferInternalService {
	private static final Logger logger = LogManager.getLogger(FundTransferInternalService.class);

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	BeneficiariesRepository beneficiariesRepository;
	@Autowired
	FundTransferStoreRepository fundTransferStoreRepository;
	@Autowired
	EnterprisesRepository enterprisesRepository;
	@Autowired
	TransactionLimitRepository transactionLimitRepository;
	@Autowired
	FundTransferRepository fundTransferRepository;
	@Autowired
	ImpsTransferRepository impsTransferRepository;
	@Autowired
	UserRepository_V1 userRepository_V1;
	@Autowired
	CommonExternalService commonExternalService;
	@Autowired
	FundTransaferTransactionInternalService fundTransaferTransactionInternalService;
	@Autowired
	MakerCheckerServiceFundTransfer checkerServiceFundTransfer;
	@Autowired
	MakerCheckerServiceImpsTransfer makercheckerServiceImpsTransfer;
	@Autowired
	FundTransferLogsEntryService fundTransferLogsEntryService;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	GlobalProperties properties;
	@Autowired
	ApplicationEnterprisRepository applicationEnterprisRepository;
	
	@Autowired
	private SimBindingService simBindingService;

	@Value("${daily_transaction}")
	private String transactionDaily;
	@Value("${monthly_transaction}")
	private String transactionMonthly;
	@Value("${fedcorp_per_transaction_limit}")
	private String perTransaction;
	// @Value("${quickpaylimit}")
	// private String quickpayLimit;
	@Value("${fblgateway.url.http}")
	private String fblgatewayurl;
//	@Value("${sendercd}")
	private String senderCD = System.getenv("callbackSenderCD");
//	@Value("${userid}")
	private String userId = System.getenv("callbackUserId");
//	@Value("${password}")
	private String password = System.getenv("callbackPassword");
//	@Value("${callback.url}")
	private String callbackURL = System.getenv("callbackURL");

	public boolean validRemitterAccountForAccountDetailsObj(JSONObject accountDetailsObj, String remmiterAccNo) {
		boolean isValid = false;
		String foracid = null;

		foracid = String.valueOf(accountDetailsObj.get("FORACID"));

//		RecordLog.writeLogFile("foracid " + foracid + "remmiterAccNo " + remmiterAccNo);

		if (foracid.equals(remmiterAccNo)) {
			isValid = true;
		} else {
			isValid = false;
		}
		return isValid;
	}

	public boolean validRemitterAccountForAccountDetailsArray() {
		boolean isValid = false;

		return isValid;
	}

	public InitTransactionRespModel modeCheck(InitTransactionReqModel initTransactionReqModel, User user,
			boolean isValidRemAccnt, Enterprises enterprises) {
		InitTransactionRespModel transactionRespModel = null;
		BenParams benParams = null;
		Optional<Beneficiaries> beneficiaries = null;
		FundTransferStore fundTransfer = null;
		FundTransferStore fundTransferStoreResp = null;
		BenDetails benDetails = null;
		String errorMsg = null;
		String mode = null;
		String sign = null;
		String transferType = null;
		String remmiterMobileNumber = null;
		if (isValidRemAccnt) {
			mode = initTransactionReqModel.getMode();
			if (mode.equals("fed2fed")) {

				benDetails = new BenDetails();
				benDetails.setBenAccNo(initTransactionReqModel.getBenAccNo());
				sign = makeSignature(initTransactionReqModel, user, benDetails);
				benParams = new BenParams();
				if (StringUtils.isNotBlank(initTransactionReqModel.getBenAccNo())) {
					beneficiaries = beneficiariesRepository.findByAccNoAndEnterpriseIdAndModeAndIsActive(
							initTransactionReqModel.getBenAccNo(), user.getEnterpriseId(), "self", true);
					if (beneficiaries.isPresent()) {

						benParams.setBenName(beneficiaries.get().getName());
						benParams.setBenNickName(beneficiaries.get().getNickName());
					} else {

						benParams.setBenName("NIL");
						benParams.setBenNickName("NIL");
					}
				} else {

					benParams.setBenName("NIL");
					benParams.setBenNickName("NIL");
				}

				logger.info("Mode in mode check" + mode);
				logger.info("benname" + benParams.getBenName());
				logger.info("bennickname" + benParams.getBenNickName());

				fundTransfer = new FundTransferStore();
				fundTransfer.setTransactionId(generateTransactionId());
				fundTransfer.setAmount(String.valueOf(initTransactionReqModel.getAmount()));
				fundTransfer.setMode(mode);
				fundTransfer.setRemAccNo(initTransactionReqModel.getRemAccNo());
				fundTransfer.setRemMobNum(initTransactionReqModel.getRemMobNo());
				fundTransfer.setRemName(enterprises.getAccName());
				fundTransfer.setBenAccNo(initTransactionReqModel.getBenAccNo());
				fundTransfer.setBenCustName(benParams.getBenName());
				fundTransfer.setBenName(benParams.getBenName());
				fundTransfer.setBenNickName(benParams.getBenNickName());
				fundTransfer.setBenAccType(initTransactionReqModel.getBenAccType());
				fundTransfer.setRemarks(initTransactionReqModel.getRemarks());
				fundTransfer.setBenIfsc(initTransactionReqModel.getBenIfsc());
				fundTransfer.setEnterpriseId(String.valueOf(enterprises.getId()));
				fundTransfer.setCreatedAt(new Timestamp(new Date().getTime()));
				fundTransfer.setUpdatedAt(new Timestamp(new Date().getTime()));
				fundTransfer.setTransactionDate(new Timestamp(new Date().getTime()));
				if (initTransactionReqModel.isQuickPay() == true) {
					transferType = "QuickPay";
				} else {
					transferType = "FundTransfer";
				}
				fundTransfer.setTransferType(transferType);
				fundTransfer.setSignature(sign);
				RecordLog.writeLogFile("before save request of fundtransfer store for mobile "+initTransactionReqModel.getMobileNo() + fundTransfer);

				fundTransferStoreResp = fundTransferStoreRepository.save(fundTransfer);
				RecordLog.writeLogFile("saved response of fundtransfer store for mobile "+initTransactionReqModel.getMobileNo() + fundTransferStoreResp);

			} else if (mode.equals("neft") || mode.equals("p2a")) {

				benDetails = new BenDetails();
				benParams = new BenParams();
				// check beneficairy table for benname by ifsccode

				if (StringUtils.isNotBlank(initTransactionReqModel.getBenIfsc())) {
					beneficiaries = beneficiariesRepository.findByAccNoAndEnterpriseIdAndModeAndIsActive(
							initTransactionReqModel.getBenAccNo(), user.getEnterpriseId(), "other", true);
					if (beneficiaries.isPresent()) {
						benDetails.setBenAccNo(beneficiaries.get().getAccNo());
						benParams.setBenName(beneficiaries.get().getName());
						benParams.setBenNickName(beneficiaries.get().getNickName());
					} else {
						benDetails.setBenAccNo(initTransactionReqModel.getBenAccNo());
						benParams.setBenName("NIL");
						benParams.setBenNickName("NIL");
					}
				} else {
					benDetails.setBenAccNo(initTransactionReqModel.getBenAccNo());
					benParams.setBenName("NIL");
					benParams.setBenNickName("NIL");
				}

				sign = makeSignature(initTransactionReqModel, user, benDetails);

				logger.info("Mode in mode check" + mode);
				logger.info("benname" + benParams.getBenName());
				logger.info("bennickname" + benParams.getBenNickName());

				fundTransfer = new FundTransferStore();
				fundTransfer.setTransactionId(generateTransactionId());
				fundTransfer.setAmount(String.valueOf(initTransactionReqModel.getAmount()));
				fundTransfer.setMode(mode);
				fundTransfer.setRemAccNo(initTransactionReqModel.getRemAccNo());
				fundTransfer.setRemMobNum(initTransactionReqModel.getRemMobNo());
				fundTransfer.setRemName(enterprises.getAccName());
				fundTransfer.setBenAccNo(initTransactionReqModel.getBenAccNo());
				fundTransfer.setBenCustName(benParams.getBenName());
				fundTransfer.setBenName(benParams.getBenName());
				fundTransfer.setBenNickName(benParams.getBenNickName());
				fundTransfer.setBenAccType(initTransactionReqModel.getBenAccType());
				fundTransfer.setRemarks(initTransactionReqModel.getRemarks());
				fundTransfer.setBenIfsc(initTransactionReqModel.getBenIfsc());
				fundTransfer.setEnterpriseId(String.valueOf(enterprises.getId()));
				fundTransfer.setCreatedAt(new Timestamp(new Date().getTime()));
				fundTransfer.setUpdatedAt(new Timestamp(new Date().getTime()));
				fundTransfer.setTransactionDate(new Timestamp(new Date().getTime()));
				if (initTransactionReqModel.isQuickPay() == true) {
					transferType = "QuickPay";
				} else {
					transferType = "FundTransfer";
				}
				fundTransfer.setTransferType(transferType);
				fundTransfer.setSignature(sign);
				RecordLog.writeLogFile("before save request of fundtransfer store for mobile "+initTransactionReqModel.getMobileNo() + fundTransfer);

				fundTransferStoreResp = fundTransferStoreRepository.save(fundTransfer);
				RecordLog.writeLogFile("saved response of fundtransfer store for mobile "+initTransactionReqModel.getMobileNo() + fundTransferStoreResp);

			} else if (mode.equals("p2p")) {
				benDetails = new BenDetails();
				benDetails.setBenAccNo(initTransactionReqModel.getBenAccNo());
				sign = makeSignature(initTransactionReqModel, user, benDetails);

				// check beneficairy table for benname by ifsccode
				benParams = new BenParams();
				if (StringUtils.isNotBlank(initTransactionReqModel.getBenMmid())) {
					beneficiaries = beneficiariesRepository.findByMobileAndMmidAndEnterpriseIdAndMode(
							initTransactionReqModel.getBenMobNo(), initTransactionReqModel.getBenMmid(),
							user.getEnterpriseId(), "mmid");
					if (beneficiaries.isPresent()) {

						benParams.setBenName(beneficiaries.get().getName());
						benParams.setBenNickName(beneficiaries.get().getNickName());
					} else {

						benParams.setBenName("NIL");
						benParams.setBenNickName("NIL");
					}
				} else {

					benParams.setBenName("NIL");
					benParams.setBenNickName("NIL");
				}
				logger.info("Mode in mode check" + mode);
				logger.info("benname" + benParams.getBenName());
				logger.info("bennickname" + benParams.getBenNickName());
				// remittermobileno get from user or from request
				if (initTransactionReqModel.getRemMobNo().isEmpty()) {
					remmiterMobileNumber = user.getMobile();
				} else {
					remmiterMobileNumber = initTransactionReqModel.getRemMobNo();
				}
				fundTransfer = new FundTransferStore();
				fundTransfer.setAmount(String.valueOf(initTransactionReqModel.getAmount()));
				fundTransfer.setMode(mode);
				fundTransfer.setRemAccNo(initTransactionReqModel.getRemAccNo());
				fundTransfer.setRemName(enterprises.getAccName());
				fundTransfer.setRemMobNum(remmiterMobileNumber);
				fundTransfer.setRemMmid(initTransactionReqModel.getRemMmid());
				fundTransfer.setRemCustId(initTransactionReqModel.getRemCustId());
				fundTransfer.setBenAadhar(initTransactionReqModel.getBenAadhar());
				fundTransfer.setBenIfsc(initTransactionReqModel.getBenIfsc());
				fundTransfer.setBenMmid(initTransactionReqModel.getBenMmid());
				fundTransfer.setBenAccNo(initTransactionReqModel.getBenAccNo());
				fundTransfer.setBenMobNum(initTransactionReqModel.getBenMobNo());
				fundTransfer.setBenNickName(benParams.getBenNickName());
				fundTransfer.setBenCustName(benParams.getBenName());
				fundTransfer.setBenName(benParams.getBenName());
				fundTransfer.setRemarks(initTransactionReqModel.getRemarks());
				fundTransfer.setEnterpriseId(String.valueOf(enterprises.getId()));
				fundTransfer.setCreatedAt(new Timestamp(new Date().getTime()));
				fundTransfer.setUpdatedAt(new Timestamp(new Date().getTime()));
				fundTransfer.setTransactionDate(new Timestamp(new Date().getTime()));
				if (initTransactionReqModel.isQuickPay() == true) {
					transferType = "QuickPay";
				} else {
					transferType = "FundTransfer";
				}
				fundTransfer.setTransferType(transferType);
				fundTransfer.setSignature(sign);
				fundTransfer.setTransactionId(generateTransactionId());
				RecordLog.writeLogFile("before save request of fundtransfer store for mobile "+initTransactionReqModel.getMobileNo() + fundTransfer);

				fundTransferStoreResp = fundTransferStoreRepository.save(fundTransfer);
				RecordLog.writeLogFile("saved response of fundtransfer store for mobile "+initTransactionReqModel.getMobileNo() + fundTransferStoreResp);

			}
		}
			else {
			transactionRespModel = new InitTransactionRespModel();
			transactionRespModel.setStatus(false);
			transactionRespModel.setDescription("remitter account did not match with FORACID");
			transactionRespModel.setTransactionId(null);
			return transactionRespModel;
		}
		RecordLog.writeLogFile("saved fundtransfer store final response for mobile "+initTransactionReqModel.getMobileNo() + fundTransferStoreResp);

if (fundTransferStoreResp == null) {
			transactionRespModel = new InitTransactionRespModel();
			transactionRespModel.setStatus(false);
			transactionRespModel.setDescription("There was an error initiating the transaction. Please try again");
			transactionRespModel.setTransactionId(null);
		} else {
			transactionRespModel = new InitTransactionRespModel();
			transactionRespModel.setStatus(true);
			transactionRespModel.setDescription("fundtransfer successfully initiated");
			transactionRespModel.setTransactionId(fundTransferStoreResp.getTransactionId());
		}
		return transactionRespModel;
	}

	public PerformTranRespModel validateTransactionForUser(PerformTransactionReqModel performTransactionReqModel) {
		PerformTranRespModel performTranRespModel = null;
		Optional<FundTransferStore> fundTransferStoreResp = null;
		FundTransferInternalModel fundtransferinternalmodel = null;
		String mode = null;

		fundTransferStoreResp = fundTransferStoreRepository
				.findByTransactionId(performTransactionReqModel.getTransactionId());
		logger.info("fundTransferStoreResp " + fundTransferStoreResp.toString());

		if (fundTransferStoreResp.isPresent()) {
			mode = fundTransferStoreResp.get().getMode();
			if (mode.equals("neft") || mode.equals("fed2fed")) {
				fundtransferinternalmodel = fed2fedCreate(fundTransferStoreResp, performTransactionReqModel);
				performTranRespModel = new PerformTranRespModel();
				performTranRespModel.setStatus(fundtransferinternalmodel.isStatus());
				performTranRespModel.setMessage(fundtransferinternalmodel.getMessage());
				performTranRespModel.setDescription(fundtransferinternalmodel.getDescription());
				performTranRespModel.setFundTransfer(fundtransferinternalmodel.getFundTransfer());
			} else if (mode.equals("p2p") || mode.equals("p2a")) {
				fundtransferinternalmodel = impsCreate(fundTransferStoreResp, performTransactionReqModel);
				performTranRespModel = new PerformTranRespModel();
				performTranRespModel.setStatus(fundtransferinternalmodel.isStatus());
				performTranRespModel.setMessage(fundtransferinternalmodel.getMessage());
				performTranRespModel.setDescription(fundtransferinternalmodel.getDescription());
				performTranRespModel.setImpsTransfer(fundtransferinternalmodel.getImpsTransfer());
			} else {
				performTranRespModel = new PerformTranRespModel();
				performTranRespModel.setStatus(false);
				performTranRespModel.setMessage("failed");
				performTranRespModel.setDescription("check the value of mode");
			}
			/* TODO generate response model after fed2fed or imps */

		} else {
			performTranRespModel = new PerformTranRespModel();
			performTranRespModel.setStatus(false);
			performTranRespModel.setMessage("failed");
			performTranRespModel.setDescription("There was an error processing the transaction. Please try again");
		}
		return performTranRespModel;
	}

	public Optional<User> getCurrentUserDetail(String authToken) {
		Optional<User> getCurrentUserDetail = null;
		getCurrentUserDetail = userRepository_V1.findByAuthToken(authToken);
		if (getCurrentUserDetail.isPresent()) {
			RecordLog.writeLogFile("getCurrentUserDetail " + getCurrentUserDetail.get().toString());
		}
		return getCurrentUserDetail;
	}

	private FundTransferInternalModel fed2fedCreate(Optional<FundTransferStore> fundTransferStoreResp,
			PerformTransactionReqModel performTransactionReqModel) {
		try {
			String enterpriseId = null;
			String remarks = null;
			String benName = null;
			String message = null;
			String mode = null;
			Optional<Enterprises> enterprises = null;
			Optional<User> getCurrentUserDetails = null;
			boolean validDailyTransaction = false;
			boolean validMonthlyTransaction = false;
			// Long perTransLimit = 0L;
			Long transLimit = 0L;
			boolean isValidQuickPay = false;
			boolean isDuplicate = false;
			FundTransferInternalModel fundTransferInternalModel = null;
			FundTransferLog fundTransferLog = null;

			enterpriseId = fundTransferStoreResp.get().getEnterpriseId();
			enterprises = enterprisesRepository.findById(Long.valueOf(enterpriseId));

			if (enterprises.isPresent()) {
				if (precisionCheck(fundTransferStoreResp.get().getAmount())) {
//					validDailyTransaction = fundTransaferTransactionInternalService
//							.validDailyTransaction(fundTransferStoreResp.get().getAmount(), enterpriseId);
//					validMonthlyTransaction = fundTransaferTransactionInternalService
//							.validMonthlyTransaction(fundTransferStoreResp.get().getAmount(), enterpriseId);

					// validDailyTransaction = true;
					// validMonthlyTransaction = true;
//				if (validDailyTransaction && validMonthlyTransaction) {
						getCurrentUserDetails = getCurrentUserDetail(performTransactionReqModel.getAuthToken());
						if (getCurrentUserDetails.isPresent()) {
							Double pertransaction = Double
									.parseDouble(getCurrentUserDetails.get().getTransLimit() != null
											? getCurrentUserDetails.get().getTransLimit()
											: "0");
							if (pertransaction == 0) {
								pertransaction = Double.parseDouble(properties.getFedcorp_per_transaction_limit());
							}
							transLimit = pertransaction.longValue();
							logger.info("TransLimit" + transLimit);
//						perTransLimit = pertransaction.longValue();
//						 = perTransLimit.equals(0L) ? Long.valueOf(perTransaction) : perTransLimit;
							if (commonExternalService.enterprise_sole_proprietorship(getCurrentUserDetails)
									|| commonExternalService
											.enterprise_zero_external_user_authorize(getCurrentUserDetails)) {
								
								
								
								
								LocalDateTime local=LocalDateTime.now(ZoneId.systemDefault());
								Timestamp current=Timestamp.valueOf(local);
								Timestamp past=Timestamp.valueOf(local.minusMinutes(10));
								
								boolean exist=fundTransferRepository.existsByAmountAndBenAccNoAndRemAccNoAndEnterpriseIdAndCreatedAtBetween(fundTransferStoreResp.get().getAmount()
										,fundTransferStoreResp.get().getBenAccNo(),fundTransferStoreResp.get().getRemAccNo(),
										fundTransferStoreResp.get().getEnterpriseId(),past,current);
								RecordLog.writeLogFile("record exist in time duration fund prefcorp "+enterprises.get().getPrefCorp()+"  "+exist);
								
								if(!exist) {
								
								validDailyTransaction = fundTransaferTransactionInternalService
										.validDailyTransaction(fundTransferStoreResp.get().getAmount(), fundTransferStoreResp.get().getEnterpriseId());
								validMonthlyTransaction = fundTransaferTransactionInternalService
										.validMonthlyTransaction(fundTransferStoreResp.get().getAmount(), fundTransferStoreResp.get().getEnterpriseId());
								
								
								if (validDailyTransaction && validMonthlyTransaction) {
								
								
								
//								if (Double.parseDouble(fundTransferStoreResp.get().getAmount()) <= transLimit) {
									if (fundTransferStoreResp.get().getTransferType().equals("QuickPay")) {
//										isValidQuickPay = fundTransaferTransactionInternalService
//												.validQuickPay(fundTransferStoreResp.get().getAmount(), enterpriseId);
//										if (isValidQuickPay) {
											fundTransferInternalModel = createFundTransfer(fundTransferStoreResp,
													enterprises);
//										} else {
//											deleteFTStoreId(fundTransferStoreResp);
//											fundTransferInternalModel = new FundTransferInternalModel();
//											fundTransferInternalModel.setStatus(false);
//											fundTransferInternalModel.setDescription(
//													"Amount exceeds the quick pay limit of Rs. " + quickpayLimit);
//											fundTransferInternalModel.setMessage("failed");
//											fundTransferInternalModel.setFundTransfer(null);
//											return fundTransferInternalModel;
//										}
									} else {
										fundTransferInternalModel = createFundTransfer(fundTransferStoreResp,
												enterprises);
									}
//								} else {
//									fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
//									fundTransferInternalModel = new FundTransferInternalModel();
//									fundTransferInternalModel.setStatus(false);
//									fundTransferInternalModel.setDescription(
//											"Amount exceeds the per transaction limit of Rs. " + transLimit);
//									fundTransferInternalModel.setMessage("failed");
//									fundTransferInternalModel.setFundTransfer(null);
//									return fundTransferInternalModel;
//								}
								if (fundTransferInternalModel.isStatus()) {
									enterprises = enterprisesRepository
											.findById(Long.valueOf(getCurrentUserDetails.get().getEnterpriseId()));
									if (enterprises.isPresent()) {
										ApplicationEnterpris applicationEnterpris=applicationEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterprises.get().getApplicationFormId(),enterprises.get().getPrefCorp());
										String authExt =applicationEnterpris==null?null:applicationEnterpris.getAuthFund();
										if (authExt == null) {
											authExt = "0";
										}
										ZonedDateTime zone=ZonedDateTime.now();
										Date now=Date.from(zone.plusHours(5).plusMinutes(30).toInstant());
									
									Date night11=Date.from(zone.withHour(23).withMinute(0).withSecond(0).toInstant());
									Date morning1=	Date.from(zone.plusDays(1).withHour(1).withMinute(0).withSecond(0).toInstant());
//										Date night11=Date.from(zone.withHour(23).withMinute(0).withSecond(0).toInstant());
//										Date morning1=	Date.from(zone.withHour(1).withMinute(0).withSecond(0).toInstant());
										RecordLog.writeLogFile("is after "+now.after(night11)+" is before "+now.before(morning1)+" mode "+fundTransferInternalModel.getFundTransfer().getMode());
										RecordLog.writeLogFile("time of fund now:" +now+" time between " +night11+"  "  +morning1);

										System.out.println(zone);
										if((now.after(night11) && now.before(morning1)) && fundTransferInternalModel.getFundTransfer().getMode().equals("neft")) {
											fundTransferInternalModel = new FundTransferInternalModel();
											fundTransferInternalModel.setStatus(false);
											fundTransferInternalModel.setDescription("NEFT transaction cannot be approved between 11.00 PM and 1.00 AM.");
											fundTransferInternalModel.setMessage("NEFT transaction cannot be approved between 11.00 PM and 1.00 AM.");
											fundTransferInternalModel.setFundTransfer(null);
											return fundTransferInternalModel;
										}else {
											RecordLog.writeLogFile("time of fund now:" +now+" time between " +night11+"  "  +morning1);
										checkerServiceFundTransfer.maker(getCurrentUserDetails, "FundTransfer",
												fundTransferInternalModel.getFundTransfer(), Integer.valueOf(authExt));
										/* TODO check with vikas */
										checkerServiceFundTransfer.checker(getCurrentUserDetails,
												fundTransferInternalModel.getFundTransfer());
//										fundTransferLogsEntryService.approveLog(
//												fundTransferInternalModel.getFundTransfer(),
//												getCurrentUserDetails.get().getPrefNo(),
//												enterprises.get().getPrefCorp());
										mode = fundTransferInternalModel.getFundTransfer().getMode();
										if (mode.equals("fed2fed")) {
											remarks = StringUtils
													.isEmpty(fundTransferInternalModel.getFundTransfer().getRemarks())
															? "FED2FED"
															: fundTransferInternalModel.getFundTransfer().getRemarks();

											benName = StringUtils
													.isEmpty(fundTransferInternalModel.getFundTransfer().getBenName())
															? "SMEMB QP"
															: fundTransferInternalModel.getFundTransfer().getBenName();

//											isDuplicate = checkForDuplicateFT(mode,
//													String.valueOf(enterprises.get().getId()), "pending",
//													fundTransferInternalModel.getFundTransfer());
//
//											if (isDuplicate == true) {
//												fundTransferRepository.deleteById(
//														fundTransferInternalModel.getFundTransfer().getId());
//												fundTransferInternalModel = new FundTransferInternalModel();
//												fundTransferInternalModel.setStatus(true);
//												fundTransferInternalModel.setDescription(
//														"This is a duplicate fund transfer transaction. Please try after 5 minutes");
//												fundTransferInternalModel.setMessage("failed");
//												fundTransferInternalModel.setFundTransfer(null);
//												return fundTransferInternalModel;
//											} else {
												String mandatoryField = checkmandatoryField(fundTransferStoreResp,
														mode);
												if (mandatoryField.equals("")) {
//											FundTransferReqModel fundTransferReqModel = createFundTransferReq(
//													fundTransferStoreResp);
													String request = createFundTransferReq(
															fundTransferInternalModel.getFundTransfer(),
															fundTransferStoreResp);
													  try {
														  RecordLog.writeLogFile("Intrabank Req String : " + request.toString());
													HttpHeaders headers = new HttpHeaders();
													headers.setContentType(MediaType.APPLICATION_JSON);
//											headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
													HttpEntity<String> entity = new HttpEntity<String>(request,
															headers);
													ResponseEntity<?> ftResponse = restTemplate.postForEntity(
															fblgatewayurl + "/gateway/fundtransfer/ift", entity,
															String.class);
													RecordLog.writeLogFile("Gateway Response 572 fund internal service" + ftResponse);

													if (ftResponse.getStatusCodeValue() == 200) {
														if (ftResponse.hasBody()) {
															JSONObject jsonObject = new JSONObject(
																	ftResponse.getBody().toString());

															auditLogService
																	.FTlog(fundTransferInternalModel.getFundTransfer(),
																			"fed2fed",
																			jsonObject.getJSONObject("recordId")
																					.getString("responseCode"),
																			enterprises);
//															fundTransferLogsEntryService.transactionLog(
//																	fundTransferInternalModel.getFundTransfer(),
//																	jsonObject.getJSONObject("recordId")
//																			.getString("responseCode"),
//																	getCurrentUserDetails.get().getPrefNo(),
//																	enterprises.get().getPrefCorp());
															if (jsonObject.getBoolean("status")) {
																fundTransferInternalModel.getFundTransfer()
																		.setStatus("approved");
																fundTransferInternalModel.getFundTransfer()
																		.setProgress("completed");
																fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getFundTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getFundTransfer().setResponseReason(jsonObject.getJSONObject("recordId").getString("reason"));
																
																FundTransfer ft = fundTransferRepository.save(
																		fundTransferInternalModel.getFundTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(true);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel.setMessage(jsonObject
																		.getJSONObject("recordId").getString("reason"));
																fundTransferInternalModel.setFundTransfer(ft);
																return fundTransferInternalModel;
															} else {
																/**
																 * Do we need to delete FundTransfer?
																 */
																fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getFundTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getFundTransfer().setResponseReason(jsonObject.getJSONObject("recordId").getString("reason"));
																fundTransferInternalModel.getFundTransfer()
																.setStatus("approved");
																fundTransferRepository.save(
																		fundTransferInternalModel.getFundTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(false);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel.setMessage(jsonObject
																		.getJSONObject("recordId").getString("reason"));
																fundTransferInternalModel.setFundTransfer(null);
																return fundTransferInternalModel;
															}
														} else {
															fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
															fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
															fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
															fundTransferInternalModel.getFundTransfer().setResponseCode("500");
															fundTransferInternalModel.getFundTransfer().setResponseReason(ftResponse.toString());
															fundTransferInternalModel.getFundTransfer()
															.setStatus("failed");
															fundTransferRepository.save(
																	fundTransferInternalModel.getFundTransfer());
															fundTransferInternalModel = new FundTransferInternalModel();
															fundTransferInternalModel.setStatus(false);
															fundTransferInternalModel
																	.setDescription("Gateway Response");
															fundTransferInternalModel
																	.setMessage("no response from gateway");
															fundTransferInternalModel.setFundTransfer(null);
															return fundTransferInternalModel;
														}
													} else {
														fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
														fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
														fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
														fundTransferInternalModel.getFundTransfer().setResponseCode("500");
														fundTransferInternalModel.getFundTransfer().setResponseReason(ftResponse.toString());
														fundTransferInternalModel.getFundTransfer()
														.setStatus("failed");
														  fundTransferRepository.save(
																fundTransferInternalModel.getFundTransfer());
														fundTransferInternalModel = deleteFTStoreId(
																fundTransferStoreResp);
														fundTransferInternalModel = new FundTransferInternalModel();
														fundTransferInternalModel.setStatus(false);
														fundTransferInternalModel.setDescription(ftResponse.toString());
														fundTransferInternalModel.setMessage("failed");
														fundTransferInternalModel.setFundTransfer(null);
														return fundTransferInternalModel;
													}
												}catch(Exception e) {
											RecordLog.writeLogFile("EXOCfundinternal 630: for mobile "+performTransactionReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());

													fundTransferInternalModel.getFundTransfer().setStatus("failed");
													fundTransferInternalModel.getFundTransfer().setProgress("completed");
													fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
													fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
													fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
													fundTransferInternalModel.getFundTransfer().setResponseCode("500");
													fundTransferInternalModel.getFundTransfer().setResponseReason("Exception During Fundtransfer");
													
													  fundTransferRepository.save(
															fundTransferInternalModel.getFundTransfer());
													 fundTransferInternalModel = new FundTransferInternalModel();
														fundTransferInternalModel.setStatus(false);
														fundTransferInternalModel.setDescription("Exception During Fundtransfer");
														fundTransferInternalModel.setMessage("Exception During Fundtransfer");
														fundTransferInternalModel.setFundTransfer(null);
														return fundTransferInternalModel;
													
													
												}
													
												} else {
													fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
													fundTransferInternalModel = new FundTransferInternalModel();
													fundTransferInternalModel.setStatus(false);
													fundTransferInternalModel.setMessage("failed");
													fundTransferInternalModel.setDescription(
															"Missing data in Fund Transfer Store : " + mandatoryField);
													fundTransferInternalModel.setFundTransfer(null);
													return fundTransferInternalModel;
												}
											//}
										} else if (mode.equals("neft")) {
											benName = StringUtils
													.isEmpty(fundTransferInternalModel.getFundTransfer().getBenName())
															? "SMEMB QP"
															: fundTransferInternalModel.getFundTransfer().getBenName();

											isDuplicate = checkForDuplicateFT(mode,
													String.valueOf(enterprises.get().getId()), "pending",
													fundTransferInternalModel.getFundTransfer());

											if (isDuplicate == true) {
												fundTransferRepository.deleteById(
														fundTransferInternalModel.getFundTransfer().getId());
												fundTransferInternalModel = new FundTransferInternalModel();
												fundTransferInternalModel.setStatus(false);
												fundTransferInternalModel.setMessage("failed");
												fundTransferInternalModel.setDescription(
														"This is a duplicate fund transfer transaction. Please try after 5 minutes");
												fundTransferInternalModel.setFundTransfer(null);
												return fundTransferInternalModel;
											} else {
												// fundTransferStoreResp
												String mandatoryField = checkmandatoryField(fundTransferStoreResp,
														mode);
												if (mandatoryField.equals("")) {
//											NeftTransferReqModel neftTransferReqModel = createNeftTransferReq(
//													fundTransferStoreResp);
													String request = createNeftTransferReq(
															fundTransferInternalModel.getFundTransfer(),
															fundTransferStoreResp);
													
													RecordLog.writeLogFile("NEFT Req String : " + request);				
													try {
													HttpHeaders headers = new HttpHeaders();
													headers.setContentType(MediaType.APPLICATION_JSON);
//											headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
													HttpEntity<String> entity = new HttpEntity<String>(request,
															headers);
													ResponseEntity<?> ftResponse = restTemplate.postForEntity(
															fblgatewayurl + "/gateway/fundtransfer/neft", entity,
															String.class);
													RecordLog.writeLogFile("Gateway Response 712  fund internal service" + ftResponse);

													if (ftResponse.getStatusCodeValue() == 200) {
														if (ftResponse.hasBody()) {
															JSONObject jsonObject = new JSONObject(
																	ftResponse.getBody().toString());

															auditLogService
																	.FTlog(fundTransferInternalModel.getFundTransfer(),
																			"neft",
																			jsonObject.getJSONObject("recordId")
																					.getString("responseCode"),
																			enterprises);
//															fundTransferLogsEntryService.transactionLog(
//																	fundTransferInternalModel.getFundTransfer(),
//																	jsonObject.getJSONObject("recordId")
//																			.getString("responseCode"),
//																	getCurrentUserDetails.get().getPrefNo(),
//																	enterprises.get().getPrefCorp());
															if (jsonObject.getBoolean("status")) {
																
																
																//simBindingService.sendSMS(properties.getFund_transfer_transaction_msg(), getCurrentUserDetails.get().getMobile());

																
																fundTransferInternalModel.getFundTransfer()
																		.setStatus("approved");
																fundTransferInternalModel.getFundTransfer().setProgress("completed");
																fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getFundTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getFundTransfer().setResponseReason(jsonObject
																		.getJSONObject("recordId").getString("reason"));
																
																FundTransfer ft = fundTransferRepository.save(
																		fundTransferInternalModel.getFundTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(true);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel.setMessage(jsonObject
																		.getJSONObject("recordId").getString("reason"));
																fundTransferInternalModel.setFundTransfer(ft);
																return fundTransferInternalModel;
															} else {
																/**
																 * Do we need to delete FundTransfer?
																 */
																fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getFundTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getFundTransfer().setResponseReason(jsonObject
																		.getJSONObject("recordId").getString("reason"));
																fundTransferInternalModel.getFundTransfer()
																.setStatus("approved");
																FundTransfer ft = fundTransferRepository.save(
																		fundTransferInternalModel.getFundTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(false);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel.setMessage(jsonObject
																		.getJSONObject("recordId").getString("reason"));
																fundTransferInternalModel.setFundTransfer(null);
																return fundTransferInternalModel;
															}
														} else {
															fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
															fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
															fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
															fundTransferInternalModel.getFundTransfer().setResponseCode("500");
															fundTransferInternalModel.getFundTransfer().setResponseReason(ftResponse.toString());
															fundTransferInternalModel.getFundTransfer()
															.setStatus("failed");
															FundTransfer ft = fundTransferRepository.save(
																	fundTransferInternalModel.getFundTransfer());
															fundTransferInternalModel = new FundTransferInternalModel();
															fundTransferInternalModel.setStatus(false);
															fundTransferInternalModel
																	.setDescription("Gateway Response");
															fundTransferInternalModel
																	.setMessage("no response from gateway");
															fundTransferInternalModel.setFundTransfer(null);
															return fundTransferInternalModel;
														}
													} else {
														fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
														fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
														fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
														fundTransferInternalModel.getFundTransfer().setResponseCode("500");
														fundTransferInternalModel.getFundTransfer().setResponseReason(ftResponse.toString());
														fundTransferInternalModel.getFundTransfer()
														.setStatus("failed");
														FundTransfer ft = fundTransferRepository.save(
																fundTransferInternalModel.getFundTransfer());
														fundTransferInternalModel = deleteFTStoreId(
																fundTransferStoreResp);
														fundTransferInternalModel = new FundTransferInternalModel();
														fundTransferInternalModel.setStatus(false);
														fundTransferInternalModel.setMessage("failed");
														fundTransferInternalModel.setDescription(ftResponse.toString());
														fundTransferInternalModel.setFundTransfer(null);
														return fundTransferInternalModel;
													}
												}catch(Exception e) {
													RecordLog.writeLogFile("EXOCfundinternal 776: for mobile "+performTransactionReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());

													fundTransferInternalModel.getFundTransfer().setStatus("failed");
													fundTransferInternalModel.getFundTransfer().setProgress("completed");
													fundTransferInternalModel.getFundTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
													fundTransferInternalModel.getFundTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
													fundTransferInternalModel.getFundTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
													fundTransferInternalModel.getFundTransfer().setResponseCode("500");
													fundTransferInternalModel.getFundTransfer().setResponseReason("Exception During Fundtransfer");
													
										         	 fundTransferRepository.save(
													fundTransferInternalModel.getFundTransfer());
										         	 
										         	  fundTransferInternalModel = new FundTransferInternalModel();
														fundTransferInternalModel.setStatus(false);
														fundTransferInternalModel.setDescription("Exception During Fundtransfer");
														fundTransferInternalModel.setMessage("Exception During Fundtransfer");
														fundTransferInternalModel.setFundTransfer(null);
														return fundTransferInternalModel;
												}
												} else {
													fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
													fundTransferInternalModel = new FundTransferInternalModel();
													fundTransferInternalModel.setStatus(false);
													fundTransferInternalModel.setMessage("failed");
													fundTransferInternalModel.setDescription(
															"Missing data in Fund Transfer Store : " + mandatoryField);
													fundTransferInternalModel.setFundTransfer(null);
													return fundTransferInternalModel;
												}
											}
										}
									}
									} else {
										fundTransferInternalModel = new FundTransferInternalModel();
										fundTransferInternalModel.setStatus(false);
										fundTransferInternalModel.setMessage("failed");
										fundTransferInternalModel.setDescription("enterprises record not found");
										fundTransferInternalModel.setFundTransfer(null);
									}
								}
								
								
								
								
								
							}else if (!validDailyTransaction) {
								Double totalLimitDaily;
								Optional<TransactionLimit> transactionDailyLimit = null;
								Long daily_amount_transacted = fundTransaferTransactionInternalService
										.approvedAmount(fundTransferStoreResp.get().getEnterpriseId());
								transactionDailyLimit = transactionLimitRepository
										.findFirstByEnterpriseIdAndModeOrderByIdDesc(fundTransferStoreResp.get().getEnterpriseId(), "daily");
								if (transactionDailyLimit.isPresent()) {
									totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0")
											? Double.parseDouble(transactionDaily)
											: Double.parseDouble(transactionDailyLimit.get().getAmount());
								} else {
									totalLimitDaily = Double.parseDouble(transactionDaily);
								}
								fundTransferInternalModel = new FundTransferInternalModel();
								fundTransferInternalModel.setStatus(false);
								fundTransferInternalModel.setMessage(
										"Transaction amount exceeds the Daily Transaction limit. You have already transacted for Rs."
												+ String.valueOf(daily_amount_transacted) + " amount and the limit is Rs."
												+ String.valueOf(totalLimitDaily));
								fundTransferInternalModel.setFundTransfer(null);
								return fundTransferInternalModel;
							} else if (!validMonthlyTransaction) {
								Double totalLimitMonthly;
								Optional<TransactionLimit> transactionMonthlyLimit = null;
								Long monthly_amount_transacted = fundTransaferTransactionInternalService
										.totalAmountMonth(fundTransferStoreResp.get().getEnterpriseId());
								transactionMonthlyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(
										fundTransferStoreResp.get().getEnterpriseId(), "monthly");
								if (transactionMonthlyLimit.isPresent()) {
									totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
											? Double.parseDouble(transactionMonthly)
											: Double.parseDouble(transactionMonthlyLimit.get().getAmount());
								} else {
									totalLimitMonthly = Double.parseDouble(transactionMonthly);
								}
								fundTransferInternalModel = new FundTransferInternalModel();
							
								fundTransferInternalModel.setStatus(false);
								fundTransferInternalModel.setMessage(
										"Transaction amount exceeds the Monthly Transaction limit. You have already transacted for Rs."
												+ String.valueOf(monthly_amount_transacted) + " amount and the limit is Rs."
												+ String.valueOf(totalLimitMonthly));
								fundTransferInternalModel.setFundTransfer(null);
								return fundTransferInternalModel;
							}		

								
								
								}else {
									fundTransferStoreRepository.deleteById(fundTransferStoreResp.get().getId());
									fundTransferInternalModel = new FundTransferInternalModel();
									fundTransferInternalModel.setStatus(false);
									fundTransferInternalModel.setDescription("Similar transactions not allowed within 10 minutes");
									fundTransferInternalModel.setMessage("Similar transactions not allowed within 10 minutes");
									fundTransferInternalModel.setFundTransfer(null);
									return fundTransferInternalModel;
									
								}
								
								
								
								
							} else {
//								if (Double.parseDouble(fundTransferStoreResp.get().getAmount()) <= transLimit) {
									if (fundTransferStoreResp.get().getTransferType().equals("QuickPay")) {
//										isValidQuickPay = fundTransaferTransactionInternalService
//												.validQuickPay(fundTransferStoreResp.get().getAmount(), enterpriseId);
//										if (isValidQuickPay) {
											fundTransferInternalModel = createFundTransfer(fundTransferStoreResp,
													enterprises);
//										} else {
//											deleteFTStoreId(fundTransferStoreResp);
//											fundTransferInternalModel = new FundTransferInternalModel();
//											fundTransferInternalModel.setStatus(false);
//											fundTransferInternalModel.setMessage("failed");
//											fundTransferInternalModel.setDescription(
//													"Amount exceeds the quick pay limit of Rs. " + quickpayLimit);
//											fundTransferInternalModel.setFundTransfer(null);
//											return fundTransferInternalModel;
//										}
									} else {
										fundTransferInternalModel = createFundTransfer(fundTransferStoreResp,
												enterprises);
									}
//								} else {
//									fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
//									fundTransferInternalModel = new FundTransferInternalModel();
//									fundTransferInternalModel.setStatus(false);
//									fundTransferInternalModel.setMessage("failed");
//									fundTransferInternalModel.setDescription(
//											"Amount exceeds the per transaction limit of Rs. " + transLimit);
//									fundTransferInternalModel.setFundTransfer(null);
//									return fundTransferInternalModel;
//								}
								if (fundTransferInternalModel.isStatus() == true) {
									enterprises = enterprisesRepository
											.findById(Long.valueOf(getCurrentUserDetails.get().getEnterpriseId()));
									if (enterprises.isPresent()) {
										ApplicationEnterpris applicationEnterpris=applicationEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterprises.get().getApplicationFormId(),enterprises.get().getPrefCorp());
										String authExt =applicationEnterpris.getAuthFund();
										if (authExt == null) {
											authExt = "0";
										}
										checkerServiceFundTransfer.maker(getCurrentUserDetails, "FundTransfer",
												fundTransferInternalModel.getFundTransfer(), Integer.valueOf(authExt));
//										fundTransferLogsEntryService.initiateLog(
//												fundTransferInternalModel.getFundTransfer(),
//												getCurrentUserDetails.get().getPrefNo(),
//												enterprises.get().getPrefCorp());

										fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
										fundTransferInternalModel.setStatus(true);
										fundTransferInternalModel.setMessage("success");
										fundTransferInternalModel.setDescription("Initiated");
										return fundTransferInternalModel;
									} else {
										fundTransferInternalModel = new FundTransferInternalModel();
										fundTransferInternalModel.setStatus(false);
										fundTransferInternalModel.setMessage("failed");
										fundTransferInternalModel.setDescription("enterprises record not found");
										fundTransferInternalModel.setFundTransfer(null);
										return fundTransferInternalModel;
									}
								}
							}
						} else {
							fundTransferInternalModel = new FundTransferInternalModel();
							fundTransferInternalModel.setStatus(false);
							fundTransferInternalModel.setMessage("failed");
							fundTransferInternalModel.setDescription("User not exist by Auth Token");
							fundTransferInternalModel.setFundTransfer(null);
							return fundTransferInternalModel;
						}
//					} 
//				else if (!validDailyTransaction) {
//						Double totalLimitDaily;
//						Optional<TransactionLimit> transactionDailyLimit = null;
//						Long daily_amount_transacted = fundTransaferTransactionInternalService
//								.approvedAmount(enterpriseId);
//						transactionDailyLimit = transactionLimitRepository
//								.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId, "daily");
//						if (transactionDailyLimit.isPresent()) {
//							totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0")
//									? Double.parseDouble(transactionDaily)
//									: Double.parseDouble(transactionDailyLimit.get().getAmount());
//						} else {
//							totalLimitDaily = Double.parseDouble(transactionDaily);
//						}
//						fundTransferInternalModel = new FundTransferInternalModel();
//						fundTransferInternalModel.setStatus(false);
//						fundTransferInternalModel.setMessage("failed");
//						fundTransferInternalModel.setDescription(
//								"Transaction amount exceeds the Daily Transaction limit. You have already transacted for Rs."
//										+ String.valueOf(daily_amount_transacted) + " amount and the limit is Rs."
//										+ String.valueOf(totalLimitDaily));
//						fundTransferInternalModel.setFundTransfer(null);
//						return fundTransferInternalModel;
//					}
//				else if (!validMonthlyTransaction) {
//						Double totalLimitMonthly;
//						Optional<TransactionLimit> transactionMonthlyLimit = null;
//						Long monthly_amount_transacted = fundTransaferTransactionInternalService
//								.totalAmountMonth(enterpriseId);
//						transactionMonthlyLimit = transactionLimitRepository
//								.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId, "monthly");
//						if (transactionMonthlyLimit.isPresent()) {
//							totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
//									? Double.parseDouble(transactionMonthly)
//									: Double.parseDouble(transactionMonthlyLimit.get().getAmount());
//						} else {
//							totalLimitMonthly = Double.parseDouble(transactionMonthly);
//						}
//						fundTransferInternalModel = new FundTransferInternalModel();
//						fundTransferInternalModel.setStatus(false);
//						fundTransferInternalModel.setMessage("failed");
//						fundTransferInternalModel.setDescription(
//								"Transaction amount exceeds the Monthly Transaction limit. You have already transacted for Rs."
//										+ String.valueOf(monthly_amount_transacted) + " amount and the limit is Rs."
//										+ String.valueOf(totalLimitMonthly));
//						fundTransferInternalModel.setFundTransfer(null);
//						return fundTransferInternalModel;
//					}
				} else {
					fundTransferInternalModel = new FundTransferInternalModel();
					fundTransferInternalModel.setStatus(false);
					fundTransferInternalModel.setMessage("failed");
					fundTransferInternalModel
							.setDescription("Invalid Amount, please check decimal should not be more than 2 digit");
					fundTransferInternalModel.setFundTransfer(null);
					return fundTransferInternalModel;
				}
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed");
				fundTransferInternalModel.setDescription("enterprises record not found");
				fundTransferInternalModel.setFundTransfer(null);
				return fundTransferInternalModel;
			}
			return fundTransferInternalModel;
		} catch (Exception e) {
			RecordLog.writeLogFile("EXOCfundinternal 949: for mobile "+performTransactionReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			FundTransferInternalModel fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("Failed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setDescription(e.getMessage());
			return fundTransferInternalModel;
		}
	}

	private boolean precisionCheck(String amount) {
		BigDecimal input = new BigDecimal(amount);
		String string = input.stripTrailingZeros().toPlainString();
		int index = string.indexOf(".");
		int value = index < 0 ? 0 : string.length() - index - 1;
		return value <= 2 ? true : false;
	}

	private String checkmandatoryField(Optional<FundTransferStore> fundTransferStoreResp, String mode) {
		StringBuilder sb = new StringBuilder();
		if (mode.equals("fed2fed")) {
			if (StringUtils.isEmpty(fundTransferStoreResp.get().getBenAccNo())) {
				sb.append("beneficiary account no |");
			}
			/*
			 * if (StringUtils.isEmpty(fundTransferStoreResp.get().getBenName())) {
			 * sb.append("beneficiary Name |"); }
			 */
			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemAccNo())) {
				sb.append("remitter acc no |");
			}
			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemName())) {
				sb.append("remitter name |");
			}
		} else if (mode.equals("neft")) {
			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemAccNo())) {
				sb.append("remitter acc no |");
			}
			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemName())) {
				sb.append("remitter name |");
			}
			/*
			 * if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemMobNum())) {
			 * sb.append("remitter Mobile no |"); }
			 */
//			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemName())) {
//				sb.append("remitter Address |");
//			}
			if (StringUtils.isEmpty(fundTransferStoreResp.get().getBenAccNo())) {
				sb.append("beneficiary account no |");
			}
			if (StringUtils.isEmpty(fundTransferStoreResp.get().getBenName())) {
				sb.append("beneficiary Name |");
			}
			/*
			 * if (StringUtils.isEmpty(fundTransferStoreResp.get().getBenMobNum())) {
			 * sb.append("beneficiary Mobile no |"); }
			 */
//			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemName())) {
//			sb.append("beneficiary Address |");
//			}
		}
		return sb.toString();
	}

	private String createFundTransferReq(FundTransfer fundTransfer, Optional<FundTransferStore> fundTransferStoreResp) {
//		FundTransferReqModel fundTransferReqModel = new FundTransferReqModel();
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		FundTransferReqModel.RemmiterDetails remitterDetails = fundTransferReqModel.new RemmiterDetails();
//		remitterDetails.setAccNumber(fundTransferStoreResp.get().getRemAccNo());
//		remitterDetails.setAcctype("");// TODO take from property file
//		remitterDetails.setEmail("");
//		remitterDetails.setMobile(StringUtils.isEmpty(fundTransferStoreResp.get().getRemMobNum()) ? ""
//				: fundTransferStoreResp.get().getRemMobNum());
//		remitterDetails.setName(fundTransferStoreResp.get().getRemName());
//		remitterDetails.setNotification_Flag("NONE");
//
//		FundTransferReqModel.BeneficiaryDetails beneficiaryDetails = fundTransferReqModel.new BeneficiaryDetails();
//		beneficiaryDetails.setAccNumber(fundTransferStoreResp.get().getBenAccNo());
//		beneficiaryDetails.setEmail("");
//		beneficiaryDetails.setMobile(StringUtils.isEmpty(fundTransferStoreResp.get().getBenMobNum()) ? ""
//				: fundTransferStoreResp.get().getBenMobNum());
//		beneficiaryDetails.setName(fundTransferStoreResp.get().getBenName());
//		beneficiaryDetails.setNotification_Flag("NONE");
//
//		fundTransferReqModel.setRespUrl(callbackURL);
//		fundTransferReqModel.setUserid(userId);
//		fundTransferReqModel.setPassword(password);
//		fundTransferReqModel.setSendercd(senderCD);
//		fundTransferReqModel.setTranDate(input.format(customDateFormatter));
//		fundTransferReqModel.setReferenceId(generateSenderRefNo(fundTransferStoreResp.get().getMode()));
//		fundTransferReqModel.setCust_Ref_No(fundTransferStoreResp.get().getTransactionId());
//		fundTransferReqModel.setAmount(fundTransferStoreResp.get().getAmount());
//		fundTransferReqModel.setRemarks(fundTransferStoreResp.get().getRemarks());
//		fundTransferReqModel.setSender_Data("Intrabank perform");
//		fundTransferReqModel.setBeneficiaryDetails(beneficiaryDetails);
//		fundTransferReqModel.setRemmiterDetails(remitterDetails);
//		return fundTransferReqModel;

		RecordLog.writeLogFile("CallBack URL >>>" + callbackURL);

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		sb.append("\"respUrl\":\"" + callbackURL + "/intrabank" + "\",");

		sb.append("\"userid\":\"" + userId + "\",");

		sb.append("\"password\":\"" + password + "\",");

		sb.append("\"sendercd\":\"" + senderCD + "\",");

		sb.append("\"tranDate\":\"" + input.format(customDateFormatter) + "\",");

		sb.append("\"ReferenceId\":\"" + fundTransfer.getRefNo() + "\",");

		sb.append("\"Cust_Ref_No\":\"" + fundTransfer.getSenderRefId() + "\",");

		sb.append("\"RemmiterDetails\":{");

		sb.append("\"Name\":\"" + getTrimmedValue( fundTransfer.getRemName() )+ "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.getRemAccNo() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getRemMobNum()) != null ? ""
				: fundTransferStoreResp.get().getRemMobNum() + "\",");

		sb.append("\"Email\":\" \",");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue(fundTransfer.getBenName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.getBenAccNo() + "\",");

		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getBenMobNum()) != null ? ""
				: fundTransferStoreResp.get().getBenMobNum() + "\",");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"Amount\":\"" + fundTransfer.getAmount() + "\",");

		sb.append("\"Remarks\":\"" + fundTransfer.getRemarks() + "\",");

		sb.append("\"Sender_Data\":\"Intrabank perform\"");

		sb.append("}");
		return sb.toString();
	}

	private String createFundTransferApprovalReq(Optional<FundTransfer> fundTransfer, String senderRefNo) {
//		FundTransferReqModel fundTransferReqModel = new FundTransferReqModel();
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		FundTransferReqModel.RemmiterDetails remitterDetails = fundTransferReqModel.new RemmiterDetails();
//		remitterDetails.setAccNumber(fundTransfer.get().getRemAccNo());
//		remitterDetails.setAcctype("");
//		remitterDetails.setEmail("");
//		remitterDetails.setMobile("");
//		remitterDetails.setNotification_Flag("NONE");
//
//		FundTransferReqModel.BeneficiaryDetails beneficiaryDetails = fundTransferReqModel.new BeneficiaryDetails();
//		beneficiaryDetails.setAccNumber(fundTransfer.get().getBenAccNo());
//		beneficiaryDetails.setEmail("");
//		beneficiaryDetails.setMobile("");
//		beneficiaryDetails.setName(fundTransfer.get().getBenName());
//		beneficiaryDetails.setNotification_Flag("NONE");
//
//		fundTransferReqModel.setRespUrl(callbackURL);
//		fundTransferReqModel.setUserid(userId);
//		fundTransferReqModel.setPassword(password);
//		fundTransferReqModel.setSendercd(senderCD);
//		fundTransferReqModel.setTranDate(input.format(customDateFormatter));
//		fundTransferReqModel.setReferenceId(senderRefNo);
//		fundTransferReqModel.setCust_Ref_No("");// need to know
//		fundTransferReqModel.setAmount(fundTransfer.get().getAmount());
//		fundTransferReqModel.setRemarks(fundTransfer.get().getRemarks());
//		fundTransferReqModel.setSender_Data("Intrabank approve");
//		fundTransferReqModel.setBeneficiaryDetails(beneficiaryDetails);
//		fundTransferReqModel.setRemmiterDetails(remitterDetails);
//		return fundTransferReqModel;

		RecordLog.writeLogFile("CallBack URL >>>" + callbackURL);

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		sb.append("\"respUrl\":\"" + callbackURL + "/intrabank" + "\",");

		sb.append("\"userid\":\"" + userId + "\",");

		sb.append("\"password\":\"" + password + "\",");

		sb.append("\"sendercd\":\"" + senderCD + "\",");

		sb.append("\"tranDate\":\"" + input.format(customDateFormatter) + "\",");

		sb.append("\"ReferenceId\":\"" + fundTransfer.get().getRefNo() + "\",");

		sb.append("\"Cust_Ref_No\":\"" + senderRefNo + "\",");// generateTransactionId

		sb.append("\"RemmiterDetails\":{");

		sb.append("\"Name\":\"" + getTrimmedValue( fundTransfer.get().getRemName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.get().getRemAccNo() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

//		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransfer.get().getRemMobNum()) != null ? ""
//				: fundTransfer.get().getRemMobNum() + "\",");
		sb.append("\"Mobile\":\"" + "" + "\","); // need to get column added in fundtransfer

		sb.append("\"Email\":\" \",");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue( fundTransfer.get().getBenName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.get().getBenAccNo() + "\",");

//		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransfer.get().getBenMobNum()) != null ? ""
//				: fundTransfer.get().getBenMobNum() + "\",");
		sb.append("\"Mobile\":\"" + "" + "\",");// need to get column added in fundtransfer
		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"Amount\":\"" + fundTransfer.get().getAmount() + "\",");

		sb.append("\"Remarks\":\"" + fundTransfer.get().getRemarks() + "\",");

		sb.append("\"Sender_Data\":\"Intrabank perform\"");

		sb.append("}");
		return sb.toString();
	}

	private String createNeftTransferReq(FundTransfer fundTransfer, Optional<FundTransferStore> fundTransferStoreResp) {
//		NeftTransferReqModel neftTransferReqModel = new NeftTransferReqModel();
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		NeftTransferReqModel.RemmiterDetails remmitterDetails = neftTransferReqModel.new RemmiterDetails();
//		remmitterDetails.setName(fundTransferStoreResp.get().getRemName());
//		remmitterDetails.setAccNumber(fundTransferStoreResp.get().getRemAccNo());
//		remmitterDetails.setAcctype("");
//		remmitterDetails.setMobile(fundTransferStoreResp.get().getRemMobNum());
//		remmitterDetails.setEmail("");
//		remmitterDetails.setAddress("");
//		remmitterDetails.setNotification_Flag("NONE");
//
//		NeftTransferReqModel.BeneficiaryDetails beneficiaryDetails = neftTransferReqModel.new BeneficiaryDetails();
//		beneficiaryDetails.setName(fundTransferStoreResp.get().getBenName());
//		beneficiaryDetails.setAccNumber(fundTransferStoreResp.get().getBenAccNo());
//		beneficiaryDetails.setAcctype(fundTransferStoreResp.get().getBenAccType());
//		beneficiaryDetails.setIFSC(fundTransferStoreResp.get().getBenIfsc());
//		beneficiaryDetails.setMobile(fundTransferStoreResp.get().getBenMobNum());
//		beneficiaryDetails.setEmail("");
//		beneficiaryDetails.setAddress("");
//		beneficiaryDetails.setNotification_Flag("NONE");
//
//		neftTransferReqModel.setRespUrl(callbackURL);
//		neftTransferReqModel.setUserid(userId);
//		neftTransferReqModel.setPassword(password);
//		neftTransferReqModel.setSendercd(senderCD);
//		neftTransferReqModel.setTranDate(input.format(customDateFormatter));
//		neftTransferReqModel.setReferenceId(generateSenderRefNo(fundTransferStoreResp.get().getMode()));
//		neftTransferReqModel.setDebitAccountNumber("");
//		neftTransferReqModel.setDebitSuspenseAccount("N");
//		neftTransferReqModel.setCust_Ref_No(fundTransferStoreResp.get().getTransactionId());
//		neftTransferReqModel.setAmount(fundTransferStoreResp.get().getAmount());
//		neftTransferReqModel.setRemarks(fundTransferStoreResp.get().getRemarks());
//		neftTransferReqModel.setSender_Data("NEFT perform");
//		neftTransferReqModel.setAlternative_Payments("N");
//		neftTransferReqModel.setPostpone("N");
//		neftTransferReqModel.setBeneficiaryDetails(beneficiaryDetails);
//		neftTransferReqModel.setRemmiterDetails(remmitterDetails);
//		return neftTransferReqModel;

		RecordLog.writeLogFile("CallBack URL >>>" + callbackURL);

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		sb.append("\"respUrl\":\"" + callbackURL + "/neft" + "\",");

		sb.append("\"userid\":\"" + userId + "\",");

		sb.append("\"password\":\"" + password + "\",");

		sb.append("\"sendercd\":\"" + senderCD + "\",");

		sb.append("\"tranDate\":\"" + input.format(customDateFormatter) + "\",");

		sb.append("\"ReferenceId\":\"" + fundTransfer.getRefNo() + "\",");

		sb.append("\"Cust_Ref_No\":\"" + fundTransfer.getSenderRefId() + "\",");

		sb.append("\"RemmiterDetails\":{");

		sb.append("\"Name\":\"" + getTrimmedValue( fundTransfer.getRemName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.getRemAccNo() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getRemMobNum()) != null ? ""
				: fundTransferStoreResp.get().getRemMobNum() + "\",");

		sb.append("\"Email\":\"\",");

		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue(fundTransfer.getBenName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.getBenAccNo() + "\",");

//		sb.append("\"Acctype\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getBenAccType()) != null ? ""
//				: fundTransferStoreResp.get().getBenAccType() + "\",");
		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"IFSC\":\"" + fundTransfer.getBenIfsc() + "\",");

		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getBenMobNum()) != null ? ""
				: fundTransferStoreResp.get().getBenMobNum() + "\",");
		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"Amount\":\"" + fundTransfer.getAmount() + "\",");

		sb.append("\"Remarks\":\"" + fundTransfer.getRemarks() + "\",");

		sb.append("\"Alternative_Payments\":\"N\",");

		sb.append("\"Sender_Data\":\"NEFT perform\"");

		sb.append("}");
		return sb.toString();

	}

	private String createNeftTransferApprovalReq(Optional<FundTransfer> fundTransfer, String senderRefNo) {
//		NeftTransferReqModel neftTransferReqModel = new NeftTransferReqModel();
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		NeftTransferReqModel.RemmiterDetails remmitterDetails = neftTransferReqModel.new RemmiterDetails();
//		remmitterDetails.setName(fundTransfer.get().getRemName());
//		remmitterDetails.setAccNumber(fundTransfer.get().getRemAccNo());
//		remmitterDetails.setAcctype("");
//		remmitterDetails.setMobile("");
//		remmitterDetails.setEmail("");
//		remmitterDetails.setAddress("");
//		remmitterDetails.setNotification_Flag("NONE");
//
//		NeftTransferReqModel.BeneficiaryDetails beneficiaryDetails = neftTransferReqModel.new BeneficiaryDetails();
//		beneficiaryDetails.setName(fundTransfer.get().getBenName());
//		beneficiaryDetails.setAccNumber(fundTransfer.get().getBenAccNo());
//		beneficiaryDetails.setAcctype(fundTransfer.get().getBenAccType());
//		beneficiaryDetails.setIFSC(fundTransfer.get().getBenIfsc());
//		beneficiaryDetails.setMobile("");
//		beneficiaryDetails.setEmail("");
//		beneficiaryDetails.setAddress("");
//		beneficiaryDetails.setNotification_Flag("NONE");
//
//		neftTransferReqModel.setRespUrl(callbackURL);
//		neftTransferReqModel.setUserid(userId);
//		neftTransferReqModel.setPassword(password);
//		neftTransferReqModel.setSendercd(senderCD);
//		neftTransferReqModel.setTranDate(input.format(customDateFormatter));
//		neftTransferReqModel.setReferenceId(senderRefNo);
//		neftTransferReqModel.setDebitAccountNumber("");
//		neftTransferReqModel.setDebitSuspenseAccount("N");
//		neftTransferReqModel.setCust_Ref_No("");// need to know
//		neftTransferReqModel.setAmount(fundTransfer.get().getAmount());
//		neftTransferReqModel.setRemarks(fundTransfer.get().getRemarks());
//		neftTransferReqModel.setSender_Data("NEFT Approve");
//		neftTransferReqModel.setAlternative_Payments("N");
//		neftTransferReqModel.setPostpone("N");
//		neftTransferReqModel.setBeneficiaryDetails(beneficiaryDetails);
//		neftTransferReqModel.setRemmiterDetails(remmitterDetails);
//		return neftTransferReqModel;

		RecordLog.writeLogFile("CallBack URL >>>" + callbackURL);

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		sb.append("\"respUrl\":\"" + callbackURL + "/neft" + "\",");

		sb.append("\"userid\":\"" + userId + "\",");

		sb.append("\"password\":\"" + password + "\",");

		sb.append("\"sendercd\":\"" + senderCD + "\",");

		sb.append("\"tranDate\":\"" + input.format(customDateFormatter) + "\",");

		sb.append("\"ReferenceId\":\"" + fundTransfer.get().getRefNo() + "\",");

		sb.append("\"Cust_Ref_No\":\"" + senderRefNo + "\",");// generateTransactionId

		sb.append("\"RemmiterDetails\":{");

		sb.append("\"Name\":\"" + getTrimmedValue(fundTransfer.get().getRemName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.get().getRemAccNo() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

//		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransfer.get().getRemMobNum()) != null ? ""
//				: fundTransfer.get().getRemMobNum() + "\",");
		sb.append("\"Mobile\":\"" + "" + "\",");
		sb.append("\"Email\":\"\",");

		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue(fundTransfer.get().getBenName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.get().getBenAccNo() + "\",");

//		sb.append("\"Acctype\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getBenAccType()) != null ? ""
//				: fundTransferStoreResp.get().getBenAccType() + "\",");
		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"IFSC\":\"" + fundTransfer.get().getBenIfsc() + "\",");

//		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransfer.get().getBenMobNum()) != null ? ""
//				: fundTransfer.get().getBenMobNum() + "\",");
		sb.append("\"Mobile\":\"" + "" + "\",");
		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"Amount\":\"" + fundTransfer.get().getAmount() + "\",");

		sb.append("\"Remarks\":\"" + fundTransfer.get().getRemarks() + "\",");

		sb.append("\"Alternative_Payments\":\"N\",");

		sb.append("\"Sender_Data\":\"NEFT perform\"");

		sb.append("}");
		return sb.toString();

	}

	public FundTransferInternalModel createFundTransfer(Optional<FundTransferStore> fundTransferStoreResp,
			Optional<Enterprises> enterprises) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransfer fundTransfer = null;
		FundTransfer fundTransferResp = null;
		if (fundTransferStoreResp.isPresent() && enterprises.isPresent()) {
			String senderRefId=generateSenderRefNo(fundTransferStoreResp.get().getMode());
			fundTransfer = new FundTransfer();
			fundTransfer.setRefNo(senderRefId);
			fundTransfer.setSenderRefId(senderRefId);
			fundTransfer.setAmount(fundTransferStoreResp.get().getAmount());
			fundTransfer.setMode(fundTransferStoreResp.get().getMode());
			fundTransfer.setRemAccNo(fundTransferStoreResp.get().getRemAccNo());
			fundTransfer.setRemName(enterprises.get().getAccName());
			fundTransfer.setBenAccNo(fundTransferStoreResp.get().getBenAccNo());
			fundTransfer.setBenNickName(fundTransferStoreResp.get().getBenNickName());
			fundTransfer.setBenName(fundTransferStoreResp.get().getBenName());
			fundTransfer.setBenAccType(fundTransferStoreResp.get().getBenAccType());
			fundTransfer.setBenIfsc(fundTransferStoreResp.get().getBenIfsc());
			fundTransfer.setRemarks(fundTransferStoreResp.get().getRemarks());
			fundTransfer.setEnterpriseId(String.valueOf(enterprises.get().getId()));
			fundTransfer.setTransactionDate(new Timestamp(new Date().getTime()));
			fundTransfer.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransfer.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransfer.setTransferType(fundTransferStoreResp.get().getTransferType());
			fundTransferResp = fundTransferRepository.save(fundTransfer);

			if (fundTransferResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(fundTransferResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("FT || Enterprise data not recieved");
			fundTransferInternalModel.setFundTransfer(null);
		}
		return fundTransferInternalModel;
	}

	public boolean checkForDuplicateFT(String mode, String enterpriseId, String status, FundTransfer fundTransfer) {
		List<FundTransfer> getDuplicateFT = null;
		boolean isDuplicate = false;
		final ZonedDateTime input = ZonedDateTime.now();

		Timestamp fromdate = null;
		Timestamp todate = null;
		Date today = Date.from(input.minusMinutes(5).toInstant());
		Date endday = Date.from(input.toInstant());
		logger.info("from:" + today + ",todate:" + endday);
		fromdate = new Timestamp(today.getTime());
		todate = new Timestamp(endday.getTime());
		if (mode.equals("neft") || mode.equals("fed2fed")) {
			if (mode.equals("neft")) {
				getDuplicateFT = fundTransferRepository.findDuplicateFTRecordsLessThan5Min(mode, enterpriseId, status,
						fromdate, todate);
			} else {
				getDuplicateFT = fundTransferRepository.findDuplicateFTRecordsLessThan5Min(mode, enterpriseId, status,
						fromdate, todate);
			}
			for (int i = 0; i < getDuplicateFT.size(); i++) {
				if (fundTransfer.getRemAccNo() == getDuplicateFT.get(i).getRemAccNo()
						&& fundTransfer.getAmount() == getDuplicateFT.get(i).getAmount()
						&& fundTransfer.getBenAccNo() == getDuplicateFT.get(i).getBenAccNo()
						&& fundTransfer.getBenIfsc() == getDuplicateFT.get(i).getBenIfsc()) {
					fundTransfer.setDuplicate(true);
					fundTransfer.setStatus("duplicate");
					fundTransferRepository.save(fundTransfer);

					isDuplicate = true;
				} else {
					isDuplicate = false;
				}
			}
		}
		RecordLog.writeLogFile("is duplicate FT: " + isDuplicate);
		return isDuplicate;
	}

	private FundTransferInternalModel impsCreate(Optional<FundTransferStore> fundTransferStoreResp,
			PerformTransactionReqModel performTransactionReqModel) {
		try {
			String enterpriseId = null;
			String remarks = null;
			String senderRefId = null;
			String message = null;
			String mode = null;
			Optional<Enterprises> enterprises = null;
			Optional<User> getCurrentUserDetails = null;
			boolean validDailyTransaction = false;
			boolean validMonthlyTransaction = false;
			Long perTransLimit = 0L;
			Long transLimit = 0L;
			boolean isValidQuickPay = false;
			boolean isDuplicate = false;
			FundTransferInternalModel fundTransferInternalModel = null;

			enterpriseId = fundTransferStoreResp.get().getEnterpriseId();
			enterprises = enterprisesRepository.findById(Long.valueOf(enterpriseId));

			if (enterprises.isPresent()) {
				if (precisionCheck(fundTransferStoreResp.get().getAmount())) {
//					validDailyTransaction = fundTransaferTransactionInternalService
//							.validDailyTransaction(fundTransferStoreResp.get().getAmount(), enterpriseId);
//					validMonthlyTransaction = fundTransaferTransactionInternalService
//							.validMonthlyTransaction(fundTransferStoreResp.get().getAmount(), enterpriseId);

					// validDailyTransaction = true;
					// validMonthlyTransaction = true;
//					if (validDailyTransaction && validMonthlyTransaction) {
						getCurrentUserDetails = getCurrentUserDetail(performTransactionReqModel.getAuthToken());
						if (getCurrentUserDetails.isPresent()) {
							Double pertransaction = Double
									.parseDouble(getCurrentUserDetails.get().getTransLimit() != null
											? getCurrentUserDetails.get().getTransLimit()
											: "0");
							if (pertransaction == 0) {
								pertransaction = Double.parseDouble(properties.getFedcorp_per_transaction_limit());
							}
							transLimit = pertransaction.longValue();
							logger.info("TransLimit" + transLimit);
//						perTransLimit = pertransaction.longValue();
//						 = perTransLimit.equals(0L) ? Long.valueOf(perTransaction) : perTransLimit;
							if (commonExternalService.enterprise_sole_proprietorship(getCurrentUserDetails)
									|| commonExternalService
											.enterprise_zero_external_user_authorize(getCurrentUserDetails)) {
								
								
								
								LocalDateTime local=LocalDateTime.now(ZoneId.systemDefault());
								Timestamp current=Timestamp.valueOf(local);
								Timestamp past=Timestamp.valueOf(local.minusMinutes(10));
								
								boolean exist=impsTransferRepository.existsByAmountAndBenAccNoAndRemAccNumAndEnterpriseIdAndCreatedAtBetween(fundTransferStoreResp.get().getAmount(),
										fundTransferStoreResp.get().getBenAccNo(),fundTransferStoreResp.get().getRemAccNo(),fundTransferStoreResp.get().getEnterpriseId(),
										past,current);
							
								boolean mmidExist=impsTransferRepository.existsByAmountAndBenMobNumAndRemAccNumAndEnterpriseIdAndCreatedAtBetween(fundTransferStoreResp.get().getAmount(),
										fundTransferStoreResp.get().getBenMobNum(),fundTransferStoreResp.get().getRemAccNo(),
										fundTransferStoreResp.get().getEnterpriseId(),past,current);
							
								RecordLog.writeLogFile("record exist in time duration fund prefcorp "+enterprises.get().getPrefCorp()+" p2a "+exist+" p2p "+mmidExist);

								
								if(!exist && !mmidExist) {
								
								validDailyTransaction = fundTransaferTransactionInternalService
										.validDailyTransaction(fundTransferStoreResp.get().getAmount(), fundTransferStoreResp.get().getEnterpriseId());
								validMonthlyTransaction = fundTransaferTransactionInternalService
										.validMonthlyTransaction(fundTransferStoreResp.get().getAmount(), fundTransferStoreResp.get().getEnterpriseId());
								
								
								if (validDailyTransaction && validMonthlyTransaction) {
									if (fundTransferStoreResp.get().getTransferType().equals("QuickPay")) {
//										isValidQuickPay = fundTransaferTransactionInternalService
//												.validQuickPay(fundTransferStoreResp.get().getAmount(), enterpriseId);
//										if (isValidQuickPay) {
											fundTransferInternalModel = createIMPSTransfer(fundTransferStoreResp,
													enterprises);
//										} else {
//											deleteFTStoreId(fundTransferStoreResp);
//											fundTransferInternalModel = new FundTransferInternalModel();
//											fundTransferInternalModel.setStatus(false);
//											fundTransferInternalModel.setMessage("failed");
//											fundTransferInternalModel.setDescription(
//													"Amount exceeds the quick pay limit of Rs. " + quickpayLimit);
//											fundTransferInternalModel.setFundTransfer(null);
//											return fundTransferInternalModel;
//										}
									} else {
										fundTransferInternalModel = createIMPSTransfer(fundTransferStoreResp,
												enterprises);
									}
//								} else {
//									fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
//									fundTransferInternalModel = new FundTransferInternalModel();
//									fundTransferInternalModel.setStatus(false);
//									fundTransferInternalModel.setMessage("failed");
//									fundTransferInternalModel.setDescription(
//											"Amount exceeds the per transaction limit of Rs. " + transLimit);
//									fundTransferInternalModel.setFundTransfer(null);
//									return fundTransferInternalModel;
//								}
								if (fundTransferInternalModel.isStatus() == true) {
									enterprises = enterprisesRepository
											.findById(Long.valueOf(getCurrentUserDetails.get().getEnterpriseId()));
									if (enterprises.isPresent()) {
										ApplicationEnterpris applicationEnterpris=applicationEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterprises.get().getApplicationFormId(),enterprises.get().getPrefCorp());
										String authExt =applicationEnterpris.getAuthFund();
										if (authExt == null) {
											authExt = "0";
										}
										/* TODO check with vikas */
										makercheckerServiceImpsTransfer.maker(getCurrentUserDetails, "IMPSTransfer",
												fundTransferInternalModel.getImpsTransfer(), Integer.valueOf(authExt));
										makercheckerServiceImpsTransfer.checker(getCurrentUserDetails,
												fundTransferInternalModel.getImpsTransfer());

										mode = fundTransferInternalModel.getImpsTransfer().getMode();
										if (mode.equals("p2p")) {
											remarks = StringUtils
													.isEmpty(fundTransferInternalModel.getImpsTransfer().getRemarks())
															? "UPIREFFS"
															: fundTransferInternalModel.getImpsTransfer().getRemarks();

											senderRefId = StringUtils.isEmpty(
													fundTransferInternalModel.getImpsTransfer().getSenderRefId())
															? generateSenderRefNo("imps")
															: fundTransferInternalModel.getImpsTransfer()
																	.getSenderRefId();

//											isDuplicate = checkForDuplicateIMPS(mode,
//													String.valueOf(enterprises.get().getId()), "pending",
//													fundTransferInternalModel.getImpsTransfer());
//
//											if (isDuplicate == true) {
//												impsTransferRepository.deleteById(
//														fundTransferInternalModel.getImpsTransfer().getId());
//												fundTransferInternalModel = new FundTransferInternalModel();
//												fundTransferInternalModel.setStatus(false);
//												fundTransferInternalModel.setMessage("failed");
//												fundTransferInternalModel.setDescription(
//														"This is a duplicate imps transfer transaction. Please try after 5 minutes");
//												fundTransferInternalModel.setImpsTransfer(null);
//												return fundTransferInternalModel;
//											} else {
//										P2PTransferReqModel p2pTransferReqModel = createP2PTransferReq(
//												fundTransferStoreResp);
												ImpsTransfer impsTransfer = fundTransferInternalModel.getImpsTransfer();
												HashMap<String, String> reqdata = new HashMap<String, String>();
//												reqdata.put("mbRefNo", impsTransfer.getRefNo());
												reqdata.put("mbRefNo", senderRefId);
												reqdata.put("amt", impsTransfer.getAmount());
												reqdata.put("fromAcc", impsTransfer.getRemAccNum());
												reqdata.put("remarks", impsTransfer.getRemarks());
												reqdata.put("benAccType",
														fundTransferStoreResp.get().getBenAccType() != null
																? fundTransferStoreResp.get().getBenAccType()
																: "10");
												reqdata.put("remName", getTrimmedValue(impsTransfer.getRemName()));
												reqdata.put("mobileNumber", impsTransfer.getRemMobNum());
												reqdata.put("remmmid", impsTransfer.getRemMmid());
												reqdata.put("customerId", impsTransfer.getRemCustId());
												reqdata.put("benIfsc", impsTransfer.getBenIfsc());
												reqdata.put("benAcc", impsTransfer.getBenAccNo());
												reqdata.put("benMmid", impsTransfer.getBenMmid());
												reqdata.put("benMobNumber", impsTransfer.getBenMobNum());
												reqdata.put("mode", "p2p");
												JSONObject reqObject = new JSONObject(reqdata);
												logger.info("P2P Req String : " + reqObject.toString());
												try {
												HttpHeaders headers = new HttpHeaders();
												headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
												HttpEntity<String> entity = new HttpEntity<String>(reqObject.toString(),
														headers);
												ResponseEntity<?> ftResponse = restTemplate.postForEntity(
														fblgatewayurl + "/gateway/fundtransfer/imps_p2p", entity,
														String.class);
												RecordLog.writeLogFile("Gateway Response 1672  fund internal service " + ftResponse);

												if (ftResponse.getStatusCodeValue() == 200) {
													if (ftResponse.hasBody()) {
														JSONObject jsonObject = new JSONObject(
																ftResponse.getBody().toString());
														if (jsonObject.get("recordId") instanceof JSONObject) {
															auditLogService.IMPSlog(
																	fundTransferInternalModel.getImpsTransfer(), "p2p",
																	jsonObject.getJSONObject("recordId")
																			.getString("responseCode"),
																	enterprises);
//															fundTransferLogsEntryService.transactionLog(
//																	fundTransferInternalModel.getImpsTransfer(),
//																	jsonObject.getJSONObject("recordId")
//																			.getString("responseCode"),
//																	getCurrentUserDetails.get().getPrefNo(),
//																	enterprises.get().getPrefCorp());
															if (jsonObject.getBoolean("status")) {
																
																//simBindingService.sendSMS(properties.getFund_transfer_transaction_msg(), getCurrentUserDetails.get().getMobile());

																
																fundTransferInternalModel.getImpsTransfer()
																		.setStatus("approved");
																fundTransferInternalModel.getImpsTransfer()
																.setProgress("completed");
																
																fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getImpsTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getImpsTransfer().setResponseReason(jsonObject.getString("message"));
														
																ImpsTransfer it = impsTransferRepository.save(
																		fundTransferInternalModel.getImpsTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(true);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel
																		.setMessage(jsonObject.getString("message"));
																fundTransferInternalModel.setImpsTransfer(it);
																return fundTransferInternalModel;
															} else {
																/**
																 * Do we need to delete FundTransfer?
																 */
																fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getImpsTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getImpsTransfer().setResponseReason(jsonObject.getString("message"));
																fundTransferInternalModel.getImpsTransfer()
																.setStatus("approved");
																ImpsTransfer it = impsTransferRepository.save(
																		fundTransferInternalModel.getImpsTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(false);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel
																		.setMessage(jsonObject.getString("message"));
																fundTransferInternalModel.setImpsTransfer(null);
																return fundTransferInternalModel;
															}
														} else {
															fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
															fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
															fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
															fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
															fundTransferInternalModel.getImpsTransfer().setResponseReason(jsonObject.getString("message"));
															fundTransferInternalModel.getImpsTransfer()
															.setStatus("failed");
															ImpsTransfer it = impsTransferRepository.save(
																	fundTransferInternalModel.getImpsTransfer());
															fundTransferInternalModel = deleteFTStoreId(
																	fundTransferStoreResp);
															fundTransferInternalModel = new FundTransferInternalModel();
															fundTransferInternalModel.setStatus(false);
															fundTransferInternalModel
																	.setDescription("Gateway Response");
															fundTransferInternalModel
																	.setMessage(jsonObject.getString("message"));
															fundTransferInternalModel.setImpsTransfer(null);
															return fundTransferInternalModel;
														}
													} else {
														fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
														fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
														fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
														fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
														fundTransferInternalModel.getImpsTransfer().setResponseReason(ftResponse.toString());
														fundTransferInternalModel.getImpsTransfer()
														.setStatus("failed");
														ImpsTransfer it = impsTransferRepository.save(
																fundTransferInternalModel.getImpsTransfer());
														fundTransferInternalModel = new FundTransferInternalModel();
														fundTransferInternalModel.setStatus(false);
														fundTransferInternalModel.setDescription("Gateway Response");
														fundTransferInternalModel
																.setMessage("no response from gateway");
														fundTransferInternalModel.setImpsTransfer(null);
														return fundTransferInternalModel;
													}
												} else {
													fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
													fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
													fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
													fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
													fundTransferInternalModel.getImpsTransfer().setResponseReason(ftResponse.toString());
													fundTransferInternalModel.getImpsTransfer()
													.setStatus("failed");
													ImpsTransfer it = impsTransferRepository.save(
															fundTransferInternalModel.getImpsTransfer());
													fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
													fundTransferInternalModel = new FundTransferInternalModel();
													fundTransferInternalModel.setStatus(false);
													fundTransferInternalModel.setMessage("failed");
													fundTransferInternalModel.setDescription(ftResponse.toString());
													fundTransferInternalModel.setImpsTransfer(null);
													return fundTransferInternalModel;
												}
											}catch(Exception e) {
												fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
												fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
												fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
												fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
												fundTransferInternalModel.getImpsTransfer().setResponseReason("Exception During FundTransfer");
												
												fundTransferInternalModel.getImpsTransfer()
												.setStatus("approved");
												fundTransferInternalModel.getImpsTransfer()
												.setProgress("completed");
										  impsTransferRepository.save(
												fundTransferInternalModel.getImpsTransfer());
										 fundTransferInternalModel = new FundTransferInternalModel();
											fundTransferInternalModel.setStatus(false);
											fundTransferInternalModel.setMessage("Exception During FundTransfer");
											fundTransferInternalModel.setDescription("Exception During FundTransfer");
											fundTransferInternalModel.setImpsTransfer(null);
											RecordLog.writeLogFile("EXOCfundinternal 1765: for mobile  "+performTransactionReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
											return fundTransferInternalModel;
											}
										//	}
										} else if (mode.equals("p2a")) {
											senderRefId = StringUtils.isEmpty(
													fundTransferInternalModel.getImpsTransfer().getSenderRefId())
															? generateSenderRefNo("imps")
															: fundTransferInternalModel.getImpsTransfer()
																	.getSenderRefId();

											isDuplicate = checkForDuplicateIMPS(mode,
													String.valueOf(enterprises.get().getId()), "pending",
													fundTransferInternalModel.getImpsTransfer());

											if (isDuplicate == true) {
												impsTransferRepository.deleteById(
														fundTransferInternalModel.getImpsTransfer().getId());
												fundTransferInternalModel = new FundTransferInternalModel();
												fundTransferInternalModel.setStatus(false);
												fundTransferInternalModel.setMessage("failed");
												fundTransferInternalModel.setDescription(
														"This is a duplicate imps transfer transaction. Please try after 5 minutes");
												fundTransferInternalModel.setImpsTransfer(null);
												return fundTransferInternalModel;
											} else {
												String p2aTransferReqModel = createP2ATransferReq(
														fundTransferInternalModel.getImpsTransfer(),
														fundTransferStoreResp);
//											HashMap<String, String> reqdata = new HashMap<String, String>();
//											reqdata.put("mbRefNo", generateSenderRefNo("imps"));
//											reqdata.put("amt", fundTransferStoreResp.get().getAmount());
//											reqdata.put("fromAcc", fundTransferStoreResp.get().getRemAccNo());
//											reqdata.put("remarks", fundTransferStoreResp.get().getRemarks());
//											reqdata.put("benAccType",
//													fundTransferStoreResp.get().getBenAccType() != null
//															? fundTransferStoreResp.get().getBenAccType()
//															: "10");
//											reqdata.put("remName", fundTransferStoreResp.get().getRemName());
//											reqdata.put("mobileNumber", fundTransferStoreResp.get().getRemMobNum());
//											// reqdata.put("remmmid", fundTransferStoreResp.get().getRemAccNo());
//											reqdata.put("customerId", fundTransferStoreResp.get().getRemCustId());
//											reqdata.put("benIfsc", fundTransferStoreResp.get().getBenIfsc());
//											reqdata.put("benAcc", fundTransferStoreResp.get().getBenAccNo());
//											reqdata.put("mode", "p2a");
//											reqdata.put("benMmid", fundTransferStoreResp.get().getBenMmid());
//
												
												try {
//											JSONObject reqObject = new JSONObject(reqdata);
												logger.info("P2A Req String : " + p2aTransferReqModel.toString());
												HttpHeaders headers = new HttpHeaders();
												headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
												HttpEntity<String> entity = new HttpEntity<String>(p2aTransferReqModel,
														headers);
												ResponseEntity<?> ftResponse = restTemplate.postForEntity(
														fblgatewayurl + "/gateway/fundtransfer/imps_p2a", entity,
														String.class);
												RecordLog.writeLogFile("Gateway Response 1821  fund internal service " + ftResponse);

												if (ftResponse.getStatusCodeValue() == 200) {
													if (ftResponse.hasBody()) {
														JSONObject jsonObject = new JSONObject(
																ftResponse.getBody().toString());
														if (jsonObject.get("recordId") instanceof JSONObject) {
															auditLogService.IMPSlog(
																	fundTransferInternalModel.getImpsTransfer(), "p2a",
																	jsonObject.getJSONObject("recordId")
																			.getString("responseCode"),
																	enterprises);
//															fundTransferLogsEntryService.transactionLog(
//																	fundTransferInternalModel.getImpsTransfer(),
//																	jsonObject.getJSONObject("recordId")
//																			.getString("responseCode"),
//																	getCurrentUserDetails.get().getPrefNo(),
//																	enterprises.get().getPrefCorp());
															if (jsonObject.getBoolean("status")) {
																
																
																//simBindingService.sendSMS(properties.getFund_transfer_transaction_msg(), getCurrentUserDetails.get().getMobile());

																
																fundTransferInternalModel.getImpsTransfer()
																		.setStatus("approved");
																fundTransferInternalModel.getImpsTransfer()
																.setProgress("comleted");
																fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getImpsTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getImpsTransfer().setResponseReason(jsonObject.getString("message"));
																ImpsTransfer it = impsTransferRepository.save(
																		fundTransferInternalModel.getImpsTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(true);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel
																		.setMessage(jsonObject.getString("message"));
																fundTransferInternalModel.setImpsTransfer(it);
																return fundTransferInternalModel;
															} else {
																/**
																 * Do we need to delete FundTransfer?
																 */
																fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
																fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
																fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
																fundTransferInternalModel.getImpsTransfer().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
																fundTransferInternalModel.getImpsTransfer().setResponseReason(jsonObject.getString("message"));
																fundTransferInternalModel.getImpsTransfer()
																.setStatus("approved");
																ImpsTransfer it = impsTransferRepository.save(
																		fundTransferInternalModel.getImpsTransfer());
																fundTransferInternalModel = deleteFTStoreId(
																		fundTransferStoreResp);
																fundTransferInternalModel = new FundTransferInternalModel();
																fundTransferInternalModel.setStatus(false);
																fundTransferInternalModel
																		.setDescription("Gateway Response");
																fundTransferInternalModel
																		.setMessage(jsonObject.getString("message"));
																fundTransferInternalModel.setImpsTransfer(null);
																return fundTransferInternalModel;
															}
														} else {
															fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
															fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
															fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
															fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
															fundTransferInternalModel.getImpsTransfer().setResponseReason(jsonObject.getString("message"));
															fundTransferInternalModel.getImpsTransfer()
															.setStatus("failed");
															ImpsTransfer it = impsTransferRepository.save(
																	fundTransferInternalModel.getImpsTransfer());
															fundTransferInternalModel = deleteFTStoreId(
																	fundTransferStoreResp);
															fundTransferInternalModel = new FundTransferInternalModel();
															fundTransferInternalModel.setStatus(false);
															fundTransferInternalModel
																	.setDescription("Gateway Response");
															fundTransferInternalModel
																	.setMessage(jsonObject.getString("message"));
															fundTransferInternalModel.setImpsTransfer(null);
															return fundTransferInternalModel;
														}
													} else {
														fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
														fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
														fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
														fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
														fundTransferInternalModel.getImpsTransfer().setResponseReason(ftResponse.toString());
														fundTransferInternalModel.getImpsTransfer()
														.setStatus("failed");
														ImpsTransfer it = impsTransferRepository.save(
																fundTransferInternalModel.getImpsTransfer());
														fundTransferInternalModel = new FundTransferInternalModel();
														fundTransferInternalModel.setStatus(false);
														fundTransferInternalModel.setDescription("Gateway Response");
														fundTransferInternalModel
																.setMessage("no response from gateway");
														fundTransferInternalModel.setImpsTransfer(null);
														return fundTransferInternalModel;
													}
												} else {
													fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
													fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
													fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
													fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
													fundTransferInternalModel.getImpsTransfer().setResponseReason(ftResponse.toString());
													fundTransferInternalModel.getImpsTransfer()
													.setStatus("failed");
													ImpsTransfer it = impsTransferRepository.save(
															fundTransferInternalModel.getImpsTransfer());
													fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
													fundTransferInternalModel = new FundTransferInternalModel();
													fundTransferInternalModel.setStatus(false);
													fundTransferInternalModel.setMessage("failed");
													fundTransferInternalModel.setDescription(ftResponse.toString());
													fundTransferInternalModel.setImpsTransfer(null);
													return fundTransferInternalModel;
												}
											}catch(Exception e) {
												RecordLog.writeLogFile("EXOCfundinternal 1911: for mobile "+performTransactionReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
												fundTransferInternalModel.getImpsTransfer().setIpAddress(performTransactionReqModel.getIpAddress());
												fundTransferInternalModel.getImpsTransfer().setGeoLocation(performTransactionReqModel.getGeoLocation());
												fundTransferInternalModel.getImpsTransfer().setChannelFlag(performTransactionReqModel.getChannelFlag());
												fundTransferInternalModel.getImpsTransfer().setResponseCode("500");
												fundTransferInternalModel.getImpsTransfer().setResponseReason("Exception During FundTransfer");
												fundTransferInternalModel.getImpsTransfer()
												.setStatus("approved");
												fundTransferInternalModel.getImpsTransfer()
												.setProgress("comleted");
										 impsTransferRepository.save(
												fundTransferInternalModel.getImpsTransfer());
										 fundTransferInternalModel = new FundTransferInternalModel();
											fundTransferInternalModel.setStatus(false);
											fundTransferInternalModel.setMessage("Exception During FundTransfer");
											fundTransferInternalModel.setDescription("Exception During FundTransfer");
											fundTransferInternalModel.setImpsTransfer(null);
											return fundTransferInternalModel;
											}
											}
										}
									} else {
										fundTransferInternalModel = new FundTransferInternalModel();
										fundTransferInternalModel.setStatus(false);
										fundTransferInternalModel.setMessage("failed");
										fundTransferInternalModel.setDescription("enterprises record not found");
										fundTransferInternalModel.setImpsTransfer(null);
									}
								}
								
								
								
							}else if (!validDailyTransaction) {
								Double totalLimitDaily;
								Optional<TransactionLimit> transactionDailyLimit = null;
								Long daily_amount_transacted = fundTransaferTransactionInternalService
										.approvedAmount(fundTransferStoreResp.get().getEnterpriseId());
								transactionDailyLimit = transactionLimitRepository
										.findFirstByEnterpriseIdAndModeOrderByIdDesc(fundTransferStoreResp.get().getEnterpriseId(), "daily");
								if (transactionDailyLimit.isPresent()) {
									totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0")
											? Double.parseDouble(transactionDaily)
											: Double.parseDouble(transactionDailyLimit.get().getAmount());
								} else {
									totalLimitDaily = Double.parseDouble(transactionDaily);
								}
								fundTransferInternalModel = new FundTransferInternalModel();
								fundTransferInternalModel.setStatus(false);
								fundTransferInternalModel.setMessage(
										"Transaction amount exceeds the Daily Transaction limit. You have already transacted for Rs."
												+ String.valueOf(daily_amount_transacted) + " amount and the limit is Rs."
												+ String.valueOf(totalLimitDaily));
								fundTransferInternalModel.setFundTransfer(null);
								return fundTransferInternalModel;
							} else if (!validMonthlyTransaction) {
								Double totalLimitMonthly;
								Optional<TransactionLimit> transactionMonthlyLimit = null;
								Long monthly_amount_transacted = fundTransaferTransactionInternalService
										.totalAmountMonth(fundTransferStoreResp.get().getEnterpriseId());
								transactionMonthlyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(
										fundTransferStoreResp.get().getEnterpriseId(), "monthly");
								if (transactionMonthlyLimit.isPresent()) {
									totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
											? Double.parseDouble(transactionMonthly)
											: Double.parseDouble(transactionMonthlyLimit.get().getAmount());
								} else {
									totalLimitMonthly = Double.parseDouble(transactionMonthly);
								}
								fundTransferInternalModel = new FundTransferInternalModel();
							
								fundTransferInternalModel.setStatus(false);
								fundTransferInternalModel.setMessage(
										"Transaction amount exceeds the Monthly Transaction limit. You have already transacted for Rs."
												+ String.valueOf(monthly_amount_transacted) + " amount and the limit is Rs."
												+ String.valueOf(totalLimitMonthly));
								fundTransferInternalModel.setFundTransfer(null);
								return fundTransferInternalModel;
							}								
							
							
								
								}else {
									fundTransferStoreRepository.deleteById(fundTransferStoreResp.get().getId());
									fundTransferInternalModel = new FundTransferInternalModel();
									
									fundTransferInternalModel.setStatus(false);
									fundTransferInternalModel.setMessage("Similar transactions not allowed within 10 minutes");
									fundTransferInternalModel.setDescription("Similar transactions not allowed within 10 minutes");
									fundTransferInternalModel.setFundTransfer(null);
									return fundTransferInternalModel;
								}
								
								
							
							} else {
								if (Double.parseDouble(fundTransferStoreResp.get().getAmount()) <= transLimit) {
									if (fundTransferStoreResp.get().getTransferType().equals("QuickPay")) {
//										isValidQuickPay = fundTransaferTransactionInternalService
//												.validQuickPay(fundTransferStoreResp.get().getAmount(), enterpriseId);
//										if (isValidQuickPay) {
											fundTransferInternalModel = createIMPSTransfer(fundTransferStoreResp,
													enterprises);
//										} else {
//											deleteFTStoreId(fundTransferStoreResp);
//											fundTransferInternalModel = new FundTransferInternalModel();
//											fundTransferInternalModel.setStatus(false);
//											fundTransferInternalModel.setMessage("failed");
//											fundTransferInternalModel.setDescription(
//													"Amount exceeds the quick pay limit of Rs. " + quickpayLimit);
//											fundTransferInternalModel.setImpsTransfer(null);
//											return fundTransferInternalModel;
//										}
									} else {
										fundTransferInternalModel = createIMPSTransfer(fundTransferStoreResp,
												enterprises);
									}
								} else {
									fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
									fundTransferInternalModel = new FundTransferInternalModel();
									fundTransferInternalModel.setStatus(false);
									fundTransferInternalModel.setMessage("failed");
									fundTransferInternalModel.setDescription(
											"Amount exceeds the per transaction limit of Rs. " + transLimit);
									fundTransferInternalModel.setImpsTransfer(null);
									return fundTransferInternalModel;
								}
								if (fundTransferInternalModel.isStatus() == true) {
									enterprises = enterprisesRepository
											.findById(Long.valueOf(getCurrentUserDetails.get().getEnterpriseId()));
									if (enterprises.isPresent()) {
										ApplicationEnterpris applicationEnterpris=applicationEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterprises.get().getApplicationFormId(),enterprises.get().getPrefCorp());
										String authExt =applicationEnterpris.getAuthFund();
										if (authExt == null) {
											authExt = "0";
										}
										makercheckerServiceImpsTransfer.maker(getCurrentUserDetails, "IMPSTransfer",
												fundTransferInternalModel.getImpsTransfer(), Integer.valueOf(authExt));
//										fundTransferLogsEntryService.initiateLog(
//												fundTransferInternalModel.getImpsTransfer(),
//												getCurrentUserDetails.get().getPrefNo(),
//												enterprises.get().getPrefCorp());

										fundTransferInternalModel = deleteFTStoreId(fundTransferStoreResp);
										fundTransferInternalModel.setStatus(true);
										fundTransferInternalModel.setMessage("success");
										fundTransferInternalModel.setDescription("Initiated");
										return fundTransferInternalModel;
									} else {
										fundTransferInternalModel = new FundTransferInternalModel();
										fundTransferInternalModel.setStatus(false);
										fundTransferInternalModel.setMessage("failed");
										fundTransferInternalModel.setDescription("enterprises record not found");
										fundTransferInternalModel.setImpsTransfer(null);
										return fundTransferInternalModel;
									}
								}
							}
						}
//					} else if (!validDailyTransaction) {
//						Double totalLimitDaily;
//						Optional<TransactionLimit> transactionDailyLimit = null;
//						Long daily_amount_transacted = fundTransaferTransactionInternalService
//								.approvedAmount(enterpriseId);
//						transactionDailyLimit = transactionLimitRepository
//								.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId, "daily");
//						if (transactionDailyLimit.isPresent()) {
//							totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0")
//									? Double.parseDouble(transactionDaily)
//									: Double.parseDouble(transactionDailyLimit.get().getAmount());
//						} else {
//							totalLimitDaily = Double.parseDouble(transactionDaily);
//						}
//						fundTransferInternalModel = new FundTransferInternalModel();
//						fundTransferInternalModel.setStatus(false);
//						fundTransferInternalModel.setMessage("failed");
//						fundTransferInternalModel.setDescription(
//								"Transaction amount exceeds the Daily Transaction limit. You have already transacted for Rs."
//										+ String.valueOf(daily_amount_transacted) + " amount and the limit is Rs."
//										+ String.valueOf(totalLimitDaily));
//						fundTransferInternalModel.setImpsTransfer(null);
//						return fundTransferInternalModel;
//					} else if (!validMonthlyTransaction) {
//						Double totalLimitMonthly;
//						Optional<TransactionLimit> transactionMonthlyLimit = null;
//						Long monthly_amount_transacted = fundTransaferTransactionInternalService
//								.totalAmountMonth(enterpriseId);
//						transactionMonthlyLimit = transactionLimitRepository
//								.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId, "monthly");
//						if (transactionMonthlyLimit.isPresent()) {
//							totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
//									? Double.parseDouble(transactionMonthly)
//									: Double.parseDouble(transactionMonthlyLimit.get().getAmount());
//						} else {
//							totalLimitMonthly = Double.parseDouble(transactionMonthly);
//						}
//						fundTransferInternalModel = new FundTransferInternalModel();
//						fundTransferInternalModel.setStatus(false);
//						fundTransferInternalModel.setMessage("failed");
//						fundTransferInternalModel.setDescription(
//								"Transaction amount exceeds the Monthly Transaction limit. You have already transacted for Rs."
//										+ String.valueOf(monthly_amount_transacted) + " amount and the limit is Rs."
//										+ String.valueOf(totalLimitMonthly));
//						fundTransferInternalModel.setImpsTransfer(null);
//						return fundTransferInternalModel;
//					}
				} else {
					fundTransferInternalModel = new FundTransferInternalModel();
					fundTransferInternalModel.setStatus(false);
					fundTransferInternalModel.setMessage("failed");
					fundTransferInternalModel
							.setDescription("Invalid Amount, Please check amount decimal is not more than 2");
					fundTransferInternalModel.setImpsTransfer(null);
					return fundTransferInternalModel;
				}
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed");
				fundTransferInternalModel.setDescription("enterprises record not found");
				fundTransferInternalModel.setImpsTransfer(null);
				return fundTransferInternalModel;
			}
			return fundTransferInternalModel;
		} catch (Exception e) {
			RecordLog.writeLogFile("EXOCfundinternal 2066: for mobile "+performTransactionReqModel.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			FundTransferInternalModel fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("Failed");
			fundTransferInternalModel.setImpsTransfer(null);
			fundTransferInternalModel.setDescription(e.getMessage());
			return fundTransferInternalModel;
		}
	}

	private P2PTransferReqModel createP2PTransferReq(Optional<FundTransferStore> fundTransferStoreResp) {
		P2PTransferReqModel p2pTransferReqModel = new P2PTransferReqModel();
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		P2PTransferReqModel.RemmiterDetails remmiterDetails = p2pTransferReqModel.new RemmiterDetails();
		remmiterDetails.setAccNumber(fundTransferStoreResp.get().getRemAccNo());
		remmiterDetails.setAcctype("");
		remmiterDetails.setEmail("");
		remmiterDetails.setMMID(fundTransferStoreResp.get().getRemMmid());
		remmiterDetails.setMobile(fundTransferStoreResp.get().getRemMobNum());
		remmiterDetails.setNotification_Flag("NONE");

		P2PTransferReqModel.BeneficiaryDetails beneficiaryDetails = p2pTransferReqModel.new BeneficiaryDetails();
		beneficiaryDetails.setAccNumber(fundTransferStoreResp.get().getBenAccNo());
		beneficiaryDetails.setEmail("");
		beneficiaryDetails.setMMID(fundTransferStoreResp.get().getBenMmid());
		beneficiaryDetails.setMobile(fundTransferStoreResp.get().getBenMobNum());
		beneficiaryDetails.setName(fundTransferStoreResp.get().getBenName());
		beneficiaryDetails.setNotification_Flag("NONE");

		p2pTransferReqModel.setRespUrl(callbackURL);
		p2pTransferReqModel.setUserid(userId);
		p2pTransferReqModel.setPassword(password);
		p2pTransferReqModel.setSendercd(senderCD);
		p2pTransferReqModel.setTranDate(input.format(customDateFormatter));
		p2pTransferReqModel.setReferenceId(generateSenderRefNo("imps"));
		p2pTransferReqModel.setCust_Ref_No(fundTransferStoreResp.get().getTransactionId());
		p2pTransferReqModel.setAmount(fundTransferStoreResp.get().getAmount());
		p2pTransferReqModel.setRemarks(fundTransferStoreResp.get().getRemarks());
		p2pTransferReqModel.setSender_Data("P2P Perform");
		p2pTransferReqModel.setBeneficiaryDetails(beneficiaryDetails);
		p2pTransferReqModel.setRemmiterDetails(remmiterDetails);
		return p2pTransferReqModel;
	}

	private String createP2ATransferReq(ImpsTransfer impsTransfer, Optional<FundTransferStore> fundTransferStoreResp) {
//		P2ATransferReqModel p2aTransferReqModel = new P2ATransferReqModel();
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		P2ATransferReqModel.RemmiterDetails remmiterDetails = p2aTransferReqModel.new RemmiterDetails();
//		remmiterDetails.setAccNumber(fundTransferStoreResp.get().getRemAccNo());
//		remmiterDetails.setAcctype("");
//		remmiterDetails.setEmail("");
//		remmiterDetails.setMobile(fundTransferStoreResp.get().getBenMobNum());
//		remmiterDetails.setNotification_Flag("NONE");
//
//		P2ATransferReqModel.BeneficiaryDetails beneficiaryDetails = p2aTransferReqModel.new BeneficiaryDetails();
//		beneficiaryDetails.setAccNumber(fundTransferStoreResp.get().getBenAccNo());
//		beneficiaryDetails.setEmail("");
//		beneficiaryDetails.setIFSC(fundTransferStoreResp.get().getBenIfsc());
//		beneficiaryDetails.setMobile(fundTransferStoreResp.get().getBenMobNum());
//		beneficiaryDetails.setName(fundTransferStoreResp.get().getBenName());
//		beneficiaryDetails.setNotification_Flag("NONE");
//
//		p2aTransferReqModel.setRespUrl(callbackURL);
//		p2aTransferReqModel.setUserid(userId);
//		p2aTransferReqModel.setPassword(password);
//		p2aTransferReqModel.setSendercd(senderCD);
//		p2aTransferReqModel.setTranDate(input.format(customDateFormatter));
//		p2aTransferReqModel.setReferenceId(generateSenderRefNo("imps"));
//		p2aTransferReqModel.setCust_Ref_No(fundTransferStoreResp.get().getTransactionId());
//		p2aTransferReqModel.setAmount(fundTransferStoreResp.get().getAmount());
//		p2aTransferReqModel.setRemarks(fundTransferStoreResp.get().getRemarks());
//		p2aTransferReqModel.setSender_Data("Fund Transfer to Hatio");
//		p2aTransferReqModel.setBeneficiaryDetails(beneficiaryDetails);
//		p2aTransferReqModel.setRemmiterDetails(remmiterDetails);
//		return p2aTransferReqModel;

		RecordLog.writeLogFile("CallBack URL >>>" + callbackURL);
		
		String remMobile=impsTransfer.getRemMobNum()== null ? "": impsTransfer.getRemMobNum();
        String benMobile=impsTransfer.getBenMobNum()== null ? "": impsTransfer.getBenMobNum();
		

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		sb.append("\"respUrl\":\"" + callbackURL + "/imps" + "\",");

		sb.append("\"userid\":\"" + userId + "\",");

		sb.append("\"password\":\"" + password + "\",");

		sb.append("\"sendercd\":\"" + senderCD + "\",");

		sb.append("\"tranDate\":\"" + input.format(customDateFormatter) + "\",");

		sb.append("\"ReferenceId\":\"" + impsTransfer.getRefNo() + "\",");

		sb.append("\"Cust_Ref_No\":\"" + impsTransfer.getSenderRefId() + "\",");

		sb.append("\"RemmiterDetails\":{");

		sb.append("\"Name\":\"" + getTrimmedValue(impsTransfer.getRemName()) + "\",");

		sb.append("\"AccNumber\":\"" + impsTransfer.getRemAccNum() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"Mobile\":\"" + remMobile + "\",");

		sb.append("\"Email\":\"\",");

//		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"SMS\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue(impsTransfer.getBenNickName()) + "\",");

		sb.append("\"AccNumber\":\"" + impsTransfer.getBenAccNo() + "\",");

//		sb.append("\"Acctype\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getBenAccType()) != null ? ""
//				: fundTransferStoreResp.get().getBenAccType() + "\",");

//		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"IFSC\":\"" + impsTransfer.getBenIfsc() + "\",");

		sb.append("\"Mobile\":\"" + benMobile + "\",");

		sb.append("\"Email\":\"\",");

//		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"Amount\":\"" + impsTransfer.getAmount() + "\",");

		sb.append("\"Remarks\":\"" + impsTransfer.getRemarks() + "\",");

//		sb.append("\"Alternative_Payments\":\"N\",");

		sb.append("\"Sender_Data\":\"SMEMB\"");

		sb.append("}");
		return sb.toString();

	}

	private String createP2ATransferApprovalReq(Optional<ImpsTransfer> impsTransfer, String senderRefId) {
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		RecordLog.writeLogFile("CallBack URL >>>" + callbackURL);
		
		
		 String remMobile=impsTransfer.get().getRemMobNum() == null ? "": impsTransfer.get().getRemMobNum();
	        String benMobile=impsTransfer.get().getBenMobNum() == null ? "": impsTransfer.get().getBenMobNum();
		

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		sb.append("\"respUrl\":\"" + callbackURL + "/imps" + "\",");

		sb.append("\"userid\":\"" + userId + "\",");

		sb.append("\"password\":\"" + password + "\",");

		sb.append("\"sendercd\":\"" + senderCD + "\",");

		sb.append("\"tranDate\":\"" + input.format(customDateFormatter) + "\",");

		sb.append("\"ReferenceId\":\"" + impsTransfer.get().getRefNo() + "\",");

		sb.append("\"Cust_Ref_No\":\"" + senderRefId + "\",");// fundTransferStoreResp.get().getTransactionId()

		sb.append("\"RemmiterDetails\":{");

		sb.append("\"Name\":\"" + getTrimmedValue(impsTransfer.get().getRemName()) + "\",");

		sb.append("\"AccNumber\":\"" + impsTransfer.get().getRemAccNum() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"Mobile\":\"" + remMobile + "\",");

//		sb.append("\"Mobile\":\"" + "" + "\",");

		sb.append("\"Email\":\"\",");

//		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"SMS\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue(impsTransfer.get().getBenNickName()) + "\",");

		sb.append("\"AccNumber\":\"" + impsTransfer.get().getBenAccNo() + "\",");

//		sb.append("\"Acctype\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getBenAccType()) != null ? ""
//				: fundTransferStoreResp.get().getBenAccType() + "\",");

//		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"IFSC\":\"" + impsTransfer.get().getBenIfsc() + "\",");

		sb.append("\"Mobile\":\"" +benMobile + "\",");

//		sb.append("\"Mobile\":\"" + "" + "\",");

		sb.append("\"Email\":\"\",");

//		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"Amount\":\"" + impsTransfer.get().getAmount() + "\",");

		sb.append("\"Remarks\":\"" + impsTransfer.get().getRemarks() + "\",");

//		sb.append("\"Alternative_Payments\":\"N\",");

		sb.append("\"Sender_Data\":\"SMEMB\"");

		sb.append("}");
		return sb.toString();
	}

	public FundTransferInternalModel createIMPSTransfer(Optional<FundTransferStore> fundTransferStoreResp,
			Optional<Enterprises> enterprises) {
		FundTransferInternalModel fundTransferInternalModel = null;
		ImpsTransfer impsTransfer = null;
		ImpsTransfer impsTransferResp = null;
		if (fundTransferStoreResp.isPresent() && enterprises.isPresent()) {
			String senderRefId=generateSenderRefNo("imps");
			
			impsTransfer = new ImpsTransfer();
			impsTransfer.setRefNo(senderRefId);
			impsTransfer.setSenderRefId(senderRefId);
			impsTransfer.setAmount(fundTransferStoreResp.get().getAmount());
			impsTransfer.setMode(fundTransferStoreResp.get().getMode());
			impsTransfer.setRemAccNum(fundTransferStoreResp.get().getRemAccNo());
			impsTransfer.setRemName(enterprises.get().getAccName());
			impsTransfer.setRemMmid(fundTransferStoreResp.get().getRemMmid());
			impsTransfer.setRemMobNum(fundTransferStoreResp.get().getRemMobNum());
			impsTransfer.setBenAccNo(fundTransferStoreResp.get().getBenAccNo());
			impsTransfer.setBenNickName(fundTransferStoreResp.get().getBenNickName());
			impsTransfer.setBenCustName(fundTransferStoreResp.get().getBenName());
			impsTransfer.setBenAadhar(fundTransferStoreResp.get().getBenAadhar());
			impsTransfer.setBenMmid(fundTransferStoreResp.get().getBenMmid());
			impsTransfer.setBenIfsc(fundTransferStoreResp.get().getBenIfsc());
			impsTransfer.setBenMobNum(fundTransferStoreResp.get().getBenMobNum());
			impsTransfer.setRemarks(fundTransferStoreResp.get().getRemarks());
			impsTransfer.setEnterpriseId(String.valueOf(enterprises.get().getId()));
			impsTransfer.setTransferType(fundTransferStoreResp.get().getTransferType());
			impsTransfer.setTransactionDate(new Timestamp(new Date().getTime()));
			impsTransfer.setCreatedAt(new Timestamp(new Date().getTime()));
			impsTransfer.setUpdatedAt(new Timestamp(new Date().getTime()));
			impsTransfer.setTransferType(fundTransferStoreResp.get().getTransferType());
			impsTransferResp = impsTransferRepository.save(impsTransfer);

			if (impsTransferResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setImpsTransfer(impsTransferResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setImpsTransfer(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("FT || Enterprise data not recieved");
			fundTransferInternalModel.setImpsTransfer(null);
		}
		return fundTransferInternalModel;
	}

	public boolean checkForDuplicateIMPS(String mode, String enterpriseId, String status, ImpsTransfer impsTransfer) {
		List<ImpsTransfer> getDuplicateImps = null;
		boolean isDuplicate = false;
		final ZonedDateTime input = ZonedDateTime.now();

		Timestamp fromdate = null;
		Timestamp todate = null;
		Date today = Date.from(input.minusMinutes(5).toInstant());
		Date endday = Date.from(input.toInstant());
		logger.info("from:" + today + ",todate:" + endday);
		fromdate = new Timestamp(today.getTime());
		todate = new Timestamp(endday.getTime());
		if (mode.equals("p2p") || mode.equals("p2a")) {
			if (mode.equals("p2p")) {
				getDuplicateImps = impsTransferRepository.findDuplicateIMPSRecordsLessThan5Min(mode, enterpriseId,
						status, fromdate, todate);
				for (int i = 0; i < getDuplicateImps.size(); i++) {
					if (impsTransfer.getAmount() == getDuplicateImps.get(i).getAmount()
							&& impsTransfer.getBenMmid() == getDuplicateImps.get(i).getBenMmid()
							&& impsTransfer.getBenMobNum() == getDuplicateImps.get(i).getBenMobNum()
							&& impsTransfer.getRemAccNum() == getDuplicateImps.get(i).getRemAccNum()) {

						impsTransfer.setDuplicate(true);
						impsTransfer.setStatus("duplicate");
						impsTransferRepository.save(impsTransfer);
						isDuplicate = true;
					} else {
						isDuplicate = false;
					}
				}
			} else {
				getDuplicateImps = impsTransferRepository.findDuplicateIMPSRecordsLessThan5Min(mode, enterpriseId,
						status, fromdate, todate);
				for (int i = 0; i < getDuplicateImps.size(); i++) {
					if (impsTransfer.getAmount() == getDuplicateImps.get(i).getAmount()
							&& impsTransfer.getRemAccNum() == getDuplicateImps.get(i).getRemAccNum()
							&& impsTransfer.getBenAccNo() == getDuplicateImps.get(i).getBenAccNo()
							&& impsTransfer.getBenIfsc() == getDuplicateImps.get(i).getBenIfsc()) {

						impsTransfer.setDuplicate(true);
						impsTransfer.setStatus("duplicate");
						impsTransferRepository.save(impsTransfer);
						isDuplicate = true;
					} else {
						isDuplicate = false;
					}
				}
			}
		}
		return isDuplicate;
	}

	public FundTransferInternalModel deleteFTStoreId(Optional<FundTransferStore> fundTransferStoreResp) {
//		RecordLog.writeLogFile("In delete FtStore ID");
		FundTransferInternalModel fundTransferInternalModel = null;

		fundTransferStoreRepository.deleteById(fundTransferStoreResp.get().getId());
		fundTransferInternalModel = new FundTransferInternalModel();
		fundTransferInternalModel.setStatus(true);
		fundTransferInternalModel.setMessage("deleted FT id");
		fundTransferInternalModel.setFundTransfer(null);
		return fundTransferInternalModel;
	}

	public String makeSignature(InitTransactionReqModel initTransactionReqModel, User user, BenDetails benDetails) {
		Enterprises enterprises = null;
		SHA512hash sha512hash = null;

		String enterprise = null;
		String mobile = null;
		String account = null;
		BigDecimal amount = null;
		String mode = null;
		String data = null;

		enterprises = enterprisesRepository.findByIdAndActive(Long.valueOf(user.getEnterpriseId()), true);
		if (enterprises == null) {
			data = "NF";
		} else {
			enterprise = enterprises.getPrefCorp();
			mobile = user.getMobile();
			account = initTransactionReqModel.getRemAccNo();
			amount = initTransactionReqModel.getAmount();
			mode = initTransactionReqModel.getMode();

			data = mobile + enterprise + account + String.valueOf(amount) + mode;
//			RecordLog.writeLogFile(data);

			if (initTransactionReqModel.isQuickPay() == true) {
				if (!StringUtils.isEmpty(initTransactionReqModel.getBenAccNo())) {
					data = data + initTransactionReqModel.getBenAccNo();
				}
			} else {
				if (!StringUtils.isEmpty(benDetails.getBenAccNo())) {
					data = data + benDetails.getBenAccNo();
				}
			}

			sha512hash = new SHA512hash();
			data = sha512hash.encryptThisString(data);
		}
		return data;
	}

	public String generateTransactionId() {
		String transactionId = null;
		String randomNo = null;
		String prefix = "TRANS";

		randomNo = RandomHEXNumber();
		transactionId = prefix + randomNo + "_" + String.valueOf(System.currentTimeMillis());

		return transactionId;
	}

	public String RandomHEXNumber() {
		String zeros = "0A";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X10), 32);
		s = zeros.substring(s.length()) + s;
		return s.toLowerCase();
	}

	public String generateRefNo(String mode) {
		String refNo = null;
		final ZonedDateTime input = ZonedDateTime.now();
		String zeros = "000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;

		if (mode.equals("fed2fed")) {
			String prefix = "SMEFB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		} else if (mode.equals("neft")) {
			String prefix = "SMEMB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		} else {
//			refNo = "IMPS" + s.toUpperCase();
			
			String prefix = "IMPS";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
			
		}
		return refNo;
	}

	public String generateSenderRefNo(String mode) {
		String refNo = null;
		final ZonedDateTime input = ZonedDateTime.now();
		String zeros = "000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s =  s + zeros.substring(s.length()) ;

		if (mode.equals("fed2fed")) {
			String prefix = "SMEFB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
			if(refNo.length()>15) {
				refNo=refNo.substring(0, 15);
			}
		} else if (mode.equals("neft")) {
			String prefix = "SMEMB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
			if(refNo.length()>15) {
				refNo=refNo.substring(0, 15);
			}
		} else {
			String prefix = "SMEIM";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
			if(refNo.length()>15) {
				refNo=refNo.substring(0, 15);
			}
		}
		RecordLog.writeLogFile("generateSenderRefNo generated refNo" + refNo+" its length "+refNo.length());
		return refNo;
	}

	public ApproveTranRespModel approveIntrabankProcess(Optional<FundTransfer> fundTransfer,
			ApproveTransactionReqModel approveTransactionReqModel) {
		boolean validDailyTransaction = false;
		boolean validMonthlyTransaction = false;
		Optional<User> getCurrentUserDetails = null;
		String senderRefId = null;
		String remarks = null;
		String benName = null;
		String mode = null;

		Optional<Enterprises> enterprises = enterprisesRepository
				.findById(Long.valueOf(fundTransfer.get().getEnterpriseId()));
		if (enterprises.isPresent()) {
			if (precisionCheck(fundTransfer.get().getAmount())) {
				
				
				
				LocalDateTime local=LocalDateTime.now(ZoneId.systemDefault());
				Timestamp current=Timestamp.valueOf(local);
				Timestamp past=Timestamp.valueOf(local.minusMinutes(10));
				
				boolean exist=fundTransferRepository.existsByStatusAndAmountAndBenAccNoAndRemAccNoAndEnterpriseIdAndCreatedAtBetween("approved",fundTransfer.get().getAmount()
						,fundTransfer.get().getBenAccNo(),fundTransfer.get().getRemAccNo(),
						fundTransfer.get().getEnterpriseId(),past,current);
				RecordLog.writeLogFile("record exist in time duration fund prefcorp "+enterprises.get().getPrefCorp()+"  "+exist);
				
				
				
				if(!exist) {
				
				
				
				
				validDailyTransaction = fundTransaferTransactionInternalService
						.validDailyTransaction(fundTransfer.get().getAmount(), fundTransfer.get().getEnterpriseId());
				validMonthlyTransaction = fundTransaferTransactionInternalService
						.validMonthlyTransaction(fundTransfer.get().getAmount(), fundTransfer.get().getEnterpriseId());

//				 validDailyTransaction = true;
//				 validMonthlyTransaction = true;
				if (validDailyTransaction && validMonthlyTransaction) {
					getCurrentUserDetails = getCurrentUserDetail(approveTransactionReqModel.getAuthToken());
					if (getCurrentUserDetails.isPresent()) {

						/* TODO check with vikas */
						//this is to block the user in between 11pm to 1am
						ZonedDateTime zone=ZonedDateTime.now();
						Date now=Date.from(zone.plusHours(5).plusMinutes(30).toInstant());
						Date night11=Date.from(zone.withHour(23).withMinute(0).withSecond(0).toInstant());
					Date morning1=	Date.from(zone.plusDays(1).withHour(1).withMinute(0).withSecond(0).toInstant());
//						Date night11=Date.from(zone.withHour(12).withMinute(0).withSecond(0).toInstant());
//						Date morning1=	Date.from(zone.withHour(17).withMinute(30).withSecond(0).toInstant());
						RecordLog.writeLogFile("is after "+now.after(night11)+" is before "+now.before(morning1)+" mode "+fundTransfer.get().getMode());
						if((now.after(night11) && now.before(morning1)) && fundTransfer.get().getMode().equals("neft")) {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(false);
							approveTranRespModel.setDescription("NEFT transaction cannot be approved between 11.00 PM and 1.00 AM.");
							approveTranRespModel.setMessage("NEFT transaction cannot be approved between 11.00 PM and 1.00 AM.");
							approveTranRespModel.setFundTransfer(null);
							return approveTranRespModel;
						}else {
							RecordLog.writeLogFile("time is valid for fund now:" +now+" time between " +night11+"  "  +morning1);


						String approval = checkerServiceFundTransfer.checker(getCurrentUserDetails, fundTransfer.get());
//						fundTransferLogsEntryService.approveLog(fundTransfer.get(),
//								getCurrentUserDetails.get().getPrefNo(), approveTransactionReqModel.getPrefCorp());
						RecordLog.writeLogFile("progress in fund 2586 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
						if (approval.equals("success")) {
							RecordLog.writeLogFile("progress in fund 2587 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
							if (fundTransfer.get().getProgress() == null
									|| !fundTransfer.get().getProgress().equals("initiated")) {
								RecordLog.writeLogFile("progress in fund 2590 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
								if (fundTransfer.get().getProgress() == null
										|| !fundTransfer.get().getProgress().equals("completed")) {
									RecordLog.writeLogFile("progress in fund 2593 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
									mode = fundTransfer.get().getMode();
									
									fundTransfer.get().setProgress("initiated");
									fundTransferRepository.save(fundTransfer.get());
									if (mode.equals("fed2fed")) {
										remarks = StringUtils.isEmpty(fundTransfer.get().getRemarks()) ? "FED2FED"
												: fundTransfer.get().getRemarks();
										benName = StringUtils.isEmpty(fundTransfer.get().getBenName()) ? "SMEMB QP"
												: fundTransfer.get().getBenName();
										senderRefId = StringUtils.isEmpty(fundTransfer.get().getSenderRefId())
												? generateSenderRefNo(fundTransfer.get().getMode())
												: fundTransfer.get().getSenderRefId();
										String mandatoryField = checkmandatoryFieldFT(fundTransfer, mode);
										RecordLog.writeLogFile("progress in fund 2608 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
										if (mandatoryField.equals("")) {
											String request = createFundTransferApprovalReq(fundTransfer, senderRefId);
										try {
											RecordLog.writeLogFile("Intrabank Req String : " + request.toString());
											HttpHeaders headers = new HttpHeaders();
											headers.setContentType(MediaType.APPLICATION_JSON);
//									headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
											HttpEntity<String> entity = new HttpEntity<String>(request, headers);
											ResponseEntity<?> ftResponse = restTemplate.postForEntity(
													fblgatewayurl + "/gateway/fundtransfer/ift", entity, String.class);
											RecordLog.writeLogFile("progress in fund 2619 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
											RecordLog.writeLogFile("Gateway Response 2612  fund internal service " + ftResponse);

											if (ftResponse.getStatusCodeValue() == 200) {
												if (ftResponse.hasBody()) {
													RecordLog.writeLogFile("progress in fund 2624 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
													JSONObject jsonObject = new JSONObject(
															ftResponse.getBody().toString());
													if (jsonObject.get("recordId") instanceof JSONObject) {
													auditLogService.FTlog(fundTransfer.get(), "fed2fed", jsonObject
															.getJSONObject("recordId").getString("responseCode"),
															enterprises);
//													fundTransferLogsEntryService.transactionLog(fundTransfer.get(),
//															jsonObject.getJSONObject("recordId")
//																	.getString("responseCode"),
//															getCurrentUserDetails.get().getPrefNo(),
//															enterprises.get().getPrefCorp());
													if (jsonObject.getBoolean("status")) {
														RecordLog.writeLogFile("progress in fund 2637 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
														String[] codes=properties.getGatewaysuccess().split(",");
														ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
														ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
														RecordLog.writeLogFile("Status codes: "+allowedIps);
														if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {
															
															//simBindingService.sendSMS(properties.getFund_transfer_transaction_msg(), getCurrentUserDetails.get().getMobile());
															
															RecordLog.writeLogFile("Gateway response code: "+jsonObject.getJSONObject("recordId").getString("responseCode"));
															FundTransfer ftObj = fundTransfer.get();
															ftObj.setStatus("approved");
															ftObj.setProgress("completed");
															
															ftObj.setIpAddress(approveTransactionReqModel.getIpAddress());
															ftObj.setGeoLocation(approveTransactionReqModel.getGeoLocation());
															ftObj.setChannelFlag(approveTransactionReqModel.getChannelFlag());
															ftObj.setUpdatedAt(new Timestamp(new Date().getTime()));
															ftObj.setResponseCode(jsonObject.getJSONObject("recordId")
																	.getString("responseCode"));
															ftObj.setResponseReason(jsonObject.getJSONObject("recordId")
																	.getString("reason"));
//															ftObj.setIpAddress(approveTransactionReqModel.get);
//															ftObj.setGeoLocation();
															fundTransferRepository.save(ftObj);

															approveTranRespModel.setStatus(true);
															approveTranRespModel
																	.setDescription(jsonObject.getJSONObject("recordId")
																			.getString("ReferenceId"));
															approveTranRespModel.setMessage(jsonObject
																	.getJSONObject("recordId").getString("reason"));
															approveTranRespModel.setFundTransfer(fundTransfer.get());

														} else {
															RecordLog.writeLogFile("progress in fund 2663 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
															FundTransfer ftObj = fundTransfer.get();
															ftObj.setStatus("approved");
															ftObj.setProgress("completed");
															ftObj.setUpdatedAt(new Timestamp(new Date().getTime()));
															ftObj.setResponseCode(jsonObject.getJSONObject("recordId")
																	.getString("responseCode"));
															ftObj.setResponseReason(jsonObject.getJSONObject("recordId")
																	.getString("reason"));
															ftObj.setIpAddress(approveTransactionReqModel.getIpAddress());
															ftObj.setGeoLocation(approveTransactionReqModel.getGeoLocation());
															ftObj.setChannelFlag(approveTransactionReqModel.getChannelFlag());
															fundTransferRepository.save(ftObj);
															checkerServiceFundTransfer.rollback(getCurrentUserDetails,
																	fundTransfer);
															approveTranRespModel.setStatus(false);
															approveTranRespModel
																	.setDescription(jsonObject.getJSONObject("recordId")
																			.getString("ReferenceId"));
															approveTranRespModel.setMessage(jsonObject
																	.getJSONObject("recordId").getString("reason"));
															approveTranRespModel.setFundTransfer(fundTransfer.get());
														}
														RecordLog.writeLogFile("progress in fund 2683 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
														return approveTranRespModel;
													} else {
														/**
														 * Vikas to right rollback
														 */
														checkerServiceFundTransfer.rollback(getCurrentUserDetails,
																fundTransfer);
														if (jsonObject.getJSONObject("recordId")
																.getString("responseCode").equals("121")
																|| jsonObject.getJSONObject("recordId")
																		.getString("responseCode").equals("116")) {
															fundTransfer.get().setProgress("nil");
														} else {
															fundTransfer.get().setStatus("deleted");
														}
														fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
														fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
														fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
														fundTransfer.get().setResponseCode(jsonObject.getJSONObject("recordId")
																.getString("responseCode"));
														fundTransfer.get().setResponseReason(jsonObject.getJSONObject("recordId")
																.getString("reason"));
														fundTransfer.get().setStatus("failed");
														fundTransferRepository.save(fundTransfer.get());
														ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
														approveTranRespModel.setStatus(false);
														approveTranRespModel.setDescription("Payment failed. Please try after some time.");
														approveTranRespModel.setMessage(jsonObject
																.getJSONObject("recordId").getString("reason"));
														approveTranRespModel.setFundTransfer(fundTransfer.get());
														RecordLog.writeLogFile("progress in fund 2706 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
														return approveTranRespModel;
													}
												} else {
													fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
													fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
													fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
													fundTransfer.get().setResponseCode("500");
													fundTransfer.get().setResponseReason(ftResponse.toString());
													fundTransfer.get().setStatus("failed");
													fundTransferRepository.save(fundTransfer.get());
													ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
													approveTranRespModel.setStatus(false);
													approveTranRespModel.setDescription("Unable to process the payment. Please try after some time.");
													approveTranRespModel.setMessage("Unable to process the payment. Please try after some time.");
													approveTranRespModel.setFundTransfer(null);
													RecordLog.writeLogFile("progress in fund 2715 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
													return approveTranRespModel;
												}
											} else {
												fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
												fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
												fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
												fundTransfer.get().setResponseCode("500");
												fundTransfer.get().setResponseReason(ftResponse.toString());
												fundTransfer.get().setStatus("failed");
												fundTransferRepository.save(fundTransfer.get());
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
													approveTranRespModel.setDescription("Unable to process the request. Please try after some time.");
													approveTranRespModel.setMessage("Unable to process the request. Please try after some time.");
													approveTranRespModel.setFundTransfer(null);
													RecordLog.writeLogFile("progress in fund 2724 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
													return approveTranRespModel;
												}
											} else {
												fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
												fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
												fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
												fundTransfer.get().setResponseCode("500");
												fundTransfer.get().setResponseReason(ftResponse.toString());
												fundTransfer.get().setStatus("failed");
												fundTransferRepository.save(fundTransfer.get());
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setMessage(ftResponse.toString());
												approveTranRespModel.setFundTransfer(null);
												RecordLog.writeLogFile("progress in fund 2732 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
												return approveTranRespModel;
											}
										}catch(Exception e) {
											 fundTransfer.get().setStatus("failed");
											 fundTransfer.get().setProgress("completed");
											 fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
											 fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
											 fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
												fundTransfer.get().setResponseCode("500");
												fundTransfer.get().setResponseReason("Exception During FundTransfer");
												
											  fundTransferRepository.save(fundTransfer.get());
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setMessage("Exception During FundTransfer");
											approveTranRespModel.setFundTransfer(null);
											RecordLog.writeLogFile("EXOCfundinternal 2743: for prefcorp "+approveTransactionReqModel.getPrefCorp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
											return approveTranRespModel;
										}
										} else {
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setMessage(mandatoryField);
											approveTranRespModel.setFundTransfer(null);
											RecordLog.writeLogFile("progress in fund 2751 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
											return approveTranRespModel;
										}
									} else if (mode.equals("neft")) {

										remarks = StringUtils.isEmpty(fundTransfer.get().getRemarks()) ? "NEFT"
												: fundTransfer.get().getRemarks();
										benName = StringUtils.isEmpty(fundTransfer.get().getBenName()) ? "SMEMB QP"
												: fundTransfer.get().getBenName();
										senderRefId = StringUtils.isEmpty(fundTransfer.get().getSenderRefId())
												? generateSenderRefNo(fundTransfer.get().getMode())
												: fundTransfer.get().getSenderRefId();

										String request = createNeftTransferApprovalReq(fundTransfer, senderRefId);								
										
										try {
											RecordLog.writeLogFile("progress in fund 2767 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
											RecordLog.writeLogFile("NEFT Req String : " + request);
										HttpHeaders headers = new HttpHeaders();
										headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
										HttpEntity<String> entity = new HttpEntity<String>(request, headers);
										ResponseEntity<?> ftResponse = restTemplate.postForEntity(
												fblgatewayurl + "/gateway/fundtransfer/neft", entity, String.class);
										RecordLog.writeLogFile("progress in fund 2751 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+ftResponse);
										RecordLog.writeLogFile("Gateway Response 2758  fund internal service " + ftResponse);

										if (ftResponse.getStatusCodeValue() == 200) {
											if (ftResponse.hasBody()) {
												RecordLog.writeLogFile("progress in fund 2780 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
												JSONObject jsonObject = new JSONObject(ftResponse.getBody().toString());

												auditLogService.FTlog(fundTransfer.get(), "neft",
														jsonObject.getJSONObject("recordId").getString("responseCode"),
														enterprises);
//												fundTransferLogsEntryService.transactionLog(fundTransfer.get(),
//														jsonObject.getJSONObject("recordId").getString("responseCode"),
//														getCurrentUserDetails.get().getPrefNo(),
//														enterprises.get().getPrefCorp());
												if (jsonObject.getBoolean("status")) {
													String[] codes=properties.getGatewaysuccess().split(",");
													ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
													RecordLog.writeLogFile("Status codes: "+allowedIps);
													if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {
														RecordLog.writeLogFile("Gateway response code: "+jsonObject.getJSONObject("recordId").getString("responseCode"));
													
														//simBindingService.sendSMS(properties.getFund_transfer_transaction_msg(), getCurrentUserDetails.get().getMobile());

														FundTransfer ftObj = fundTransfer.get();
														
														ftObj.setStatus("approved");
														ftObj.setProgress("completed");
														ftObj.setIpAddress(approveTransactionReqModel.getIpAddress());
														ftObj.setGeoLocation(approveTransactionReqModel.getGeoLocation());
														ftObj.setChannelFlag(approveTransactionReqModel.getChannelFlag());
														ftObj.setUpdatedAt(new Timestamp(new Date().getTime()));
														ftObj.setResponseCode(jsonObject.getJSONObject("recordId")
																.getString("responseCode"));
														ftObj.setResponseReason(jsonObject.getJSONObject("recordId")
																.getString("reason"));
														fundTransferRepository.save(ftObj);

														ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
														approveTranRespModel.setStatus(true);
														approveTranRespModel.setDescription("Gateway Response");
														approveTranRespModel.setMessage(jsonObject
																.getJSONObject("recordId").getString("reason"));
														approveTranRespModel.setFundTransfer(fundTransfer.get());
														RecordLog.writeLogFile("progress in fund 2812 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
														return approveTranRespModel;
													} else {
														FundTransfer ftObj = fundTransfer.get();
														ftObj.setStatus("approved");
														ftObj.setProgress("completed");
														
														ftObj.setIpAddress(approveTransactionReqModel.getIpAddress());
														ftObj.setGeoLocation(approveTransactionReqModel.getGeoLocation());
														ftObj.setChannelFlag(approveTransactionReqModel.getChannelFlag());
														ftObj.setUpdatedAt(new Timestamp(new Date().getTime()));
														ftObj.setResponseCode(jsonObject.getJSONObject("recordId")
																.getString("responseCode"));
														ftObj.setResponseReason(jsonObject.getJSONObject("recordId")
																.getString("reason"));
														fundTransferRepository.save(ftObj);

														checkerServiceFundTransfer.rollback(getCurrentUserDetails,
																fundTransfer);
														ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
														approveTranRespModel.setStatus(false);
														approveTranRespModel.setDescription(jsonObject
																.getJSONObject("recordId").getString("ReferenceId"));
														approveTranRespModel.setMessage(jsonObject
																.getJSONObject("recordId").getString("reason"));
														approveTranRespModel.setFundTransfer(fundTransfer.get());
														RecordLog.writeLogFile("progress in fund 2834 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
													}
												} else {

													/**
													 * Vikas to right rollback
													 */
													checkerServiceFundTransfer.rollback(getCurrentUserDetails,
															fundTransfer);
													if (jsonObject.getJSONObject("recordId").getString("responseCode")
															.equals("121")
															|| jsonObject.getJSONObject("recordId")
																	.getString("responseCode").equals("116")) {
														fundTransfer.get().setProgress("nil");
													} else {
														fundTransfer.get().setStatus("deleted");
													}
													fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
													fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
													fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
													fundTransfer.get().setResponseCode(jsonObject.getJSONObject("recordId").getString("responseCode"));
													fundTransfer.get().setResponseReason(jsonObject.getJSONObject("recordId").getString("reason"));
													fundTransfer.get().setStatus("failed");
													fundTransferRepository.save(fundTransfer.get());
													ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
													approveTranRespModel.setStatus(false);
													approveTranRespModel.setDescription("Gateway Response");
													approveTranRespModel.setMessage(
															jsonObject.getJSONObject("recordId").getString("reason"));
													approveTranRespModel.setFundTransfer(fundTransfer.get());
													RecordLog.writeLogFile("progress in fund 2858 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
													return approveTranRespModel;
												}
											} else {
												fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
												fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
												fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
												fundTransfer.get().setResponseCode("500");
												fundTransfer.get().setResponseReason(ftResponse.toString());
												fundTransfer.get().setStatus("failed");
												fundTransferRepository.save(fundTransfer.get());
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage("no response from gateway");
												approveTranRespModel.setFundTransfer(null);
												RecordLog.writeLogFile("progress in fund 2867 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
												return approveTranRespModel;
											}
										} else {
											fundTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
											fundTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
											fundTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
											fundTransfer.get().setResponseCode("500");
											fundTransfer.get().setStatus("failed");
											fundTransfer.get().setResponseReason(ftResponse.toString());
											fundTransferRepository.save(fundTransfer.get());
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setMessage(ftResponse.toString());
											approveTranRespModel.setFundTransfer(null);
											RecordLog.writeLogFile("progress in fund 2875 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
											return approveTranRespModel;
										}
									}catch(Exception e) {
                                        FundTransfer ftObj2 = fundTransfer.get();	
                                        ftObj2.setStatus("failed");
                                        ftObj2.setProgress("completed");
                                        ftObj2.setIpAddress(approveTransactionReqModel.getIpAddress());
                                        ftObj2.setGeoLocation(approveTransactionReqModel.getGeoLocation());
                                        ftObj2.setChannelFlag(approveTransactionReqModel.getChannelFlag());
                                        fundTransfer.get().setResponseCode("500");
										fundTransfer.get().setResponseReason("Exception During FundTransfer");
										fundTransferRepository.save(ftObj2);
										ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
										approveTranRespModel.setStatus(false);
										approveTranRespModel.setMessage("Exception During FundTransfer");
										approveTranRespModel.setFundTransfer(null);
										RecordLog.writeLogFile("EXOCfundinternal 2858: for prefcorp "+approveTransactionReqModel.getPrefCorp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
										return approveTranRespModel;
									}
									}
								} else {
									RecordLog.writeLogFile("progress in fund 2873"+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
									ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
									approveTranRespModel.setStatus(false);
									approveTranRespModel.setMessage("Already Completed");
									approveTranRespModel.setDescription("Already Completed");
									RecordLog.writeLogFile("progress in fund 2897 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
									return approveTranRespModel;
								}
							} else {
								RecordLog.writeLogFile("progress in fund 2880"+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile());
								ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
								approveTranRespModel.setStatus(false);
								approveTranRespModel.setMessage("Already Initiated");
								approveTranRespModel.setDescription("Already Initiated");
								RecordLog.writeLogFile("progress in fund 2906 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
								return approveTranRespModel;
							}
						} else if (approval.equals("signed")) {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(true);
							approveTranRespModel.setMessage("sucess");
							approveTranRespModel.setDescription("Signed");
							RecordLog.writeLogFile("progress in fund 2914 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
							return approveTranRespModel;
						} else if (approval.equals("already_approved")) {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(false);
							approveTranRespModel.setMessage("Transaction : Already Approved");
							approveTranRespModel.setDescription("Already Approved ");
							RecordLog.writeLogFile("progress in fund 2921 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
							return approveTranRespModel;
						} else {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(false);
							approveTranRespModel.setMessage("Transaction");
							approveTranRespModel.setFundTransfer(fundTransfer.get());
							approveTranRespModel.setDescription("Failed by the approver");
							RecordLog.writeLogFile("progress in fund 2929 "+fundTransfer.get().getProgress()+" mobile: "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
							return approveTranRespModel;
						}
					}
					}
				} else if (!validDailyTransaction) {
					Double totalLimitDaily;
					Optional<TransactionLimit> transactionDailyLimit = null;
					Long daily_amount_transacted = fundTransaferTransactionInternalService
							.approvedAmount(fundTransfer.get().getEnterpriseId());
					transactionDailyLimit = transactionLimitRepository
							.findFirstByEnterpriseIdAndModeOrderByIdDesc(fundTransfer.get().getEnterpriseId(), "daily");
					if (transactionDailyLimit.isPresent()) {
						totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0")
								? Double.parseDouble(transactionDaily)
								: Double.parseDouble(transactionDailyLimit.get().getAmount());
					} else {
						totalLimitDaily = Double.parseDouble(transactionDaily);
					}
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage(
							"Transaction amount exceeds the Daily Transaction limit. You have already transacted for Rs."
									+ String.valueOf(daily_amount_transacted) + " amount and the limit is Rs."
									+ String.valueOf(totalLimitDaily));
					approveTranRespModel.setFundTransfer(null);
					return approveTranRespModel;
				} else if (!validMonthlyTransaction) {
					Double totalLimitMonthly;
					Optional<TransactionLimit> transactionMonthlyLimit = null;
					Long monthly_amount_transacted = fundTransaferTransactionInternalService
							.totalAmountMonth(fundTransfer.get().getEnterpriseId());
					transactionMonthlyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(
							fundTransfer.get().getEnterpriseId(), "monthly");
					if (transactionMonthlyLimit.isPresent()) {
						totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
								? Double.parseDouble(transactionMonthly)
								: Double.parseDouble(transactionMonthlyLimit.get().getAmount());
					} else {
						totalLimitMonthly = Double.parseDouble(transactionMonthly);
					}
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage(
							"Transaction amount exceeds the Monthly Transaction limit. You have already transacted for Rs."
									+ String.valueOf(monthly_amount_transacted) + " amount and the limit is Rs."
									+ String.valueOf(totalLimitMonthly));
					approveTranRespModel.setDescription(approveTranRespModel.getMessage());
					approveTranRespModel.setFundTransfer(null);
					return approveTranRespModel;
				}
				
				
				}else {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage("Similar transactions not allowed within 10 minutes");
					approveTranRespModel.setDescription("Similar transactions not allowed within 10 minutes");
					approveTranRespModel.setFundTransfer(null);
					return approveTranRespModel;
				}
				
				
			} else {
				ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
				approveTranRespModel.setStatus(false);
				approveTranRespModel
						.setMessage("Invalid Amount, Please check whether decimal entered is below 2 digit");
				approveTranRespModel.setFundTransfer(null);
				return approveTranRespModel;
			}
		} else {
			ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
			approveTranRespModel.setStatus(false);
			approveTranRespModel.setMessage("enterprises record not found");
			approveTranRespModel.setFundTransfer(null);
			return approveTranRespModel;
		}
		ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
		approveTranRespModel.setStatus(false);
		approveTranRespModel.setMessage("Something went wrong");
		approveTranRespModel.setFundTransfer(null);
		return approveTranRespModel;

	}

	private String checkmandatoryFieldFT(Optional<FundTransfer> fundTransfer, String mode) {
		StringBuilder sb = new StringBuilder();
		if (mode.equals("fed2fed")) {
			if (StringUtils.isEmpty(fundTransfer.get().getBenAccNo())) {
				sb.append("beneficiary account no |");
			}
			if (StringUtils.isEmpty(fundTransfer.get().getBenName())) {
				sb.append("beneficiary Name |");
			}
			if (StringUtils.isEmpty(fundTransfer.get().getRemAccNo())) {
				sb.append("remitter acc no |");
			}
			if (StringUtils.isEmpty(fundTransfer.get().getRemName())) {
				sb.append("remitter name |");
			}
		} else if (mode.equals("neft")) {
			if (StringUtils.isEmpty(fundTransfer.get().getRemAccNo())) {
				sb.append("remitter acc no |");
			}
			if (StringUtils.isEmpty(fundTransfer.get().getRemName())) {
				sb.append("remitter name |");
			}
//			if (StringUtils.isEmpty(fundTransfer.get().getRemMobNum())) {
//				sb.append("remitter Mobile no |");
//			}
//			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemName())) {
//				sb.append("remitter Address |");
//			}
			if (StringUtils.isEmpty(fundTransfer.get().getBenAccNo())) {
				sb.append("beneficiary account no |");
			}
			if (StringUtils.isEmpty(fundTransfer.get().getBenName())) {
				sb.append("beneficiary Name |");
			}
//			if (StringUtils.isEmpty(fundTransfer.get().getBenMobNum())) {
//				sb.append("beneficiary Mobile no |");
//			}
//			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemName())) {
//			sb.append("beneficiary Address |");
//			}
		}
		return sb.toString();
	}

	public ApproveTranRespModel approveImpsProcess(Optional<ImpsTransfer> impsTransfer,
			ApproveTransactionReqModel approveTransactionReqModel) {
		boolean validDailyTransaction = false;
		boolean validMonthlyTransaction = false;
		Optional<User> getCurrentUserDetails = null;
		String senderRefId = null;
		String remarks = null;
		String benName = null;
		String mode = null;

		Optional<Enterprises> enterprises = enterprisesRepository
				.findById(Long.valueOf(impsTransfer.get().getEnterpriseId()));
		if (enterprises.isPresent()) {
			if (precisionCheck(impsTransfer.get().getAmount())) {
				
				
				
				LocalDateTime local=LocalDateTime.now(ZoneId.systemDefault());
				Timestamp current=Timestamp.valueOf(local);
				Timestamp past=Timestamp.valueOf(local.minusMinutes(10));
				
				boolean exist=impsTransferRepository.existsByStatusAndAmountAndBenAccNoAndRemAccNumAndEnterpriseIdAndCreatedAtBetween("approved",impsTransfer.get().getAmount(),
						impsTransfer.get().getBenAccNo(),impsTransfer.get().getRemAccNum(),
						impsTransfer.get().getEnterpriseId(),
						past,current);
				
				boolean mmidExist=impsTransferRepository.existsByStatusAndAmountAndBenMobNumAndRemAccNumAndEnterpriseIdAndCreatedAtBetween("approved",impsTransfer.get().getAmount(),
						impsTransfer.get().getBenMobNum(),impsTransfer.get().getRemAccNum(),
						impsTransfer.get().getEnterpriseId(),past,current);
			
				RecordLog.writeLogFile("record exist in time duration imps prefcorp "+enterprises.get().getPrefCorp()+" p2a "+exist+" p2p "+mmidExist);

				
				if(!exist && !mmidExist) {
				
				
				
				
				validDailyTransaction = fundTransaferTransactionInternalService
						.validDailyTransaction(impsTransfer.get().getAmount(), impsTransfer.get().getEnterpriseId());
				validMonthlyTransaction = fundTransaferTransactionInternalService
						.validMonthlyTransaction(impsTransfer.get().getAmount(), impsTransfer.get().getEnterpriseId());

				// validDailyTransaction = true;
				// validMonthlyTransaction = true;
				if (validDailyTransaction && validMonthlyTransaction) {
					getCurrentUserDetails = getCurrentUserDetail(approveTransactionReqModel.getAuthToken());
					if (getCurrentUserDetails.isPresent()) {

						/* TODO check with vikas */

						String approval = makercheckerServiceImpsTransfer.checker(getCurrentUserDetails,
								impsTransfer.get());
//						fundTransferLogsEntryService.approveLog(impsTransfer.get(),
//								getCurrentUserDetails.get().getPrefNo(), approveTransactionReqModel.getPrefCorp());
						RecordLog.writeLogFile("progress of imps 3077 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());
						if (approval.equals("success")) {
							RecordLog.writeLogFile("progress of imps 3079 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());
							if (impsTransfer.get().getProgress() == null
									|| !impsTransfer.get().getProgress().equals("initiated")) {
								if (impsTransfer.get().getProgress() == null
										|| !impsTransfer.get().getProgress().equals("completed")) {
									mode = impsTransfer.get().getMode();
									impsTransfer.get().setProgress("initiated");
									impsTransferRepository.save(impsTransfer.get());
									if (mode.equals("p2p")) {
										remarks = StringUtils.isEmpty(impsTransfer.get().getRemarks()) ? "UPIREFFS"
												: impsTransfer.get().getRemarks();

										senderRefId = StringUtils.isEmpty(impsTransfer.get().getSenderRefId())
												? generateSenderRefNo(impsTransfer.get().getMode())
												: impsTransfer.get().getSenderRefId();
										RecordLog.writeLogFile("progress of imps 3094 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());

//									FundTransferReqModel fundTransferReqModel = createP2PTransferReq(impsTransfer,
//											senderRefId);
										HashMap<String, String> reqdata = new HashMap<String, String>();
//										reqdata.put("mbRefNo", generateSenderRefNo("imps"));
										reqdata.put("mbRefNo", senderRefId);
										reqdata.put("amt", impsTransfer.get().getAmount());
										reqdata.put("fromAcc", impsTransfer.get().getRemAccNum());
										reqdata.put("remarks", impsTransfer.get().getRemarks());
										reqdata.put("benAccType", "10");
//											impsTransfer.get().getBenAccType() != null
//													? impsTransfer.get().getBenAccType()
//													: "10");
										reqdata.put("remName", getTrimmedValue( impsTransfer.get().getRemName()));
										reqdata.put("mobileNumber", impsTransfer.get().getRemMobNum());
										reqdata.put("remmmid", impsTransfer.get().getRemMmid());
										reqdata.put("customerId", impsTransfer.get().getRemCustId());
										reqdata.put("benIfsc", impsTransfer.get().getBenIfsc());
										reqdata.put("benAcc", impsTransfer.get().getBenAccNo());
										reqdata.put("benMmid", impsTransfer.get().getBenMmid());
										reqdata.put("benMobNumber", impsTransfer.get().getBenMobNum());
										reqdata.put("mode", "p2p");
										JSONObject reqObject = new JSONObject(reqdata);
										RecordLog.writeLogFile("P2P Req String : " + reqObject.toString());
										

										RecordLog.writeLogFile("progress of imps 3121 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());

										try {
										HttpHeaders headers = new HttpHeaders();
										headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
										HttpEntity<String> entity = new HttpEntity<String>(reqObject.toString(),
												headers);
										ResponseEntity<?> ftResponse = restTemplate.postForEntity(
												fblgatewayurl + "/gateway/fundtransfer/imps_p2p", entity, String.class);
										RecordLog.writeLogFile("progress of imps 3131 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+ftResponse);
										RecordLog.writeLogFile("Gateway Response 3095  fund internal service " + ftResponse);

										if (ftResponse.getStatusCodeValue() == 200) {
											if (ftResponse.hasBody()) {
												JSONObject jsonObject = new JSONObject(ftResponse.getBody().toString());
												if (jsonObject.has("recordId") && !jsonObject.isNull("recordId")) {
													RecordLog.writeLogFile("progress of imps 3138 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+ftResponse);
													auditLogService.IMPSlog(impsTransfer.get(), "p2p", jsonObject
															.getJSONObject("recordId").getString("responseCode"),
															enterprises);
//													fundTransferLogsEntryService.transactionLog(impsTransfer.get(),
//															jsonObject.getJSONObject("recordId")
//																	.getString("responseCode"),
//															getCurrentUserDetails.get().getPrefNo(),
//															enterprises.get().getPrefCorp());
													if (jsonObject.getBoolean("status")) {

														String[] codes=properties.getGatewaysuccess().split(",");
														ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
														RecordLog.writeLogFile("Status codes: "+allowedIps);
														if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {
															
														//	simBindingService.sendSMS(properties.getFund_transfer_transaction_msg(), getCurrentUserDetails.get().getMobile());
															
															
															RecordLog.writeLogFile("Gateway response code: "+allowedIps);
															ImpsTransfer impsTrf = impsTransfer.get();
															impsTrf.setStatus("approved");
															impsTrf.setProgress("completed");
														
															impsTrf.setIpAddress(approveTransactionReqModel.getIpAddress());
															impsTrf.setGeoLocation(approveTransactionReqModel.getGeoLocation());
															impsTrf.setChannelFlag(approveTransactionReqModel.getChannelFlag());
															impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
																	.getString("responseCode"));
															impsTrf.setResponseReason(jsonObject
																	.getJSONObject("recordId").getString("reason"));
															impsTransferRepository.save(impsTrf);

															ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
															approveTranRespModel.setStatus(true);
															approveTranRespModel.setDescription("Gateway Response");
															approveTranRespModel.setMessage(jsonObject
																	.getJSONObject("recordId").getString("reason"));
															approveTranRespModel.setImpsTransfer(impsTransfer.get());
															RecordLog.writeLogFile("progress of imps 3169 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+approveTranRespModel);
															return approveTranRespModel;
														} else {
															ImpsTransfer impsTrf = impsTransfer.get();
															impsTrf.setStatus("approved");
															impsTrf.setProgress("completed");
															impsTrf.setIpAddress(approveTransactionReqModel.getIpAddress());
															impsTrf.setGeoLocation(approveTransactionReqModel.getGeoLocation());
															impsTrf.setChannelFlag(approveTransactionReqModel.getChannelFlag());

															impsTrf.setUpdatedAt(new Timestamp(new Date().getTime()));
															impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
																	.getString("responseCode"));
															impsTrf.setResponseReason(jsonObject
																	.getJSONObject("recordId").getString("reason"));
															impsTransferRepository.save(impsTrf);
															checkerServiceFundTransfer
																	.rollbackimps(getCurrentUserDetails, impsTransfer);
															ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
															approveTranRespModel.setStatus(false);
															approveTranRespModel.setDescription("Gateway Response");
															approveTranRespModel.setMessage(jsonObject
																	.getJSONObject("recordId").getString("reason"));
															approveTranRespModel.setImpsTransfer(impsTransfer.get());
															RecordLog.writeLogFile("progress of imps 3189 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+approveTranRespModel);
															return approveTranRespModel;
														}
													} else {
														ImpsTransfer impsTrf = impsTransfer.get();
														impsTrf.setStatus("approved");
														impsTrf.setProgress("completed");
														impsTrf.setIpAddress(approveTransactionReqModel.getIpAddress());
														impsTrf.setGeoLocation(approveTransactionReqModel.getGeoLocation());
														impsTrf.setChannelFlag(approveTransactionReqModel.getChannelFlag());

														impsTrf.setUpdatedAt(new Timestamp(new Date().getTime()));
														impsTrf.setResponseCode("500");
														impsTrf.setResponseReason(jsonObject.getString("message"));
														impsTransferRepository.save(impsTrf);
														checkerServiceFundTransfer.rollbackimps(getCurrentUserDetails,
																impsTransfer);
														ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
														approveTranRespModel.setStatus(false);
														approveTranRespModel.setDescription("Gateway Response");
														approveTranRespModel.setMessage(jsonObject.getString("message"));
														approveTranRespModel.setImpsTransfer(impsTransfer.get());
														RecordLog.writeLogFile("progress of imps 3207 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+approveTranRespModel);
														return approveTranRespModel;
													}
												} else {
													/**
													 * Vikas to right rollback
													 */
													makercheckerServiceImpsTransfer.rollback(getCurrentUserDetails,
															impsTransfer);
													if (jsonObject.get("recordId") instanceof JSONObject) {
															if((jsonObject.getJSONObject("recordId").getString("responseCode")
															.equals("121")
															|| jsonObject.getJSONObject("recordId")
																	.getString("responseCode").equals("116"))) {
														impsTransfer.get().setProgress("nil");
													}
													}else {
														impsTransfer.get().setStatus("deleted");
													}
													impsTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
													impsTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
													impsTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
													impsTransfer.get().setResponseCode("500");
													impsTransfer.get().setResponseReason(jsonObject.getString("message"));
													impsTransfer.get().setStatus("failed");
													impsTransferRepository.save(impsTransfer.get());
													ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
													approveTranRespModel.setStatus(false);
													approveTranRespModel.setDescription("Unable to process your request, please try again later");
													approveTranRespModel.setMessage(
															jsonObject.getString("message"));
													approveTranRespModel.setImpsTransfer(impsTransfer.get());
													RecordLog.writeLogFile("progress of imps 3233 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+approveTranRespModel);
													return approveTranRespModel;
												}
											} else {
												impsTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
												impsTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
												impsTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
												impsTransfer.get().setResponseCode("500");
												impsTransfer.get().setResponseReason(ftResponse.toString());
												impsTransfer.get().setStatus("failed");
												impsTransferRepository.save(impsTransfer.get());
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage("Unable to process your request, please try again later");
												approveTranRespModel.setImpsTransfer(null);
												RecordLog.writeLogFile("progress of imps 3242 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+approveTranRespModel);
												return approveTranRespModel;
											}
										} else {
											impsTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
											impsTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
											impsTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
											impsTransfer.get().setResponseCode("500");
											impsTransfer.get().setResponseReason(ftResponse.toString());
											impsTransfer.get().setStatus("failed");
											impsTransferRepository.save(impsTransfer.get());
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setMessage(ftResponse.toString());
											approveTranRespModel.setImpsTransfer(null);
											RecordLog.writeLogFile("progress of imps 3250 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+"  resp "+approveTranRespModel);
											return approveTranRespModel;
										}
									}catch(Exception e) {
										
										ImpsTransfer impsTrf2 = impsTransfer.get();
										impsTrf2.setStatus("failed");
										impsTrf2.setProgress("completed");
										impsTrf2.setIpAddress(approveTransactionReqModel.getIpAddress());
										impsTrf2.setGeoLocation(approveTransactionReqModel.getGeoLocation());
										impsTrf2.setChannelFlag(approveTransactionReqModel.getChannelFlag());
										impsTrf2.setResponseCode("500");
										impsTrf2.setResponseReason("Exception During FundTransfer");
										
										impsTransferRepository.save(impsTrf2);
										ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
										approveTranRespModel.setStatus(false);
										approveTranRespModel.setMessage("Exception During FundTransfer");
										approveTranRespModel.setImpsTransfer(null);
										RecordLog.writeLogFile("EXOCfundinternal 3219: for prefcorp "+approveTransactionReqModel.getPrefCorp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());

										return approveTranRespModel;
									}

									} else if (mode.equals("p2a")) {
										RecordLog.writeLogFile("progress of imps 3269 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());
										remarks = StringUtils.isEmpty(impsTransfer.get().getRemarks()) ? "UPIREFFS"
												: impsTransfer.get().getRemarks();

										senderRefId = StringUtils.isEmpty(impsTransfer.get().getSenderRefId())
												? generateSenderRefNo(impsTransfer.get().getMode())
												: impsTransfer.get().getSenderRefId();
										String p2aTransferReqModel = createP2ATransferApprovalReq(impsTransfer,
												senderRefId);
//									HashMap<String, String> reqdata = new HashMap<String, String>();
//									reqdata.put("mbRefNo", generateSenderRefNo("imps"));
//									reqdata.put("amt", fundTransferStoreResp.get().getAmount());
//									reqdata.put("fromAcc", fundTransferStoreResp.get().getRemAccNo());
//									reqdata.put("remarks", fundTransferStoreResp.get().getRemarks());
//									reqdata.put("benAccType",
//											fundTransferStoreResp.get().getBenAccType() != null
//													? fundTransferStoreResp.get().getBenAccType()
//													: "10");
//									reqdata.put("remName", fundTransferStoreResp.get().getRemName());
//									reqdata.put("mobileNumber", fundTransferStoreResp.get().getRemMobNum());
//									// reqdata.put("remmmid", fundTransferStoreResp.get().getRemAccNo());
//									reqdata.put("customerId", fundTransferStoreResp.get().getRemCustId());
//									reqdata.put("benIfsc", fundTransferStoreResp.get().getBenIfsc());
//									reqdata.put("benAcc", fundTransferStoreResp.get().getBenAccNo());
//									reqdata.put("mode", "p2a");
//									reqdata.put("benMmid", fundTransferStoreResp.get().getBenMmid());
//
//									JSONObject reqObject = new JSONObject(reqdata);
										
										
										try {
											RecordLog.writeLogFile("progress of imps 3300 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());
											RecordLog.writeLogFile("P2A Req String : " + p2aTransferReqModel.toString());
										HttpHeaders headers = new HttpHeaders();
										headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
										HttpEntity<String> entity = new HttpEntity<String>(p2aTransferReqModel,
												headers);
										ResponseEntity<?> ftResponse = restTemplate.postForEntity(
												fblgatewayurl + "/gateway/fundtransfer/imps_p2a", entity, String.class);
										RecordLog.writeLogFile("progress of imps 3309 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());
										RecordLog.writeLogFile("Gateway Response 3264  fund internal service " + ftResponse);

										if (ftResponse.getStatusCodeValue() == 200) {
											if (ftResponse.hasBody()) {
												JSONObject jsonObject = new JSONObject(ftResponse.getBody().toString());
												RecordLog.writeLogFile("progress of imps 3315 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile());
												auditLogService.IMPSlog(impsTransfer.get(), "p2a",
														jsonObject.getJSONObject("recordId").getString("responseCode"),
														enterprises);
//												fundTransferLogsEntryService.transactionLog(impsTransfer.get(),
//														jsonObject.getJSONObject("recordId").getString("responseCode"),
//														getCurrentUserDetails.get().getPrefNo(),
//														enterprises.get().getPrefCorp());
												if (jsonObject.getBoolean("status")) {
													String[] codes=properties.getGatewaysuccess().split(",");
													ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
													RecordLog.writeLogFile("Status codes: "+allowedIps);
													if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {
														RecordLog.writeLogFile("Gateway response code: "+jsonObject.getJSONObject("recordId").getString("responseCode"));
														
													//	simBindingService.sendSMS(properties.getFund_transfer_transaction_msg(), getCurrentUserDetails.get().getMobile());

														
														
														ImpsTransfer impsTrf = impsTransfer.get();
														impsTrf.setStatus("approved");
														impsTrf.setProgress("completed");
														impsTrf.setIpAddress(approveTransactionReqModel.getIpAddress());
														impsTrf.setGeoLocation(approveTransactionReqModel.getGeoLocation());
														impsTrf.setChannelFlag(approveTransactionReqModel.getChannelFlag());
														impsTrf.setUpdatedAt(new Timestamp(new Date().getTime()));
														impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
																.getString("responseCode"));
														impsTrf.setResponseReason(jsonObject.getJSONObject("recordId")
																.getString("reason"));
														impsTransferRepository.save(impsTrf);

														ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
														approveTranRespModel.setStatus(true);
														approveTranRespModel.setDescription("Gateway Response");
														approveTranRespModel.setMessage(jsonObject
																.getJSONObject("recordId").getString("reason"));
														approveTranRespModel.setImpsTransfer(impsTransfer.get());
														RecordLog.writeLogFile("progress of imps 3345 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
														return approveTranRespModel;
													} else {
														ImpsTransfer impsTrf = impsTransfer.get();
														impsTrf.setStatus("approved");
														impsTrf.setProgress("completed");
														impsTrf.setIpAddress(approveTransactionReqModel.getIpAddress());
														impsTrf.setGeoLocation(approveTransactionReqModel.getGeoLocation());
														impsTrf.setChannelFlag(approveTransactionReqModel.getChannelFlag());

														impsTrf.setUpdatedAt(new Timestamp(new Date().getTime()));
														impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
																.getString("responseCode"));
														impsTrf.setResponseReason(jsonObject.getJSONObject("recordId")
																.getString("reason"));
														impsTransferRepository.save(impsTrf);
														checkerServiceFundTransfer.rollbackimps(getCurrentUserDetails,
																impsTransfer);
														ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
														approveTranRespModel.setStatus(false);
														approveTranRespModel.setDescription("Gateway Response");
														approveTranRespModel.setMessage(jsonObject
																.getJSONObject("recordId").getString("reason"));
														approveTranRespModel.setImpsTransfer(impsTransfer.get());
														RecordLog.writeLogFile("progress of imps 3365 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
														return approveTranRespModel;
													}
												} else {
													/**
													 * Vikas to right rollback
													 */
													makercheckerServiceImpsTransfer.rollback(getCurrentUserDetails,
															impsTransfer);
													if (jsonObject.getJSONObject("recordId").getString("responseCode")
															.equals("121")
															|| jsonObject.getJSONObject("recordId")
																	.getString("responseCode").equals("116")) {
														impsTransfer.get().setProgress("nil");
													} else {
														impsTransfer.get().setStatus("deleted");
													}
													impsTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
													impsTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
													impsTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
													impsTransfer.get().setResponseCode(jsonObject.getJSONObject("recordId")
															.getString("responseCode"));
													impsTransfer.get().setResponseReason(jsonObject.getJSONObject("recordId")
															.getString("reason"));
													impsTransfer.get().setStatus("failed");
													impsTransferRepository.save(impsTransfer.get());
													ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
													approveTranRespModel.setStatus(false);
													approveTranRespModel.setDescription("Gateway Response");
													approveTranRespModel.setMessage(
															jsonObject.getJSONObject("recordId").getString("reason"));
													approveTranRespModel.setImpsTransfer(impsTransfer.get());
													RecordLog.writeLogFile("progress of imps 3389 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
													return approveTranRespModel;
												}
											} else {
												impsTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
												impsTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
												impsTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
												impsTransfer.get().setResponseCode("500");
												impsTransfer.get().setResponseReason(ftResponse.toString());
												impsTransfer.get().setStatus("failed");
												
												impsTransferRepository.save(impsTransfer.get());
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage("no response from gateway");
												approveTranRespModel.setImpsTransfer(null);
												RecordLog.writeLogFile("progress of imps 3398 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
												return approveTranRespModel;
											}
										} else {
											impsTransfer.get().setIpAddress(approveTransactionReqModel.getIpAddress());
											impsTransfer.get().setGeoLocation(approveTransactionReqModel.getGeoLocation());
											impsTransfer.get().setChannelFlag(approveTransactionReqModel.getChannelFlag());
											impsTransfer.get().setResponseCode("500");
											impsTransfer.get().setResponseReason(ftResponse.toString());
											impsTransfer.get().setStatus("failed");
											impsTransferRepository.save(impsTransfer.get());
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setMessage(ftResponse.toString());
											approveTranRespModel.setImpsTransfer(null);
											RecordLog.writeLogFile("progress of imps 3406 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
											return approveTranRespModel;
										}
									}catch(Exception e) {
										ImpsTransfer impsTrf2 = impsTransfer.get();
										impsTrf2.setStatus("failed");
										impsTrf2.setProgress("completed");
										impsTrf2.setIpAddress(approveTransactionReqModel.getIpAddress());
										impsTrf2.setGeoLocation(approveTransactionReqModel.getGeoLocation());
										impsTrf2.setChannelFlag(approveTransactionReqModel.getChannelFlag());
										impsTrf2.setResponseCode("500");
										impsTrf2.setResponseReason("Exception During FundTransfer");
										impsTransferRepository.save(impsTrf2);
										ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
										approveTranRespModel.setStatus(false);
										approveTranRespModel.setMessage("Exception During FundTransfer");
										approveTranRespModel.setImpsTransfer(null);
										RecordLog.writeLogFile("EXOCfundinternal 3370: for prefcorp "+approveTransactionReqModel.getPrefCorp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
										return approveTranRespModel;
									}
									}
								} else {
									ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
									approveTranRespModel.setStatus(false);
									approveTranRespModel.setMessage("Already Completed");
									approveTranRespModel.setDescription("Already Completed");
									RecordLog.writeLogFile("progress of imps 3427 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
									return approveTranRespModel;
								}
							} else {
								ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
								approveTranRespModel.setStatus(false);
								approveTranRespModel.setMessage("Already Initiated");
								approveTranRespModel.setDescription("Already Initiated");
								RecordLog.writeLogFile("progress of imps 3433 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
								return approveTranRespModel;
							}
						} else if (approval.equals("signed")) {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(true);
							approveTranRespModel.setMessage("sucess");
							approveTranRespModel.setDescription("Signed");
							RecordLog.writeLogFile("progress of imps 3443 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
							return approveTranRespModel;
						} else if (approval.equals("already_approved")) {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(false);
							approveTranRespModel.setMessage("Transaction : Already Approved");
							approveTranRespModel.setDescription("Already Approved ");
							RecordLog.writeLogFile("progress of imps 3450 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
							return approveTranRespModel;
						} else {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(false);
							approveTranRespModel.setMessage("Transaction");
							approveTranRespModel.setImpsTransfer(impsTransfer.get());
							approveTranRespModel.setDescription("Failed by the approver");
							RecordLog.writeLogFile("progress of imps 3458 "+impsTransfer.get().getProgress()+" mobile "+getCurrentUserDetails.get().getMobile()+" resp "+approveTranRespModel);
							return approveTranRespModel;
						}
					}
				} else if (!validDailyTransaction) {
					Double totalLimitDaily;
					Optional<TransactionLimit> transactionDailyLimit = null;
					Long daily_amount_transacted = fundTransaferTransactionInternalService
							.approvedAmount(impsTransfer.get().getEnterpriseId());
					transactionDailyLimit = transactionLimitRepository
							.findFirstByEnterpriseIdAndModeOrderByIdDesc(impsTransfer.get().getEnterpriseId(), "daily");
					if (transactionDailyLimit.isPresent()) {
						totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0")
								? Double.parseDouble(transactionDaily)
								: Double.parseDouble(transactionDailyLimit.get().getAmount());
					} else {
						totalLimitDaily = Double.parseDouble(transactionDaily);
					}
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage(
							"Transaction amount exceeds the Daily Transaction limit. You have already transacted for Rs."
									+ String.valueOf(daily_amount_transacted) + " amount and the limit is Rs."
									+ String.valueOf(totalLimitDaily));
					approveTranRespModel.setFundTransfer(null);
					return approveTranRespModel;
				} else if (!validMonthlyTransaction) {
					Double totalLimitMonthly;
					Optional<TransactionLimit> transactionMonthlyLimit = null;
					Long monthly_amount_transacted = fundTransaferTransactionInternalService
							.totalAmountMonth(impsTransfer.get().getEnterpriseId());
					transactionMonthlyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(
							impsTransfer.get().getEnterpriseId(), "monthly");
					if (transactionMonthlyLimit.isPresent()) {
						totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
								? Double.parseDouble(transactionMonthly)
								: Double.parseDouble(transactionMonthlyLimit.get().getAmount());
					} else {
						totalLimitMonthly = Double.parseDouble(transactionMonthly);
					}
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage(
							"Transaction amount exceeds the Monthly Transaction limit. You have already transacted for Rs."
									+ String.valueOf(monthly_amount_transacted) + " amount and the limit is Rs."
									+ String.valueOf(totalLimitMonthly));
					approveTranRespModel.setFundTransfer(null);
					return approveTranRespModel;
				}
				
				
				}else {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage("Similar transactions can not be made until next 10 minutes");
					approveTranRespModel.setDescription("Similar transactions can not be made until next 10 minutes");
					approveTranRespModel.setFundTransfer(null);
					return approveTranRespModel;
				}
				
				
			} else {
				ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
				approveTranRespModel.setStatus(false);
				approveTranRespModel.setMessage("Invalid amount, Please check the amount decimal is not more than 2");
				approveTranRespModel.setFundTransfer(null);
				return approveTranRespModel;
			}
		} else {
			ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
			approveTranRespModel.setStatus(false);
			approveTranRespModel.setMessage("enterprises record not found");
			approveTranRespModel.setFundTransfer(null);
			return approveTranRespModel;
		}
		ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
		approveTranRespModel.setStatus(false);
		approveTranRespModel.setMessage("Something went wrong");
		approveTranRespModel.setFundTransfer(null);
		return approveTranRespModel;
	}
	
	String getTrimmedValue(String name){
		if(name.length()>40) {
			return name.substring(0, 39);
		}else {
			return name;
		}
	}
}