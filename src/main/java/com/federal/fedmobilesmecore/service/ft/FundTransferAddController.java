package com.federal.fedmobilesmecore.service.ft;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.TransactionLimit;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.ApproveTranRespModel;
import com.federal.fedmobilesmecore.model.ApproveTransactionReqModel;
import com.federal.fedmobilesmecore.model.FTApproveResponse;
import com.federal.fedmobilesmecore.model.FTInput;
import com.federal.fedmobilesmecore.model.FundTransferDTO;
import com.federal.fedmobilesmecore.model.MakerCheckerListGeneric;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.SchedulePaymentMessage;
import com.federal.fedmobilesmecore.model.TransactionLimitsOutput;
import com.federal.fedmobilesmecore.repository.CheckerRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.FundTransferRepository;
import com.federal.fedmobilesmecore.repository.ImpsTransferRepository;
import com.federal.fedmobilesmecore.repository.OperationRepository;
import com.federal.fedmobilesmecore.repository.SignatureRepository;
import com.federal.fedmobilesmecore.repository.TransactionLimitRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.service.FundTransferInternalService;
import com.federal.fedmobilesmecore.service.FundTransferService;
import com.federal.fedmobilesmecore.service.FundTransferService.PERIODTYPE;
import com.federal.fedmobilesmecore.service.SchedulePaymentsService;

@RestController
@RequestMapping(path = "/core/fundtransfer")
public class FundTransferAddController {

	private static final Logger log4j = LogManager.getLogger(FundTransferAddController.class);

	@Autowired
	UserRepository userrepo;

	@Autowired
	EnterprisesRepository entrepo;

	@Autowired
	FundTransferService ftservice;

	@Autowired
	CheckerRepository checkerRepository;

	@Autowired
	TransactionLimitRepository transactionLimitRepository;

	@Autowired
	SchedulePaymentsService schservice;

	@Autowired
	SchedulePaymentsService schser;

	@Autowired
	ImpsTransferRepository impsrepo;

	@Autowired
	FundTransferRepository ftrepo;

	@Autowired
	OperationRepository operationRepository;

	@Autowired
	SignatureRepository signatureRepository;

	@Autowired
	FundTransferInternalService ftinternalservice;

	@Autowired
	GlobalProperties properties;

//	@Value("${tl.dailytrans}")
//	Double dailylimit;
//
//	@Value("${tl.monthlytrans}")
//	Double monthlylimit;
//
//	@Value("${tl.quickpaylimit}")
//	Double quickpaylimit;
//
//	@Value("${tl.pertrans}")
//	Double pertranslimit;

