package com.federal.fedmobilesmecore.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.ScheduledPayment;
import com.federal.fedmobilesmecore.dto.ScheduledPaymentStore;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.InitTransactionRespModel;
import com.federal.fedmobilesmecore.model.MakerCheckerListGeneric;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.model.SchedulePaymentCreateInput;
import com.federal.fedmobilesmecore.model.SchedulePaymentMessage;
import com.federal.fedmobilesmecore.model.SchedulePaymentsInput;
import com.federal.fedmobilesmecore.model.TransactionMessage;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.BeneficiariesRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.SchedulePaymentStoreRepo;
import com.federal.fedmobilesmecore.repository.ScheduledPaymentRepo;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.service.CommonExternalService;
import com.federal.fedmobilesmecore.service.FundTransferInternalService;
import com.federal.fedmobilesmecore.service.FundTransferService;
import com.federal.fedmobilesmecore.service.SchedulePaymentsService;

@RestController
@RequestMapping(path = "/core/schedule")
public class SchedulePaymentsController {

	private static final Logger log4j = LogManager.getLogger(SchedulePaymentsController.class);

	@Autowired
	EnterprisesRepository entrepo;

	@Autowired
	UserRepository userrepo;

	@Autowired
	BeneficiariesRepository benrepo;

	@Autowired
	GlobalProperties properties;

	@Autowired
	SchedulePaymentsService schservice;

	@Autowired
	SchedulePaymentStoreRepo storerepo;

	@Autowired
	FundTransferInternalService ftservice;

	@Autowired
	CommonExternalService commonExternalService;
	
	@Autowired 
	FundTransferService fundTransferService;

	@Autowired
	ScheduledPaymentRepo schrepo;
	
	@Autowired
	ApplicationEnterprisRepository appliEnterprisRepository;

	ObjectMapper objectMapper = new ObjectMapper();

