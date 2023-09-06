package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.Checker;
import com.federal.fedmobilesmecore.dto.EnterpriseUser;
import com.federal.fedmobilesmecore.dto.Maker;
import com.federal.fedmobilesmecore.dto.Operation;
import com.federal.fedmobilesmecore.dto.Signature;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.MakerCheckerListModel;
import com.federal.fedmobilesmecore.repository.ActionRepository;
import com.federal.fedmobilesmecore.repository.CheckerRepository;
import com.federal.fedmobilesmecore.repository.EnterpriseUserRepository;
import com.federal.fedmobilesmecore.repository.MakerRepository;
import com.federal.fedmobilesmecore.repository.OperationRepository;
import com.federal.fedmobilesmecore.repository.SignatureRepository;
import com.federal.fedmobilesmecore.repository.SignatureRepository_V1;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class MakerCheckerService {
	@Autowired
	OperationRepository operationRepository;
	@Autowired
	SignatureRepository signatureRepository;
	@Autowired
	CheckerRepository checkerRepository;
	@Autowired
	ActionRepository actionRepository;
	@Autowired
	MakerRepository makerRepository;
	@Autowired
	EnterpriseUserRepository enterpriseUserRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserRepository_V1 userRepository_v1;
	@Autowired
	CommonExternalService commonExternalService;
	@Autowired
	SignatureRepository_V1 signatureRepoV1;

	public String testMaker() {
		return maker(userRepository.findById(3L), "external user", enterpriseUserRepository.findById(1L).get(), 1);
	}

	public void testChecker() {
		checker(userRepository.findById(3L), enterpriseUserRepository.findById(1L).get());
	}

	// operation = 'external user'
	public String maker(Optional<User> getUserDetails, String operation, EnterpriseUser enterpriseUser,
			int requiredCount) {
		int final_required_count = 0;
		Operation operationSaveResp = null;
		Action actionSaveResp = null;
		EnterpriseUser enterpriseUser2 = null;
		String statusResp = "failed";
		try {
			if (getUserDetails.get().getRole() != null && getUserDetails.get().getRole().equals("external")) {
				final_required_count = requiredCount + 1;
				Operation operation_ = new Operation();
				operation_.setUserId(String.valueOf(getUserDetails.get().getId()));
				operation_.setOperationName(operation);
				operation_.setRequiredSignatureCount(String.valueOf(final_required_count));
				operation_.setCurrentSignatureCount("0");
				operation_.setCreatedAt(new Timestamp(new Date().getTime()));
				operation_.setUpdatedAt(new Timestamp(new Date().getTime()));
				operationSaveResp = operationRepository.save(operation_);
//				RecordLog.writeLogFile("operationSaveResp " + operationSaveResp);
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
			action.setStatus("initiated");
			action.setOperationId(String.valueOf(operationSaveResp.getId()));
			action.setUserName(getUserDetails.get().getUserName());
			action.setPrefNo(getUserDetails.get().getPrefNo());
			action.setCreatedAt(new Timestamp(new Date().getTime()));
			action.setUpdatedAt(new Timestamp(new Date().getTime()));
			actionSaveResp = actionRepository.save(action);
//			RecordLog.writeLogFile("actionSaveResp " + actionSaveResp);

			Maker maker = new Maker();
			maker.setUserId(String.valueOf(getUserDetails.get().getId()));
			maker.setActionId(String.valueOf(actionSaveResp.getId()));
			maker.setCreatedAt(new Timestamp(new Date().getTime()));
			maker.setUpdatedAt(new Timestamp(new Date().getTime()));
			makerRepository.save(maker);

//			RecordLog.writeLogFile("maker " + maker);

			enterpriseUser.setOperationId(String.valueOf(operationSaveResp.getId()));
			RecordLog.writeLogFile("operation Id " + operationSaveResp.getId());
			enterpriseUser2 = enterpriseUserRepository.save(enterpriseUser);
			if (enterpriseUser2 != null) {
				statusResp = "success";
			} else {
				statusResp = "failed";
			}
//			RecordLog.writeLogFile("statusResp " + statusResp);
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
		}
		return statusResp;
	}

	public synchronized String checker(Optional<User> getUserDetails, EnterpriseUser enterpriseUser) {
		Operation operation = null;
		Signature signature = null;
		Signature signatureResp = null;
		Checker checker = null;
		Checker checkerResp = null;
		Operation operationResp = null;
		int count;
		int actualCount;
		int requiredCount;
		String resp = "failed";
		if (enterpriseUser == null) {
//			RecordLog.writeLogFile("enterpriseUser is null");
			return resp;
		} else {
			if (enterpriseUser.getStatus().equals("approved") || enterpriseUser.getStatus().equals("rejected")
					|| enterpriseUser.getStatus().equals("deleted")) {
//				RecordLog.writeLogFile("enterpriseUser getStatus() is approved & rejected");
				resp = "failed";
			} else {
				RecordLog.writeLogFile("enterpriseUser status: " + enterpriseUser.getStatus());
//				RecordLog.writeLogFile("enterpriseUser else ");
				Optional<Operation> Optionaloperation = operationRepository
						.findById(Long.valueOf(enterpriseUser.getOperationId()));
				operation = Optionaloperation.get();
//				RecordLog.writeLogFile("operation " + operation);
				List<Signature> signs = signatureRepository.findByOperationIdAndStatus("" + operation.getId(),
						"approved");
				if (signs.size() > 0) {
					signature = signs.get(0);
				}
//				RecordLog.writeLogFile("signature " + signature);
				if (signs.size() > Long.valueOf(operation.getRequiredSignatureCount())) {
					resp = "failed";
				} else {
					signature = new Signature();
					signature.setUserId(String.valueOf(getUserDetails.get().getId()));
					signature.setOperationId(enterpriseUser.getOperationId());
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

//					RecordLog.writeLogFile("checkerResp " + checkerResp);

					count = Integer.valueOf(operation.getCurrentSignatureCount());
					actualCount = Integer.valueOf(operation.getCurrentSignatureCount());
					requiredCount = Integer.valueOf(operation.getRequiredSignatureCount());

//					RecordLog.writeLogFile("count " + count);
//					RecordLog.writeLogFile("actualCount " + actualCount);
//					RecordLog.writeLogFile("requiredCount " + requiredCount);

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
						resp = "success";
					} else {
						resp = "signed";
					}
				}
			}
		}
//		RecordLog.writeLogFile("resp " + resp);

		return resp;
	}

	// TODO MODIFICATION DONE :- else in if condition
	public synchronized String reject(User user, EnterpriseUser enterpriseUser) {
		String rejectResp = "failed";
		Signature signatureResp = null;
		if (enterpriseUser.getStatus().equals("pending") || enterpriseUser.getStatus().equals("new")
				|| enterpriseUser.getStatus().equals("pending_approval")) {
			Signature signature = new Signature();
			signature.setUserId(String.valueOf(user.getId()));
			signature.setOperationId(enterpriseUser.getOperationId());
			signature.setStatus("rejected");
			signature.setUserName(user.getUserName());
			signature.setPrefNo(user.getPrefNo());
			signature.setCreatedAt(new Timestamp(new Date().getTime()));
			signature.setUpdatedAt(new Timestamp(new Date().getTime()));
			signatureResp = signatureRepository.save(signature);

			if (signatureResp == null) {
				rejectResp = "failed";
			} else {
//				RecordLog.writeLogFile("signatureResp " + signatureResp.toString());
				rejectResp = "success";
			}
		} else {
			rejectResp = "failed";
		}
		return rejectResp;
	}

	public MakerCheckerListModel makerCheckerList(User user, EnterpriseUser enterpriseUser) {
		MakerCheckerListModel makerCheckerListModel = null;
		Operation operation = null;
		Action action = null;
		Signature signature = null;
		List<Signature> signatureList=null;
		String operationId = null;
		// TODO
		// FIX FOR PRIMARY USER
		user.setRole(user.getRole() != null ? user.getRole() : "");
		if (!user.getRole().equals("external")) {
			operationId = enterpriseUser.getOperationId();
//			RecordLog.writeLogFile("operationId inside" + operationId);
			if (operationId != null) {
				operation = operationRepository.findById(Long.valueOf(operationId)).get();
				action = actionRepository.findByOperationId(String.valueOf(operation.getId()));
				signatureList = signatureRepoV1.findByOperationId(String.valueOf(operation.getId()));
				if(signatureList.size()>0) {
				signature=signatureList.get(0);
				}
				enterpriseUser.setPassword("*******");
				enterpriseUser.setViewPwd("*******");
				makerCheckerListModel = new MakerCheckerListModel();
				makerCheckerListModel.setFunction(enterpriseUser);
				makerCheckerListModel.setMaker(action);
				makerCheckerListModel.setChecker(signature);
				makerCheckerListModel.setApprovalPermission(is_approver(user, operation));
				makerCheckerListModel.setUserNull(false);
				makerCheckerListModel.setOperationNull(false);
				boolean unblock=enterpriseUser.getWebBlockStatus()!=null && enterpriseUser.getWebBlockStatus().equals("blocked")?true:false;
				makerCheckerListModel.setUnblockExtUser(unblock);
				
				boolean unblockExtPass=userRepository_v1.existsByPrefNoAndMobileAndWebCheckStatusAndMarkAsEnabled(enterpriseUser.getPrefNo(), enterpriseUser.getMobile(), "blocked",true);
				makerCheckerListModel.setUnblockExternalPassword(unblockExtPass);
			} else {
				makerCheckerListModel = new MakerCheckerListModel();
				makerCheckerListModel.setUserNull(false);
				makerCheckerListModel.setOperationNull(true);
			}
		} else {
			// condition need be changed !!! important
			if (enterpriseUser.getEnterpriseId() != null) {
				if (!enterpriseUser.getStatus().equals("imported")) {
					enterpriseUser.setPassword("*******");
					enterpriseUser.setViewPwd("*******");
					operation = null;
					action = null;
					signature = null;
					makerCheckerListModel = new MakerCheckerListModel();
					makerCheckerListModel.setFunction(enterpriseUser);
					makerCheckerListModel.setMaker(action);
					makerCheckerListModel.setChecker(signature);
					makerCheckerListModel.setApprovalPermission(false);
					makerCheckerListModel.setUnblockExtUser(false);
					makerCheckerListModel.setUnblockExternalPassword(false);
				} else {
					operation = operationRepository.findById(Long.valueOf(enterpriseUser.getOperationId())).get();
					action = actionRepository.findByOperationId(String.valueOf(operation.getId()));
					signatureList = signatureRepoV1.findByOperationId(String.valueOf(operation.getId()));
					signature=signatureList.get(0);
					enterpriseUser.setPassword("*******");
					enterpriseUser.setViewPwd("*******");
					makerCheckerListModel = new MakerCheckerListModel();
					makerCheckerListModel.setFunction(enterpriseUser);
					makerCheckerListModel.setMaker(action);
					makerCheckerListModel.setChecker(signature);
					makerCheckerListModel.setApprovalPermission(is_approver(user, operation));
					//boolean unblock=enterpriseUser.getWebBlockStatus()!=null && enterpriseUser.getWebBlockStatus().equals("blocked")?true:false;
					makerCheckerListModel.setUnblockExtUser(false);
					makerCheckerListModel.setUnblockExternalPassword(false);
				}
			} else {
				operation = operationRepository.findById(Long.valueOf(enterpriseUser.getOperationId())).get();
				action = actionRepository.findByOperationId(String.valueOf(operation.getId()));
				signatureList = signatureRepoV1.findByOperationId(String.valueOf(operation.getId()));
				signature=signatureList.get(0);				
				makerCheckerListModel = new MakerCheckerListModel();
				makerCheckerListModel.setFunction(enterpriseUser);
				makerCheckerListModel.setMaker(action);
				makerCheckerListModel.setChecker(signature);
				makerCheckerListModel.setApprovalPermission(is_approver(user, operation));
				//boolean unblock=enterpriseUser.getWebBlockStatus()!=null && enterpriseUser.getWebBlockStatus().equals("blocked")?true:false;
				makerCheckerListModel.setUnblockExtUser(false);
				makerCheckerListModel.setUnblockExternalPassword(false);
			}
		}

		return makerCheckerListModel;
	}

	public MakerCheckerListModel makerCheckerList_v1(Optional<User> user, EnterpriseUser enterpriseUser) {
		MakerCheckerListModel makerCheckerListModel = null;
		Optional<Operation> operation = null;
		Action action = null;
		Signature signature = null;
		String operationId = null;
		String user_role = user.get().getRole() == null ? "" : user.get().getRole();
		operationId = enterpriseUser.getOperationId();
//		RecordLog.writeLogFile("operationId inside" + operationId);
		if (!user_role.equals("external")) {
			RecordLog.writeLogFile("User Role: " + user.get().getRole());
			operation = operationRepository.findById(Long.valueOf(operationId));
			action = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
//			RecordLog.writeLogFile(">>>>>>>>>>>>>>" + operation.get().getId());
			List<Signature> signList=signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
			if(signList.size()>0) {
			signature = signList.get(0);
			}
			makerCheckerListModel = new MakerCheckerListModel();
			makerCheckerListModel.setFunction(enterpriseUser);
			makerCheckerListModel.setMaker(action);
			makerCheckerListModel.setChecker(signature);
			makerCheckerListModel.setApprovalPermission(is_approver(user.get(), operation.get()));
			makerCheckerListModel.setUserNull(false);
			makerCheckerListModel.setOperationNull(false);
		} else {
//			RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>" + user.get().getRole());
			// These are external users.
			operation = operationRepository.findById(Long.valueOf(operationId));

//							signature = signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
			if (operation.isPresent()) {
				action = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
				Long signature_cont = signatureRepository
						.countByOperationIdAndStatus(String.valueOf(operation.get().getId()), "approved");
				if (signature_cont > 0) {
					signature = signatureRepository
							.findByOperationIdAndStatus(String.valueOf(operation.get().getId()), "approved").get(0);
				}
			}
			makerCheckerListModel = new MakerCheckerListModel();
			makerCheckerListModel.setFunction(enterpriseUser);
			makerCheckerListModel.setMaker(action);
			makerCheckerListModel.setChecker(signature);
			makerCheckerListModel.setApprovalPermission(is_approver(user.get(), operation.get()));
			makerCheckerListModel.setUserNull(false);
			makerCheckerListModel.setOperationNull(false);
		}
		return makerCheckerListModel;
	}

	boolean is_approver(User user, Operation operation) {
		Boolean isApprover = false;
		List<Action> action = null;
		Signature signature;
		String role = user.getRole() != null ? user.getRole() : "";
		if (StringUtils.isNotBlank(role)) {
			if (role.equals("external")) {
				return false;
			}
		}

		action = actionRepository.findByOperationIdOrderByIdAsc(String.valueOf(operation.getId()));
		if (action == null) {
			isApprover = false;
		} else if (operation.getUserId().equals("" + user.getId())) {
			// Logged user is maker
			isApprover = false;
         } else {
			if (action.get(0).getUserId().equals("" + user.getId())) {
				// This is a case where for a primary user who created the request the approve
				// button is not visible to him.
				return isApprover;
			} else {
				isApprover = true;
			}
			List<Signature> signs = signatureRepository.findByOperationIdAndStatus("" + operation.getId(), "approved");
			// The below case won't come in the way currently way it is programmed. Just for
			// handling future cases.
			if (!CollectionUtils.isEmpty(signs)) {
				for (Signature sign : signs) {
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

	public MakerCheckerListModel makeAndRejectedCheckerList(User user, EnterpriseUser enterpriseUser, int entCount) {
		MakerCheckerListModel makerCheckerListModel = null;
		Optional<Operation> operation = null;
		Action action = null;
		Signature signature = null;
		String operationId = null;
		String count = null;

		operationId = enterpriseUser.getOperationId();
//		RecordLog.writeLogFile("operationId inside" + operationId);
		if (operationId != null) {
			enterpriseUser.setViewPwd("*********");
			count = String.valueOf(entCount);
//			RecordLog.writeLogFile("count " + count);
			if (count.equals("1")) {
				makerCheckerListModel = new MakerCheckerListModel();
				makerCheckerListModel.setFunction(enterpriseUser);
				makerCheckerListModel.setMaker(action);
				makerCheckerListModel.setChecker(signature);
				makerCheckerListModel.setApprovalPermission(false);
				makerCheckerListModel.setUserNull(false);
				makerCheckerListModel.setOperationNull(false);

				return makerCheckerListModel;
			} else {
				operation = operationRepository.findById(Long.valueOf(enterpriseUser.getOperationId()));
				if (operation.isPresent()) {
					action = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
					List<Signature> signList=signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
					if(signList.size()>0) {
					signature = signList.get(0);
					}
					enterpriseUser.setPassword("*******");
					makerCheckerListModel = new MakerCheckerListModel();
					makerCheckerListModel.setFunction(enterpriseUser);
					makerCheckerListModel.setMaker(action);
					makerCheckerListModel.setChecker(signature);
					makerCheckerListModel.setApprovalPermission(is_approver(user, operation.get()));
					makerCheckerListModel.setUserNull(false);
					makerCheckerListModel.setOperationNull(false);
				} else {
					makerCheckerListModel = new MakerCheckerListModel();
					makerCheckerListModel.setUserNull(false);
					makerCheckerListModel.setOperationNull(true);
				}
			}
		} else {
			makerCheckerListModel = new MakerCheckerListModel();
			makerCheckerListModel.setUserNull(false);
			makerCheckerListModel.setOperationNull(true);
		}

		return makerCheckerListModel;
	}

	public MakerCheckerListModel makeAndRejectedCheckerList(User user, EnterpriseUser enterpriseUser) {
		MakerCheckerListModel makerCheckerListModel = null;
		Optional<Operation> operation = null;
		Action action = null;
		Signature signature = null;
		String enterpriseId = null;
		int count = 0;

		enterpriseUser.setViewPwd("*********");
		enterpriseId = user.getEnterpriseId();
		RecordLog.writeLogFile("enterpriseId " + enterpriseId);
		count = userRepository_v1.countByEnterpriseId(enterpriseId);
//		RecordLog.writeLogFile("count " + count);
		if (count == 1) {
			makerCheckerListModel = new MakerCheckerListModel();
			makerCheckerListModel.setFunction(enterpriseUser);
			makerCheckerListModel.setMaker(null);
			makerCheckerListModel.setChecker(null);
			makerCheckerListModel.setApprovalPermission(false);
			makerCheckerListModel.setUserNull(false);
			makerCheckerListModel.setOperationNull(false);
		} else {
			operation = operationRepository.findById(Long.valueOf(enterpriseUser.getOperationId()));
			if (operation.isPresent()) {
				action = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
				List<Signature> signList= signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
				if(signList.size()>0) {
				signature = signList.get(0);
				}
				makerCheckerListModel = new MakerCheckerListModel();
				makerCheckerListModel.setFunction(enterpriseUser);
				makerCheckerListModel.setMaker(action);
				makerCheckerListModel.setChecker(signature);
				makerCheckerListModel.setApprovalPermission(is_approver(user, operation.get()));
				makerCheckerListModel.setUserNull(false);
				makerCheckerListModel.setOperationNull(false);
			} else {
				makerCheckerListModel = new MakerCheckerListModel();
				makerCheckerListModel.setUserNull(false);
				makerCheckerListModel.setOperationNull(true);
			}
		}
		return makerCheckerListModel;
	}
}