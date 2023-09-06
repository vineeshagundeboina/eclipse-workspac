package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.Checker;
import com.federal.fedmobilesmecore.dto.EnterpriseUser;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.Maker;
import com.federal.fedmobilesmecore.dto.Operation;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;
import com.federal.fedmobilesmecore.dto.Signature;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.MakerCheckerListModel;
import com.federal.fedmobilesmecore.repository.ActionRepository;
import com.federal.fedmobilesmecore.repository.CheckerRepository;
import com.federal.fedmobilesmecore.repository.FundTransferRepository;
import com.federal.fedmobilesmecore.repository.MakerRepository;
import com.federal.fedmobilesmecore.repository.OperationRepository;
import com.federal.fedmobilesmecore.repository.SignatureRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.repository.UserRepository_V1;

@Service
public class MakerCheckerServiceFundTransfer {
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
	FundTransferRepository fundTransferRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserRepository_V1 userRepository_v1;

	// operation = 'FundTransfer'
	public String maker(Optional<User> getUserDetails, String operation, FundTransfer fundTransfer, int requiredCount) {
		int final_required_count = 0;
		Operation operationSaveResp = null;
		Action actionSaveResp = null;
		FundTransfer fundTransferResp = null;
		String statusResp = "failed";
		try {
			/**
			 * TODO : Need to ask about Role
			 */
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
			action.setOperationId(String.valueOf(operationSaveResp.getId()));
			action.setUserName(getUserDetails.get().getUserName());
			action.setPrefNo(getUserDetails.get().getPrefNo());
			action.setCreatedAt(new Timestamp(new Date().getTime()));
			action.setUpdatedAt(new Timestamp(new Date().getTime()));
			action.setStatus("initiated");
			actionSaveResp = actionRepository.save(action);
//			RecordLog.writeLogFile("actionSaveResp " + actionSaveResp);

			Maker maker = new Maker();
			maker.setUserId(String.valueOf(getUserDetails.get().getId()));
			maker.setActionId(String.valueOf(actionSaveResp.getId()));
			maker.setCreatedAt(new Timestamp(new Date().getTime()));
			maker.setUpdatedAt(new Timestamp(new Date().getTime()));
			makerRepository.save(maker);

//			RecordLog.writeLogFile("maker " + maker);

			fundTransfer.setOperationId(String.valueOf(operationSaveResp.getId()));
			fundTransfer.setStatus("pending");
			fundTransfer.setProgress("not started");
//			RecordLog.writeLogFile("operationSaveResp.getId() " + operationSaveResp.getId());
			fundTransferResp = fundTransferRepository.save(fundTransfer);
			if (fundTransferResp != null) {
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

	public synchronized String checker(Optional<User> getUserDetails, FundTransfer fundTransfer) {
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
		if (fundTransfer == null) {
//			RecordLog.writeLogFile("enterpriseUser is null");
			return resp;
		} else {
			if (fundTransfer.getStatus().equals("approved") || fundTransfer.getStatus().equals("rejected")
					|| fundTransfer.getStatus().equals("deleted")) {
//				RecordLog.writeLogFile("enterpriseUser getStatus() is approved & rejected");
				resp = "already_approved";
			} else {
				// TODO check for null
				RecordLog.writeLogFile("enterpriseUser status: " + fundTransfer.getStatus());
//				RecordLog.writeLogFile("enterpriseUser else ");
				Optional<Operation> Optionaloperation = operationRepository
						.findById(Long.valueOf(fundTransfer.getOperationId()));
				operation = Optionaloperation.get();
//				RecordLog.writeLogFile("operation " + operation);
				List<Signature> signs = signatureRepository.findByOperationIdAndStatus("" + operation.getId(),
						"approved");
				if (signs.size() > 0) {
					signature = signs.get(0);
				}
//				RecordLog.writeLogFile("Signature Size : " + signs.size());
//				RecordLog.writeLogFile("signature " + signature);
				if (signs.size() > Long.valueOf(operation.getRequiredSignatureCount())) {
					resp = "failed";
				} else {
					signature = new Signature();
					//signature.setUserId(String.valueOf(fundTransfer.getId()));
					signature.setUserId(String.valueOf(getUserDetails.get().getId()));
					signature.setOperationId(fundTransfer.getOperationId());
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

	public synchronized String reject(User user, EnterpriseUser enterpriseUser) {
		String rejectResp = "failed";
		Signature signatureResp = null;
		RecordLog.writeLogFile("enterpriseUser status: " + enterpriseUser.getStatus());
		if (enterpriseUser.getStatus().equals("pending") || enterpriseUser.getStatus().equals("new")
				|| enterpriseUser.getStatus().equals("pending_approval")) {
			rejectResp = "failed";
		} else {
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
		}
		return rejectResp;
	}

	public MakerCheckerListModel makerCheckerList(User user, EnterpriseUser enterpriseUser) {
		MakerCheckerListModel makerCheckerListModel = null;
		Operation operation = null;
		Action action = null;
		Signature signature = null;
		String operationId = null;

		if (user.getRole() == null) {
			makerCheckerListModel = new MakerCheckerListModel();
			makerCheckerListModel.setUserNull(true);
			makerCheckerListModel.setOperationNull(true);
			return makerCheckerListModel;
		} else {
			if (!user.getRole().equals("external")) {
				operationId = enterpriseUser.getOperationId();
//				RecordLog.writeLogFile("operationId inside" + operationId);
				if (operationId != null) {
					operation = operationRepository.findById(Long.valueOf(operationId)).get();
					action = actionRepository.findByOperationId(String.valueOf(operation.getId()));
					List<Signature> signList=signatureRepository.findByOperationId(String.valueOf(operation.getId()));
					if(signList.size()>0) {
					signature = signList.get(0);
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
					} else {
						operation = operationRepository.findById(Long.valueOf(enterpriseUser.getOperationId())).get();
						action = actionRepository.findByOperationId(String.valueOf(operation.getId()));
						List<Signature> signList=signatureRepository.findByOperationId(String.valueOf(operation.getId()));
						if(signList.size()>0) {
						signature = signList.get(0);
						}
						enterpriseUser.setPassword("*******");
						enterpriseUser.setViewPwd("*******");
						makerCheckerListModel = new MakerCheckerListModel();
						makerCheckerListModel.setFunction(enterpriseUser);
						makerCheckerListModel.setMaker(action);
						makerCheckerListModel.setChecker(signature);
						makerCheckerListModel.setApprovalPermission(is_approver(user, operation));
					}
				} else {
					operation = operationRepository.findById(Long.valueOf(enterpriseUser.getOperationId())).get();
					action = actionRepository.findByOperationId(String.valueOf(operation.getId()));
					List<Signature> signList= signatureRepository.findByOperationId(String.valueOf(operation.getId()));
					if(signList.size()>0) {
					signature = signList.get(0);
					}
					makerCheckerListModel = new MakerCheckerListModel();
					makerCheckerListModel.setFunction(enterpriseUser);
					makerCheckerListModel.setMaker(action);
					makerCheckerListModel.setChecker(signature);
					makerCheckerListModel.setApprovalPermission(is_approver(user, operation));
				}
			}
		}
		return makerCheckerListModel;
	}

	boolean is_approver(User user, Operation operation) {
		Boolean isApprover = false;
		List<Action> action = null;
		Signature signature=null;

		if (user.getRole() == "external") {
			return false;
		}
		action = actionRepository.findByOperationIdOrderByIdAsc(String.valueOf(operation.getId()));
		if (action == null) {
			isApprover = false;
		} else {
			List<Signature> signList= signatureRepository.findByOperationId(String.valueOf(operation.getId()));
			if(signList.size()>0) {
			signature = signList.get(0);
			}
			if (signature == null) {
				isApprover = false;
			} else {
				if (action.get(0).getUserId().equals(signature.getUserId())) {
					isApprover = true;
				} else {
					isApprover = false;
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
//		RecordLog.writeLogFile("enterpriseId " + enterpriseId);
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
				List<Signature> signList=signatureRepository.findByOperationId(String.valueOf(operation.get().getId()));
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

	public MakerCheckerListModel rollback(Optional<User> getCurrentUserDetails, Optional<FundTransfer> fundTransfer) {
		MakerCheckerListModel makerCheckerListModel = null;
		return makerCheckerListModel;
	}

	public MakerCheckerListModel rollbackimps(Optional<User> getCurrentUserDetails,
			Optional<ImpsTransfer> impsTransfer) {
		MakerCheckerListModel makerCheckerListModel = null;
		return makerCheckerListModel;

	}
	
	public MakerCheckerListModel rollbackSchTransaction(Optional<User> getCurrentUserDetails,
			Optional<ScheduledTransaction> scheduledTransaction) {
		MakerCheckerListModel makerCheckerListModel = null;
		return makerCheckerListModel;

	}

}