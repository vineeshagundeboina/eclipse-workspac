package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.ApplicationEnterpris;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.APIResponse;
import com.federal.fedmobilesmecore.model.AproveRejectAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiariesAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiaryAccountNoAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiaryMobileNumberAPIRequest;
import com.federal.fedmobilesmecore.model.BeneficiaryNickNameAPIRequest;
import com.federal.fedmobilesmecore.model.CreateBeneficiaryResponse;
import com.federal.fedmobilesmecore.model.CreateBenificiaryApprovedEnterpriseReq;
import com.federal.fedmobilesmecore.model.DeleteBeneficiaryRequest;
import com.federal.fedmobilesmecore.model.MakerCheckerBeneficiaryModel;
import com.federal.fedmobilesmecore.model.MakerCheckerRejecedList;
import com.federal.fedmobilesmecore.model.SpcificPendingBeneficiaryModel;
import com.federal.fedmobilesmecore.repository.ApplicationEnterprisRepository;
import com.federal.fedmobilesmecore.repository.BeneficiariesRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository_V1;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class BeneficiaryService {

	@Autowired
	UserRepository_V1 userRepository;

	@Autowired
	GlobalProperties globalProperties;

	@Autowired
	BeneficiariesRepository beneficiariesRepository;

	@Autowired
	EnterprisesRepository_V1 enterprisesRepository;

	@Autowired
	ApplicationEnterprisRepository appliEnterprisRepository;

	@Autowired
	MakerCheckerForBeneficiaryService makerservice;

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	private SimBindingService simBindingService;

	public APIResponse validateNickName(BeneficiaryNickNameAPIRequest nickNameAPIRequest) {
		APIResponse apiResponse = new APIResponse();
		try {
			if (validateNickname(nickNameAPIRequest.getNick_name())) {
				List<Beneficiaries> beneficiaries = getBeneficiariesNickName(nickNameAPIRequest.getAuth_token(),
						nickNameAPIRequest.getNick_name());
//				RecordLog.writeLogFile("beneficiaries " + beneficiaries);
				/*
				 * if (beneficiaries != null) { for (Beneficiaries beneficiaries2 :
				 * beneficiaries) { System.out.println("beneficiaries" +
				 * (beneficiaries2).getNickName()); if (beneficiaries2.getNickName() == null ||
				 * beneficiaries2.getNickName().equals("")) { apiResponse.setStatus(true);
				 * apiResponse.setMessage(globalProperties.getSuccessmsg()); } else if
				 * (!(beneficiaries2).getNickName().isEmpty()) { apiResponse.setStatus(false);
				 * apiResponse.setRecordId(null);
				 * apiResponse.setMessage(globalProperties.getNickNameExists()); }
				 * 
				 * } }
				 */ /*
					 * else { apiResponse.setStatus(false);
					 * apiResponse.setMessage(globalProperties.getErrormsg()); }
					 */
				if (CollectionUtils.isEmpty(beneficiaries)) {
					apiResponse.setStatus(true);
					apiResponse.setMessage(globalProperties.getSuccessmsg());
				} else {
					apiResponse.setStatus(false);
					apiResponse.setDescription(globalProperties.getNickNameExists());
					apiResponse.setMessage(globalProperties.getNickNameExists());
					// apiResponse.setMessage(globalProperties.getErrormsg());
				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(
						"Nick name consist of either space or special character except underscore or hypens");
				apiResponse.setMessage("Nickname is not valid");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for mobile "+nickNameAPIRequest.getPref_corp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setMessage(e.getMessage());
		}

		return apiResponse;
	}

	public boolean validateNickname(String nickname) {

		Pattern pattern = Pattern.compile("^[\\w-_]+$");
		Matcher matcher = pattern.matcher(nickname);
		boolean matches = false;
		while (matcher.find()) {
			matches = true;
		}
		return matches;
	}

	public APIResponse validateMobileNumber(BeneficiaryMobileNumberAPIRequest mobileNumberAPIRequest) {
		APIResponse apiResponse = new APIResponse();
		try {
			List<Beneficiaries> beneficiaries = getBeneficiariesMobileNo(mobileNumberAPIRequest.getAuth_token(),
					mobileNumberAPIRequest.getMobile());
			/*
			 * if (beneficiaries != null) { for (Beneficiaries beneficiaries2 :
			 * beneficiaries) { System.out.println("beneficiaries" +
			 * (beneficiaries2).getMobile()); if (beneficiaries2.getMobile() == null ||
			 * beneficiaries2.getMobile().equals("")) { apiResponse.setStatus(true);
			 * apiResponse.setMessage(globalProperties.getSuccessmsg()); } else if
			 * (!(beneficiaries2).getMobile().isEmpty()) { apiResponse.setStatus(false);
			 * apiResponse.setMessage(globalProperties.getMobileExist());
			 * apiResponse.setRecordId((beneficiaries2).getMobile()); }
			 * 
			 * } } else { apiResponse.setStatus(false);
			 * apiResponse.setMessage(globalProperties.getErrormsg()); }
			 */
			if (CollectionUtils.isEmpty(beneficiaries)) {
				apiResponse.setStatus(true);
				apiResponse.setMessage(globalProperties.getSuccessmsg());
			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(globalProperties.getMobileExist());
				apiResponse.setMessage(globalProperties.getMobileExist());
				// apiResponse.setMessage(globalProperties.getErrormsg());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for mobile "+mobileNumberAPIRequest.getMobile()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setMessage(e.getMessage());
		}

		return apiResponse;
	}

	private List<Beneficiaries> getBeneficiariesNickName(String auth_token, String nickName) {
		List<Beneficiaries> beneficiaries = null;
		Optional<User> user = userRepository.findByAuthTokenAndMarkAsEnabled(auth_token, true);
		if (user.isPresent()) {
//			RecordLog.writeLogFile(user.get().getEnterpriseId());
			beneficiaries = beneficiariesRepository.findAllByEnterpriseIdAndNickNameIgnoreCaseAndStatusIsNot(user.get().getEnterpriseId(),
					nickName,"rejected");
		}

		return beneficiaries;
	}

	private List<Beneficiaries> getBeneficiariesMobileNo(String auth_token, String mobileno) {
		List<Beneficiaries> beneficiaries = null;
		Optional<User> user = userRepository.findByAuthTokenAndMarkAsEnabled(auth_token, true);
		if (user.isPresent()) {
//			RecordLog.writeLogFile(user.get().getEnterpriseId());
			beneficiaries = beneficiariesRepository.findAllByEnterpriseIdAndMobile(user.get().getEnterpriseId(),
					mobileno);
		}

		return beneficiaries;
	}

	private List<Beneficiaries> getBeneficiariesAccNo(String auth_token, String accno) {
		Optional<User> user = userRepository.findByAuthTokenAndMarkAsEnabled(auth_token, true);
		List<Beneficiaries> beneficiaries = null;
		if (user.isPresent()) {
//			RecordLog.writeLogFile(user.get().getEnterpriseId());
			beneficiaries = beneficiariesRepository.findAllByEnterpriseIdAndAccNo(user.get().getEnterpriseId(), accno);
		}
		return beneficiaries;

	}
	
	private List<Beneficiaries> getActiveBeneficiariesAccNo(String auth_token, String accno) {
		Optional<User> user = userRepository.findByAuthTokenAndMarkAsEnabled(auth_token, true);
		List<Beneficiaries> beneficiaries = null;
		if (user.isPresent()) {
//			RecordLog.writeLogFile(user.get().getEnterpriseId());
			beneficiaries = beneficiariesRepository.findAllByEnterpriseIdAndAccNoAndIsActive(user.get().getEnterpriseId(), accno, true);
		}
		return beneficiaries;

	}

	/*
	 * private List<Beneficiaries> getBeneficiaries(String auth_token) {
	 * Optional<User> user =
	 * userRepository.findByAuthTokenAndMarkAsEnabled(auth_token, true);
	 * System.out.println(user.get().getEnterpriseId()); List<Beneficiaries>
	 * beneficiaries =
	 * beneficiariesRepository.findAllByEnterpriseId(user.get().getEnterpriseId());
	 * return beneficiaries; }
	 */

	public APIResponse validateAccountNumber(BeneficiaryAccountNoAPIRequest validateAPIRequest) {
		APIResponse apiResponse = new APIResponse();
		try {
			List<Beneficiaries> beneficiaries = getBeneficiariesAccNo(validateAPIRequest.getAuth_token(),
					validateAPIRequest.getAcc_no());

			/*
			 * if (beneficiaries != null) { for (Beneficiaries beneficiaries2 :
			 * beneficiaries) { System.out.println("beneficiaries" +
			 * beneficiaries2.getAccNo()); if (beneficiaries2.getAccNo() == null ||
			 * beneficiaries2.getAccNo().equals("")) { apiResponse.setStatus(true);
			 * apiResponse.setMessage(globalProperties.getSuccessmsg()); } else if
			 * (!beneficiaries2.getAccNo().isEmpty()) { apiResponse.setStatus(false);
			 * apiResponse.setMessage(globalProperties.getAccNoExist());
			 * apiResponse.setRecordId(beneficiaries2.getAccNo());
			 * 
			 * } else { apiResponse.setStatus(true);
			 * apiResponse.setMessage(globalProperties.getSuccessmsg()); }
			 * 
			 * 
			 * } } else { apiResponse.setStatus(false);
			 * apiResponse.setMessage(globalProperties.getErrormsg()); }
			 */
			if (CollectionUtils.isEmpty(beneficiaries)) {
				apiResponse.setStatus(true);
				apiResponse.setMessage(globalProperties.getSuccessmsg());
			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(globalProperties.getAccNoExist());
				apiResponse.setMessage(globalProperties.getAccNoExist());
				// apiResponse.setMessage(globalProperties.getErrormsg());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for account "+validateAPIRequest.getAcc_no()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getMessage());
		}

		return apiResponse;
	}
	
	
	
	
	
	public APIResponse validateActiveAccountNumber(BeneficiaryAccountNoAPIRequest validateAPIRequest) {
		APIResponse apiResponse = new APIResponse();
		try {
			List<Beneficiaries> beneficiaries = getActiveBeneficiariesAccNo(validateAPIRequest.getAuth_token(),
					validateAPIRequest.getAcc_no());

			/*
			 * if (beneficiaries != null) { for (Beneficiaries beneficiaries2 :
			 * beneficiaries) { System.out.println("beneficiaries" +
			 * beneficiaries2.getAccNo()); if (beneficiaries2.getAccNo() == null ||
			 * beneficiaries2.getAccNo().equals("")) { apiResponse.setStatus(true);
			 * apiResponse.setMessage(globalProperties.getSuccessmsg()); } else if
			 * (!beneficiaries2.getAccNo().isEmpty()) { apiResponse.setStatus(false);
			 * apiResponse.setMessage(globalProperties.getAccNoExist());
			 * apiResponse.setRecordId(beneficiaries2.getAccNo());
			 * 
			 * } else { apiResponse.setStatus(true);
			 * apiResponse.setMessage(globalProperties.getSuccessmsg()); }
			 * 
			 * 
			 * } } else { apiResponse.setStatus(false);
			 * apiResponse.setMessage(globalProperties.getErrormsg()); }
			 */
			if (CollectionUtils.isEmpty(beneficiaries)) {
				apiResponse.setStatus(true);
				apiResponse.setMessage(globalProperties.getSuccessmsg());
			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(globalProperties.getAccNoExist());
				apiResponse.setMessage(globalProperties.getAccNoExist());
				// apiResponse.setMessage(globalProperties.getErrormsg());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for mobile "+validateAPIRequest.getAcc_no()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(e.getMessage());
		}

		return apiResponse;
	}
	
	

	// PendingBeneficiaryCount
	public APIResponse pendingBeneficiaryCount(BeneficiariesAPIRequest pendingBeneficiary) {
		APIResponse apiResponse = new APIResponse();
		Enterprises enterprises = enterprisesRepository.findByPrefCorpAndActive(pendingBeneficiary.getPref_corp(),true);
//		RecordLog.writeLogFile("Hiiii");
		try {
			if (enterprises!=null) {
				long beneficiaries = beneficiariesRepository.countByStatusAndEnterpriseId("new",
						String.valueOf(enterprises.getId()));
//				RecordLog.writeLogFile("pending beneficiary count: " + beneficiaries);
				apiResponse.setStatus(true);
				apiResponse.setMessage(globalProperties.getSuccessmsg());
				apiResponse.setRecordId(String.valueOf(beneficiaries));
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getInvalidEnterprise());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: for prefcorp "+pendingBeneficiary.getPref_corp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setMessage(e.getMessage());
		}
		return apiResponse;

	}

	// create
	public APIResponse createBenificiaryForApprovedEnterprise(
			CreateBenificiaryApprovedEnterpriseReq approvedEnterprise) {
		CreateBeneficiaryResponse response = new CreateBeneficiaryResponse();
		APIResponse apiResponse = new APIResponse();
//		RecordLog.writeLogFile("Enterprise details: " + enterprises.get().toString());
		try {
			Enterprises enterprises = enterprisesRepository.findByPrefCorpAndActive(approvedEnterprise.getPref_corp(),true);
			if (enterprises!=null) {
				List<Beneficiaries> activeBeneficiary = beneficiariesRepository
						.findByEnterpriseIdAndStatusIsNot(String.valueOf(enterprises.getId()), "rejected");
				List<String> active_acc_no = new ArrayList<String>();
				List<String> active_nick_name = new ArrayList<String>();

				for (Beneficiaries activeaccNo : activeBeneficiary) {
					active_acc_no.add(activeaccNo.getAccNo());
					active_nick_name.add(activeaccNo.getNickName());
				}
				if (active_acc_no.contains(approvedEnterprise.getBenificiary().getAcc_no())) {
					apiResponse.setStatus(false);
					apiResponse.setMessage(globalProperties.getAccNoExist());
					apiResponse.setDescription("failed");
					apiResponse.setRecordId(null);
				} else if (active_nick_name.contains(approvedEnterprise.getBenificiary().getNick_name())) {
					apiResponse.setStatus(false);
					apiResponse.setMessage(globalProperties.getNickNameExists());
					apiResponse.setDescription("failed");
					apiResponse.setRecordId(null);
				} else {
					// Creating the ObjectMapper object
					mapper = new ObjectMapper();
					String jsonString;
					Beneficiaries beneficiaries = null;
					Optional<User> currentUser = userRepository.findByAuthToken(approvedEnterprise.getAuth_token());
					if (currentUser.isPresent()) {
					try {
						beneficiaries = createBeneficiaries(enterprises, approvedEnterprise);
					} catch (Exception e) {
						RecordLog.writeLogFile("Exception occured at: for prefcorp "+approvedEnterprise.getPref_corp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
						e.printStackTrace();
						if (e.getMessage().equals("Invalid IFSC code")) {
							apiResponse.setStatus(false);
							apiResponse.setMessage("Invalid IFSC code");
							apiResponse.setDescription("failed");
							apiResponse.setRecordId(null);
						}
					}
					if (beneficiaries.getId() != 0) {
//						Optional<User> currentUser = userRepository.findByAuthToken(approvedEnterprise.getAuth_token());
//						if (currentUser.isPresent()) 
						ApplicationEnterpris applicationEnterprise=appliEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterprises.getApplicationFormId(),enterprises.getPrefCorp());

						String count=applicationEnterprise.getAuthBen()==null?"0":applicationEnterprise.getAuthBen();
						String role=currentUser.get().getRole()==null?"":currentUser.get().getRole();
						
						System.out.println("iam checking role: "+role);
						System.out.println(" iam checking auth ben: "+count);
						RecordLog.writeLogFile("enterprises>>>>>>>>>>>>>>>>>" + enterprises.getConstitution());
						if ((applicationEnterprise.getConstitution().equals("SP") 
								|| (count.equals("0") && ( role.equals("") )))
								&& (!role.equals("external"))) {
								makerservice.maker(currentUser.get(), "Beneficiary", beneficiaries,
										enterprises.getAuthBen());
								makerservice.checker(currentUser.get(), beneficiaries);
								beneficiaries.setStatus("approved");
								
								Long cool=applicationEnterprise.getCoolingPeriod()==null?Long.valueOf(0):applicationEnterprise.getCoolingPeriod();
								if(cool.equals(Long.valueOf(0l))) {
								beneficiaries.setActive(true);
								simBindingService.sendSMS(String.format(globalProperties.getBeneficiary_approved_msg(),beneficiaries.getNickName()), currentUser.get().getMobile());
								}else {
									beneficiaries.setActive(false);
								}
								
								
								
								beneficiaries.setApprovedAt(new Timestamp(new Date().getTime()));
								beneficiariesRepository.save(beneficiaries);
								apiResponse.setStatus(true);
								apiResponse.setMessage(globalProperties.getSuccessmsg());
								apiResponse.setDescription(globalProperties.getSuccessmsg());
								response.setStatus(globalProperties.getSuccessmsg());
								response.setRef_No(beneficiaries.getRefNo());
								response.setBen_acc_n(beneficiaries.getAccNo());
								response.setBen_name(beneficiaries.getName());
								response.setBeneficiaries(beneficiaries);
								// Converting the Object to JSONString
								jsonString = mapper.writeValueAsString(response);
//								RecordLog.writeLogFile("Beneficiary details: " + jsonString);
								apiResponse.setRecordId(jsonString);

							} else {
								makerservice.maker(currentUser.get(), "Beneficiary", beneficiaries,
										applicationEnterprise.getAuthBen());
								// benResponse.put("Status", "initiated");
								response.setStatus("initiated");
								response.setRef_No(beneficiaries.getRefNo());
								response.setBen_acc_n(beneficiaries.getAccNo());
								response.setBen_name(beneficiaries.getName());
								response.setBeneficiaries(beneficiaries);
								// Converting the Object to JSONString
								jsonString = mapper.writeValueAsString(response);
								apiResponse.setStatus(true);
								apiResponse.setMessage(globalProperties.getSuccessmsg());
								apiResponse.setDescription(globalProperties.getSuccessmsg());
								apiResponse.setRecordId(jsonString);
							}
						} else {
						apiResponse.setStatus(false);
						apiResponse.setDescription("Unable to create beneficiary for this enterprise.");
						apiResponse.setMessage("Error creating beneficiary.");
						apiResponse.setRecordId(null);
					}

				} else {
					apiResponse.setStatus(false);
					apiResponse.setMessage(globalProperties.getUserNotFound());
					apiResponse.setDescription(globalProperties.getUsernotfoundmsg());
					apiResponse.setRecordId(null);
				}
				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getInvalidEnterprise());
				apiResponse.setDescription(globalProperties.getInvalidEnterprise());
				apiResponse.setRecordId(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("EXOCBeneficiaryService  createbeneficiary 448: for prefcorp "+approvedEnterprise.getPref_corp()+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
		}

		return apiResponse;

	}

	private Beneficiaries createBeneficiaries(Enterprises enterprises,
			CreateBenificiaryApprovedEnterpriseReq approvedEnterprise) throws Exception {
		Beneficiaries beneficiaries = new Beneficiaries();
		String ref_no = generateRef_no();
		RecordLog.writeLogFile("Beneficiary refNo: " + ref_no);
//		RecordLog.writeLogFile("enterpri");
		// beneficiaries.setId(enterprises.get().getId());
		beneficiaries.setMode(approvedEnterprise.getBenificiary().getMode().toLowerCase());
		beneficiaries.setNickName(approvedEnterprise.getBenificiary().getNick_name());
		beneficiaries.setMmid(approvedEnterprise.getBenificiary().getMmid());
		beneficiaries.setMobile(approvedEnterprise.getBenificiary().getMobile());
		beneficiaries.setRefNo(ref_no);
		// Added by vikas as a bugfix in pending list.
		beneficiaries.setStatus("new");
		beneficiaries.setEnterpriseId(String.valueOf(enterprises.getId()));
		if (approvedEnterprise.getBenificiary().getIfsc().length() > 11) {
			throw new Exception("Invalid IFSC code");
		}
		beneficiaries.setIfsc(approvedEnterprise.getBenificiary().getMode().equals("self") ? "FEDC123"
				: approvedEnterprise.getBenificiary().getIfsc());
		beneficiaries.setName(approvedEnterprise.getBenificiary().getName());
		// beneficiaries.setBen_No(approvedEnterprise.getBenificiary().getBen_acc_no());
		beneficiaries.setCreatedAt(new Timestamp(new Date().getTime()));
		beneficiaries.setAccNo(approvedEnterprise.getBenificiary().getBen_acc_no());
		beneficiaries.setUpdatedAt(new Timestamp(new Date().getTime()));
		return beneficiariesRepository.save(beneficiaries);

	}

	private String generateRef_no() {

		String zeros = "0000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X10000000), 16);
		s = zeros.substring(s.length()) + s;
		s = "BEN" + s;
		return s.toUpperCase();

	}

	// get specific pending beneficiary
	@SuppressWarnings("unused")
	public APIResponse getSpcificPendingBeneficiary(SpcificPendingBeneficiaryModel approvedEnterprise) {
		APIResponse apiResponse = new APIResponse();
		try {
			// Creating the ObjectMapper object
			// mapper = new ObjectMapper();
			String jsonString;
			MakerCheckerBeneficiaryModel beneficiaryModel = null;
			List<MakerCheckerBeneficiaryModel> specificBeneficiary = null;
			List<MakerCheckerBeneficiaryModel> jsonBeneficiaryModels = null;
			Enterprises enterprises = enterprisesRepository.findByPrefCorpAndActive(approvedEnterprise.getPref_corp(),true);
			if (enterprises!=null) {
				jsonBeneficiaryModels = new ArrayList<MakerCheckerBeneficiaryModel>();
				List<Beneficiaries> beneficiaries = beneficiariesRepository
						.findByStatusAndEnterpriseIdOrderByUpdatedAtDesc("new",
								String.valueOf(enterprises.getId()));
				Optional<User> current_User = userRepository.findByAuthToken(approvedEnterprise.getAuth_token());
				if (current_User.isPresent()) {
					for (Beneficiaries beneficiaries2 : beneficiaries) {
						beneficiaryModel = makerservice.makerCheckerList(current_User.get(), beneficiaries2);
						jsonBeneficiaryModels.add(beneficiaryModel);
					}
				} else {
					apiResponse.setStatus(false);
					apiResponse.setDescription("Invalid user login, you need to login again!");
					apiResponse.setMessage("Invalid user login, you need to login again!");
					apiResponse.setRecordId(null);
				}

//				RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>>>>>>" + jsonBeneficiaryModels.size());
				if (jsonBeneficiaryModels != null) {
					specificBeneficiary = jsonBeneficiaryModels.stream().limit(approvedEnterprise.getCount())
							.collect(Collectors.toList());
//					RecordLog.writeLogFile(">>>>>>>>>>>>>>" + specificBeneficiary);
					// Converting the Object to JSONString
					jsonString = mapper.writeValueAsString(specificBeneficiary);
					apiResponse.setStatus(true);
					apiResponse.setDescription(globalProperties.getSuccessmsg());
					apiResponse.setMessage(globalProperties.getSuccessmsg());
					apiResponse.setRecordId(jsonString);

				} else {
					jsonString = mapper.writeValueAsString(jsonBeneficiaryModels);
					apiResponse.setStatus(true);
					apiResponse.setDescription(globalProperties.getSuccessmsg());
					apiResponse.setMessage(globalProperties.getSuccessmsg());
					apiResponse.setRecordId(jsonString);
				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(globalProperties.getInvalidEnterprise());
				apiResponse.setMessage(globalProperties.getInvalidEnterprise());
				apiResponse.setRecordId(null);

			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setDescription(globalProperties.getExceptionerrcode());
			apiResponse.setMessage(globalProperties.getExceptionerrmsg());
			apiResponse.setRecordId(null);
		}

		return apiResponse;

	}

	// approveBeneficiary with BeneficiaryID
	public APIResponse approveBeneficiaryWithBeneficiryId(AproveRejectAPIRequest beneficiaryRequest) {
		APIResponse apiResponse = new APIResponse();
		String approval_status = null;

		try {

			Optional<Beneficiaries> beneficiaries = beneficiariesRepository.findByRefNo(beneficiaryRequest.getRef_no());
			if (beneficiaries.isPresent()) {
				Optional<User> current_User = userRepository.findByAuthToken(beneficiaryRequest.getAuth_token());
				if (current_User.isPresent()) {
					approval_status = makerservice.checker(current_User.get(), beneficiaries.get());
//					RecordLog.writeLogFile(">>>>>>>>" + approval_status);
					if (approval_status.equals(globalProperties.getSuccessmsg())) {
						Beneficiaries beneficiary = beneficiariesRepository.getOne(beneficiaries.get().getId());
						beneficiary.setStatus("approved");
						Optional<Enterprises> enterpriseV1 = enterprisesRepository.findById(Long.parseLong(beneficiary.getEnterpriseId()));
						
						
						
						
						ApplicationEnterpris applnApplicationEnterpris=appliEnterprisRepository.findByApplicationFormIdAndPrefCorpIgnoreCase(enterpriseV1.get().getApplicationFormId(),enterpriseV1.get().getPrefCorp());
						
						
						
						RecordLog.writeLogFile("Cooling period"+applnApplicationEnterpris.getCoolingPeriod());
						Long cool=applnApplicationEnterpris.getCoolingPeriod()==null?Long.valueOf(0):applnApplicationEnterpris.getCoolingPeriod();
						if(cool== 0l) {
							beneficiary.setActive(true);
							simBindingService.sendSMS(String.format(globalProperties.getBeneficiary_approved_msg(),beneficiary.getNickName()), current_User.get().getMobile());
						}
						else {
							beneficiary.setActive(false);
						}
						beneficiary.setUpdatedAt(new Timestamp(new Date().getTime()));
						beneficiary.setApprovedAt(new Timestamp(new Date().getTime()));
						beneficiariesRepository.save(beneficiary);
						apiResponse.setStatus(true);
						apiResponse.setMessage(globalProperties.getSuccessmsg());
					} else if (approval_status.equals("signed")) {
						apiResponse.setStatus(true);
						apiResponse.setMessage(globalProperties.getSuccessmsg());
					} else if (approval_status.equals(globalProperties.getAlreadyApproved())) {
						apiResponse.setStatus(false);
						apiResponse.setMessage(globalProperties.getSuccessmsg());
						apiResponse.setRecordId(globalProperties.getApproved());

					} else {
						apiResponse.setStatus(false);
						apiResponse.setMessage(beneficiaries.get().getStatus());
						apiResponse.setDescription(beneficiaries.get().getStatus());
					}

				} else {
					apiResponse.setStatus(false);
					apiResponse.setDescription(globalProperties.getUserNotFound());
					apiResponse.setMessage(globalProperties.getUserNotFound());
				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage("Error getting beneficiary.");
				apiResponse.setDescription("Beneficiary not found with given reference number.");
				apiResponse.setRecordId(null);
			}

		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(globalProperties.getExceptionerrmsg());
			apiResponse.setDescription(e.getLocalizedMessage());
		}

		return apiResponse;

	}

	// gives approved beneficiaries
	public APIResponse listOfApprovedbeneficiaries(BeneficiariesAPIRequest beneficiaryRequest) {
		APIResponse apiResponse = new APIResponse();
		List<MakerCheckerBeneficiaryModel> beneficiaryModels = new ArrayList<MakerCheckerBeneficiaryModel>();
		try {
			String jsonString;
			MakerCheckerBeneficiaryModel checkerListModel = null;
		    Enterprises enterprises = enterprisesRepository.findByActiveAndPrefCorp(true,beneficiaryRequest.getPref_corp());
			if (enterprises!=null) {
				List<Beneficiaries> beneficiaries = beneficiariesRepository
						.findByStatusAndIsActiveAndIsJobActiveAndEnterpriseIdOrderByApprovedAtDesc("approved", true, 0,
								String.valueOf(enterprises.getId()));
//				RecordLog.writeLogFile("Approved beneficiaries size: "+beneficiaries.size());
				if (beneficiaries.size() != 0) {
//					RecordLog.writeLogFile("Hello");
					Optional<User> current_User = userRepository
							.findByAuthTokenAndMarkAsEnabled(beneficiaryRequest.getAuth_token(), true);
					if (current_User.isPresent()) {
						for (Beneficiaries beneficiary : beneficiaries) {
							checkerListModel = makerservice.makerCheckerList(current_User.get(), beneficiary);
							checkerListModel.setCoolingPeriod(enterprises.getCoolingPeriod());
							beneficiaryModels.add(checkerListModel);

//							RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>>" + beneficiaryModels.get(0));
							jsonString = mapper.writeValueAsString(beneficiaryModels);
							apiResponse.setStatus(true);
							apiResponse.setMessage(globalProperties.getSuccessmsg());
							apiResponse.setRecordId(jsonString);
						}
					} else {
						apiResponse.setStatus(false);
						apiResponse.setDescription(globalProperties.getUserNotFound());
						apiResponse.setMessage(globalProperties.getUserNotFound());
						apiResponse.setRecordId(null);

					}

				} else {
					jsonString = mapper.writeValueAsString(beneficiaries);
					apiResponse.setStatus(true);
					apiResponse.setMessage(globalProperties.getSuccessmsg());
					apiResponse.setRecordId(jsonString);
				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(globalProperties.getInvalidEnterprise());
				apiResponse.setMessage(globalProperties.getInvalidEnterprise());
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			apiResponse.setStatus(false);
			apiResponse.setMessage(globalProperties.getExceptionerrmsg());
			apiResponse.setDescription(e.getLocalizedMessage());
		}

		return apiResponse;

	}

	// Gives pendingBeneficiaryList
	public APIResponse listOfPendingBeneficies(BeneficiariesAPIRequest beneficiaryRequest) {
		APIResponse apiResponse = new APIResponse();
		MakerCheckerBeneficiaryModel checkerListModel = null;
		try {
			// Creating the ObjectMapper object
			// ObjectMapper mapper = new ObjectMapper();
			String jsonString;
			List<MakerCheckerBeneficiaryModel> beneficiaryModels = null;
		  Enterprises enterprises = enterprisesRepository.findByPrefCorpAndActive(beneficiaryRequest.getPref_corp(),true);
//			RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>>" + enterprises.get());
			if (enterprises!=null) {
//				RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Hello>>>>>>>>>>>>");
				List<Beneficiaries> beneficiaries = beneficiariesRepository
						.findByStatusAndEnterpriseIdAndOperationIdNotNullOrderByUpdatedAtDesc("new",
								String.valueOf(enterprises.getId()));
				if (!beneficiaries.isEmpty()) {
					Optional<User> current_User = userRepository
							.findByAuthTokenAndMarkAsEnabled(beneficiaryRequest.getAuth_token(), true);
					if (current_User.isPresent()) {
						beneficiaryModels = new ArrayList<MakerCheckerBeneficiaryModel>();
						for (Beneficiaries beneficiary : beneficiaries) {
							checkerListModel = makerservice.makerCheckerList(current_User.get(), beneficiary);
//							RecordLog.writeLogFile("checkerListModel" + checkerListModel.toString());
							beneficiaryModels.add(checkerListModel);
//							RecordLog.writeLogFile("beneficiaries size"+beneficiaryModels.size());
						}
						
						Collections.sort(beneficiaryModels, new Comparator<MakerCheckerBeneficiaryModel>() {
							@Override
							public int compare(MakerCheckerBeneficiaryModel o1, MakerCheckerBeneficiaryModel o2) {
							    boolean b1 = o1.isApprovalPermission();
								   boolean b2 = o2.isApprovalPermission();
								   return Boolean.compare( b2, b1 );
							}
						});
						
						// Converting the Object to JSONString
						jsonString = mapper.writeValueAsString(beneficiaryModels);
						RecordLog.writeLogFile("json String converted " + jsonString);
						apiResponse.setStatus(true);
						apiResponse.setMessage(globalProperties.getSuccessmsg());
						apiResponse.setRecordId(jsonString);
					} else {
						apiResponse.setStatus(false);
						apiResponse.setDescription(globalProperties.getCurrentUser());
						apiResponse.setMessage(globalProperties.getUserNotFound());
						apiResponse.setRecordId(null);
					}
				} else {
					// Converting the Object to JSONString
					jsonString = mapper.writeValueAsString(beneficiaries);
					apiResponse.setStatus(true);
					apiResponse.setMessage(globalProperties.getSuccessmsg());
					apiResponse.setRecordId(jsonString);
				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage(globalProperties.getInvalidEnterprise());
				apiResponse.setRecordId(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(globalProperties.getExceptionerrmsg());
			apiResponse.setDescription(e.getLocalizedMessage());
		}

		return apiResponse;

	}

	// RejectedList
	@SuppressWarnings("null")
	public APIResponse rejectedListBeneficiary(BeneficiariesAPIRequest beneficiaryRequest) {
		APIResponse apiResponse = new APIResponse();
		MakerCheckerRejecedList checkerRejecedList = null;
		try {
			String jsonString;
			List<MakerCheckerRejecedList> checkerRejecedLists = null;
			Optional<User> current_User = userRepository
					.findByAuthTokenAndMarkAsEnabled(beneficiaryRequest.getAuth_token(), true);
			Enterprises enterprises = enterprisesRepository.findByPrefCorpAndActive(beneficiaryRequest.getPref_corp(),true);
			if (current_User.isPresent()) {
				if (enterprises!=null) {
					List<Beneficiaries> beneficiaries = beneficiariesRepository
							.findByStatusAndEnterpriseIdOrderByUpdatedAtDesc("rejected",
									String.valueOf(enterprises.getId()));
					if (beneficiaries != null) {
						checkerRejecedLists = new ArrayList<MakerCheckerRejecedList>();
						for (Beneficiaries beneficiaries2 : beneficiaries) {
							checkerRejecedList = makerservice.makerCheckerRejecedList(current_User.get(),
									beneficiaries2);
							checkerRejecedLists.add(checkerRejecedList);
						}
						if (checkerRejecedList != null) {
							// Converting the Object to JSONString
							jsonString = mapper.writeValueAsString(checkerRejecedLists);
							apiResponse.setStatus(true);
							apiResponse.setDescription(globalProperties.getSuccessmsg());
							apiResponse.setMessage(globalProperties.getSuccessmsg());
							apiResponse.setRecordId(jsonString);
						} else {
							// Converting the Object to JSONString
							jsonString = mapper.writeValueAsString(beneficiaries);
							apiResponse.setStatus(false);
							apiResponse
									.setDescription("Unable to retrieve rejected list, maker checker list not found");
							apiResponse.setMessage("Unable to retrieve rejected list");
							apiResponse.setRecordId(null);
						}

					}

				} else {
					apiResponse.setStatus(false);
					apiResponse.setMessage(globalProperties.getInvalidEnterprise());
					apiResponse.setRecordId(null);
				}
			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(globalProperties.getUsernotfoundmsg());
				apiResponse.setMessage(globalProperties.getUsernotfoundmsg());
				apiResponse.setRecordId(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			apiResponse.setStatus(false);
			apiResponse.setMessage(globalProperties.getExceptionerrmsg());
			apiResponse.setDescription(e.getLocalizedMessage());
		}
		return apiResponse;
	}

	// rejected Beneficiary
	public APIResponse rejectedBeneficiary(AproveRejectAPIRequest reject) {
		APIResponse apiResponse = new APIResponse();
		try {
			Optional<Beneficiaries> beneficiary = beneficiariesRepository.findByRefNo(reject.getRef_no());

			if (beneficiary.isPresent()) {
				Optional<User> current_User = userRepository.findByAuthTokenAndMarkAsEnabled(reject.getAuth_token(),
						true);
				if (current_User.isPresent()) {
					String reject_status = makerservice.reject(current_User.get(), beneficiary.get());
//					RecordLog.writeLogFile(">>>>>>>>>...." + reject_status);
					if (reject_status.equals(globalProperties.getSuccessmsg())) {
						Beneficiaries beneficiaries = beneficiariesRepository.getOne(beneficiary.get().getId());
						beneficiaries.setStatus(globalProperties.getReject());
						beneficiaries.setUpdatedAt(new Timestamp(new Date().getTime()));
						beneficiariesRepository.save(beneficiaries);
						apiResponse.setStatus(true);
						apiResponse.setDescription(globalProperties.getSuccesscode());
						apiResponse.setMessage(globalProperties.getSuccessmsg());
						apiResponse.setRecordId(null);
					} else if (reject_status.equals(globalProperties.getAlreadyApproved())) {
						apiResponse.setStatus(true);
						apiResponse.setDescription(globalProperties.getBeneRejectedstatus());
						apiResponse.setMessage(globalProperties.getBeneRejectedstatus());
						apiResponse.setRecordId(null);
					} else {
						apiResponse.setStatus(false);
						apiResponse.setDescription("Beneficiary Rejection Failed");
						apiResponse.setMessage("Beneficiary Rejection Failed");
						apiResponse.setRecordId(null);
					}
				} else {
					apiResponse.setStatus(false);
					apiResponse.setDescription(globalProperties.getUsernotfoundmsg());
					apiResponse.setMessage(globalProperties.getUsernotfoundmsg());
					apiResponse.setRecordId(null);
				}

			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription(globalProperties.getInvalidRefNo());
				apiResponse.setMessage(globalProperties.getInvalidRefNo());
				apiResponse.setRecordId(null);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(true);
			apiResponse.setDescription(globalProperties.getSuccesscode());
			apiResponse.setMessage(globalProperties.getSuccessmsg());
			apiResponse.setRecordId(null);
		}

		return apiResponse;
	}

	// Delete PendingBemificiary
	public APIResponse deletePendingBeneficiary(DeleteBeneficiaryRequest deleteBeneficiaryRequest) {
		APIResponse apiResponse = new APIResponse();
		try {
		Optional<Beneficiaries> beneficiaries = beneficiariesRepository
				.findByRefNo(deleteBeneficiaryRequest.getRef_no());
		
//			RecordLog.writeLogFile("delete beneficiary  --->>" + beneficiaries.get().getId());
//			RecordLog.writeLogFile("delete 2 --->>" + beneficiaries.get().getLockVersion());
			if (beneficiaries.isPresent()) {
				beneficiariesRepository.deleteByIdAndLockVersion(beneficiaries.get().getId(),
						beneficiaries.get().getLockVersion());
				apiResponse.setStatus(true);
				apiResponse.setMessage("Beneficiary Deleted Succesfully");
				apiResponse.setDescription(globalProperties.getSuccessmsg());
			} else {
				apiResponse.setStatus(false);
				apiResponse.setDescription("Cannot delete beneficiary, beneficiary not found");
				apiResponse.setMessage("Cannot delete beneficiary, beneficiary not found");
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setMessage(globalProperties.getExceptionerrmsg());
			apiResponse.setDescription(null);
			apiResponse.setRecordId(null);
		}
		return apiResponse;

	}

}
