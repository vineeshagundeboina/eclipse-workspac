package com.federal.fedmobilesmecore.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.federal.fedmobilesmecore.dto.AuditLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.FundTransfer;
import com.federal.fedmobilesmecore.dto.ImpsTransfer;
import com.federal.fedmobilesmecore.dto.ScheduledPayment;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;
import com.federal.fedmobilesmecore.repository.AuditLogRepository;

@Component
public class AuditLogService {
	@Autowired
	AuditLogRepository auditLogRepository;

	public AuditLog FTlog(FundTransfer fundTransfer, String code, String responseCode,
			Optional<Enterprises> enterprises) {

		AuditLog auditLog = new AuditLog();
		auditLog.setRemitterAccNo(fundTransfer.getRemAccNo());
		auditLog.setBeneficiaryAccNo(fundTransfer.getBenAccNo());
		auditLog.setSenderRefNo(fundTransfer.getSenderRefId());
		auditLog.setRefNo(fundTransfer.getRefNo());
		auditLog.setAmount(fundTransfer.getAmount());
		auditLog.setResponseCode(responseCode);
		auditLog.setMode(fundTransfer.getMode());
		auditLog.setTransDate(new Timestamp(new Date().getTime()).toString());
		auditLog.setPrefCorp(enterprises.get().getPrefCorp());
		auditLog.setEnterpriseId(String.valueOf(enterprises.get().getId()));
		auditLog.setCreatedAt(new Timestamp(new Date().getTime()));
		auditLog.setUpdatedAt(new Timestamp(new Date().getTime()));
		return auditLogRepository.save(auditLog);
	}
	
	public AuditLog FTSchlog(ScheduledTransaction fundTransfer, String code, String responseCode,
			Optional<Enterprises> enterprises) {

		AuditLog auditLog = new AuditLog();
		auditLog.setRemitterAccNo(fundTransfer.getRemAccNo());
		auditLog.setBeneficiaryAccNo(fundTransfer.getBenAccNo());
		auditLog.setSenderRefNo(fundTransfer.getSenderRefId());
		auditLog.setRefNo(fundTransfer.getRefNo());
		auditLog.setAmount(fundTransfer.getAmount());
		auditLog.setResponseCode(responseCode);
		auditLog.setMode(fundTransfer.getMode());
		auditLog.setTransDate(new Timestamp(new Date().getTime()).toString());
		auditLog.setPrefCorp(enterprises.get().getPrefCorp());
		auditLog.setEnterpriseId(String.valueOf(enterprises.get().getId()));
		auditLog.setCreatedAt(new Timestamp(new Date().getTime()));
		auditLog.setUpdatedAt(new Timestamp(new Date().getTime()));
		return auditLogRepository.save(auditLog);
	}

	public AuditLog IMPSlog(ImpsTransfer impsTransfer, String code, String responseCode,
			Optional<Enterprises> enterprises) {
		AuditLog auditLog = new AuditLog();
		auditLog.setRemitterAccNo(impsTransfer.getRemAccNum());
		auditLog.setBeneficiaryAccNo(impsTransfer.getBenAccNo());
		auditLog.setSenderRefNo(impsTransfer.getSenderRefId());
		auditLog.setRefNo(impsTransfer.getRefNo());
		auditLog.setAmount(impsTransfer.getAmount());
		auditLog.setResponseCode(responseCode);
		auditLog.setMode(impsTransfer.getMode());
		auditLog.setTransDate(new Timestamp(new Date().getTime()).toString());
		auditLog.setPrefCorp(enterprises.get().getPrefCorp());
		auditLog.setEnterpriseId(String.valueOf(enterprises.get().getId()));
		auditLog.setCreatedAt(new Timestamp(new Date().getTime()));
		auditLog.setUpdatedAt(new Timestamp(new Date().getTime()));
		return auditLogRepository.save(auditLog);

	}
	
	public AuditLog IMPSlogSch(ScheduledTransaction impsTransfer, String code, String responseCode,
			Optional<Enterprises> enterprises) {
		AuditLog auditLog = new AuditLog();
		auditLog.setRemitterAccNo(impsTransfer.getRemAccNo());
		auditLog.setBeneficiaryAccNo(impsTransfer.getBenAccNo());
		auditLog.setSenderRefNo(impsTransfer.getSenderRefId());
		auditLog.setRefNo(impsTransfer.getRefNo());
		auditLog.setAmount(impsTransfer.getAmount());
		auditLog.setResponseCode(responseCode);
		auditLog.setMode(impsTransfer.getMode());
		auditLog.setTransDate(new Timestamp(new Date().getTime()).toString());
		auditLog.setPrefCorp(enterprises.get().getPrefCorp());
		auditLog.setEnterpriseId(String.valueOf(enterprises.get().getId()));
		auditLog.setCreatedAt(new Timestamp(new Date().getTime()));
		auditLog.setUpdatedAt(new Timestamp(new Date().getTime()));
		return auditLogRepository.save(auditLog);

	}
}