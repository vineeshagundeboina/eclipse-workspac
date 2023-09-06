package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.FundTransferLog;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;
import com.federal.fedmobilesmecore.model.FundTransferInternalModel;
import com.federal.fedmobilesmecore.repository.FundTransferLogRepository;

@Component
public class FundTransferLogsEntryService {
	@Autowired
	FundTransferLogRepository FundTransferLogRepository;

	public FundTransferLog approveLog(FundTransfer fundTransfer, String prefNo, String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;

		if (fundTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(fundTransfer.getAmount());
			fundTransferLog.setRefNo(fundTransfer.getRefNo());
			fundTransferLog.setFromAccount(fundTransfer.getRemAccNo());
			fundTransferLog.setToAccount(fundTransfer.getBenAccNo());
			fundTransferLog.setOperation(fundTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}
		
		

		return fundTransferLogResp;
	}

	public FundTransferLog approveSchLog(ScheduledTransaction fundTransfer, String prefNo, String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;

		if (fundTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(fundTransfer.getAmount());
			fundTransferLog.setRefNo(fundTransfer.getRefNo());
			fundTransferLog.setFromAccount(fundTransfer.getRemAccNo());
			fundTransferLog.setToAccount(fundTransfer.getBenAccNo());
			fundTransferLog.setOperation(fundTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}
		
		

		return fundTransferLogResp;
	}
	
	public FundTransferLog transactionLog(FundTransfer fundTransfer, String responsecode, String prefNo,
			String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;
		if (fundTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(fundTransfer.getAmount());
			fundTransferLog.setRefNo(fundTransfer.getRefNo());
			fundTransferLog.setFromAccount(fundTransfer.getRemAccNo());
			fundTransferLog.setToAccount(fundTransfer.getBenAccNo());
			fundTransferLog.setOperation(fundTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}

		return fundTransferLogResp;
	}
	
	public FundTransferLog transactionSchLog(ScheduledTransaction fundTransfer, String responsecode, String prefNo,
			String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;
		if (fundTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(fundTransfer.getAmount());
			fundTransferLog.setRefNo(fundTransfer.getRefNo());
			fundTransferLog.setFromAccount(fundTransfer.getRemAccNo());
			fundTransferLog.setToAccount(fundTransfer.getBenAccNo());
			fundTransferLog.setOperation(fundTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}

		return fundTransferLogResp;
	}

	public FundTransferLog initiateLog(FundTransfer fundTransfer, String prefNo, String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;
		if (fundTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(fundTransfer.getAmount());
			fundTransferLog.setRefNo(fundTransfer.getRefNo());
			fundTransferLog.setFromAccount(fundTransfer.getRemAccNo());
			fundTransferLog.setToAccount(fundTransfer.getBenAccNo());
			fundTransferLog.setOperation(fundTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}

		return fundTransferLogResp;

	}

	public FundTransferLog initiateLog(ImpsTransfer impsTransfer, String prefNo, String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;
		if (impsTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(impsTransfer.getAmount());
			fundTransferLog.setRefNo(impsTransfer.getRefNo());
			fundTransferLog.setFromAccount(impsTransfer.getRemAccNum());
			fundTransferLog.setToAccount(impsTransfer.getBenAccNo());
			fundTransferLog.setOperation(impsTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}

		return fundTransferLogResp;

	}

	public FundTransferLog approveLog(ImpsTransfer impsTransfer, String prefNo, String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;

		if (impsTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(impsTransfer.getAmount());
			fundTransferLog.setRefNo(impsTransfer.getRefNo());
			fundTransferLog.setFromAccount("");
			fundTransferLog.setToAccount(impsTransfer.getBenAccNo());
			fundTransferLog.setOperation(impsTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}

		return fundTransferLogResp;

	}

	public FundTransferLog transactionLog(ImpsTransfer impsTransfer, String string, String prefNo, String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;
		if (impsTransfer != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(impsTransfer.getAmount());
			fundTransferLog.setRefNo(impsTransfer.getRefNo());
			fundTransferLog.setFromAccount(impsTransfer.getRemAccNum());
			fundTransferLog.setToAccount(impsTransfer.getBenAccNo());
			fundTransferLog.setOperation(impsTransfer.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}

		return fundTransferLogResp;

	}

	public FundTransferLog approveSchPayLog(ScheduledTransaction scheduledTransaction, String prefNo, String prefCorp) {
		FundTransferInternalModel fundTransferInternalModel = null;
		FundTransferLog fundTransferLog = null;
		FundTransferLog fundTransferLogResp = null;

		if (scheduledTransaction != null && StringUtils.isNotEmpty(prefNo) && StringUtils.isNotEmpty(prefCorp)) {
			fundTransferLog = new FundTransferLog();
			fundTransferLog.setAmount(scheduledTransaction.getAmount());
			fundTransferLog.setRefNo(scheduledTransaction.getRefNo());
			fundTransferLog.setFromAccount(scheduledTransaction.getRemAccNo());
			fundTransferLog.setToAccount(scheduledTransaction.getBenAccNo());
			fundTransferLog.setOperation(scheduledTransaction.getOperationId());
			fundTransferLog.setApprovedBy(prefNo);
			fundTransferLog.setEnterpriseName(prefCorp);
			fundTransferLog.setCreatedAt(new Timestamp(new Date().getTime()));
			fundTransferLog.setUpdatedAt(new Timestamp(new Date().getTime()));
			fundTransferLogResp = FundTransferLogRepository.save(fundTransferLog);

			if (fundTransferLogResp != null) {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(true);
				fundTransferInternalModel.setMessage("success");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(fundTransferLogResp);
			} else {
				fundTransferInternalModel = new FundTransferInternalModel();
				fundTransferInternalModel.setStatus(false);
				fundTransferInternalModel.setMessage("failed to store");
				fundTransferInternalModel.setFundTransfer(null);
				fundTransferInternalModel.setFundTransferLog(null);
			}
		} else {
			fundTransferInternalModel = new FundTransferInternalModel();
			fundTransferInternalModel.setStatus(false);
			fundTransferInternalModel.setMessage("proper data not passed");
			fundTransferInternalModel.setFundTransfer(null);
			fundTransferInternalModel.setFundTransferLog(null);
		}

		return fundTransferLogResp;
		
	}
}