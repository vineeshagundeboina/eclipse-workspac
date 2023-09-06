package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Action;
import com.federal.fedmobilesmecore.dto.Checker;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.Maker;
import com.federal.fedmobilesmecore.dto.Operation;
import com.federal.fedmobilesmecore.dto.ScheduledPayment;
import com.federal.fedmobilesmecore.dto.Signature;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.SMEMessage;
import com.federal.fedmobilesmecore.repository.ActionRepository;
import com.federal.fedmobilesmecore.repository.CheckerRepository;
import com.federal.fedmobilesmecore.repository.ImpsTransferRepository;
import com.federal.fedmobilesmecore.repository.MakerRepository;
import com.federal.fedmobilesmecore.repository.OperationRepository;
import com.federal.fedmobilesmecore.repository.SignatureRepository;

@Service
public class MakerCheckerServiceImpsTransfer {
	@Autowired
	OperationRepository operationRepository;
	@Autowired
	ActionRepository actionRepository;
	@Autowired
	SignatureRepository signatureRepository;
	@Autowired
	MakerRepository makerRepository;
	@Autowired
	CheckerRepository checkerRepository;

	@Autowired
	ImpsTransferRepository impsTransferRepository;

	public String maker(Optional<User> getUserDetails, String operation, ImpsTransfer impsTransfer, int requiredCount) {
		int final_required_count = 0;
		Operation operationSaveResp = null;
		Action actionSaveResp = null;
		ImpsTransfer impsTransferResp = null;
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

			impsTransfer.setOperationId(String.valueOf(operationSaveResp.getId()));
			impsTransfer.setStatus("pending");
			impsTransfer.setProgress("not started");
//			RecordLog.writeLogFile("operationSaveResp.getId() " + operationSaveResp.getId());
			impsTransferResp = impsTransferRepository.save(impsTransfer);
			if (impsTransferResp != null) {
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

	public synchronized String checker(Optional<User> getUserDetails, ImpsTransfer impsTransfer) {
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
		if (impsTransfer == null) {
//			RecordLog.writeLogFile("enterpriseUser is null");
			return resp;
		} else {
			if (impsTransfer.getStatus().equals("approved") || impsTransfer.getStatus().equals("rejected")
					|| impsTransfer.getStatus().equals("deleted")) {
//				RecordLog.writeLogFile("enterpriseUser getStatus() is approved & rejected");
				resp = "already_approved";
			} else {
				// TODO check for null
				RecordLog.writeLogFile("enterpriseUser status: " + impsTransfer.getStatus());
//				RecordLog.writeLogFile("enterpriseUser else ");
				Optional<Operation> Optionaloperation = operationRepository
						.findById(Long.valueOf(impsTransfer.getOperationId()));
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
					// signature.setUserId(String.valueOf(impsTransfer.getId()));
					signature.setUserId(String.valueOf(getUserDetails.get().getId()));
					signature.setOperationId(impsTransfer.getOperationId());
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

	public SMEMessage rollback(Optional<User> getCurrentUserDetails, Optional<ImpsTransfer> impsTransfer) {
		SMEMessage message = new SMEMessage();
		if (getCurrentUserDetails.isPresent() && impsTransfer.isPresent()) {
			List<Signature> signs = signatureRepository.findByOperationIdAndUserIdAndUserNameAndPrefNo(
					"" + impsTransfer.get().getId(), "" + getCurrentUserDetails.get().getId(),
					getCurrentUserDetails.get().getUserName(), getCurrentUserDetails.get().getPrefNo());
			if (CollectionUtils.isEmpty(signs)) {
				message.setStatus(false);
				message.setMessage("No records available for rollback.");
			} else {
				if (signs.size() == 1) {
					Signature signature = signs.get(0);
					Optional<Checker> cheker = checkerRepository.findBySignatureIdAndUserId("" + signature.getId(),
							"" + getCurrentUserDetails.get().getId());
					if (cheker.isPresent()) {
						checkerRepository.delete(cheker.get());
						signatureRepository.delete(signature);
						message.setStatus(true);
						message.setMessage("Rollback successfull");
					} else {
						signatureRepository.delete(signature);
						message.setStatus(true);
						message.setMessage("Rollback successfull. No valid checker available.");
					}

				} else {
					message.setStatus(true);
					message.setMessage("Rollback successfull");
				}
			}
			message.setStatus(true);
			message.setMessage("Rollback successfull");
		} else {
			message.setStatus(false);
			message.setMessage("User and IMPS details are mandatory");
		}

		return message;

	}
	
	public SMEMessage rollbackSch(Optional<User> getCurrentUserDetails, Optional<ScheduledPayment> impsTransfer) {
		SMEMessage message = new SMEMessage();
		if (getCurrentUserDetails.isPresent() && impsTransfer.isPresent()) {
			List<Signature> signs = signatureRepository.findByOperationIdAndUserIdAndUserNameAndPrefNo(
					"" + impsTransfer.get().getId(), "" + getCurrentUserDetails.get().getId(),
					getCurrentUserDetails.get().getUserName(), getCurrentUserDetails.get().getPrefNo());
			if (CollectionUtils.isEmpty(signs)) {
				message.setStatus(false);
				message.setMessage("No records available for rollback.");
			} else {
				if (signs.size() == 1) {
					Signature signature = signs.get(0);
					Optional<Checker> cheker = checkerRepository.findBySignatureIdAndUserId("" + signature.getId(),
							"" + getCurrentUserDetails.get().getId());
					if (cheker.isPresent()) {
						checkerRepository.delete(cheker.get());
						signatureRepository.delete(signature);
						message.setStatus(true);
						message.setMessage("Rollback successfull");
					} else {
						signatureRepository.delete(signature);
						message.setStatus(true);
						message.setMessage("Rollback successfull. No valid checker available.");
					}

				} else {
					message.setStatus(true);
					message.setMessage("Rollback successfull");
				}
			}
			message.setStatus(true);
			message.setMessage("Rollback successfull");
		} else {
			message.setStatus(false);
			message.setMessage("User and IMPS details are mandatory");
		}

		return message;

	}
}