	/**
	 * Transaction limits for Fund Transfer based on Enterprise & User.
	 * 
	 * @param input
	 * @return TransactionLimitsOutput
	 */
	@io.swagger.v3.oas.annotations.Operation(summary = "Transaction limits for Fund Transfer")
	@PostMapping(path = "/transactionlimits", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> initate(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /transactionlimits api is calling. API request: "+input);
		TransactionLimitsOutput message = new TransactionLimitsOutput();
		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
				message = getAllDetails(enterprise.get(), user);
			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /transactionlimits api completed. API response: for prefcorp "+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}

	/**
	 * Pending Fund Transfers based on Enterprise & User.
	 * 
	 * @param input
	 * @return TransactionLimitsOutput
	 */
	@io.swagger.v3.oas.annotations.Operation(summary = "Pending Transactions of Fund Transfer")
	@PostMapping(path = "/pendingtrans", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pendingTransactions(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /pendingtrans api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();

		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
				List<MakerCheckerListGeneric> ftresponse = getPendingFTTransactions(enterprise.get(), user,
						input.getTransfer_type(), "pending", null);

				List<MakerCheckerListGeneric> impsresponse = getPendingIMPSTransactions(enterprise.get(), user,
						input.getTransfer_type(), "pending", null);
				List<MakerCheckerListGeneric> finalresponse = new ArrayList<MakerCheckerListGeneric>();
				List<MakerCheckerListGeneric> finalresponse1 = new ArrayList<MakerCheckerListGeneric>();
				List<MakerCheckerListGeneric> finalresponse2 = new ArrayList<MakerCheckerListGeneric>();
				List<MakerCheckerListGeneric> finalresponse3 = new ArrayList<MakerCheckerListGeneric>();
				finalresponse.addAll(ftresponse);
				finalresponse.addAll(impsresponse);
				Collections.sort(finalresponse, new Comparator<MakerCheckerListGeneric>() {
					@Override
					public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
						return o2.getFt().getUpdatedAt().toLocalDateTime()
								.compareTo(o1.getFt().getUpdatedAt().toLocalDateTime());
					}
				});
				for(MakerCheckerListGeneric i:finalresponse ) {
					if(i.isApprovalPermission()) {
						finalresponse1.add(i);
					}
					else {
						finalresponse2.add(i);
					}
					
				}
				for(MakerCheckerListGeneric i:finalresponse1) {
					finalresponse3.add(i);
				}
				for(MakerCheckerListGeneric i:finalresponse2) {
					finalresponse3.add(i);
				}
				
//				RecordLog.writeLogFile("finalresponse3===============>"+finalresponse3);

//				Collections.sort(finalresponse, (p1, p2) -> p1.getFt().getUpdatedAt().toLocalDateTime()
//						.compareTo(p1.getFt().getUpdatedAt().toLocalDateTime()));
				if (finalresponse3.size() > 0) {
					message.setStatus(true);
					message.setMessage("Success");
					message.setTransactions(finalresponse3);

				} else {
					message.setStatus(true);
					message.setMessage("No records found");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /pendingtrans api completed. API response: for prefcorp "+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}
	
	
	
	
	
	@io.swagger.v3.oas.annotations.Operation(summary = "Pending Transactions of Fund Transfer and Quick Pay for Mobile")
	@PostMapping(path = "/pendingtransMobile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pendingTransactionsMobile(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /pendingtransMobile api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();

		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
				List<MakerCheckerListGeneric> ftresponse = getPendingFTTransactions(enterprise.get(), user,
						"FundTransfer", "pending", null);

				List<MakerCheckerListGeneric> impsresponse = getPendingIMPSTransactions(enterprise.get(), user,
						"FundTransfer", "pending", null);
				
				List<MakerCheckerListGeneric> qpresponse = getPendingFTTransactions(enterprise.get(), user,
						"QuickPay", "pending", null);

				List<MakerCheckerListGeneric> qpimpsresponse = getPendingIMPSTransactions(enterprise.get(), user,
						"QuickPay", "pending", null);
				List<MakerCheckerListGeneric> finalresponse = new ArrayList<MakerCheckerListGeneric>();
				List<MakerCheckerListGeneric> finalresponse1 = new ArrayList<MakerCheckerListGeneric>();
				List<MakerCheckerListGeneric> finalresponse2 = new ArrayList<MakerCheckerListGeneric>();
				List<MakerCheckerListGeneric> finalresponse3 = new ArrayList<MakerCheckerListGeneric>();
			
				finalresponse.addAll(ftresponse);
				finalresponse.addAll(impsresponse);
				finalresponse.addAll(qpresponse);
				finalresponse.addAll(qpimpsresponse);
				
				Collections.sort(finalresponse, new Comparator<MakerCheckerListGeneric>() {
					@Override
					public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
						return o2.getFt().getUpdatedAt().toLocalDateTime()
								.compareTo(o1.getFt().getUpdatedAt().toLocalDateTime());
					}
				});
				
				for(MakerCheckerListGeneric i:finalresponse) {
					if(i.isApprovalPermission()) {
						
						finalresponse1.add(i);
					}
					else {
						finalresponse2.add(i);
					}
				}
				for(MakerCheckerListGeneric i:finalresponse1) {
					finalresponse3.add(i);
				}
				for(MakerCheckerListGeneric i:finalresponse2) {
					finalresponse3.add(i);
				}
				
				
				
//				Collections.sort(finalresponse, (p1, p2) -> p1.getFt().getUpdatedAt().toLocalDateTime()
//						.compareTo(p1.getFt().getUpdatedAt().toLocalDateTime()));
				if (finalresponse3.size() > 0) {
					message.setStatus(true);
					message.setMessage("Success");
					message.setTransactions(finalresponse3);

				} else {
					message.setStatus(true);
					message.setMessage("No records found");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /pendingtransMobile api completed. API response: for prefcorp "+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}
	
	
	
	

	/**
	 * Rejected Fund Transfers based on Enterprise & User.
	 * 
	 * @param input
	 * @return TransactionLimitsOutput
	 */
	@io.swagger.v3.oas.annotations.Operation(summary = "Rejected Transactions of Fund Transfer")
	@PostMapping(path = "/rejectedlist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectedTransactions(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /rejectedlist api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();

		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
				List<MakerCheckerListGeneric> ftresponse = getPendingFTTransactions(enterprise.get(), user,
						input.getTransfer_type(), "rejected", null);

				List<MakerCheckerListGeneric> impsresponse = getPendingIMPSTransactions(enterprise.get(), user,
						input.getTransfer_type(), "rejected", null);
				List<MakerCheckerListGeneric> finalresponse = new ArrayList<MakerCheckerListGeneric>();
				finalresponse.addAll(ftresponse);
				finalresponse.addAll(impsresponse);
				Collections.sort(finalresponse, new Comparator<MakerCheckerListGeneric>() {
					@Override
					public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
						return o2.getFt().getUpdatedAt().toLocalDateTime()
								.compareTo(o1.getFt().getUpdatedAt().toLocalDateTime());
					}
				});
//				Collections.sort(finalresponse, (p1, p2) -> p1.getFt().getUpdatedAt().toLocalDateTime()
//						.compareTo(p1.getFt().getUpdatedAt().toLocalDateTime()));
				if (finalresponse.size() > 0) {
					message.setStatus(true);
					message.setMessage("Success");
					message.setTransactions(finalresponse);

				} else {
					message.setStatus(true);
					message.setMessage("No records found");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /rejectedlist api completed. API response: for prefcorp  "+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}

	
	@io.swagger.v3.oas.annotations.Operation(summary = "Rejected Transactions of Fund Transfer and Quick Pay")
	@PostMapping(path = "/rejectedlistForMobile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectedTransactionsForMobile(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /rejectedlistForMobile api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();

		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
				List<MakerCheckerListGeneric> ftresponse = getPendingFTTransactions(enterprise.get(), user,
						"FundTransfer", "rejected", null);

				List<MakerCheckerListGeneric> impsresponse = getPendingIMPSTransactions(enterprise.get(), user,
						"FundTransfer", "rejected", null);
				List<MakerCheckerListGeneric> qpftresponse = getPendingFTTransactions(enterprise.get(), user,
						"QuickPay", "rejected", null);

				List<MakerCheckerListGeneric> qpimpsresponse = getPendingIMPSTransactions(enterprise.get(), user,
						"QuickPay", "rejected", null);
				
				List<MakerCheckerListGeneric> finalresponse = new ArrayList<MakerCheckerListGeneric>();
				finalresponse.addAll(ftresponse);
				finalresponse.addAll(impsresponse);
				finalresponse.addAll(qpftresponse);
				finalresponse.addAll(qpimpsresponse);
				Collections.sort(finalresponse, new Comparator<MakerCheckerListGeneric>() {
					@Override
					public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
						return o2.getFt().getUpdatedAt().toLocalDateTime()
								.compareTo(o1.getFt().getUpdatedAt().toLocalDateTime());
					}
				});
//				Collections.sort(finalresponse, (p1, p2) -> p1.getFt().getUpdatedAt().toLocalDateTime()
//						.compareTo(p1.getFt().getUpdatedAt().toLocalDateTime()));
				if (finalresponse.size() > 0) {
					message.setStatus(true);
					message.setMessage("Success");
					message.setTransactions(finalresponse);

				} else {
					message.setStatus(true);
					message.setMessage("No records found");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /rejectedlistForMobile api completed. API response: for prefcorp "+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}
	
	
	/**
	 * Approved Fund Transfers based on Enterprise & User.
	 * 
	 * @param input
	 * @return TransactionLimitsOutput
	 */
	@io.swagger.v3.oas.annotations.Operation(summary = "Approved Transactions of Fund Transfer")
	@PostMapping(path = "/approvedlist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approvedTransactions(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /approvedlist api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();

		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
//				List<MakerCheckerListGeneric> ftresponse = getPendingFTTransactions(enterprise.get(), user,
//						input.getTransfer_type(), "approved", null);
//
//				List<MakerCheckerListGeneric> impsresponse = getPendingIMPSTransactions(enterprise.get(), user,
//						input.getTransfer_type(), "approved", null);
				List<MakerCheckerListGeneric> ftresponse = getApprovedFTTransactions(enterprise.get(), user,
						input.getTransfer_type(), "approved", null);

				List<MakerCheckerListGeneric> impsresponse = getApprovedIMPSTransactions(enterprise.get(), user,
						input.getTransfer_type(), "approved", null);
				List<MakerCheckerListGeneric> finalresponse = new ArrayList<MakerCheckerListGeneric>();
				finalresponse.addAll(ftresponse);
				finalresponse.addAll(impsresponse);
				Collections.sort(finalresponse, new Comparator<MakerCheckerListGeneric>() {
					@Override
					public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
						return o2.getFt().getUpdatedAt().toLocalDateTime()
								.compareTo(o1.getFt().getUpdatedAt().toLocalDateTime());
					}
				});
//				Collections.sort(finalresponse, (p1, p2) -> p1.getFt().getUpdatedAt().toLocalDateTime()
//						.compareTo(p1.getFt().getUpdatedAt().toLocalDateTime()));

				if (finalresponse.size() > 0) {
					message.setStatus(true);
					message.setMessage("Success");
					message.setTransactions(finalresponse);

				} else {
					message.setStatus(true);
					message.setMessage("No records found");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /approvedlist api completed. API response: for prefcorp "+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}
	
	
	
	
	@io.swagger.v3.oas.annotations.Operation(summary = "Approved Transactions of Fund Transfer and Quick Pay")
	@PostMapping(path = "/approvedlistForMobile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approvedTransactionsforMobile(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /approvedlistForMobile api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();

		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
//				List<MakerCheckerListGeneric> ftresponse = getPendingFTTransactions(enterprise.get(), user,
//						"FundTransfer", "approved", null);
//
//				List<MakerCheckerListGeneric> impsresponse = getPendingIMPSTransactions(enterprise.get(), user,
//						"FundTransfer", "approved", null);
//				List<MakerCheckerListGeneric> qpftresponse = getPendingFTTransactions(enterprise.get(), user,
//						"QuickPay", "approved", null);
//
//				List<MakerCheckerListGeneric> qpimpsresponse = getPendingIMPSTransactions(enterprise.get(), user,
//						"QuickPay", "approved", null);
				List<MakerCheckerListGeneric> ftresponse = getApprovedFTTransactions(enterprise.get(), user,
						"FundTransfer", "approved", null);

				List<MakerCheckerListGeneric> impsresponse = getApprovedIMPSTransactions(enterprise.get(), user,
						"FundTransfer", "approved", null);
				List<MakerCheckerListGeneric> qpftresponse = getApprovedFTTransactions(enterprise.get(), user,
						"QuickPay", "approved", null);

				List<MakerCheckerListGeneric> qpimpsresponse = getApprovedIMPSTransactions(enterprise.get(), user,
						"QuickPay", "approved", null);
				List<MakerCheckerListGeneric> finalresponse = new ArrayList<MakerCheckerListGeneric>();
				finalresponse.addAll(ftresponse);
				finalresponse.addAll(impsresponse);
				finalresponse.addAll(qpftresponse);
				finalresponse.addAll(qpimpsresponse);
				Collections.sort(finalresponse, new Comparator<MakerCheckerListGeneric>() {
					@Override
					public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
						return o2.getFt().getUpdatedAt().toLocalDateTime()
								.compareTo(o1.getFt().getUpdatedAt().toLocalDateTime());
					}
				});
//				Collections.sort(finalresponse, (p1, p2) -> p1.getFt().getUpdatedAt().toLocalDateTime()
//						.compareTo(p1.getFt().getUpdatedAt().toLocalDateTime()));

				if (finalresponse.size() > 0) {
					message.setStatus(true);
					message.setMessage("Success");
					message.setTransactions(finalresponse);

				} else {
					message.setStatus(true);
					message.setMessage("No records found");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /approvedlistForMobile api completed. API response: for prefcorp"+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}
	

	/**
	 * Pending Fund Transfers based on Enterprise & User.
	 * 
	 * @param input
	 * @return TransactionLimitsOutput
	 */
	@io.swagger.v3.oas.annotations.Operation(summary = "Pending Transactions of Fund Transfer")
	@PostMapping(path = "/pendingtrans_specific", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pendingTransactionsSpecific(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /pendingtrans_specific api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();

		User user = userrepo.findByAuthTokenAndMarkAsEnabled(input.getAuth_token(), true);
		if (user != null) {
			Optional<Enterprises> enterprise = entrepo.findById(Long.parseLong(user.getEnterpriseId()));
			if (enterprise.isPresent()) {
				Pageable pageble = PageRequest.of(0, input.getCount(), Sort.by(Sort.Direction.ASC, "updatedAt"));

				List<MakerCheckerListGeneric> ftresponse = getPendingFTTransactions(enterprise.get(), user,
						input.getTransfer_type(), "pending", pageble);
				List<MakerCheckerListGeneric> impsresponse = getPendingIMPSTransactions(enterprise.get(), user,
						input.getTransfer_type(), "pending", pageble);

				List<MakerCheckerListGeneric> finalresponse = new ArrayList<MakerCheckerListGeneric>();

				finalresponse.addAll(ftresponse);
				finalresponse.addAll(impsresponse);
				Collections.sort(finalresponse, new Comparator<MakerCheckerListGeneric>() {
					@Override
					public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
						return o2.getFt().getUpdatedAt().toLocalDateTime()
								.compareTo(o1.getFt().getUpdatedAt().toLocalDateTime());
					}
				});
//				Collections.sort(finalresponse, (p1, p2) -> p1.getFt().getUpdatedAt().toLocalDateTime()
//						.compareTo(p1.getFt().getUpdatedAt().toLocalDateTime()));
				List<MakerCheckerListGeneric> finalresponseafterlimit = finalresponse.stream().limit(input.getCount())
						.collect(Collectors.toList());
				if (finalresponse.size() > 0) {
					message.setStatus(true);
					message.setMessage("Success");
					message.setTransactions(finalresponseafterlimit);
					message.setRecordid("" + input.getCount());
					message.setDescription("Record ID contains number of records requested.");
				} else {
					message.setStatus(true);
					message.setMessage("No records found");
				}

			} else {
				message.setStatus(false);
				message.setMessage("Invalid Corporate");
			}

		} else {
			message.setStatus(false);
			message.setMessage("Invalid User");
			return ResponseEntity.status(401).body(message);
		}
		RecordLog.writeLogFile("FundTransferAddController /pendingtrans_specific api completed. API response: for prefcorp "+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Reject a specific of Fund Transfer")
	@PostMapping(path = "/reject", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectSpecificFT(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /reject api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		if (StringUtils.isNotBlank(input.getRef_no())) {
			Optional<User> currentuser = ftinternalservice.getCurrentUserDetail(input.getAuth_token());
			if (currentuser.isPresent()) {
				Optional<FundTransfer> ft = ftrepo.findByRefNo(input.getRef_no());
				Optional<ImpsTransfer> imps = impsrepo.findByRefNo(input.getRef_no());
				if (ft.isPresent() || imps.isPresent()) {
					if (ft.isPresent()) {
						if (!ft.get().getStatus().equals("error")) {
							// Call Maker checker reject.
							FundTransferDTO dto = schservice.convertObject(ft.get());
							SMEMessage response = schservice.reject(currentuser.get(), null, dto);
							if (response.getMessage().equals("success") || response.getMessage().equals("signed")) {
								FundTransfer payment = ft.get();
								payment.setStatus("rejected");
								ftrepo.save(payment);
								message.setStatus(true);
								message.setMessage("Success");
							} else if (response.getMessage().equals("already_approved")) {
								message.setStatus(false);
								message.setMessage("Transaction is already approved");
							} else {
								message.setStatus(false);
								message.setMessage("Transaction is already rejected");
							}

						} else {
							message.setStatus(false);
							message.setMessage("Request is marked as error.");
						}
					} else {
						// IMPS Present.
						if (!imps.get().getStatus().equals("error")) {
							FundTransferDTO dto = schservice.convertObject(imps.get());
							SMEMessage response = schservice.reject(currentuser.get(), null, dto);
							if (response.getMessage().equals("success") || response.getMessage().equals("signed")) {
								ImpsTransfer payment = imps.get();
								payment.setStatus("rejected");
								impsrepo.save(payment);
								message.setStatus(true);
								message.setMessage("Success");
							} else if (response.getMessage().equals("already_approved")) {
								message.setStatus(false);
								message.setMessage("Transaction is already approved");
							} else {
								message.setStatus(false);
								message.setMessage("Transaction is already rejected");
							}

						} else {
							message.setStatus(false);
							message.setMessage("Request is marked as error.");
						}
					}
				} else {
					message.setStatus(false);
					message.setMessage("Invalid Fund Transfer.");
				}
			} else {
				message.setStatus(false);
				message.setMessage("Invalid User.");
			}
		} else {
			message.setStatus(false);
			message.setMessage("Reference Number is mandatory.");
		}
		RecordLog.writeLogFile("FundTransferAddController /reject api completed. API response: for refno "+input.getRef_no()+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Approve a specific of Fund Transfer")
	@PostMapping(path = "/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approveFT(@RequestBody FTInput input) {
		RecordLog.writeLogFile("FundTransferAddController /approve api is calling. API request: "+input);
		ApproveTransactionReqModel approveTransactionReqModel = new ApproveTransactionReqModel();
		ApproveTranRespModel performTranRespModel = null;
		FTApproveResponse message = new FTApproveResponse();
try {
		if (StringUtils.isNotEmpty(input.getRef_no()) && StringUtils.isNotEmpty(input.getAuth_token())
				&& StringUtils.isNotEmpty(input.getPref_corp())) {
			approveTransactionReqModel.setAuthToken(input.getAuth_token());
			approveTransactionReqModel.setPrefCorp(input.getPref_corp());
			approveTransactionReqModel.setRefNo(input.getRef_no());
			approveTransactionReqModel.setIpAddress(input.getIpAddress());
			approveTransactionReqModel.setGeoLocation(input.getGeoLocation());
			approveTransactionReqModel.setChannelFlag(input.getChannelFlag());
			performTranRespModel = ftservice.approveTransaction(approveTransactionReqModel);
			RecordLog.writeLogFile("progress of fund fundddcontroller 700 "+" refno no "+input.getPref_corp()+" resp "+performTranRespModel);
			message.setStatus(performTranRespModel.isStatus());
			message.setMessage(performTranRespModel.getMessage());
			message.setDescription(performTranRespModel.getDescription());

			if (performTranRespModel.getFundTransfer() != null) {
				message.setSenderrefid(performTranRespModel.getFundTransfer().getSenderRefId());
				// Balance is not available from APIGATEWAY
				message.setAccountbalance(performTranRespModel.getFundTransfer().getAmount());
				// TANSACTION ID is not available from APIGATEWAY
				message.setTransactionid("000");
				if (performTranRespModel.isStatus()) {
					message.setReason("success");
				} else {
					message.setReason(performTranRespModel.getMessage());
				}

				message.setCraccountbalance("0");
			}

			if (performTranRespModel.getImpsTransfer() != null) {
				message.setSenderrefid(performTranRespModel.getImpsTransfer().getSenderRefId());
				// Balance is not available from APIGATEWAY
				message.setAccountbalance(performTranRespModel.getImpsTransfer().getAmount());
				// TANSACTION ID is not available from APIGATEWAY
				message.setTransactionid("000");
				if (performTranRespModel.isStatus()) {
					message.setReason("success");
				} else {
					message.setReason(performTranRespModel.getMessage());
				}

				message.setCraccountbalance("0");
			}
		} else {

			message.setStatus(false);
			message.setDescription("There are one or more field are missing");
			message.setMessage("Request With incomplete Fields");
		}
}catch(Exception e) {
	RecordLog.writeLogFile("Exception occured at for prefcorp "+input.getPref_corp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
	e.printStackTrace();
	message.setStatus(false);
	message.setDescription("Exception while approving the fundtransfer");
	message.setMessage("Exception while approving the fundtransfer");
}
		RecordLog.writeLogFile("FundTransferAddController /approve api completed. API response: for prefcorp"+input.getPref_corp()+message);
		return ResponseEntity.ok(message);
	}

	/**
	 * Get pending FT transactions
	 * 
	 * @param enterprise
	 * @param user
	 * @param transferType
	 * @param status
	 * @return
	 */
	public List<MakerCheckerListGeneric> getPendingFTTransactions(Enterprises enterprise, User user,
			String transferType, String status, Pageable pageable) {
		List<MakerCheckerListGeneric> output = new ArrayList<MakerCheckerListGeneric>();
		if (enterprise != null && user != null) {
			List<FundTransfer> ftlist = new ArrayList<FundTransfer>();
			if (pageable == null) {
				ftlist = ftrepo.getPendingList("" + enterprise.getId(), status, transferType);
			} else {
				ftlist = ftrepo.getPendingList("" + enterprise.getId(), status, transferType, pageable).getContent();
			}

			if (!CollectionUtils.isEmpty(ftlist)) {
//				RecordLog.writeLogFile("Total FT Records available:" + ftlist.size());
				for (FundTransfer ft : ftlist) {
					MakerCheckerListGeneric response = schser.makerCheckerList(user, null, ft, null);
					if (response != null) {
						output.add(response);
					}
				}
			}
		}
		return output;
	}
	
	
	
	public List<MakerCheckerListGeneric> getApprovedFTTransactions(Enterprises enterprise, User user,
			String transferType, String status, Pageable pageable) {
		List<MakerCheckerListGeneric> output = new ArrayList<MakerCheckerListGeneric>();
		if (enterprise != null && user != null) {
			List<FundTransfer> ftlist = new ArrayList<FundTransfer>();
			Calendar cal=Calendar.getInstance();
			Timestamp current=Timestamp.from(cal.toInstant());
			cal.add(Calendar.MONTH, -1);
			Timestamp lastmonth=Timestamp.from(cal.toInstant());
			System.out.println(current +" "+lastmonth);
			if (pageable == null) {
				ftlist = ftrepo.getPendingListWithTime("" + enterprise.getId(), status, transferType,lastmonth,current);
			} else {
				ftlist = ftrepo.getPendingList("" + enterprise.getId(), status, transferType, pageable).getContent();
			}
			if (!CollectionUtils.isEmpty(ftlist)) {
				for (FundTransfer ft : ftlist) {
					MakerCheckerListGeneric response = schser.makerCheckerList(user, null, ft, null);
					if (response != null) {
						output.add(response);
					}
				}
			}
		}
		return output;
	}


	public List<MakerCheckerListGeneric> getApprovedIMPSTransactions(Enterprises enterprise, User user,
			String transferType, String status, Pageable pageable) {
		List<MakerCheckerListGeneric> output = new ArrayList<MakerCheckerListGeneric>();
		if (enterprise != null && user != null) {
			Calendar cal=Calendar.getInstance();
			Timestamp current=Timestamp.from(cal.toInstant());
			cal.add(Calendar.MONTH, -1);
			Timestamp lastmonth=Timestamp.from(cal.toInstant());
			List<ImpsTransfer> ftlist = new ArrayList<ImpsTransfer>();
			ftlist = impsrepo.getApprovedListTime("" + enterprise.getId(), status, transferType,lastmonth,current);
			if (!CollectionUtils.isEmpty(ftlist)) {
				for (ImpsTransfer imps : ftlist) {
					MakerCheckerListGeneric response = schser.makerCheckerList(user, null, null, imps);
					if (response != null) {
						output.add(response);
					}
				}
			}
		}
		return output;
	}

	/**
	 * Total number of records with the supplied criteria.
	 * 
	 * @param enterprise
	 * @param user
	 * @param transferType
	 * @param status
	 * @return Long
	 */
	public Long getCountOfFTTransactions(Enterprises enterprise, User user, String transferType, String status) {
		Long count = ftrepo.getCountPendingList("" + enterprise.getId(), status, transferType);
		return count;
	}

	/**
	 * Total number of records with the supplied criteria
	 * 
	 * @param enterprise
	 * @param user
	 * @param transferType
	 * @param status
	 * @return Long
	 */
	public Long getCountOfIMPSTransactions(Enterprises enterprise, User user, String transferType, String status) {
		Long count = impsrepo.getCountPendingList("" + enterprise.getId(), status, transferType);
		return count;
	}

	/**
	 * 
	 * Get pending IMPS transactions
	 * 
	 * @param enterprise
	 * @param user
	 * @param transferType
	 * @param status
	 * @return List<MakerCheckerListGeneric>
	 */
	public List<MakerCheckerListGeneric> getPendingIMPSTransactions(Enterprises enterprise, User user,
			String transferType, String status, Pageable pageable) {
		List<MakerCheckerListGeneric> output = new ArrayList<MakerCheckerListGeneric>();
		if (enterprise != null && user != null) {
			List<ImpsTransfer> ftlist = new ArrayList<ImpsTransfer>();
			ftlist = impsrepo.getPendingList("" + enterprise.getId(), status, transferType);
			if (!CollectionUtils.isEmpty(ftlist)) {
				for (ImpsTransfer imps : ftlist) {
					MakerCheckerListGeneric response = schser.makerCheckerList(user, null, null, imps);
					if (response != null) {
						output.add(response);
					}
				}
			}
		}
		return output;
	}

	public TransactionLimitsOutput getAllDetails(Enterprises enterprise, User user) {
		TransactionLimitsOutput message = new TransactionLimitsOutput();
		if (enterprise != null && user != null) {
			message.setStatus(true);
			message.setMessage("Success");

			Double dailylimit = getTransactionLimit(enterprise, Limit.daily);
			Double monthlylimit = getTransactionLimit(enterprise, Limit.monthly);
			Double dailyused = ftservice.getTotalConsumedAmount(PERIODTYPE.DAY, enterprise).doubleValue();
			Double monthlyused = ftservice.getTotalConsumedAmount(PERIODTYPE.MONTH, enterprise).doubleValue();
			Double dailyrem = dailylimit - dailyused;
			Double monthlyrem = monthlylimit - monthlyused;
			Double pertransaction = Double.parseDouble(user.getTransLimit() != null ? user.getTransLimit() : "0");
			if (pertransaction == 0) {
				pertransaction = Double.parseDouble(properties.getFedcorp_per_transaction_limit());
			}
			message.setDaily(dailylimit);
			message.setDaily_used(dailyused);
			message.setDaily_rem(dailyrem);
			message.setMonthly(monthlylimit);
			message.setMonthly_used(monthlyused);
			message.setMonthly_rem(monthlyrem);
			message.setPer_transaction(pertransaction);

		} else {
			message.setStatus(false);
			message.setMessage("Invalid Corporate / User.");
		}
		return message;
	}

	/**
	 * Get transaction limits based on corporate and limit
	 * 
	 * @param enterprise
	 * @param limit
	 * @return Double
	 */
	public Double getTransactionLimit(Enterprises enterprise, Limit limit) {
		Double amount = 0.0d;

		RecordLog.writeLogFile("enterprise:" + enterprise.getId() + ":limit:" + limit.name());
		Optional<TransactionLimit> transactionDailyLimit = transactionLimitRepository
				.findFirstByEnterpriseIdAndModeOrderByIdDesc("" + enterprise.getId(), limit.name());

		if (transactionDailyLimit.isPresent()) {
			if (limit == Limit.daily) {
				amount = transactionDailyLimit.get().getAmount().equals("0")
						? Double.parseDouble(properties.getDaily_transaction())
						: Double.valueOf(transactionDailyLimit.get().getAmount());
			} else if (limit == Limit.monthly) {
				amount = transactionDailyLimit.get().getAmount().equals("0")
						? Double.parseDouble(properties.getMonthly_transaction())
						: Double.valueOf(transactionDailyLimit.get().getAmount());
			}

		}
		
		if (limit == Limit.daily) {
			amount=amount.compareTo(0.0d)==0? Double.parseDouble(properties.getDaily_transaction())
					: amount;
			}else if (limit == Limit.monthly) {
				amount = amount.compareTo(0.0d)==0
						? Double.parseDouble(properties.getMonthly_transaction())
						: amount;
			}
		

		return amount;
	}

//, FundTransferDTO payment
	public enum Limit {
		daily, monthly
	}

}
