package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.Checker;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.Maker;
import com.federal.fedmobilesmecore.dto.Operation;
import com.federal.fedmobilesmecore.dto.ScheduledPayment;
import com.federal.fedmobilesmecore.dto.ScheduledPaymentStore;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;
import com.federal.fedmobilesmecore.dto.Signature;
import com.federal.fedmobilesmecore.dto.TransactionLimit;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.ApproveTranRespModel;
import com.federal.fedmobilesmecore.model.ApproveTransactionReqModel;
import com.federal.fedmobilesmecore.model.FundTransferDTO;
import com.federal.fedmobilesmecore.model.GetCustDetails;
import com.federal.fedmobilesmecore.model.MakerCheckerListGeneric;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.SchedulePaymentsInput;
import com.federal.fedmobilesmecore.repository.ActionRepository;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.CheckerRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.MakerRepository;
import com.federal.fedmobilesmecore.repository.OperationRepository;
import com.federal.fedmobilesmecore.repository.SchedulePaymentStoreRepo;
import com.federal.fedmobilesmecore.repository.ScheduledPaymentRepo;
import com.federal.fedmobilesmecore.repository.ScheduledTransactionRepo;
import com.federal.fedmobilesmecore.repository.SignatureRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class SchedulePaymentsService {

	private static final String transaction_prefix = "SCHEDULEPAY";
	private static final String ref_prefix = "SCHPAY";
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddhhmmssSS");
	DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");

	private static final Logger log4j = LogManager.getLogger(SchedulePaymentsService.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	OperationRepository operationRepository;

	@Autowired
	GetCustDetails getCustDetails;

	@Autowired
	ScheduledPaymentRepo schrepo;

	@Autowired
	SchedulePaymentStoreRepo storerepo;

	@Autowired
	FundTransferInternalService ftservice;

	@Autowired
	ActionRepository actionRepository;

	@Autowired
	MakerRepository makerRepository;

	@Autowired
	CheckerRepository checkerRepository;

	@Autowired
	SignatureRepository signatureRepository;

	@Autowired
	ScheduledTransactionRepo schtranrepo;

	@Autowired
	UserRepository userRepository;

	@Autowired
	EnterprisesRepository enterprisesRepository;

	@Autowired
	UserRepository_V1 userRepository_V1;
	@Autowired
	MakerCheckerServiceFundTransfer checkerServiceFundTransfer;
	@Autowired
	MakerCheckerServiceImpsTransfer makercheckerServiceImpsTransfer;
	@Autowired
	FundTransferLogsEntryService fundTransferLogsEntryService;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	CommonExternalService commonExternalService;
	
	@Autowired
	ApplicationEnterprisRepository applicationEnterprisRepository;

	@Autowired
	GlobalProperties properties;

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
	@Value("${sendercd}")
	private String senderCD;
	@Value("${userid}")
	private String userId;
	@Value("${password}")
	private String password;
	@Value("${callback.url}")
	private String callbackURL;

	public ScheduledPayment createSchedulePaymentRecord(ScheduledPaymentStore store, Enterprises enterprise,
			User user) {
		ScheduledPayment record = new ScheduledPayment();
		if (store.getMode().equals("neft") || store.getMode().equals("fed2fed")) {
			record.setRemName(enterprise.getAccName());
			record.setBenAccType(store.getBenAccType());
		} else if (store.getMode().equals("p2p") || store.getMode().equals("p2a")) {
			record.setRemMobNum(store.getRemMobNum());
			record.setRemMmid(store.getRemMmid());
			record.setRemCustId(store.getRemCustId());
			record.setBenAadhar(store.getBenAadhar());
			record.setBenMmid(store.getBenMmid());
			record.setBenMobNum(store.getBenMobNum());
			record.setBenCustName(store.getBenCustName());

		}
		record.setRefNo(ref_prefix + "" + getRandomHexString(6).toUpperCase());
		record.setStatus("pending");
		record.setUpdatedAt(new Timestamp(new Date().getTime()));
		record.setCreatedAt(new Timestamp(new Date().getTime()));
		record.setEnterpriseId("" + enterprise.getId());
		record.setMode(store.getMode());
		record.setBenName(store.getBenName());
		record.setBenNickName(store.getBenNickName());
		record.setAmount(store.getAmount());
		record.setRemAccNo(store.getRemAccNo());
		record.setRemName(enterprise.getAccName());
		record.setBenIfsc(store.getBenIfsc());
		record.setBenAccNo(store.getBenAccNo());
		record.setRemarks(store.getRemarks());
		record.setCount(store.getCount());
		record.setStartDate(store.getStartDate());
		record.setNextExecutionDate(store.getNextExecutionDate());
		record.setFrequency(store.getFrequency());
		record.setRemainingCount(store.getRemainingCount());
		record.setMakerId(new BigDecimal(user.getId()));
		record.setLockVersion(BigDecimal.ZERO);
		record.setIsJobActive(BigDecimal.ZERO);
		record.setIsCancelled(BigDecimal.ZERO);
		record.setIsCompleted(BigDecimal.ZERO);
		schrepo.save(record);
		return record;
	}

	/**
	 * Update the End date based on the frequency
	 * 
	 * @param payment
	 * @return ScheduledPayment
	 */
	public ScheduledPayment setdates(ScheduledPayment payment) {
//		BigDecimal count = payment.getRemainingCount().subtract(BigDecimal.ONE);
//		payment.setRemainingCount(count);
		BigDecimal count = payment.getRemainingCount();
		payment.setRemainingCount(count);
		count=count.subtract(BigDecimal.ONE);
		
		ZonedDateTime start_date = ZonedDateTime.ofInstant(payment.getStartDate().toInstant(), ZoneId.systemDefault());
		if (payment.getFrequency().equalsIgnoreCase("daily")) {
			Date daily_date = Date.from(start_date.plusDays(count.longValue()).toInstant());
			payment.setEndDate(daily_date);
		} else if (payment.getFrequency().equalsIgnoreCase("weekly")) {
			Date daily_date = Date.from(start_date.plusWeeks(count.longValue()).toInstant());
			payment.setEndDate(daily_date);
		} else if (payment.getFrequency().equalsIgnoreCase("monthly")) {
			Date daily_date = Date.from(start_date.plusMonths(count.longValue()).toInstant());
			payment.setEndDate(daily_date);
		} else if (payment.getFrequency().equalsIgnoreCase("quarterly")) {
			Date daily_date = Date.from(start_date.plusMonths(count.longValue() * 3).toInstant());
			payment.setEndDate(daily_date);
		} else if (payment.getFrequency().equalsIgnoreCase("half_yearly")) {
			Date daily_date = Date.from(start_date.plusMonths(count.longValue() * 6).toInstant());
			payment.setEndDate(daily_date);
		} else if (payment.getFrequency().equalsIgnoreCase("yearly")) {
			Date daily_date = Date.from(start_date.plusYears(count.longValue()).toInstant());
			payment.setEndDate(daily_date);
		}
		schrepo.save(payment);
		return payment;
	}

	/**
	 * @param str_date
	 * @return Date
	 */
	public Date convertStringToDate(String str_date) {
		Date date = null;
		try {
			date = formatter.parse(str_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @param input
	 * @param benname
	 * @param ben_nick_name
	 * @param enterprise
	 * @return ScheduledPaymentStore
	 */
	public ScheduledPaymentStore createSchedulePaymentStoreRecord(SchedulePaymentsInput input, String benname,
			String ben_nick_name, Enterprises enterprise) {
		ScheduledPaymentStore record = new ScheduledPaymentStore();
		record.setAmount(input.getAmount().toString());
		record.setMode(input.getMode());
		record.setUpdatedAt(new Timestamp(new Date().getTime()));
		record.setCreatedAt(new Timestamp(new Date().getTime()));
		record.setRemAccNo(input.getRem_acc_no());
		record.setRemName(enterprise.getAccName());
		record.setBenAccNo(input.getBen_acc_no());
	    record.setBenName(benname);
		record.setBenNickName(ben_nick_name);
		record.setBenAccType(input.getBen_acc_type());
		record.setRemarks(input.getRemarks());
		record.setBenIfsc(input.getBen_ifsc());
		record.setEnterpriseId("" + enterprise.getId());
		record.setCount(new BigDecimal(input.getCount()));
		record.setStartDate(convertStringToDate(input.getStart_date()));
		if(input.getFrequency().equals("Half Yearly")) {
			record.setFrequency("half_yearly");
		}else {
		record.setFrequency(input.getFrequency().toLowerCase());
		}
		record.setRemainingCount(new BigDecimal(input.getCount()));
		record.setNextExecutionDate(convertStringToDate(input.getStart_date()));
		record.setTransactionId(generateTransactionID());
		if (input.getMode().equals("p2p")) {
			Optional<User> user = ftservice.getCurrentUserDetail(input.getAuthtoken());
			if (StringUtils.isBlank(input.getRem_mob_num())) {
				if (user.isPresent()) {
					record.setRemMobNum(user.get().getMobile());
				}
			} else {
				record.setRemMobNum(input.getRem_mob_num());
			}
			record.setBenMmid(input.getBen_mmid());
			record.setRemCustId(input.getRem_cust_id());
			record.setBenAadhar(input.getBen_aadhar());
			record.setBenMobNum(input.getBen_mob_num());
			record.setBenCustName(input.getBen_name());
			String value=benname.isEmpty()?input.getBen_name():benname;
			record.setBenName(value);
			value=ben_nick_name.isEmpty()?input.getBen_nick_name():ben_nick_name;
            record.setBenNickName(value);
            record.setBenAccNo("");
		}
		storerepo.save(record);
		return record;
	}

	public String generateTransactionID() {
		String currentDate = simpleDateFormat.format(new Date()).toString();
		return transaction_prefix + getRandomHexString(2) + "_" + currentDate;
	}

	private String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}
		return sb.toString().substring(0, numchars);
	}

	public SMEMessage validateAccountNumber(String account_number, boolean isremitter) {
		SMEMessage message = new SMEMessage();
		String errorCode = null;
		String errorMsg = null;

		String getCustDetailsResp = getCustDetails.getData(account_number);
		JSONObject convertXml = XML.toJSONObject(getCustDetailsResp);
		if (convertXml.getJSONObject("GetCustomerDetailsResp").get("AccountDetails") instanceof JSONObject) {
			JSONObject accountDetailsObj = convertXml.getJSONObject("GetCustomerDetailsResp")
					.getJSONObject("AccountDetails");

			errorCode = String.valueOf(convertXml.getJSONObject("GetCustomerDetailsResp")
					.getJSONObject("AccountDetails").get("ERRORCODE"));
			errorMsg = String.valueOf(
					convertXml.getJSONObject("GetCustomerDetailsResp").getJSONObject("AccountDetails").get("ERRORMSG"));

			if (errorCode.equals("00")) {
				if (isremitter) {
					String foracid = String.valueOf(accountDetailsObj.get("FORACID"));
					if (foracid.equals(account_number)) {
						message.setStatus(true);
						message.setMessage("Success");
					} else {
						message.setStatus(false);
						message.setMessage("Invalid Remitter account number.");
					}
				} else {
					message.setStatus(true);
					message.setMessage("Success");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Account number.");
			}
		}else {
			if(convertXml.getJSONObject("GetCustomerDetailsResp").get("AccountDetails") instanceof JSONArray) {
				JSONArray accounts=convertXml.getJSONObject("GetCustomerDetailsResp").getJSONArray("AccountDetails");
				 errorCode=null;
				 errorMsg=null;
				for(int i=0;i<accounts.length();i++) {
					JSONObject singleAccunt=accounts.getJSONObject(i);
					errorCode=String.valueOf(singleAccunt.get("ERRORCODE"));
					errorMsg=String.valueOf(singleAccunt.get("ERRORMSG"));
					String account=String.valueOf(singleAccunt.get("FORACID"));
					if(account.equals(account_number)) {
						if (errorCode.equals("00")) {
							if (isremitter) {
								String foracid = String.valueOf(singleAccunt.get("FORACID"));
								if (foracid.equals(account_number)) {
									message.setStatus(true);
									message.setMessage("Success");
								} else {
									message.setStatus(false);
									message.setMessage("Invalid Remitter account number.");
								}
							} else {
								message.setStatus(true);
								message.setMessage("Success");
							}

						} else {
							message.setStatus(false);
							message.setMessage("Invalid Account number.");
						}
					}
				}
			}
		}
		return message;
	}

	public SMEMessage maker(Optional<User> getUserDetails, String operation, ScheduledPayment payment,
			int requiredCount) {
		SMEMessage message = new SMEMessage();
		int final_required_count = 0;
		Operation operationSaveResp = null;
		Action actionSaveResp = null;

		try {

			if (getUserDetails.get().getRole() != null) {
				if (getUserDetails.get().getRole().equals("external")) {
					// getUserDetails.get().getRole().equals("external")
					final_required_count = requiredCount + 1;
					Operation operation_ = new Operation();
					operation_.setUserId(String.valueOf(getUserDetails.get().getId()));
					operation_.setOperationName(operation);
					// TODO Need to test maker checker in benificiary and fix this.
					operation_.setRequiredSignatureCount(String.valueOf(final_required_count));
					operation_.setCurrentSignatureCount("0");
					operation_.setCreatedAt(new Timestamp(new Date().getTime()));
					operation_.setUpdatedAt(new Timestamp(new Date().getTime()));
					operationSaveResp = operationRepository.save(operation_);
//					RecordLog.writeLogFile("operationSaveResp " + operationSaveResp);
				} else {
					Operation operation_ = new Operation();
					operation_.setUserId(String.valueOf(getUserDetails.get().getId()));
					operation_.setOperationName(operation);
					operation_.setRequiredSignatureCount(String.valueOf(requiredCount));
					operation_.setCurrentSignatureCount("0");
					operation_.setCreatedAt(new Timestamp(new Date().getTime()));
					operation_.setUpdatedAt(new Timestamp(new Date().getTime()));
					operationSaveResp = operationRepository.save(operation_);
//					RecordLog.writeLogFile("operationSaveResp " + operationSaveResp);
				}

			} else {
				Operation operation_ = new Operation();
				operation_.setUserId(String.valueOf(getUserDetails.get().getId()));
				operation_.setOperationName(operation);
				operation_.setRequiredSignatureCount(String.valueOf(requiredCount));
				operation_.setCurrentSignatureCount("0");
				operation_.setCreatedAt(new Timestamp(new Date().getTime()));
				operation_.setUpdatedAt(new Timestamp(new Date().getTime()));
				operationSaveResp = operationRepository.save(operation_);
//				RecordLog.writeLogFile("operationSaveResp " + operationSaveResp);
			}

			Action action = new Action();
			action.setUserId(String.valueOf(getUserDetails.get().getId()));
			action.setOperationId(String.valueOf(operationSaveResp.getId()));
			action.setUserName(getUserDetails.get().getUserName());
			action.setPrefNo(getUserDetails.get().getPrefNo());
			action.setCreatedAt(new Timestamp(new Date().getTime()));
			action.setUpdatedAt(new Timestamp(new Date().getTime()));
			action.setStatus("initiated");

			actionSaveResp = actionRepository.save(action);
			RecordLog.writeLogFile("actionSaveResp " + actionSaveResp);

			Maker maker = new Maker();
			maker.setUserId(String.valueOf(getUserDetails.get().getId()));
			maker.setActionId(String.valueOf(actionSaveResp.getId()));
			maker.setCreatedAt(new Timestamp(new Date().getTime()));
			maker.setUpdatedAt(new Timestamp(new Date().getTime()));
			makerRepository.save(maker);

//			RecordLog.writeLogFile("maker " + maker);
			payment.setOperationId(String.valueOf(operationSaveResp.getId()));
			RecordLog.writeLogFile("operation Id:  " + operationSaveResp.getId());
			schrepo.save(payment);
			if (payment != null) {
				message.setStatus(true);
				message.setMessage("Maker created.");
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * Copied from Maker Checker service.
	 * 
	 * @param getUserDetails
	 * @param payment
	 * @return
	 */
	public synchronized SMEMessage checker(Optional<User> getUserDetails, ScheduledPayment payment) {
		SMEMessage message = new SMEMessage();
		Operation operation = null;
		Signature signature = null;
		Signature signatureResp = null;
		Checker checker = null;
		Checker checkerResp = null;
		Operation operationResp = null;
		int count;
		int actualCount;
		int requiredCount;

		if (payment == null) {
			message.setStatus(false);
			message.setMessage("Scheduled Payment is empty");
			return message;
		} else {
			if (payment.getStatus().equals("approved") || payment.getStatus().equals("rejected")
					|| payment.getStatus().equals("deleted") || payment.getStatus().equals("already_approved")) {
				message.setStatus(false);
				message.setMessage("Request is already approved/rejected/deleted.");
				message.setDescription(payment.getStatus());
				return message;
			} else {
				RecordLog.writeLogFile("Scheduled apyment status: " + payment.getStatus());
				Optional<Operation> Optionaloperation = operationRepository
						.findById(Long.valueOf(payment.getOperationId()));
				operation = Optionaloperation.get();
//				RecordLog.writeLogFile("operation " + operation);
				List<Signature> signs = signatureRepository.findByOperationIdAndStatus("" + operation.getId(),
						"approved");
				if (signs.size() > 0) {
					signature = signs.get(0);
				}
//				RecordLog.writeLogFile("signature " + signature);
				if (signs.size() >Integer.valueOf( Optionaloperation.get().getRequiredSignatureCount())) {
					message.setStatus(false);
					message.setMessage("No signature available.");
					message.setDescription("already_approved");
				} else {
					signature = new Signature();
					signature.setUserId(String.valueOf(getUserDetails.get().getId()));
					signature.setOperationId(payment.getOperationId());
					signature.setStatus("approved");
					signature.setUserName(getUserDetails.get().getUserName());
					signature.setPrefNo(getUserDetails.get().getPrefNo());
					signature.setCreatedAt(new Timestamp(new Date().getTime()));
					signature.setUpdatedAt(new Timestamp(new Date().getTime()));
					signatureResp = signatureRepository.save(signature);

//					RecordLog.writeLogFile("signatureResp " + signatureResp);

					checker = new Checker();
					checker.setUserId(String.valueOf(getUserDetails.get().getId()));
					checker.setStatus("approved");
					checker.setSignatureId(String.valueOf(signatureResp.getId()));
					checker.setCreatedAt(new Timestamp(new Date().getTime()));
					checker.setUpdatedAt(new Timestamp(new Date().getTime()));
					checkerResp = checkerRepository.save(checker);

					count = Integer.valueOf(operation.getCurrentSignatureCount());
					actualCount = Integer.valueOf(operation.getCurrentSignatureCount());
					requiredCount = Integer.valueOf(operation.getRequiredSignatureCount());

//					RecordLog.writeLogFile("count " + count);
					RecordLog.writeLogFile("actualCount " + actualCount);
					RecordLog.writeLogFile("requiredCount " + requiredCount);

					if (actualCount != requiredCount) {
						count += 1;
//						RecordLog.writeLogFile("count count" + count);
					}
					operation.setUpdatedAt(new Timestamp(new Date().getTime()));
					operation.setCurrentSignatureCount(String.valueOf(count));
					operationResp = operationRepository.save(operation);
//					RecordLog.writeLogFile("operationResp " + operationResp);
					// fund.save!
					if (Integer.valueOf(operationResp.getRequiredSignatureCount()) == Integer
							.valueOf(operationResp.getCurrentSignatureCount())) {
						message.setStatus(true);
						message.setDescription("success");
					} else {
						message.setStatus(false);
						message.setDescription("signed");

					}
				}
			}
		}

		return message;
	}

	/**
	 * Logically this should accept only param where parent is transfer.Because it
	 * is a migration project going with different objects.It can be done using
	 * Generics Also.To quickly close API's going with this approach please think of
	 * changing this.
	 * 
	 * @param user
	 * @param payment
	 * @param transfer
	 * @param impstransfer
	 * @return MakerCheckerListGeneric
	 */
	public MakerCheckerListGeneric makerCheckerList(User user, ScheduledPayment payment, FundTransfer transfer,
			ImpsTransfer impstransfer) {
		MakerCheckerListGeneric makerCheckerListModel = new MakerCheckerListGeneric();
		//Operation operation = null;
		List<Signature> listSign=null;
		Action action = null;
		Signature signature = null;
		String operationId = null;
		String status = "";
		if (payment != null) {
			operationId = payment.getOperationId();
			status = payment.getStatus();
			RecordLog.writeLogFile("payment id:" + payment.getId());
		} else if (transfer != null) {
			operationId = transfer.getOperationId();
			status = transfer.getStatus();
		} else if (impstransfer != null) {
			operationId = impstransfer.getOperationId();
			status = impstransfer.getStatus();
		}
		String role = user.getRole() != null ? user.getRole() : "";
		RecordLog.writeLogFile("user:" + user.getId() + ":Role:" + role + ":Operation ID:" + operationId);
		if (!role.equals("external")) {
			if (StringUtils.isNotBlank(operationId)) {
//				RecordLog.writeLogFile("operationId is available:" + operationId);
				//operation = operationRepository.findById(Long.valueOf(operationId)).get();
				action = actionRepository.findByOperationId(operationId);
				// FIXME Current UI will break in case we are returning array so as work around
				// one signature is getting passed.
//				Long signature_cont = signatureRepository.countByOperationIdAndStatus(operationId,
//						"approved");
//				if (signature_cont > 0) {
//					signature = signatureRepository
//							.findByOperationIdAndStatus(operationId, "approved").get(0);
//				}
//				Long rej_signature_cont = signatureRepository
//						.countByOperationIdAndStatus(operationId, "rejected");
//				if (rej_signature_cont > 0) {
//					signature = signatureRepository
//							.findByOperationIdAndStatus(operationId, "rejected").get(0);
//				}

				listSign = signatureRepository
						.findByOperationId(operationId);
			if(listSign.size()>0) {
				signature=listSign.get(0);
			}
				
				if (payment != null) {
					makerCheckerListModel.setPayment(payment);
				} else if (transfer != null) {
					makerCheckerListModel.setFt(convertObject(transfer));
				} else if (impstransfer != null) {
					makerCheckerListModel.setFt(convertObject(impstransfer));
				}

				if (StringUtils.isNotBlank(operationId)) {
					if (action != null) {
						RecordLog.writeLogFile("Maker:" + action.toString());
					}
					if (signature != null) {
						RecordLog.writeLogFile("Checker:" + signature.toString());
					}
					makerCheckerListModel.setMaker(action);
					makerCheckerListModel.setChecker(signature);
				} else {
					RecordLog.writeLogFile("operationId id is not available. There are some data inaccuracy's plese fix it.");
				}
				if (!status.equals("approved")) {
					makerCheckerListModel.setApprovalPermission(is_approver(user, operationId));
				} else {
					makerCheckerListModel.setApprovalPermission(false);
				}

				makerCheckerListModel.setUserNull(false);
				makerCheckerListModel.setOperationNull(false);
			} else {
				if (payment != null) {
					makerCheckerListModel.setPayment(payment);
				} else if (transfer != null) {
					makerCheckerListModel.setFt(convertObject(transfer));
				} else if (impstransfer != null) {
					makerCheckerListModel.setFt(convertObject(impstransfer));
				}
				makerCheckerListModel.setUserNull(false);
				makerCheckerListModel.setOperationNull(true);
			}
		} else {
			// External Users & Imported cases will be handled here.

//			RecordLog.writeLogFile("Status :" + status);
			if (status.equals("imported")) {
				// Maker checker won't be there for status where it is imported.
				//operation = null;
				action = null;
				signature = null;
				if (payment != null) {
					makerCheckerListModel.setPayment(payment);
				} else if (transfer != null) {
					makerCheckerListModel.setFt(convertObject(transfer));
				} else if (impstransfer != null) {
					makerCheckerListModel.setFt(convertObject(impstransfer));
				}

				makerCheckerListModel.setMaker(action);
				makerCheckerListModel.setChecker(signature);
				makerCheckerListModel.setApprovalPermission(false);
			} else {

				RecordLog.writeLogFile("Operation id:" + operationId);
				if (StringUtils.isNotBlank(operationId)) {
					//operation = operationRepository.findById(Long.valueOf(operationId)).get();
					action = actionRepository.findByOperationId(operationId);
					// FIXME Current UI will break in case we are returning array so as work around
					// one signature is getting passed.
//					Long signature_cont = signatureRepository
//							.countByOperationIdAndStatus(operationId, "approved");
//					if (signature_cont > 0) {
//						signature = signatureRepository
//								.findByOperationIdAndStatus(operationId, "approved").get(0);
//					}
//					Long rej_signature_cont = signatureRepository
//							.countByOperationIdAndStatus(operationId, "rejected");
//					if (rej_signature_cont > 0) {
//						signature = signatureRepository
//								.findByOperationIdAndStatus(operationId, "rejected").get(0);
//					}
					listSign = signatureRepository
							.findByOperationId(operationId);
				if(listSign.size()>0) {
					signature=listSign.get(0);
				}
					if (action != null) {
						RecordLog.writeLogFile("Maker:" + action.toString());
					}
					if (signature != null) {
						RecordLog.writeLogFile("Checker:" + signature.toString());
					}

					if (payment != null) {
						makerCheckerListModel.setPayment(payment);
					} else if (transfer != null) {
						makerCheckerListModel.setFt(convertObject(transfer));
					} else if (impstransfer != null) {
						makerCheckerListModel.setFt(convertObject(impstransfer));
					}

					makerCheckerListModel.setMaker(action);
					makerCheckerListModel.setChecker(signature);
					if (!status.equals("approved")) {
						makerCheckerListModel.setApprovalPermission(is_approver(user, operationId));
					} else {
						makerCheckerListModel.setApprovalPermission(false);
					}
				} else {
					// Where operation is not available. Just to handle invalid data.
					if (payment != null) {
						makerCheckerListModel.setPayment(payment);
					} else if (transfer != null) {
						makerCheckerListModel.setFt(convertObject(transfer));
					} else if (impstransfer != null) {
						makerCheckerListModel.setFt(convertObject(impstransfer));
					}

					makerCheckerListModel.setMaker(null);
					makerCheckerListModel.setChecker(null);
					makerCheckerListModel.setApprovalPermission(false);
				}

			}

		}

		return makerCheckerListModel;
	}

	public SMEMessage reject(User user, ScheduledPayment payment, FundTransferDTO transfer) {

		SMEMessage message = new SMEMessage();
		boolean eligible_reject = false;
		Signature signatureResp = null;
		String operation_id = null;
		String status = "";
		try {
			if (payment != null) {
				status = payment.getStatus();
				if (payment.getStatus().equals("pending") || payment.getStatus().equals("new")
						|| payment.getStatus().equals("pending_approval")) {
					// If the request is not approved it cannot be rejected.
					eligible_reject = true;
					operation_id = payment.getOperationId();
				}
			} else if (transfer != null) {
				status = transfer.getStatus();
				if (transfer.getStatus().equals("pending") || transfer.getStatus().equals("new")
						|| transfer.getStatus().equals("pending_approval")) {
					// If the request is not approved it cannot be rejected.
					eligible_reject = true;
					operation_id = transfer.getOperationId();
				}
			}

			RecordLog.writeLogFile("eligible to reject schedule payment: " + eligible_reject);

			if (eligible_reject == true) {
				Signature signature = new Signature();
				signature.setUserId(String.valueOf(user.getId()));
				signature.setOperationId(operation_id);
				signature.setStatus("rejected");
				signature.setUserName(user.getUserName());
				signature.setPrefNo(user.getPrefNo());
				signature.setCreatedAt(new Timestamp(new Date().getTime()));
				signature.setUpdatedAt(new Timestamp(new Date().getTime()));
				signatureResp = signatureRepository.save(signature);
//				RecordLog.writeLogFile("signatureResp " + signatureResp);
				if (signatureResp == null) {
					message.setStatus(false);
					message.setMessage("Failed");
				} else {
//					RecordLog.writeLogFile("signatureResp " + signatureResp.toString());

					Checker checker = new Checker();
					checker.setUserId("" + user.getId());
					checker.setStatus("rejected");
					checker.setSignatureId(String.valueOf(signatureResp.getId()));
					checker.setCreatedAt(new Timestamp(new Date().getTime()));
					checker.setUpdatedAt(new Timestamp(new Date().getTime()));
					checkerRepository.save(checker);

					message.setStatus(true);
					message.setMessage("success");
				}
			} else {
				message.setStatus(true);
				message.setMessage(status);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
		}

		return message;
	}

	public List<ScheduledTransaction> getScheduledTransactions(ScheduledPayment payment) {
		List<ScheduledTransaction> transactionlist = new ArrayList<ScheduledTransaction>();
		List<String> status = Arrays.asList("failed", "pending", "approved", "success");
		transactionlist = schtranrepo.findByScheduledPaymentIdAndStatusInOrderByIdDesc("" + payment.getId(), status);
		return transactionlist;
	}

	public boolean enterprise_sole_proprietorship(Optional<User> getUserDetails) {
		boolean enterpriseSoleProprietorshipResp = false;
		String enterpriseId = null;
		Optional<Enterprises> getEnterprise = null;
		Enterprises enterprises = null;

		enterpriseId = getUserDetails.get().getEnterpriseId();
		RecordLog.writeLogFile("enterprise_sole_proprietorship enterpriseId " + enterpriseId);
		getEnterprise = enterprisesRepository.findById(Long.valueOf(enterpriseId));
//		RecordLog.writeLogFile("getEnterprise __ " + getEnterprise.toString());
		if (getEnterprise.isPresent() == true) {
			enterprises = getEnterprise.get();
			if (enterprises.getConstitution().equals("SP")) {
				if (getUserDetails.get().getRole() != null) {
					if (!getUserDetails.get().getRole().equals("external")) {
						enterpriseSoleProprietorshipResp = true;
					}
				} else {
					enterpriseSoleProprietorshipResp = true;
				}

			} else {
				enterpriseSoleProprietorshipResp = false;
			}
		} else {
			enterpriseSoleProprietorshipResp = false;
		}
		RecordLog.writeLogFile("enterprise SoleProprietorship response: " + enterpriseSoleProprietorshipResp);
		return enterpriseSoleProprietorshipResp;
	}

	public boolean enterprise_zero_external_user_authorize(Optional<User> getUserDetails) {
		boolean enterpriseZeroExternalUserAuthorize = false;

		String enterpriseId = null;
		Optional<Enterprises> getEnterprise = null;
		Enterprises enterprises = null;
//		RecordLog.writeLogFile("getUserDetails isPresent " + getUserDetails.isPresent());
		enterpriseId = getUserDetails.get().getEnterpriseId();
		getEnterprise = enterprisesRepository.findById(Long.valueOf(enterpriseId));
//		RecordLog.writeLogFile("getEnterprise " + getEnterprise.toString());
		try {
			if (getEnterprise.isPresent() == true) {
				enterprises = getEnterprise.get();
				ApplicationEnterpris applicationEnterpris=applicationEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterprises.getApplicationFormId(),enterprises.getPrefCorp());
				if (applicationEnterpris.getAuthExt() != null) {
					if (applicationEnterpris.getAuthFund().equals("0")) {
						if (getUserDetails.get().getRole() != null) {
							if (!getUserDetails.get().getRole().equals("external")) {
								enterpriseZeroExternalUserAuthorize = true;
							}
						} else {
							enterpriseZeroExternalUserAuthorize = true;
						}

					} else {
						enterpriseZeroExternalUserAuthorize = false;
					}
				} else {
					enterpriseZeroExternalUserAuthorize = false;
				}
			} else {
				enterpriseZeroExternalUserAuthorize = false;
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			enterpriseZeroExternalUserAuthorize = false;
		}
		RecordLog.writeLogFile("enterpriseZeroExternalUserAuthorize:" + enterpriseZeroExternalUserAuthorize);
		return enterpriseZeroExternalUserAuthorize;
	}

	public FundTransferDTO convertObject(FundTransfer transfer) {
		FundTransferDTO dto = new FundTransferDTO();
		dto.setType("FT");
		dto.setId(transfer.getId());
		dto.setAmount(transfer.getAmount());
		dto.setBenAccNo(transfer.getBenAccNo());
		dto.setBenIfsc(transfer.getBenIfsc());
		dto.setBenNickName(transfer.getBenNickName() == null || transfer.getBenNickName().equals("NIL")
				|| transfer.getBenNickName().equals("") ? "" : transfer.getBenNickName());
		dto.setCreatedAt(transfer.getCreatedAt());
		dto.setUpdatedAt(transfer.getUpdatedAt());
		dto.setEnterpriseId(transfer.getEnterpriseId());
		dto.setDuplicate(transfer.isDuplicate());
		dto.setMode(transfer.getMode());
		dto.setOperationId(transfer.getOperationId());
		dto.setProgress(transfer.getProgress());
		dto.setRefNo(transfer.getRefNo());
		dto.setRemName(transfer.getRemName());
		dto.setRemarks(transfer.getRemarks());
		dto.setSenderRefId(transfer.getSenderRefId());
		dto.setStatus(transfer.getStatus());
		dto.setTransactionDate(transfer.getTransactionDate());
		dto.setTransferType(transfer.getTransferType());
		dto.setBenAccType(transfer.getBenAccType());
		dto.setBenName(
				(transfer.getBenName()==null || transfer.getBenName().equals("NIL")) ? "" : transfer.getBenName());
		dto.setRemAccNo(transfer.getRemAccNo());
		dto.setGatewayStatus(transfer.getGatewayStatus());
		dto.setResponseCode(transfer.getResponseCode());
		dto.setResponseReason(transfer.getResponseReason());
		return dto;
	}

	public FundTransferDTO convertObject(ImpsTransfer transfer) {
		FundTransferDTO dto = new FundTransferDTO();
		dto.setType("IMPS");
		dto.setId(transfer.getId());
		dto.setAmount(transfer.getAmount());
		dto.setBenAccNo(transfer.getBenAccNo());
		dto.setBenIfsc(transfer.getBenIfsc());
		dto.setBenNickName((transfer.getBenNickName()==null || transfer.getBenNickName().equals("NIL") || transfer.getBenNickName().equals("")) ? ""
				: transfer.getBenNickName());
		dto.setCreatedAt(transfer.getCreatedAt());
		dto.setUpdatedAt(transfer.getUpdatedAt());
		dto.setEnterpriseId(transfer.getEnterpriseId());
		dto.setDuplicate(transfer.isDuplicate());
		dto.setMode(transfer.getMode());
		dto.setOperationId(transfer.getOperationId());
		dto.setProgress(transfer.getProgress());
		dto.setRefNo(transfer.getRefNo());
		dto.setRemName(transfer.getRemName());
		dto.setRemarks(transfer.getRemarks());
		dto.setSenderRefId(transfer.getSenderRefId());
		dto.setStatus(transfer.getStatus());
		dto.setTransactionDate(transfer.getTransactionDate());
		dto.setTransferType(transfer.getTransferType());
		dto.setBenAadhar(transfer.getBenAadhar());
		dto.setBenCustName((transfer.getBenCustName() == null || transfer.getBenCustName().equals("NIL") || transfer.getBenCustName().equals(""))  ? ""
				: transfer.getBenCustName());
		dto.setBenMmid(transfer.getBenMmid());
		dto.setBenMobNum(transfer.getBenMobNum());
		dto.setRemAccNo(transfer.getRemAccNum());
		dto.setRemCustId(transfer.getRemCustId());
		dto.setRemMmid(transfer.getRemMmid());
		dto.setRemMobNum(transfer.getRemMobNum());
		dto.setGatewayStatus(transfer.getGatewayStatus());
		dto.setResponseCode(transfer.getResponseCode());
		dto.setResponseReason(transfer.getResponseReason());
		return dto;

	}

	/**
	 * This is very critical logic. Based on the existing ruby code 1)By default
	 * there approve button should not come for external user. 2)If a primary user
	 * signature is already there operations table then also the approval should not
	 * come.
	 * 
	 * @param user
	 * @param operation
	 * @return boolean
	 */
	boolean is_approver(User user, String operation) {
		Boolean isApprover = false;
		List<Action> action = null;
		Signature signature = null;
		String role = user.getRole() != null ? user.getRole() : "";

		if (StringUtils.isNotBlank(role)) {
			if (role.equals("external")) {
				return false;
			}
		}
		action = actionRepository.findByOperationIdOrderByIdAsc(operation);
		if (action == null) {
			// Invalid case so false is returned.
			isApprover = false;
		} else {
			if (action.get(0).getUserId().equals("" + user.getId())) {
				// This is a case where for a primary user who created the request the approve
				// button is not visible to him.
				return isApprover;
			} else {
				isApprover = true;
			}
			List<Signature> signs = signatureRepository.findByOperationIdAndStatus("" + operation, "approved");
			// The below case won't come in the way currently way it is programmed. Just for
			// handling future cases.
			if (!CollectionUtils.isEmpty(signs)) {
				for (Signature sign : signs) {
					RecordLog.writeLogFile("Signature of " + sign.getCustomerName() + " userID: " + sign.getUserId()
							+ " current user id :" + user.getId());
					// If users approves a request a signature will be there. So if he access the
					// same request once again it will disable approve and reject buttons.
					if (sign.getUserId().equals("" + user.getId())) {
						isApprover = false;
						return isApprover;
					}
				}
			}
		}

		return isApprover;
	}

	public Optional<User> getCurrentUserDetail(String authToken) {
		Optional<User> getCurrentUserDetail = null;
		getCurrentUserDetail = userRepository_V1.findByAuthToken(authToken);
		if (getCurrentUserDetail.isPresent()) {
			RecordLog.writeLogFile("getCurrentUserDetail " + getCurrentUserDetail.get().toString());
		}
		return getCurrentUserDetail;
	}

	public String generateSenderRefNo(String mode) {
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
			String prefix = "SMEIM";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		}
		return refNo;
	}

	private String createNeftTransferApprovalReq(Optional<ScheduledTransaction> fundTransfer, String senderRefNo) {
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

	private String createFundTransferApprovalReq(Optional<ScheduledTransaction> fundTransfer, String senderRefNo) {
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

		sb.append("\"Name\":\"" + getTrimmedValue(fundTransfer.get().getRemName()) + "\",");

		sb.append("\"AccNumber\":\"" + fundTransfer.get().getRemAccNo() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

//		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(fundTransfer.get().getRemMobNum()) != null ? ""
//				: fundTransfer.get().getRemMobNum() + "\",");
		sb.append("\"Mobile\":\"" + "" + "\","); // need to get column added in fundtransfer

		sb.append("\"Email\":\" \",");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue(fundTransfer.get().getBenName()) + "\",");

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

	public ApproveTranRespModel approveIntrabankProcess(Optional<ScheduledPayment> payment,
			Optional<ScheduledTransaction> schTransaction, Optional<User> user) {
		boolean validDailyTransaction = false;
		boolean validMonthlyTransaction = false;
		Optional<User> getCurrentUserDetails = null;
		String senderRefId = null;
		String remarks = null;
		String benName = null;
		String mode = null;

		Optional<Enterprises> enterprises = enterprisesRepository
				.findById(Long.valueOf(payment.get().getEnterpriseId()));
		if (enterprises.isPresent()) {

			getCurrentUserDetails = user;
			if (getCurrentUserDetails.isPresent()) {

				/* TODO check with vikas */
				SMEMessage approvalMsg = checker(getCurrentUserDetails, payment.get());
				String approval = approvalMsg.getMessage();
//				fundTransferLogsEntryService.approveSchPayLog(schTransaction.get(),
//						getCurrentUserDetails.get().getPrefNo(), enterprises.get().getPrefCorp());
				if (approval.equals("success")) {
					if (schTransaction.get().getProgress() != null
							&& !schTransaction.get().getProgress().equals("initiated")) {
						if (schTransaction.get().getProgress() != null
								&& !schTransaction.get().getProgress().equals("completed")) {
							mode = schTransaction.get().getMode();
							schTransaction.get().setProgress("initiated");
							schtranrepo.save(schTransaction.get());
							if (mode.equals("fed2fed")) {
								remarks = StringUtils.isEmpty(schTransaction.get().getRemarks()) ? "FED2FED"
										: schTransaction.get().getRemarks();
								benName = StringUtils.isEmpty(schTransaction.get().getBenName()) ? "SMEMB QP"
										: schTransaction.get().getBenName();
								senderRefId = StringUtils.isEmpty(schTransaction.get().getSenderRefId())
										? generateSenderRefNo(schTransaction.get().getMode())
										: schTransaction.get().getSenderRefId();
								String mandatoryField = checkmandatoryFieldFT(schTransaction, mode);
								if (mandatoryField.equals("")) {
									String request = createFundTransferApprovalReq(schTransaction, senderRefId);
									RecordLog.writeLogFile("Intrabank Req String : " + request.toString());
									HttpHeaders headers = new HttpHeaders();
									headers.setContentType(MediaType.APPLICATION_JSON);
//									headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
									HttpEntity<String> entity = new HttpEntity<String>(request, headers);
									ResponseEntity<?> ftResponse = restTemplate.postForEntity(
											fblgatewayurl + "/gateway/fundtransfer/ift", entity, String.class);
									RecordLog.writeLogFile("Gateway Response" + ftResponse);

									if (ftResponse.getStatusCodeValue() == 200) {
										if (ftResponse.hasBody()) {
											JSONObject jsonObject = new JSONObject(ftResponse.getBody().toString());

											auditLogService.FTSchlog(schTransaction.get(), "fed2fed",
													jsonObject.getJSONObject("recordId").getString("responseCode"),
													enterprises);
//											fundTransferLogsEntryService.transactionSchLog(schTransaction.get(),
//													jsonObject.getJSONObject("recordId").getString("responseCode"),
//													getCurrentUserDetails.get().getPrefNo(),
//													enterprises.get().getPrefCorp());
											if (jsonObject.getBoolean("status")) {
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												String[] codes=properties.getGatewaysuccess().split(",");
												ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
												RecordLog.writeLogFile("Status codes: "+allowedIps);
												if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {
													ScheduledTransaction ftObj = schTransaction.get();
													ftObj.setStatus("approved");
													ftObj.setProgress("completed");
													// FIXME
//													ftObj.setResponseCode(jsonObject.getJSONObject("recordId")
//															.getString("responseCode"));
													ftObj.setReason(
															jsonObject.getJSONObject("recordId").getString("reason"));
													schtranrepo.save(ftObj);

													approveTranRespModel.setStatus(true);
													approveTranRespModel.setDescription(jsonObject
															.getJSONObject("recordId").getString("ReferenceId"));
													approveTranRespModel.setMessage(
															jsonObject.getJSONObject("recordId").getString("reason"));
													approveTranRespModel.setScheduledTransaction(schTransaction.get());

												} else {
													ScheduledTransaction ftObj = schTransaction.get();
													ftObj.setStatus("approved");
													ftObj.setProgress("completed");
													// FIXME
//													ftObj.setResponseCode(jsonObject.getJSONObject("recordId")
//															.getString("responseCode"));
													ftObj.setReason(
															jsonObject.getJSONObject("recordId").getString("reason"));
													schtranrepo.save(ftObj);
													checkerServiceFundTransfer.rollbackSchTransaction(
															getCurrentUserDetails, schTransaction);
													approveTranRespModel.setStatus(false);
													approveTranRespModel.setDescription(jsonObject
															.getJSONObject("recordId").getString("ReferenceId"));
													approveTranRespModel.setMessage(
															jsonObject.getJSONObject("recordId").getString("reason"));
													approveTranRespModel.setScheduledTransaction(schTransaction.get());
												}

												return approveTranRespModel;
											} else {
												/**
												 * Vikas to right rollback
												 */
												checkerServiceFundTransfer.rollbackSchTransaction(getCurrentUserDetails,
														schTransaction);
												if (jsonObject.getJSONObject("recordId").getString("responseCode")
														.equals("121")
														|| jsonObject.getJSONObject("recordId")
																.getString("responseCode").equals("116")) {
													schTransaction.get().setProgress("nil");
												} else {
													schTransaction.get().setStatus("deleted");
												}
												schtranrepo.save(schTransaction.get());
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage(
														jsonObject.getJSONObject("recordId").getString("reason"));
												approveTranRespModel.setScheduledTransaction(schTransaction.get());
												return approveTranRespModel;
											}
										} else {
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setDescription("Gateway Response");
											approveTranRespModel.setMessage("no response from gateway");
											approveTranRespModel.setFundTransfer(null);
											return approveTranRespModel;
										}
									} else {
										ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
										approveTranRespModel.setStatus(false);
										approveTranRespModel.setMessage(ftResponse.toString());
										approveTranRespModel.setFundTransfer(null);
										return approveTranRespModel;
									}
								} else {
									ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
									approveTranRespModel.setStatus(false);
									approveTranRespModel.setMessage(mandatoryField);
									approveTranRespModel.setFundTransfer(null);
									return approveTranRespModel;
								}
							} else if (mode.equals("neft")) {

								remarks = StringUtils.isEmpty(schTransaction.get().getRemarks()) ? "NEFT"
										: schTransaction.get().getRemarks();
								benName = StringUtils.isEmpty(schTransaction.get().getBenName()) ? "SMEMB QP"
										: schTransaction.get().getBenName();
								senderRefId = StringUtils.isEmpty(schTransaction.get().getSenderRefId())
										? generateSenderRefNo(schTransaction.get().getMode())
										: schTransaction.get().getSenderRefId();

								String request = createNeftTransferApprovalReq(schTransaction, senderRefId);

								RecordLog.writeLogFile("NEFT Req String : " + request);
								HttpHeaders headers = new HttpHeaders();
								headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
								HttpEntity<String> entity = new HttpEntity<String>(request, headers);
								ResponseEntity<?> ftResponse = restTemplate.postForEntity(
										fblgatewayurl + "/gateway/fundtransfer/neft", entity, String.class);
								RecordLog.writeLogFile("Gateway Response" + ftResponse);

								if (ftResponse.getStatusCodeValue() == 200) {
									if (ftResponse.hasBody()) {
										JSONObject jsonObject = new JSONObject(ftResponse.getBody().toString());

										auditLogService.FTSchlog(schTransaction.get(), "neft",
												jsonObject.getJSONObject("recordId").getString("responseCode"),
												enterprises);
//										fundTransferLogsEntryService.transactionSchLog(schTransaction.get(),
//												jsonObject.getJSONObject("recordId").getString("responseCode"),
//												getCurrentUserDetails.get().getPrefNo(),
//												enterprises.get().getPrefCorp());
										if (jsonObject.getBoolean("status")) {
											String[] codes=properties.getGatewaysuccess().split(",");
											ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
											RecordLog.writeLogFile("Status codes: "+allowedIps);
											if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {
												ScheduledTransaction ftObj = schTransaction.get();
												ftObj.setStatus("approved");
												ftObj.setProgress("completed");
//												ftObj.setResponseCode(
//														jsonObject.getJSONObject("recordId").getString("responseCode"));
												ftObj.setReason(
														jsonObject.getJSONObject("recordId").getString("reason"));
												schtranrepo.save(ftObj);

												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(true);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage(
														jsonObject.getJSONObject("recordId").getString("reason"));
												approveTranRespModel.setScheduledTransaction(schTransaction.get());
												return approveTranRespModel;
											} else {
												ScheduledTransaction ftObj = schTransaction.get();
												ftObj.setStatus("approved");
												ftObj.setProgress("completed");
//												ftObj.setResponseCode(
//														jsonObject.getJSONObject("recordId").getString("responseCode"));
												ftObj.setReason(
														jsonObject.getJSONObject("recordId").getString("reason"));
												schtranrepo.save(ftObj);

												checkerServiceFundTransfer.rollbackSchTransaction(getCurrentUserDetails,
														schTransaction);
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setDescription(
														jsonObject.getJSONObject("recordId").getString("ReferenceId"));
												approveTranRespModel.setMessage(
														jsonObject.getJSONObject("recordId").getString("reason"));
												approveTranRespModel.setScheduledTransaction(ftObj);
											}
										} else {

											/**
											 * Vikas to right rollback
											 */
											checkerServiceFundTransfer.rollbackSchTransaction(getCurrentUserDetails,
													schTransaction);
											if (jsonObject.getJSONObject("recordId").getString("responseCode")
													.equals("121")
													|| jsonObject.getJSONObject("recordId").getString("responseCode")
															.equals("116")) {
												schTransaction.get().setProgress("nil");
											} else {
												schTransaction.get().setStatus("deleted");
											}
											schtranrepo.save(schTransaction.get());
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setDescription("Gateway Response");
											approveTranRespModel.setMessage(
													jsonObject.getJSONObject("recordId").getString("reason"));
											approveTranRespModel.setScheduledTransaction(schTransaction.get());
											return approveTranRespModel;
										}
									} else {
										ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
										approveTranRespModel.setStatus(false);
										approveTranRespModel.setDescription("Gateway Response");
										approveTranRespModel.setMessage("no response from gateway");
										approveTranRespModel.setFundTransfer(null);
										return approveTranRespModel;
									}
								} else {
									ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
									approveTranRespModel.setStatus(false);
									approveTranRespModel.setMessage(ftResponse.toString());
									approveTranRespModel.setFundTransfer(null);
									return approveTranRespModel;
								}

							}
						} else {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(false);
							approveTranRespModel.setMessage("Already Completed");
							approveTranRespModel.setDescription("Already Completed");
							return approveTranRespModel;
						}
					} else {
						ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
						approveTranRespModel.setStatus(false);
						approveTranRespModel.setMessage("Already Initiated");
						approveTranRespModel.setDescription("Already Initiated");
						return approveTranRespModel;
					}
				} else if (approval.equals("signed")) {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(true);
					approveTranRespModel.setMessage("sucess");
					approveTranRespModel.setDescription("Signed");
					return approveTranRespModel;
				} else if (approval.equals("already_approved")) {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage("Transaction : Already Approved");
					approveTranRespModel.setDescription("Already Approved ");
					return approveTranRespModel;
				} else {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage("Transaction");
					approveTranRespModel.setScheduledTransaction(schTransaction.get());
					approveTranRespModel.setDescription("Failed by the approver");
					return approveTranRespModel;
				}
			}
		}

		ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
		approveTranRespModel.setStatus(false);
		approveTranRespModel.setMessage("Something went wrong");
		approveTranRespModel.setFundTransfer(null);
		return approveTranRespModel;

	}

	private String checkmandatoryFieldFT(Optional<ScheduledTransaction> schTransaction, String mode) {
		StringBuilder sb = new StringBuilder();
		if (mode.equals("fed2fed")) {
			if (StringUtils.isEmpty(schTransaction.get().getBenAccNo())) {
				sb.append("beneficiary account no |");
			}
			if (StringUtils.isEmpty(schTransaction.get().getBenName())) {
				sb.append("beneficiary Name |");
			}
			if (StringUtils.isEmpty(schTransaction.get().getRemAccNo())) {
				sb.append("remitter acc no |");
			}
			if (StringUtils.isEmpty(schTransaction.get().getRemName())) {
				sb.append("remitter name |");
			}
		} else if (mode.equals("neft")) {
			if (StringUtils.isEmpty(schTransaction.get().getRemAccNo())) {
				sb.append("remitter acc no |");
			}
			if (StringUtils.isEmpty(schTransaction.get().getRemName())) {
				sb.append("remitter name |");
			}
//			if (StringUtils.isEmpty(fundTransfer.get().getRemMobNum())) {
//				sb.append("remitter Mobile no |");
//			}
//			if (StringUtils.isEmpty(fundTransferStoreResp.get().getRemName())) {
//				sb.append("remitter Address |");
//			}
			if (StringUtils.isEmpty(schTransaction.get().getBenAccNo())) {
				sb.append("beneficiary account no |");
			}
			if (StringUtils.isEmpty(schTransaction.get().getBenName())) {
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

	private String createP2ATransferApprovalReq(Optional<ScheduledTransaction> impsTransfer, String senderRefId) {
		final ZonedDateTime input = ZonedDateTime.now();
		DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		RecordLog.writeLogFile("CallBack URL >>>" + callbackURL);

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

		sb.append("\"AccNumber\":\"" + impsTransfer.get().getRemAccNo() + "\",");

		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(impsTransfer.get().getRemMobNum()) != null ? ""
				: impsTransfer.get().getRemMobNum() + "\",");

//		sb.append("\"Mobile\":\"" + "" + "\",");

		sb.append("\"Email\":\"\",");

//		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"BeneficiaryDetails\": {");

		sb.append("\"Name\":\"" + getTrimmedValue(impsTransfer.get().getBenNickName()) + "\",");

		sb.append("\"AccNumber\":\"" + impsTransfer.get().getBenAccNo() + "\",");

//		sb.append("\"Acctype\":\"" + StringUtils.isEmpty(fundTransferStoreResp.get().getBenAccType()) != null ? ""
//				: fundTransferStoreResp.get().getBenAccType() + "\",");

//		sb.append("\"Acctype\":\"10\" ,");

		sb.append("\"IFSC\":\"" + impsTransfer.get().getBenIfsc() + "\",");

		sb.append("\"Mobile\":\"" + StringUtils.isEmpty(impsTransfer.get().getRemMobNum()) != null ? ""
				: impsTransfer.get().getRemMobNum() + "\",");

//		sb.append("\"Mobile\":\"" + "" + "\",");

		sb.append("\"Email\":\"\",");

//		sb.append("\"address\":\"NA\" ,");

		sb.append("\"Notification_Flag\": \"NONE\"");

		sb.append("},");

		sb.append("\"Amount\":\"" + impsTransfer.get().getAmount() + "\",");

		sb.append("\"Remarks\":\"" + impsTransfer.get().getRemarks() + "\",");

//		sb.append("\"Alternative_Payments\":\"N\",");

		sb.append("\"Sender_Data\":\"IMPS P2A perform\"");

		sb.append("}");
		return sb.toString();
	}

	public ApproveTranRespModel approveImpsProcess(Optional<ScheduledTransaction> impsTransfer,
			Optional<ScheduledPayment> payment, Optional<User> user) {
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

			getCurrentUserDetails = user;
			if (getCurrentUserDetails.isPresent()) {

				/* TODO check with vikas */

				SMEMessage approvalMsg = checker(getCurrentUserDetails, payment.get());
				String approval = approvalMsg.getMessage();
//				fundTransferLogsEntryService.approveSchLog(impsTransfer.get(), getCurrentUserDetails.get().getPrefNo(),
//						enterprises.get().getPrefCorp());
				if (approval.equals("success")) {
					if (impsTransfer.get().getProgress() != null
							&& !impsTransfer.get().getProgress().equals("initiated")) {
						if (impsTransfer.get().getProgress() != null
								&& !impsTransfer.get().getProgress().equals("completed")) {
							mode = impsTransfer.get().getMode();
							impsTransfer.get().setProgress("initiated");
							schtranrepo.save(impsTransfer.get());
							if (mode.equals("p2p")) {
								remarks = StringUtils.isEmpty(impsTransfer.get().getRemarks()) ? "UPIREFFS"
										: impsTransfer.get().getRemarks();

								senderRefId = StringUtils.isEmpty(impsTransfer.get().getSenderRefId())
										? generateSenderRefNo(impsTransfer.get().getMode())
										: impsTransfer.get().getSenderRefId();

//									FundTransferReqModel fundTransferReqModel = createP2PTransferReq(impsTransfer,
//											senderRefId);
								HashMap<String, String> reqdata = new HashMap<String, String>();
								reqdata.put("mbRefNo", generateSenderRefNo("imps"));
								reqdata.put("amt", impsTransfer.get().getAmount());
								reqdata.put("fromAcc", impsTransfer.get().getRemAccNo());
								reqdata.put("remarks", impsTransfer.get().getRemarks());
								reqdata.put("benAccType", "10");
//											impsTransfer.get().getBenAccType() != null
//													? impsTransfer.get().getBenAccType()
//													: "10");
								reqdata.put("remName", getTrimmedValue(impsTransfer.get().getRemName()));
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
								HttpHeaders headers = new HttpHeaders();
								headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
								HttpEntity<String> entity = new HttpEntity<String>(reqObject.toString(), headers);
								ResponseEntity<?> ftResponse = restTemplate.postForEntity(
										fblgatewayurl + "/gateway/fundtransfer/imps_p2p", entity, String.class);
								RecordLog.writeLogFile("Gateway Response" + ftResponse);

								if (ftResponse.getStatusCodeValue() == 200) {
									if (ftResponse.hasBody()) {
										JSONObject jsonObject = new JSONObject(ftResponse.getBody().toString());

										auditLogService.IMPSlogSch(impsTransfer.get(), "p2p",
												jsonObject.getJSONObject("recordId").getString("responseCode"),
												enterprises);
//										fundTransferLogsEntryService.transactionSchLog(impsTransfer.get(),
//												jsonObject.getJSONObject("recordId").getString("responseCode"),
//												getCurrentUserDetails.get().getPrefNo(),
//												enterprises.get().getPrefCorp());
										if (jsonObject.getBoolean("status")) {

											String[] codes=properties.getGatewaysuccess().split(",");
											ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
											RecordLog.writeLogFile("Status codes: "+allowedIps);
											if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {

												ScheduledTransaction impsTrf = impsTransfer.get();
												impsTrf.setStatus("approved");
												impsTrf.setProgress("completed");
//														impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
//																.getString("responseCode"));
												impsTrf.setReason(
														jsonObject.getJSONObject("recordId").getString("reason"));
												schtranrepo.save(impsTrf);

												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(true);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage(
														jsonObject.getJSONObject("recordId").getString("reason"));
												approveTranRespModel.setScheduledTransaction(impsTransfer.get());
												return approveTranRespModel;
											} else {
												ScheduledTransaction impsTrf = impsTransfer.get();
												impsTrf.setStatus("approved");
												impsTrf.setProgress("completed");
//														impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
//																.getString("responseCode"));
												impsTrf.setReason(
														jsonObject.getJSONObject("recordId").getString("reason"));
												schtranrepo.save(impsTrf);
												checkerServiceFundTransfer.rollbackSchTransaction(getCurrentUserDetails,
														impsTransfer);
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage(
														jsonObject.getJSONObject("recordId").getString("reason"));
												approveTranRespModel.setScheduledTransaction(impsTransfer.get());
												return approveTranRespModel;
											}
										} else {
											/**
											 * Vikas to right rollback
											 */
											makercheckerServiceImpsTransfer.rollbackSch(getCurrentUserDetails, payment);
											if (jsonObject.getJSONObject("recordId").getString("responseCode")
													.equals("121")
													|| jsonObject.getJSONObject("recordId").getString("responseCode")
															.equals("116")) {
												impsTransfer.get().setProgress("nil");
											} else {
												impsTransfer.get().setStatus("deleted");
											}
											schtranrepo.save(impsTransfer.get());
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setDescription("Gateway Response");
											approveTranRespModel.setMessage(
													jsonObject.getJSONObject("recordId").getString("reason"));
											approveTranRespModel.setScheduledTransaction(impsTransfer.get());
											return approveTranRespModel;
										}
									} else {
										ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
										approveTranRespModel.setStatus(false);
										approveTranRespModel.setDescription("Gateway Response");
										approveTranRespModel.setMessage("no response from gateway");
										approveTranRespModel.setImpsTransfer(null);
										return approveTranRespModel;
									}
								} else {
									ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
									approveTranRespModel.setStatus(false);
									approveTranRespModel.setMessage(ftResponse.toString());
									approveTranRespModel.setImpsTransfer(null);
									return approveTranRespModel;
								}

							} else if (mode.equals("p2a")) {
								remarks = StringUtils.isEmpty(impsTransfer.get().getRemarks()) ? "UPIREFFS"
										: impsTransfer.get().getRemarks();

								senderRefId = StringUtils.isEmpty(impsTransfer.get().getSenderRefId())
										? generateSenderRefNo(impsTransfer.get().getMode())
										: impsTransfer.get().getSenderRefId();
								String p2aTransferReqModel = createP2ATransferApprovalReq(impsTransfer, senderRefId);
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
								RecordLog.writeLogFile("P2A Req String : " + p2aTransferReqModel.toString());
								HttpHeaders headers = new HttpHeaders();
								headers.setContentType(MediaType.APPLICATION_JSON);
//										headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
								HttpEntity<String> entity = new HttpEntity<String>(p2aTransferReqModel, headers);
								ResponseEntity<?> ftResponse = restTemplate.postForEntity(
										fblgatewayurl + "/gateway/fundtransfer/imps_p2a", entity, String.class);
								RecordLog.writeLogFile("Gateway Response" + ftResponse);

								if (ftResponse.getStatusCodeValue() == 200) {
									if (ftResponse.hasBody()) {
										JSONObject jsonObject = new JSONObject(ftResponse.getBody().toString());

										auditLogService.IMPSlogSch(impsTransfer.get(), "p2a",
												jsonObject.getJSONObject("recordId").getString("responseCode"),
												enterprises);
//										fundTransferLogsEntryService.transactionSchLog(impsTransfer.get(),
//												jsonObject.getJSONObject("recordId").getString("responseCode"),
//												getCurrentUserDetails.get().getPrefNo(),
//												enterprises.get().getPrefCorp());
										if (jsonObject.getBoolean("status")) {
											String[] codes=properties.getGatewaysuccess().split(",");
											ArrayList<String> allowedIps =new ArrayList<String>(Arrays.asList(codes));
											RecordLog.writeLogFile("Status codes: "+allowedIps);
											if (allowedIps.contains(jsonObject.getJSONObject("recordId").getString("responseCode"))) {
												ScheduledTransaction impsTrf = impsTransfer.get();
												impsTrf.setStatus("approved");
												impsTrf.setProgress("completed");
//														impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
//																.getString("responseCode"));
												impsTrf.setReason(
														jsonObject.getJSONObject("recordId").getString("reason"));
												schtranrepo.save(impsTrf);

												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(true);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage(
														jsonObject.getJSONObject("recordId").getString("reason"));
												approveTranRespModel.setScheduledTransaction(impsTransfer.get());
												return approveTranRespModel;
											} else {
												ScheduledTransaction impsTrf = impsTransfer.get();
												impsTrf.setStatus("approved");
												impsTrf.setProgress("completed");
//														impsTrf.setResponseCode(jsonObject.getJSONObject("recordId")
//																.getString("responseCode"));
												impsTrf.setReason(
														jsonObject.getJSONObject("recordId").getString("reason"));
												schtranrepo.save(impsTrf);
												checkerServiceFundTransfer.rollbackSchTransaction(getCurrentUserDetails,
														impsTransfer);
												ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
												approveTranRespModel.setStatus(false);
												approveTranRespModel.setDescription("Gateway Response");
												approveTranRespModel.setMessage(
														jsonObject.getJSONObject("recordId").getString("reason"));
												approveTranRespModel.setScheduledTransaction(impsTransfer.get());
												return approveTranRespModel;
											}
										} else {
											/**
											 * Vikas to right rollback
											 */
											makercheckerServiceImpsTransfer.rollbackSch(getCurrentUserDetails, payment);
											if (jsonObject.getJSONObject("recordId").getString("responseCode")
													.equals("121")
													|| jsonObject.getJSONObject("recordId").getString("responseCode")
															.equals("116")) {
												impsTransfer.get().setProgress("nil");
											} else {
												impsTransfer.get().setStatus("deleted");
											}
											schtranrepo.save(impsTransfer.get());
											ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
											approveTranRespModel.setStatus(false);
											approveTranRespModel.setDescription("Gateway Response");
											approveTranRespModel.setMessage(
													jsonObject.getJSONObject("recordId").getString("reason"));
											approveTranRespModel.setScheduledTransaction(impsTransfer.get());
											return approveTranRespModel;
										}
									} else {
										ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
										approveTranRespModel.setStatus(false);
										approveTranRespModel.setDescription("Gateway Response");
										approveTranRespModel.setMessage("no response from gateway");
										approveTranRespModel.setImpsTransfer(null);
										return approveTranRespModel;
									}
								} else {
									ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
									approveTranRespModel.setStatus(false);
									approveTranRespModel.setMessage(ftResponse.toString());
									approveTranRespModel.setImpsTransfer(null);
									return approveTranRespModel;
								}

							}
						} else {
							ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
							approveTranRespModel.setStatus(false);
							approveTranRespModel.setMessage("Already Completed");
							approveTranRespModel.setDescription("Already Completed");
							return approveTranRespModel;
						}
					} else {
						ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
						approveTranRespModel.setStatus(false);
						approveTranRespModel.setMessage("Already Initiated");
						approveTranRespModel.setDescription("Already Initiated");
						return approveTranRespModel;
					}
				} else if (approval.equals("signed")) {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(true);
					approveTranRespModel.setMessage("sucess");
					approveTranRespModel.setDescription("Signed");
					return approveTranRespModel;
				} else if (approval.equals("already_approved")) {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage("Transaction : Already Approved");
					approveTranRespModel.setDescription("Already Approved ");
					return approveTranRespModel;
				} else {
					ApproveTranRespModel approveTranRespModel = new ApproveTranRespModel();
					approveTranRespModel.setStatus(false);
					approveTranRespModel.setMessage("Transaction");
					approveTranRespModel.setScheduledTransaction(impsTransfer.get());
					approveTranRespModel.setDescription("Failed by the approver");
					return approveTranRespModel;
				}
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
		if(name.length()>39) {
			return name.substring(0, 38);
		}else {
			return name;
		}
	}

}