	@io.swagger.v3.oas.annotations.Operation(summary = "")
	@PostMapping(path = "/initiate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> initate(@RequestBody SchedulePaymentsInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /initiate api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		String ben_name = "";
		String ben_nick_name = "";
		ScheduledPaymentStore store = null;
		// TODO Add not blanks here.
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				if (enterprise.isActive()) {
					User user = userrepo.findByMobileAndMarkAsEnabled(input.getMobile(), true);
					if (user != null) {
						InitTransactionRespModel transactionRespModel = null;
						transactionRespModel=fundTransferService.limitCheckOfTrancaction(new BigDecimal(input.getAmount()),input.getMode(),false, user.getTransLimit());
						if(transactionRespModel==null) {
						SMEMessage msg = schservice.validateAccountNumber(enterprise.getAccNo(), false);
						if (msg.isStatus()) {
							SMEMessage remittermsg = schservice.validateAccountNumber(input.getRem_acc_no(), true);
							if (remittermsg.isStatus()) {
								if (input.getMode().equals("neft") || input.getMode().equals("fed2fed")) {
									if (StringUtils.isEmpty(input.getBen_acc_no())) {
										ben_name = "nil";
										ben_nick_name = "nil";
									} else {
										if (StringUtils.isEmpty(input.getBen_ifsc())) {
											Optional<Beneficiaries> benificiary = benrepo.findByAccNoAndEnterpriseId(
													input.getBen_acc_no(), "" + enterprise.getId());
											if (benificiary.isPresent()) {
												ben_name = benificiary.get().getName();
												ben_nick_name = benificiary.get().getNickName();
											}
										} else {
											Optional<Beneficiaries> benificiary = benrepo
													.findByAccNoAndEnterpriseIdAndIfsc(input.getBen_acc_no(),
															"" + enterprise.getId(), input.getBen_ifsc());
											if (benificiary.isPresent()) {
												ben_name = benificiary.get().getName();
												ben_nick_name = benificiary.get().getNickName();
											}
										}
									}
									// TODO More validations to be added in future.
									store = schservice.createSchedulePaymentStoreRecord(input, ben_name, ben_nick_name,
											enterprise);

								} else if (input.getMode().equals("p2p") || input.getMode().equals("p2a")) {
								
									if (StringUtils.isEmpty(input.getBen_mob_num())) {
										if (StringUtils.isEmpty(input.getBen_acc_no())) {
											ben_name = "nil";
											ben_nick_name = "nil";
										} else {
											if (StringUtils.isEmpty(input.getBen_ifsc()) || input.getBen_ifsc().equals("null")) {
												Optional<Beneficiaries> benificiary = benrepo
														.findByAccNoAndEnterpriseId(input.getBen_acc_no(),
																"" + enterprise.getId());
												if (benificiary.isPresent()) {
													ben_name = benificiary.get().getName();
													ben_nick_name = benificiary.get().getNickName();
												}
											} else {
												Optional<Beneficiaries> benificiary = benrepo
														.findByAccNoAndEnterpriseIdAndIfsc(input.getBen_acc_no(),
																"" + enterprise.getId(), input.getBen_ifsc());
												if (benificiary.isPresent()) {
													ben_name = benificiary.get().getName();
													ben_nick_name = benificiary.get().getNickName();
												}
											}
										}
									} else {
										if (StringUtils.isEmpty(input.getBen_mmid())) {
											Optional<Beneficiaries> benificiary = benrepo
													.findByMobile(input.getBen_mob_num());
											if (benificiary.isPresent()) {
												ben_name = benificiary.get().getName();
												ben_nick_name = benificiary.get().getNickName();
											}
										} else {
											Optional<Beneficiaries> benificiary = benrepo
													.findByMobileAndMmidAndEnterpriseId(input.getBen_mob_num(),
															input.getBen_mmid(), "" + enterprise.getId());
											if (benificiary.isPresent()) {
												ben_name = benificiary.get().getName();
												ben_nick_name = benificiary.get().getNickName();
											}
										}
									}
									// Main logic starts..
									store = schservice.createSchedulePaymentStoreRecord(input, ben_name, ben_nick_name,
											enterprise);
								}
								if (store != null) {
									message.setStatus(true);
									message.setMessage(store.getTransactionId());
								} else {
									message.setStatus(false);
									message.setMessage(
											"There was an error initiating the transaction. Please try again");
								}
							} else {
								message.setStatus(false);
								message.setMessage("Invalid Debit Account Number");
							}
						} else {
							message.setStatus(false);
							message.setMessage("Invalid Account Number");
						}
					}else {
						message.setStatus(transactionRespModel.getStatus());
						message.setDescription(transactionRespModel.getDescription());
					}
					} else {
						message.setStatus(false);
						message.setMessage("Mobile number is not registered with any user or User is in-active");
					}
				} else {
					message.setStatus(false);
					message.setMessage(properties.getSchpaymentsinactivecorp());
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Invalid Corporate.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /initiate api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Creating a scheduled transaction.")
	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /create api is calling. API request: "+input);
		TransactionMessage message = new TransactionMessage();
		String paymentobjstring = "";
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				if (enterprise.isActive()) {
					User user = userrepo.findByMobileAndMarkAsEnabled(input.getMobile(), true);
					if (user != null) {
						Optional<ScheduledPaymentStore> store = storerepo
								.findByTransactionId(input.getTransaction_id());
						if (store.isPresent()) {
							if (store.get().getMode().equals("neft") || store.get().getMode().equals("fed2fed")
									|| store.get().getMode().equals("p2p") || store.get().getMode().equals("p2a")) {
								ScheduledPayment payment = schservice.createSchedulePaymentRecord(store.get(),
										enterprise, user);
								if (payment != null) {
									if (payment.getId() > 0) {
										storerepo.delete(store.get());
										// Updating the next dates.
										schservice.setdates(payment);
										// TODO Maker checker
										Optional<User> currentuser = ftservice
												.getCurrentUserDetail(input.getAuth_token());
										if (currentuser.isPresent()) {
											Optional<Enterprises> userenterprise = entrepo
													.findById(Long.parseLong(currentuser.get().getEnterpriseId()));
											if (userenterprise.isPresent()) {
												ApplicationEnterpris applicationEnterpris	=appliEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(userenterprise.get().getApplicationFormId(),userenterprise.get().getPrefCorp());
												String authfund = applicationEnterpris.getAuthFund();
												String authben = applicationEnterpris.getAuthBen();
												if (authben == null) {
													authben = "0";
												}
												if (authfund == null) {
													authfund = "0";
												}
												if (schservice.enterprise_sole_proprietorship(currentuser) || schservice
														.enterprise_zero_external_user_authorize(currentuser)) {
													schservice.maker(currentuser, "ScheduledPayment", payment,
															Integer.valueOf(authfund));
													schservice.checker(currentuser, payment);
													payment.setStatus("approved");
													schrepo.save(payment);

													// TODO Add validations in future.
													message.setStatus(true);
													message.setMessage("success");
													message.setPayments(Arrays.asList(payment));
												} else {
//													log4j.info("Only maker logic will be called");
													schservice.maker(currentuser, "ScheduledPayment", payment,
															Integer.valueOf(authben));
													message.setStatus(true);
													message.setMessage("initiated");
												}

											}
										} else {
											message.setStatus(false);
											message.setMessage("Invalid user. Please try again");
										}
									}
								} else {
									message.setStatus(false);
									message.setMessage(
											"There was an error initiating the transaction. Please try again");
								}
							} else {
								message.setStatus(false);
								message.setMessage("Invalid mode. Please try again.");
							}
						} else {
							message.setStatus(false);
							message.setMessage("There was an error processing the transaction. Please try again.");
						}
					}
				} else {
					message.setStatus(false);
					message.setMessage(properties.getSchpaymentsinactivecorp());
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /create api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "List of all pending scheduled transactions.")
	@PostMapping(path = "/pending", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pending(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /pending api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();
		List<MakerCheckerListGeneric> output = new ArrayList<MakerCheckerListGeneric>();
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				List<ScheduledPayment> paymentlist = schrepo.findByStatusAndEnterpriseIdOrderByCreatedAtDesc("pending",
						"" + enterprise.getId());
				if (!CollectionUtils.isEmpty(paymentlist)) {
					String paymentobjstring = null;

					Optional<User> user = ftservice.getCurrentUserDetail(input.getAuth_token());
					if (user.isPresent()) {
						for (ScheduledPayment payment : paymentlist) {
							// TODO Completed according to the document. Need to understand how to identify
							// username based on maker and checker.
							MakerCheckerListGeneric resp = schservice.makerCheckerList(user.get(), payment, null, null);
							if (resp != null) {
								output.add(resp);
							}
						}
						Collections.sort(output, new Comparator<MakerCheckerListGeneric>() {
							@Override
							public int compare(MakerCheckerListGeneric o1, MakerCheckerListGeneric o2) {
							    boolean b1 = o1.isApprovalPermission();
								   boolean b2 = o2.isApprovalPermission();
								   return Boolean.compare( b2, b1 );
							}
						});			
						// paymentobjstring = objectMapper.writeValueAsString(output);
						message.setStatus(true);
						message.setMessage("Success");
						message.setTransactions(output);
					} else {
						message.setStatus(true);
						message.setMessage("No data found");
						message.setRecordid("");
					}

				} else {
					message.setStatus(true);
					message.setMessage("No data found");
					message.setRecordid("");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}

		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /pending api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "List of all approved scheduled transactions.")
	@PostMapping(path = "/approved", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approved(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /approved api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();
		List<MakerCheckerListGeneric> output = new ArrayList<MakerCheckerListGeneric>();
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				List<ScheduledPayment> paymentlist = schrepo.findByStatusAndEnterpriseIdOrderByCreatedAtDesc("approved",
						"" + enterprise.getId());
				if (!CollectionUtils.isEmpty(paymentlist)) {
//					log4j.info("Approved list size:" + paymentlist.size());
					String paymentobjstring = null;

					Optional<User> user = ftservice.getCurrentUserDetail(input.getAuth_token());
					if (user.isPresent()) {
						for (ScheduledPayment payment : paymentlist) {
							// TODO Completed according to the document. Need to understand how to identify
							// username based on maker and checker.
							MakerCheckerListGeneric resp = schservice.makerCheckerList(user.get(), payment, null, null);
							if (resp != null) {
								output.add(resp);
							}
						}
						// paymentobjstring = objectMapper.writeValueAsString(output);
						message.setStatus(true);
						message.setMessage("Success");
						message.setTransactions(output);
					} else {
						message.setStatus(true);
						message.setMessage("No data found");
						message.setRecordid("");
					}

				} else {
					message.setStatus(true);
					message.setMessage("No data found");
					message.setRecordid("");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}

		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /approved api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "List of all rejected scheduled transactions.")
	@PostMapping(path = "/rejected", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejected(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /rejected api is calling. API request: "+input);
		SchedulePaymentMessage message = new SchedulePaymentMessage();
		List<MakerCheckerListGeneric> output = new ArrayList<MakerCheckerListGeneric>();
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				List<ScheduledPayment> paymentlist = schrepo.findByStatusAndEnterpriseIdOrderByCreatedAtDesc("rejected",
						"" + enterprise.getId());
				if (!CollectionUtils.isEmpty(paymentlist)) {
					Optional<User> user = ftservice.getCurrentUserDetail(input.getAuth_token());
					if (user.isPresent()) {
						for (ScheduledPayment payment : paymentlist) {
							// TODO Completed according to the document. Need to understand how to identify
							// username based on maker and checker.
							MakerCheckerListGeneric resp = schservice.makerCheckerList(user.get(), payment, null, null);
							if (resp != null) {
								output.add(resp);
							}
						}
						message.setStatus(true);
						message.setMessage("Success");
						message.setTransactions(output);
					} else {
						message.setStatus(true);
						message.setMessage("No data found");
						message.setRecordid("");
					}

				} else {
					message.setStatus(true);
					message.setMessage("No data found");
					message.setRecordid("");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}

		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /rejected api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Ongoing count details")
	@PostMapping(path = "/ongoing_count", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ongoingcount(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /ongoing_count api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();

		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				List<ScheduledPayment> paymentlist = schrepo.findByStatusAndEnterpriseIdAndIsCompletedAndIsCancelled(
						"approved", "" + enterprise.getId(), BigDecimal.ZERO, BigDecimal.ZERO);
				if (CollectionUtils.isEmpty(paymentlist)) {
					message.setStatus(false);
					message.setRecordid("0");
					message.setMessage("No ongoing schedule payments available.");
					message.setDescription("If record id is zero there are no ongoing payments");
				} else {
					message.setStatus(true);
					message.setMessage("Ongoing schedule payments are available.");
					message.setRecordid("" + paymentlist.size());
					message.setDescription("If record id is greater than zero there are ongoing payments");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /ongoing_count api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Reject the schedule payment")
	@PostMapping(path = "/reject", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectpayment(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /reject api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				Optional<User> currentuser = ftservice.getCurrentUserDetail(input.getAuth_token());
				if (currentuser.isPresent()) {
					if (StringUtils.isNotBlank(input.getPayment_id())) {
						Optional<ScheduledPayment> pay = schrepo.findById(Long.parseLong(input.getPayment_id()));
						if (pay.isPresent()) {
							SMEMessage response = schservice.reject(currentuser.get(), pay.get(), null);
							if (response.getMessage().equals("success") || response.getMessage().equals("signed")) {
								ScheduledPayment payment = pay.get();
								payment.setStatus("rejected");
								schrepo.save(payment);
								message.setStatus(true);
								message.setMessage("Success");
							} else if (response.getMessage().equals("already_approved")) {
								message.setStatus(false);
								message.setMessage("Request is already approved");
							} else {
								message.setStatus(false);
								message.setMessage("Invalid Status");
							}
						} else {
							message.setStatus(false);
							message.setMessage("Invalid Scheduled Payment");
						}
					} else {
						message.setStatus(false);
						message.setMessage("Schedule Payment Record is mandatory");
					}

				} else {
					message.setStatus(false);
					message.setMessage("Invalid User.");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /reject api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Cancel the schedule payment")
	@PostMapping(path = "/cancel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cancelpayment(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /cancel api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				Optional<User> currentuser = ftservice.getCurrentUserDetail(input.getAuth_token());
				if (currentuser.isPresent()) {
					if (StringUtils.isNotBlank(input.getPayment_id())) {
						Optional<ScheduledPayment> pay = schrepo.findById(Long.parseLong(input.getPayment_id()));
						if (pay.isPresent()) {
							ScheduledPayment payment = pay.get();
							payment.setIsCancelled(BigDecimal.ONE);
							payment.setUpdatedAt(new Timestamp(new Date().getTime()));
							schrepo.save(payment);

							message.setStatus(true);
							message.setMessage("Success");
						} else {
							message.setStatus(false);
							message.setMessage("Invalid Scheduled Payment");
						}
					} else {
						message.setStatus(false);
						message.setMessage("Schedule Payment Record is mandatory");
					}

				} else {
					message.setStatus(false);
					message.setMessage("Invalid User.");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /cancel api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "Approve the schedule payment")
	@PostMapping(path = "/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approvepayment(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /approve api is calling. API request: "+input);
		SMEMessage message = new SMEMessage();
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				Optional<User> currentuser = ftservice.getCurrentUserDetail(input.getAuth_token());
				if (currentuser.isPresent()) {
					if (StringUtils.isNotBlank(input.getPayment_id())) {
						Optional<ScheduledPayment> pay = schrepo.findById(Long.parseLong(input.getPayment_id()));
						if (pay.isPresent()) {
					    	ZonedDateTime createdAt = ZonedDateTime.ofInstant( pay.get().getStartDate().toInstant(), ZoneId.systemDefault());
					    	Date startDate=Date.from(createdAt.toInstant());
					    	Timestamp currentDate= new Timestamp(new Date().getTime());
					    	 Timestamp endOfTheDay=new Timestamp(startDate.getTime());
							if(currentDate.before(endOfTheDay)) {	
							SMEMessage resp_message = schservice.checker(currentuser, pay.get());
							if (resp_message.getDescription().equals("success")) {
								ScheduledPayment payment = pay.get();
								payment.setStatus("approved");
								payment.setUpdatedAt(new Timestamp(new Date().getTime()));
								schrepo.save(payment);
								message.setStatus(true);
								message.setMessage("Success");
							} else if(resp_message.getDescription().equals("signed")) {
								message.setStatus(true);
								message.setMessage("Success");
							} else {
								message.setStatus(false);
								message.setMessage("Scheduled Payment:" + resp_message.getDescription());
							}
						}else {
							message.setStatus(false);
							message.setMessage("Start date cannot be previous/current date.");
						}
						} else {
							message.setStatus(false);
							message.setMessage("Invalid Scheduled Payment");
						}
					} else {
						message.setStatus(false);
						message.setMessage("Schedule Payment Record is mandatory");
					}

				} else {
					message.setStatus(false);
					message.setMessage("Invalid User.");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /approve api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

	@io.swagger.v3.oas.annotations.Operation(summary = "List of transactions belongs to schedule payment")
	@PostMapping(path = "/transactionlist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> transactionlist(@RequestBody SchedulePaymentCreateInput input) {
		RecordLog.writeLogFile("SchedulePaymentsController /transactionlist api is calling. API request: "+input);
		TransactionMessage message = new TransactionMessage();
		if (StringUtils.isNotBlank(input.getPref_corp())) {
			Enterprises enterprise = entrepo.findByPrefCorpAndActive(input.getPref_corp(),true);
			if (enterprise != null) {
				Optional<User> currentuser = ftservice.getCurrentUserDetail(input.getAuth_token());
				if (currentuser.isPresent()) {
					if (StringUtils.isNotBlank(input.getPayment_id())) {
						Optional<ScheduledPayment> pay = schrepo.findById(Long.parseLong(input.getPayment_id()));
						if (pay.isPresent()) {
							message.setStatus(true);
							List<ScheduledTransaction> transactions = schservice.getScheduledTransactions(pay.get());
							if (CollectionUtils.isEmpty(transactions)) {
								message.setStatus(true);

								message.setMessage("No Transactions.");

							} else {
								message.setStatus(true);
								message.setMessage("success");
								message.setTransactions(transactions);
							}

						} else {
							message.setStatus(false);
							message.setMessage("Invalid Scheduled Payment");
						}
					} else {
						message.setStatus(false);
						message.setMessage("Schedule Payment Record is mandatory");
					}

				} else {
					message.setStatus(false);
					message.setMessage("Invalid User.");
				}
			} else {
				message.setStatus(false);
				message.setMessage(properties.getSchpaymentsinvalidcorp());
			}
		} else {
			message.setStatus(false);
			message.setMessage("Enterprise cannot be blank.");
		}
		RecordLog.writeLogFile("SchedulePaymentsController /transactionlist api completed. API response: "+message);
		return ResponseEntity.ok(message);
	}

}
