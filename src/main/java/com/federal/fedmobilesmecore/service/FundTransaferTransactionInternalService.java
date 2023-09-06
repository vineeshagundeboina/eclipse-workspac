package com.federal.fedmobilesmecore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.TransactionLimit;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.FundTransferRepository;
import com.federal.fedmobilesmecore.repository.ImpsTransferRepository;
import com.federal.fedmobilesmecore.repository.TransactionLimitRepository;
import com.federal.fedmobilesmecore.service.FundTransferService.PERIODTYPE;

@Component
public class FundTransaferTransactionInternalService {

	@Autowired
	TransactionLimitRepository transactionLimitRepository;
	@Autowired
	FundTransferRepository fundTransferRepository;
	@Autowired
	ImpsTransferRepository impsTransferRepository;
	@Autowired
	FundTransferService fundTransferService;
	@Autowired
	EnterprisesRepository enterprisesRepository;

	@Value("${daily_transaction}")
	String transactionDaily;
	@Value("${monthly_transaction}")
	String transactionMonthly;
	// @Value("${quickpaylimit}")
	// String quickpayLimit;

	public boolean validDailyTransaction(String amount, String enterpriseId) {
		return dailyTransactionLimitExceeded(amount, enterpriseId);
	}

	public boolean validMonthlyTransaction(String amount, String enterpriseId) {
		return monthlyTransactionLimitExceeded(amount, enterpriseId);
	}

	public boolean dailyTransactionLimitExceeded(String amount, String enterpriseId) {
		Double totalLimitDaily;
		Double currentLimit;
		Optional<TransactionLimit> transactionDailyLimit = null;
		boolean validDailyTransaction = false;

		transactionDailyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId,
				"daily");
		if (transactionDailyLimit.isPresent()) {
			totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0") ? Double.parseDouble(transactionDaily)
					: Double.parseDouble(transactionDailyLimit.get().getAmount());
		} else {
			totalLimitDaily = Double.parseDouble(transactionDaily);
		}

		if (totalLimitDaily != null) {
			currentLimit = approvedAmount(enterpriseId) + Double.parseDouble(amount);
			if (currentLimit > totalLimitDaily) {
				validDailyTransaction = false;
			} else {
				validDailyTransaction = true;
			}
		} else {
			validDailyTransaction = false;
		}
		RecordLog.writeLogFile("is valid Daily transaction: " + validDailyTransaction);
		return validDailyTransaction;
	}

	public boolean monthlyTransactionLimitExceeded(String amount, String enterpriseId) {
		Double totalLimitMonthly;
		Double currentLimit;
		Optional<TransactionLimit> transactionMonthlyLimit = null;
		boolean validMonthlyTransaction = false;

		transactionMonthlyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId,
				"monthly");
		if (transactionMonthlyLimit.isPresent()) {
			totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
					? Double.parseDouble(transactionMonthly)
					: Double.parseDouble(transactionMonthlyLimit.get().getAmount());
		} else {
			totalLimitMonthly = Double.parseDouble(transactionMonthly);
		}
		if (totalLimitMonthly != null) {
			currentLimit = totalAmountMonth(enterpriseId) + Double.parseDouble(amount);
			if (currentLimit > totalLimitMonthly) {
				validMonthlyTransaction = false;
			} else {
				validMonthlyTransaction = true;
			}

		} else {
			validMonthlyTransaction = false;
		}
		RecordLog.writeLogFile("is valid Monthly transaction: " + validMonthlyTransaction);
		return validMonthlyTransaction;
	}

//	public boolean validQuickPay(String amount, String enterpriseId) {
//		boolean validQuickPay = false;
//		boolean isLimitExceeded = false;
//		isLimitExceeded = quickPayLimitExceeded(amount, enterpriseId);

//		if (isLimitExceeded == false) {
//			validQuickPay = true;
//		} else {
//			validQuickPay = false;
//		}
//		RecordLog.writeLogFile("valid Quick pay :" + validQuickPay);
//		return validQuickPay;
//	}

//	public boolean quickPayLimitExceeded(String amount, String enterpriseId) {
//		boolean isQuickPayLimitExceeded = false;
//		Double transactionQuickpay;
//		Double totalLimitQuickpay;
//		Double currentLimit;
//
//		transactionQuickpay = Double.parseDouble(quickpayLimit);
//		if (transactionQuickpay != null) {
//			totalLimitQuickpay = transactionQuickpay;
//			currentLimit = totalAmountQuickpay(enterpriseId) + Double.parseDouble(amount);
//			isQuickPayLimitExceeded = currentLimit > totalLimitQuickpay;
//		} else {
//			isQuickPayLimitExceeded = true;
//		}
//		RecordLog.writeLogFile("Quick pay Limit Exceed" + isQuickPayLimitExceeded);
//		return isQuickPayLimitExceeded;
//	}

	private Long totalAmountQuickpay(String enterpriseId) {
		Enterprises enterprises = null;
		Long totalApprovedAmount = null;
		enterprises = enterprisesRepository.findById(Long.valueOf(enterpriseId)).orElse(null);
		totalApprovedAmount = fundTransferService.getTotalAmountQuickpay(PERIODTYPE.DAY, enterprises).longValue();
		return totalApprovedAmount;
	}

	public Long approvedAmount(String enterpriseId) {
		Enterprises enterprises = null;
		Long totalApprovedAmount = null;
		enterprises = enterprisesRepository.findById(Long.valueOf(enterpriseId)).orElse(null);
		totalApprovedAmount = fundTransferService.getTotalConsumedAmount(PERIODTYPE.DAY, enterprises).longValue();
		return totalApprovedAmount;
	}

	public Long totalAmountMonth(String enterpriseId) {
		Enterprises enterprises = null;
		Long totalApprovedAmount = null;
		enterprises = enterprisesRepository.findById(Long.valueOf(enterpriseId)).orElse(null);
		totalApprovedAmount = fundTransferService.getTotalConsumedAmount(PERIODTYPE.MONTH, enterprises).longValue();
		return totalApprovedAmount;
	}
}