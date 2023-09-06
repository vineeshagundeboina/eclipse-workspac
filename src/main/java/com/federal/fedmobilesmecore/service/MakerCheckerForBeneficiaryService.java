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

import com.federal.fedmobilesmecore.config.GlobalProperties;
import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Checker;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.Maker;
import com.federal.fedmobilesmecore.dto.Operation;
import com.federal.fedmobilesmecore.dto.Signature;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.MakerCheckerBeneficiaryModel;
import com.federal.fedmobilesmecore.model.MakerCheckerRejecedList;
import com.federal.fedmobilesmecore.repository.ActionRepository;
import com.federal.fedmobilesmecore.repository.BeneficiariesRepository;
import com.federal.fedmobilesmecore.repository.CheckerRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository_V1;
import com.federal.fedmobilesmecore.repository.MakerRepository;
import com.federal.fedmobilesmecore.repository.OperationRepository;
import com.federal.fedmobilesmecore.repository.SignatureRepository_V1;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class MakerCheckerForBeneficiaryService {

	@Autowired
	OperationRepository operationRepository;
	@Autowired
	SignatureRepository_V1 signatureRepository;
	@Autowired
	CheckerRepository checkerRepository;
	@Autowired
	ActionRepository actionRepository;
	@Autowired
	MakerRepository makerRepository;
	@Autowired
	BeneficiariesRepository beneficiariesRepository;
	@Autowired
	UserRepository_V1 userRepository;

	@Autowired
	GlobalProperties globalproperties;

	@Autowired
	EnterprisesRepository_V1 enterprisesRepository;

	public String maker(User user, String operationuser, Beneficiaries beneficiaries, String benCount) {
		int final_required_count;
		Operation operationSaveResp;
		Action actionSaveResp;
		String statusResp = "failed";
		Operation operation = new Operation();
		int ben_count;

		try {
			if (benCount == null) {
				ben_count = 0;
			} else {
				ben_count = Integer.valueOf(benCount);
			}
//			RecordLog.writeLogFile("operationuser" + operationuser);
			String user_role = user.getRole() == null ? "" : user.getRole();

			if (StringUtils.isNotBlank(user_role) && user_role.equals(operationuser)) {
//				RecordLog.writeLogFile(">>>>>>>>>>>>>>" + benCount);
				final_required_count = ben_count + 1;
				operation.setUserId(String.valueOf(user.getId()));
				operation.setOperationName(operationuser);
				operation.setCurrentSignatureCount("0");
				operation.setRequiredSignatureCount(String.valueOf(final_required_count));
				operation.setCreatedAt(new Timestamp(new Date().getTime()));
				operation.setUpdatedAt(new Timestamp(new Date().getTime()));
				operationSaveResp = operationRepository.save(operation);
//				RecordLog.writeLogFile("operationSaveResp " + operationSaveResp);
			} else {
//				RecordLog.writeLogFile(">>>>>>>>>>>>>>" + ben_count);
				operation.setUserId(String.valueOf(user.getId()));
				operation.setOperationName(operationuser);
				operation.setCurrentSignatureCount("0");
				operation.setRequiredSignatureCount(String.valueOf(ben_count));
				operation.setCreatedAt(new Timestamp(new Date().getTime()));
				operation.setUpdatedAt(new Timestamp(new Date().getTime()));
				operationSaveResp = operationRepository.save(operation);
//				RecordLog.writeLogFile("operationSaveResp " + operationSaveResp);
			}
			Action action = new Action();
			action.setUserId(String.valueOf(user.getId()));
			action.setOperationId(String.valueOf(operationSaveResp.getId()));
			action.setUserName(user.getUserName());
			action.setPrefNo(user.getPrefNo());
			action.setStatus("initiated");
			action.setCreatedAt(new Timestamp(new Date().getTime()));
			action.setUpdatedAt(new Timestamp(new Date().getTime()));
			actionSaveResp = actionRepository.save(action);
//			RecordLog.writeLogFile("actionSaveResp " + actionSaveResp);

			Maker maker = new Maker();
			maker.setUserId(String.valueOf(user.getId()));
			maker.setActionId(String.valueOf(actionSaveResp.getId()));
			maker.setCreatedAt(new Timestamp(new Date().getTime()));
			maker.setUpdatedAt(new Timestamp(new Date().getTime()));
			makerRepository.save(maker);

//			RecordLog.writeLogFile("maker " + maker);

			beneficiaries.setOperationId(String.valueOf(operationSaveResp.getId()));
			beneficiaries.setApprovedAt(new Timestamp(new Date().getTime()));
			beneficiaries.setLockVersion(0);
//			RecordLog.writeLogFile("operationSaveResp.getId() " + operationSaveResp.getId());
			if (beneficiariesRepository.save(beneficiaries) != null) {
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

	public String checker(User user, Beneficiaries beneficiaries) {
		Operation operation;
		Optional<Signature> signature;
		Signature signatureResp;
		Checker checker;
		Checker checkerResp;
		Operation operationResp = null;
		int count;
		int actualCount;
		int requiredCount;

		String approval_status = "failed";

		if (beneficiaries == null) {
			return approval_status;
		} else {
			String benificiary_status = beneficiaries.getStatus() == null ? "new" : beneficiaries.getStatus();
			if (beneficiaries.getStatus().equals("new")) {
				Optional<Operation> Optionaloperation = operationRepository
						.findById(Long.valueOf(beneficiaries.getOperationId()));
				operation = Optionaloperation.get();
//				RecordLog.writeLogFile("operation " + operation.getId());
				signature = signatureRepository.findByIdAndStatus(operation.getId(), "approved");
//				RecordLog.writeLogFile("signature " + signature);
				if (signature.isPresent()) {
					if (signature.get().getStatus().equals("approved")) {
						approval_status = globalproperties.getAlreadyApproved();
					}
				} else {
					signatureResp = new Signature();
					// signatureResp.setUserId(String.valueOf(beneficiaries.getId()));
					signatureResp.setUserId(String.valueOf(user.getId()));
					signatureResp.setOperationId(beneficiaries.getOperationId());
					signatureResp.setStatus("approved");
					signatureResp.setUserName(user.getUserName());
					signatureResp.setPrefNo(user.getPrefNo());
					signatureResp.setCreatedAt(new Timestamp(new Date().getTime()));
					signatureResp.setUpdatedAt(new Timestamp(new Date().getTime()));
					signatureResp = signatureRepository.save(signatureResp);

//					RecordLog.writeLogFile("signatureResp " + signatureResp);

					checker = new Checker();
					checker.setUserId(String.valueOf(user.getId()));
					checker.setStatus("approved");
					checker.setSignatureId(String.valueOf(signatureResp.getId()));
					checker.setCreatedAt(new Timestamp(new Date().getTime()));
					checker.setUpdatedAt(new Timestamp(new Date().getTime()));
					checkerResp = checkerRepository.save(checker);

					/*
					 * if (operation.getCurrentSignatureCount()==null) { count = 0; } else { count =
					 * Integer.valueOf(operation.getCurrentSignatureCount()); } if
					 * (operation.getRequiredSignatureCount()==null) { actualCount = 0; }else {
					 * actualCount = Integer.valueOf(operation.getCurrentSignatureCount()); }
					 * if(operation.getRequiredSignatureCount().equals("null")) { requiredCount = 0;
					 * }else { requiredCount =
					 * Integer.valueOf(operation.getRequiredSignatureCount()); }
					 */

//					RecordLog.writeLogFile("checkerResp " + checkerResp);
					count = Integer.valueOf(operation.getCurrentSignatureCount());
					actualCount = Integer.valueOf(operation.getCurrentSignatureCount());
					requiredCount = Integer.valueOf(operation.getRequiredSignatureCount());
//					RecordLog.writeLogFile("count " + count);
					RecordLog.writeLogFile("actual signature count " + actualCount);
					RecordLog.writeLogFile("required signature count " + requiredCount);

					if (actualCount != requiredCount) {
						count += 1;
//						RecordLog.writeLogFile("count count" + count);

					}
					operation.setUpdatedAt(new Timestamp(new Date().getTime()));
					operation.setCurrentSignatureCount(String.valueOf(count));
					operationResp = operationRepository.save(operation);
//					RecordLog.writeLogFile("operationResp " + operationResp);

					if (operationResp.getRequiredSignatureCount().equals((operationResp.getCurrentSignatureCount()))) {
						approval_status = "success";
					} else {
						approval_status = "signed";
					}
				}

			} else if (benificiary_status.equals("approved") || benificiary_status.equals("rejected")
					|| benificiary_status.equals("deleted")) {
				approval_status = "failed";
			}
		}
		return approval_status;
	}

	public String reject(User user, Beneficiaries beneficiaries) {
		String rejectResp = "failed";
		Signature signatureResp;
		RecordLog.writeLogFile("beneficiaries status: " + beneficiaries.getStatus());
		if (beneficiaries.getStatus() == "pending" || beneficiaries.getStatus() == "new"
				|| beneficiaries.getStatus() == "pending_approval") {
//			rejectResp = globalproperties.getFailedMsg();
			rejectResp = "Cannot reject an unapproved Beneficiary";
		} else if (beneficiaries.getStatus().equals("approved")) {
//			RecordLog.writeLogFile("globalproperties.getApproved()" + globalproperties.getAlreadyApproved());
			rejectResp = globalproperties.getAlreadyApproved();
		} else {
			Signature signature = new Signature();
			signature.setUserId(String.valueOf(user.getId()));
			signature.setOperationId(beneficiaries.getOperationId());
			signature.setStatus("rejected");
			signature.setUserName(user.getUserName());
			signature.setPrefNo(user.getPrefNo());
			signature.setCreatedAt(new Timestamp(new Date().getTime()));
			signature.setUpdatedAt(new Timestamp(new Date().getTime()));
			signatureResp = signatureRepository.save(signature);

			Checker checker = new Checker();
			checker.setUserId(String.valueOf(user.getId()));
			checker.setStatus("rejected");
			checker.setSignatureId(String.valueOf(signatureResp.getId()));
			checker.setCreatedAt(new Timestamp(new Date().getTime()));
			checker.setUpdatedAt(new Timestamp(new Date().getTime()));
			checkerRepository.save(checker);
			rejectResp = globalproperties.getSuccessmsg();

		}
		return rejectResp;
	}

	public MakerCheckerBeneficiaryModel makerCheckerList(User user, Beneficiaries beneficiaries) {
		MakerCheckerBeneficiaryModel beneficiaryModel = new MakerCheckerBeneficiaryModel();
		Optional<Operation> operation;
		Action action = null;
		List<Signature> signature = null;
		String user_role = user.getRole() == null ? "" : user.getRole();
		if (!user_role.equals("external")) {
//			RecordLog.writeLogFile(">>>>>>>>>>>>>>>>>>" + user.getRole());
			operation = operationRepository.findById(Long.valueOf(beneficiaries.getOperationId()));
			action = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
//			RecordLog.writeLogFile(">>>>>>>>>>>>>>" + operation.get().getId());
			signature = signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
			beneficiaryModel.setBeneficiaries(beneficiaries);
			beneficiaryModel.setMaker(action);
			beneficiaryModel.setChecker(signature);
			// Approval Logic changed by vikas.
			boolean approval_permission = false;
			if (operation.isPresent() && user != null) {
				approval_permission = is_approver(user, operation.get());
			}
			beneficiaryModel.setApprovalPermission(approval_permission);
		} else {
			// These are external users.
			operation = operationRepository.findById(Long.valueOf(beneficiaries.getOperationId()));

//				signature = signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
			if (operation.isPresent()) {
				action = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
				Long signature_cont = signatureRepository
						.countByOperationIdAndStatus(String.valueOf(operation.get().getId()), "approved");
				if (signature_cont > 0) {
					signature = signatureRepository.findByOperationIdAndStatus(String.valueOf(operation.get().getId()),
							"approved");
				}
			}
			beneficiaryModel.setBeneficiaries(beneficiaries);
			beneficiaryModel.setMaker(action);
			beneficiaryModel.setChecker(signature);
			beneficiaryModel.setApprovalPermission(false);

		}
		return beneficiaryModel;
	}

	@SuppressWarnings("null")
	public MakerCheckerRejecedList makerCheckerRejecedList(User current_user, Beneficiaries beneficiaries) {
		MakerCheckerRejecedList checkerRejecedList = null;
		Optional<Operation> operation = null;
		Action action = null;
		Signature signature = null;
		Optional<User> user = null;
		try {
			user = userRepository.findById(current_user.getId());
			Enterprises enterprises = enterprisesRepository.findByActiveAndId(true,
					Long.valueOf(user.get().getEnterpriseId()));
			// RecordLog.writeLogFile(">>>>>>>>>>>..count>"+enterprises.getId());
			int count = userRepository.countByEnterpriseId(String.valueOf(enterprises.getId()));
//			RecordLog.writeLogFile(">>>>>>>>>>>..count1>" + count);
			if (count == 1) {
				checkerRejecedList = new MakerCheckerRejecedList();
				checkerRejecedList.setApprovalPermission(false);
				checkerRejecedList.setBeneficiaries(null);
				checkerRejecedList.setChecker(null);
				checkerRejecedList.setMaker(null);

			} else {
				operation = operationRepository.findById(Long.valueOf(beneficiaries.getOperationId()));
				action = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
				Long signature_cont = signatureRepository
						.countByOperationIdAndStatus(String.valueOf(operation.get().getId()), "rejected");
				if (signature_cont > 0) {
					signature = signatureRepository
							.findByOperationIdAndStatus(String.valueOf(operation.get().getId()), "rejected").get(0);
				}
				boolean approval_permission = isApproved(user, operation);
				checkerRejecedList = new MakerCheckerRejecedList();
				checkerRejecedList.setBeneficiaries(beneficiaries);
				checkerRejecedList.setChecker(signature);
				checkerRejecedList.setMaker(action);
				checkerRejecedList.setApprovalPermission(approval_permission);
			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			e.printStackTrace();
			checkerRejecedList = new MakerCheckerRejecedList();
			checkerRejecedList.setApprovalPermission(false);
			checkerRejecedList.setBeneficiaries(null);
			checkerRejecedList.setChecker(null);
			checkerRejecedList.setMaker(null);
		}

		return checkerRejecedList;

	}

	@SuppressWarnings("null")
	private boolean isApproved(Optional<User> user, Optional<Operation> operation) {
		List<Long> user_list = null;
		boolean resp = false;
		try {
			String role=user.get().getRole()==null?"":user.get().getRole();
			if (role.equals("external")) {
				return false;
			} else {
				Action actionIds = actionRepository.findByOperationId(String.valueOf(operation.get().getId()));
				List<Signature> ids = signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
				user_list.add(actionIds.getId());
				for (Signature s : ids) {
					user_list.add(s.getId());
				}
				for (Long userId : user_list) {
					if (userId.equals(user.get().getId())) {
						resp = true;
					} else {
						resp = false;
					}
				}

			}
		} catch (Exception e) {
			RecordLog.writeLogFile("Exception occured at: "+Arrays.toString(e.getStackTrace())+" Exception name: "+e.getMessage());
			resp = false;
		}

		return resp;
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

}
