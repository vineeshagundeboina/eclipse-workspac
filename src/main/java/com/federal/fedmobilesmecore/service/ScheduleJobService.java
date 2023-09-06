package com.federal.fedmobilesmecore.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.federal.fedmobilesmecore.config.RecordLog;
import com.federal.fedmobilesmecore.dto.Beneficiaries;
import com.federal.fedmobilesmecore.dto.Enterprises;
import com.federal.fedmobilesmecore.dto.ScheduledPayment;
import com.federal.fedmobilesmecore.dto.ScheduledTransaction;
import com.federal.fedmobilesmecore.dto.TransactionLimit;
import com.federal.fedmobilesmecore.dto.User;
import com.federal.fedmobilesmecore.model.ApproveTransactionReqModel;
import com.federal.fedmobilesmecore.model.FundTransferInternalModel;
import com.federal.fedmobilesmecore.repository.BeneficiariesRepository;
import com.federal.fedmobilesmecore.repository.EnterprisesRepository;
import com.federal.fedmobilesmecore.repository.ScheduledPaymentRepo;
import com.federal.fedmobilesmecore.repository.ScheduledTransactionRepo;
import com.federal.fedmobilesmecore.repository.TransactionLimitRepository;
import com.federal.fedmobilesmecore.repository.UserRepository;
import com.federal.fedmobilesmecore.service.FundTransferService.PERIODTYPE;

@Service
public class ScheduleJobService {
	
	private static final Logger log4j = LogManager.getLogger(ScheduleJobService.class);
	
	@Autowired
	BeneficiariesRepository beneficiariesRepository;
	@Autowired
	ScheduledPaymentRepo ScheduledPaymentRepo;
	@Autowired
	EnterprisesRepository enterprisesRepository;
	@Autowired
	ScheduledTransactionRepo ScheduledTransactionRepo;
	@Autowired
	UserRepository userRepositoryrepo;
	@Autowired
	TransactionLimitRepository transactionLimitRepository;
	@Autowired
	FundTransferService fundTransferService;
	@Autowired
	FundTransaferTransactionInternalService fundTransaferTransactionInternalService;
	@Autowired
	SchedulePaymentsService schedulePaymentsService;
	
	@Value("${tl.dailytrans}")
	String transactionDaily;
	@Value("${tl.monthlytrans}")
	String transactionMonthly;
	// @Value("${tl.quickpaylimit}")
	// String quickpayLimit;
	
	@Scheduled(cron = "${cron.expression1}")
	public void ActivateBeneficiary() {
		
		Enterprises enterprises=null;
		
		RecordLog.writeLogFile("ActivateBeneficiary Scheduler is RUNNING!!!! :" +new Date());
		
		List<Beneficiaries> beneficiaries = beneficiariesRepository.findByStatusAndIsActiveAndIsJobActiveOrderByUpdatedAtAsc("approved", false, 0);
		
		if (beneficiaries.size() > 0) {
			
			RecordLog.writeLogFile("ActivateBeneficiary: Found {"+beneficiaries.size()+"} approved and inactive beneficiaries...");

			for(int i=0; i<beneficiaries.size(); i++)
			{
//			   RecordLog.writeLogFile("ActivateBeneficiary: Processing {"+beneficiaries.get(i).getId()+"}");

			   enterprises = enterprisesRepository.findByIdAndActive(Long.valueOf(beneficiaries.get(i).getEnterpriseId()), true);
			   
			   if(enterprises != null)
			   {
//				   RecordLog.writeLogFile("ActivateBeneficiary: Beneficiary Update for {"+beneficiaries.get(i).getId()+"}");
				   
				   Optional<Beneficiaries> beneficiaries2 = beneficiariesRepository.findById(Long.valueOf(beneficiaries.get(i).getId()));
				   
				   if(beneficiaries2.isPresent())
				   {
//					   RecordLog.writeLogFile("ActivateBeneficiary: Beneficiary Update for {"+beneficiaries.get(i).getId()+"}");
					   beneficiaries2.get().setIsJobActive(1);
					   beneficiariesRepository.save(beneficiaries2.get());
				   }
				   
//				   RecordLog.writeLogFile("ActivateBeneficiary: Beneficiary Starting for {"+beneficiaries.get(i).getId()+"}");
				   
				   int coolingperoid=(int) enterprises.getCoolingPeriod();
				   
				   if(beneficiaries2.get().getApprovedAt() == null)
				   {
					   beneficiaries2.get().setIsJobActive(0);
					   beneficiariesRepository.save(beneficiaries2.get());
					   RecordLog.writeLogFile("ActivateBeneficiary: Completed job for {"+beneficiaries.get(i).getId()+"} Approved at nil");
				   }
				   
				   else
				   {
					   String coolingperoidmins=minutesToDaysHoursMinutes(coolingperoid);		   
					   long coldays=Long.parseLong(coolingperoidmins.split("_")[0]);
					   long colhours=Long.parseLong(coolingperoidmins.split("_")[1]);
					   long colminutes=Long.parseLong(coolingperoidmins.split("_")[2]);
					   					   
					   Timestamp old = beneficiaries2.get().getApprovedAt();
					   ZonedDateTime zonedDateTime = old.toInstant().atZone(ZoneId.of("UTC"));
					   Timestamp total_time = Timestamp.from(zonedDateTime.plus(coldays,ChronoUnit.DAYS).plus(colhours,ChronoUnit.HOURS).plus(colminutes, ChronoUnit.MINUTES).toInstant());
					   Timestamp currentdate = new Timestamp(new Date().getTime());

					  if(total_time.before(currentdate))
					  {
						  beneficiaries2.get().setActive(true);
						  beneficiaries2.get().setIsJobActive(0);
						  beneficiaries2.get().setUpdatedAt(new Timestamp(new Date().getTime()));
						  beneficiariesRepository.save(beneficiaries2.get());
						  RecordLog.writeLogFile("ActivateBeneficiary: Completed activation of beneficiary {"+beneficiaries.get(i).getId()+"}");
					  }
					   
					  else
					  {
						  beneficiaries2.get().setIsJobActive(0);
						  beneficiariesRepository.save(beneficiaries2.get());
						  RecordLog.writeLogFile("ActivateBeneficiary: Completed job for {"+beneficiaries.get(i).getId()+"}");
					  }
					   
				   }
				   
			   }
			   
			   else
			   {
				   RecordLog.writeLogFile("ActivateBeneficiary: Enterprise blank for {"+beneficiaries.get(i).getId()+"}");
			   }
				
			}
			
		}
		
		else
		{			
			RecordLog.writeLogFile("ActivateBeneficiary: No beneficiary found for approved and inactive state, skipping...");
		}

		
	}
	
	@Scheduled(cron = "${cron.expression2}")
	public void ScheduledPaymentsJob() {
		
		RecordLog.writeLogFile("ScheduledPayment Scheduler is RUNNING!!!! :" +new Date());
		
		long paytranindexid=0;
		
		List<ScheduledPayment> Schedulepayget = ScheduledPaymentRepo.findByStatusAndIsJobActiveAndIsCompletedAndIsCancelled("approved", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		
		if (Schedulepayget.size() > 0) {
			
			RecordLog.writeLogFile("ScheduledPayment: Found {"+Schedulepayget.size()+"} approved and incomplete jobs...");
			
			for(int i=0; i<Schedulepayget.size(); i++)
			{
			   RecordLog.writeLogFile("ScheduledPayment: Processing {"+Schedulepayget.get(i).getId()+"}");
			   
			   Schedulepayget.get(i).setIsJobActive(BigDecimal.ONE);
			   ScheduledPaymentRepo.save(Schedulepayget.get(i));
			   
			   RecordLog.writeLogFile("ScheduledPayment: Starting job {"+Schedulepayget.get(i).getId()+"}");
			   
				ScheduledTransaction ScheduledTransactionrecord = new ScheduledTransaction();
			   
				 RecordLog.writeLogFile("ScheduledPayment: Starting job >>>>>>>> {"+Schedulepayget.get(i).getNextExecutionDate()+"}");
				
			   if(Schedulepayget.get(i).getNextExecutionDate().before(new Timestamp(new Date().getTime())))
			   {
				   if (Schedulepayget.get(i).getMode().equals("neft") || Schedulepayget.get(i).getMode().equals("fed2fed"))
				   {
					  // ScheduledTransactionrecord.setId(ScheduledTransactionrecord.getId());
					   ScheduledTransactionrecord.setAmount(Schedulepayget.get(i).getAmount());
					   ScheduledTransactionrecord.setMode(Schedulepayget.get(i).getMode());
					   ScheduledTransactionrecord.setRemAccNo(Schedulepayget.get(i).getRemAccNo());
					   ScheduledTransactionrecord.setRemName(Schedulepayget.get(i).getRemName());
					   ScheduledTransactionrecord.setBenAccNo(Schedulepayget.get(i).getBenAccNo());
					   ScheduledTransactionrecord.setBenName(Schedulepayget.get(i).getBenName());
					   ScheduledTransactionrecord.setBenNickName(Schedulepayget.get(i).getBenNickName());
					   ScheduledTransactionrecord.setBenAccType(Schedulepayget.get(i).getBenAccType());
					   ScheduledTransactionrecord.setRemarks(Schedulepayget.get(i).getRemarks());
					   ScheduledTransactionrecord.setBenIfsc(Schedulepayget.get(i).getBenIfsc());
					   ScheduledTransactionrecord.setCreatedAt(new Timestamp(new Date().getTime()));
					   ScheduledTransactionrecord.setLockVersion(BigDecimal.ZERO);
					   ScheduledTransactionrecord.setUpdatedAt(new Timestamp(new Date().getTime()));
					   ScheduledTransactionrecord.setEnterpriseId(Schedulepayget.get(i).getEnterpriseId());
					   ScheduledTransactionrecord.setTransactionDate(new Timestamp(new Date().getTime()));
					   ScheduledTransactionrecord.setScheduledPaymentId(String.valueOf(Schedulepayget.get(i).getId()));			
				   }
				   else if (Schedulepayget.get(i).getMode().equals("p2p") || Schedulepayget.get(i).getMode().equals("p2a"))
				   {
					  // ScheduledTransactionrecord.setId(ScheduledTransactionrecord.getId());
					   ScheduledTransactionrecord.setAmount(Schedulepayget.get(i).getAmount());
					   ScheduledTransactionrecord.setMode(Schedulepayget.get(i).getMode());
					   ScheduledTransactionrecord.setRemAccNo(Schedulepayget.get(i).getRemAccNo());
					   ScheduledTransactionrecord.setRemName(Schedulepayget.get(i).getRemName());
					   ScheduledTransactionrecord.setRemMobNum(Schedulepayget.get(i).getRemMobNum());
					   ScheduledTransactionrecord.setRemMmid(Schedulepayget.get(i).getRemMmid());
					   ScheduledTransactionrecord.setRemCustId(Schedulepayget.get(i).getRemCustId());
					   ScheduledTransactionrecord.setBenAadhar(Schedulepayget.get(i).getBenAadhar());
					   ScheduledTransactionrecord.setBenIfsc(Schedulepayget.get(i).getBenIfsc());
					   ScheduledTransactionrecord.setBenAccNo(Schedulepayget.get(i).getBenAccNo());
					   ScheduledTransactionrecord.setBenMmid(Schedulepayget.get(i).getBenMmid());
					   ScheduledTransactionrecord.setRemarks(Schedulepayget.get(i).getRemarks());
					   ScheduledTransactionrecord.setBenMobNum(Schedulepayget.get(i).getBenMobNum());
					   ScheduledTransactionrecord.setBenCustName(Schedulepayget.get(i).getBenCustName());
					   ScheduledTransactionrecord.setBenNickName(Schedulepayget.get(i).getBenNickName());
					   ScheduledTransactionrecord.setLockVersion(BigDecimal.ZERO);
					   ScheduledTransactionrecord.setCreatedAt(new Timestamp(new Date().getTime()));
					   ScheduledTransactionrecord.setUpdatedAt(new Timestamp(new Date().getTime()));
					   ScheduledTransactionrecord.setEnterpriseId(Schedulepayget.get(i).getEnterpriseId());
					   ScheduledTransactionrecord.setTransactionDate(new Timestamp(new Date().getTime()));
					   ScheduledTransactionrecord.setScheduledPaymentId(String.valueOf(Schedulepayget.get(i).getId()));					   
				   }
				   
				    ScheduledTransactionRepo.save(ScheduledTransactionrecord);
				    
				    paytranindexid=ScheduledTransactionrecord.getId();
				   
				    RecordLog.writeLogFile("ScheduledPayment: ScheduledTransaction created {"+paytranindexid+"}");
				    
				    if(paytranindexid != 0)
				    {
				    	 ScheduledTransactionWorker(paytranindexid);
				    	 RecordLog.writeLogFile("ScheduledTransactionWorker: perform_async {"+paytranindexid+"}");
				    }
				    
				    BigDecimal payremainigcount=Schedulepayget.get(i).getRemainingCount().subtract(BigDecimal.ONE);
//				    RecordLog.writeLogFile("<<<<<<<<>>>>>>>>"+payremainigcount);
				    
				    if(!payremainigcount.equals(BigDecimal.ZERO) && Schedulepayget.get(i).getNextExecutionDate().equals(Schedulepayget.get(i).getEndDate()))
				    {
				    	   Schedulepayget.get(i).setIsCompleted(BigDecimal.ONE);
						   ScheduledPaymentRepo.save(Schedulepayget.get(i));
						   RecordLog.writeLogFile("Next execution Date: "+Schedulepayget.get(i).getNextExecutionDate());
				    }
				    else
				    {
				    	BigDecimal count = BigDecimal.ONE;
				    	ZonedDateTime start_date = ZonedDateTime.ofInstant( Schedulepayget.get(i).getStartDate().toInstant(), ZoneId.systemDefault());
				    	if ( Schedulepayget.get(i).getFrequency().equals("daily")) {
							Date daily_date = Date.from(start_date.plusDays(count.longValue()).toInstant());
							 Schedulepayget.get(i).setNextExecutionDate(daily_date);
						} else if ( Schedulepayget.get(i).getFrequency().equals("weekly")) {
							Date daily_date = Date.from(start_date.plusWeeks(count.longValue()).toInstant());
							Schedulepayget.get(i).setNextExecutionDate(daily_date);
						} else if ( Schedulepayget.get(i).getFrequency().equals("monthly")) {
							Date daily_date = Date.from(start_date.plusMonths(count.longValue()).toInstant());
							Schedulepayget.get(i).setNextExecutionDate(daily_date);
						} else if ( Schedulepayget.get(i).getFrequency().equals("quarterly")) {
							Date daily_date = Date.from(start_date.plusMonths(count.longValue() * 3).toInstant());
							Schedulepayget.get(i).setNextExecutionDate(daily_date);
						} else if ( Schedulepayget.get(i).getFrequency().equals("half_yearly")) {
							Date daily_date = Date.from(start_date.plusMonths(count.longValue() * 6).toInstant());
							Schedulepayget.get(i).setNextExecutionDate(daily_date);
						} else if ( Schedulepayget.get(i).getFrequency().equals("yearly")) {
							Date daily_date = Date.from(start_date.plusYears(count.longValue()).toInstant());
							Schedulepayget.get(i).setNextExecutionDate(daily_date);
							RecordLog.writeLogFile("Daily execution Date: "+daily_date);
						}
				    	
				    	 ScheduledPaymentRepo.save(Schedulepayget.get(i));
				    	 
				    }
				    
			   }
			   
			   else
			   {
				   RecordLog.writeLogFile("ScheduledPayment: Execution date #{"+Schedulepayget.get(i).getNextExecutionDate()+"}");
				   RecordLog.writeLogFile("ScheduledPayment: Execution date not yet reached for #{"+Schedulepayget.get(i).getId()+"}");
				  
			   }
			   
			   Schedulepayget.get(i).setIsJobActive(BigDecimal.ZERO);
			   ScheduledPaymentRepo.save(Schedulepayget.get(i));
			   RecordLog.writeLogFile("ScheduledPayment: Completed Processing of #{"+Schedulepayget.get(i).getId()+"}");
			   
			}

			
		}
		else
		{
			RecordLog.writeLogFile("ScheduledPayment: No jobs found for approved and incomplete state, skipping...");
		}
	}
	
	
	public void ScheduledTransactionWorker(long ScheduleTranId) {
		
		RecordLog.writeLogFile("ScheduledTransactionWorker is RUNNING!!!! :" +new Date());
		
		Enterprises enterprises=null;
		
		boolean validDailyTransaction = false;
		boolean validMonthlyTransaction = false;		
		
		long paytranindexid=0;
		
		Optional<ScheduledTransaction> Scheduletrans = ScheduledTransactionRepo.findById(ScheduleTranId);
		
		if (Scheduletrans.isPresent()) {
			
			Optional<ScheduledPayment> Schedulepayid = ScheduledPaymentRepo.findById(Long.valueOf(Scheduletrans.get().getScheduledPaymentId()));
			
			if(Schedulepayid.isPresent())
			{
				RecordLog.writeLogFile("ScheduledTransactionWorker: Processing scheduled transaction {"+Scheduletrans.get().getId()+"}");
				
				Scheduletrans.get().setIsJobActive(BigDecimal.ONE);
				ScheduledTransactionRepo.save(Scheduletrans.get());
				
				Optional<User> usermarkerid = userRepositoryrepo.findById(Schedulepayid.get().getMakerId().longValue());
				
				if(usermarkerid.isPresent())
				{
					enterprises = enterprisesRepository.findByIdAndActive(Long.valueOf(Schedulepayid.get().getEnterpriseId()), true);
					
					if(enterprises != null)
					{
						RecordLog.writeLogFile("ScheduledTransactionWorker: Processing scheduled transaction started {"+Scheduletrans.get().getId()+"}");
						
						
						validDailyTransaction = validDailyTransaction(Scheduletrans.get().getAmount(), Schedulepayid.get().getEnterpriseId());
						validMonthlyTransaction = validMonthlyTransaction(Scheduletrans.get().getAmount(), Schedulepayid.get().getEnterpriseId());

						if (validDailyTransaction && validMonthlyTransaction) {
							
							//------Fund Transfer Code-----//
							
							if(Scheduletrans.get().getMode().equals("neft")||Scheduletrans.get().getMode().equals("fed2fed")) {
								schedulePaymentsService.approveIntrabankProcess(Schedulepayid, Scheduletrans, usermarkerid);
							} else {
								schedulePaymentsService.approveImpsProcess(Scheduletrans, Schedulepayid, usermarkerid);
							}
							
						}
						else if (!validDailyTransaction) {
							Long totalLimitDaily = 0L;
							Optional<TransactionLimit> transactionDailyLimit = null;
							Long daily_amount_transacted = fundTransaferTransactionInternalService.approvedAmount(Schedulepayid.get().getEnterpriseId());
							transactionDailyLimit = transactionLimitRepository
									.findFirstByEnterpriseIdAndModeOrderByIdDesc(Schedulepayid.get().getEnterpriseId(), "daily");
							if (transactionDailyLimit.isPresent()) {
								totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0")
										? Long.valueOf(transactionDaily)
										: Long.valueOf(transactionDailyLimit.get().getAmount());
							} else {
								totalLimitDaily = Long.valueOf(transactionDaily);
							}
							Optional<ScheduledTransaction> Scheduletransfailedupd=ScheduledTransactionRepo.findByIdAndLockVersion(ScheduleTranId, BigDecimal.ZERO);
							
							Scheduletransfailedupd.get().setStatus("failed");
							Scheduletransfailedupd.get().setReason("Transaction amount exceeds the Daily Transaction limit. You have already transacted for Rs."
									+ String.valueOf(daily_amount_transacted) + " amount and the limit is Rs."
									+ String.valueOf(totalLimitDaily));
							Scheduletransfailedupd.get().setIsJobActive(BigDecimal.ZERO);
							ScheduledTransactionRepo.save(Scheduletransfailedupd.get());
						}
						else if (!validMonthlyTransaction) {

							Long totalLimitMonthly = 0L;
							Optional<TransactionLimit> transactionMonthlyLimit = null;
							Long monthly_amount_transacted = fundTransaferTransactionInternalService.totalAmountMonth(Schedulepayid.get().getEnterpriseId());
							transactionMonthlyLimit = transactionLimitRepository
									.findFirstByEnterpriseIdAndModeOrderByIdDesc(Schedulepayid.get().getEnterpriseId(), "monthly");
							if (transactionMonthlyLimit.isPresent()) {
								totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0")
										? Long.valueOf(transactionMonthly)
										: Long.valueOf(transactionMonthlyLimit.get().getAmount());
							} else {
								totalLimitMonthly = Long.valueOf(transactionMonthly);
							}
							
							Optional<ScheduledTransaction> Scheduletransfailedupd=ScheduledTransactionRepo.findByIdAndLockVersion(ScheduleTranId, BigDecimal.ZERO);
							
							Scheduletransfailedupd.get().setStatus("failed");
							Scheduletransfailedupd.get().setReason("Transaction amount exceeds the Monthly Transaction limit. You have already transacted for Rs."
									+ String.valueOf(monthly_amount_transacted) + " amount and the limit is Rs."
									+ String.valueOf(totalLimitMonthly));
							Scheduletransfailedupd.get().setIsJobActive(BigDecimal.ZERO);
							ScheduledTransactionRepo.save(Scheduletransfailedupd.get());
						
						}
						
						
					}
					else
					{
						RecordLog.writeLogFile("ScheduledTransactionWorker: Processing scheduled transaction failed due to Invalid Corporate {"+Scheduletrans.get().getId()+"}");
						
						Optional<ScheduledTransaction> Scheduletransfailedupd=ScheduledTransactionRepo.findByIdAndLockVersion(ScheduleTranId, BigDecimal.ZERO);
						
						Scheduletransfailedupd.get().setStatus("failed");
						Scheduletransfailedupd.get().setRefNo(generateRefNo(Scheduletrans.get().getMode()));
						Scheduletransfailedupd.get().setReason("Invalid Corporate");
						Scheduletransfailedupd.get().setUpdatedAt(new Timestamp(new Date().getTime()));
						Scheduletransfailedupd.get().setLockVersion(BigDecimal.ONE);
						
						ScheduledTransactionRepo.save(Scheduletransfailedupd.get());
						
						
						
					}
					   
				}
				
				else
				{
					RecordLog.writeLogFile("ScheduledTransactionWorker: User id don't Exist {"+ScheduleTranId+"}");
					
				}
				
			}
			
			else
			{
				RecordLog.writeLogFile("ScheduledTransactionWorker: Payment id don't Exist {"+ScheduleTranId+"}");
			}
			
		}
		
		else
		{
			RecordLog.writeLogFile("ScheduledTransactionWorker: Transaction id don't Exist {"+ScheduleTranId+"}");
			
		}
		
		
		Optional<ScheduledTransaction> Scheduletransfailedupd=ScheduledTransactionRepo.findByIdAndLockVersion(ScheduleTranId, BigDecimal.ZERO);
		if(Scheduletransfailedupd.isPresent())
		{
		Scheduletransfailedupd.get().setRefNo(generateRefNo(Scheduletrans.get().getMode()));
		Scheduletransfailedupd.get().setIsJobActive(BigDecimal.ZERO);
		Scheduletransfailedupd.get().setUpdatedAt(new Timestamp(new Date().getTime()));
		Scheduletransfailedupd.get().setLockVersion(BigDecimal.ONE);		
		ScheduledTransactionRepo.save(Scheduletransfailedupd.get());
		}
		
		RecordLog.writeLogFile("ScheduledTransactionWorker: Completed Processing {"+ScheduleTranId+"}");
		
	}
	
	
	public String generateRefNo(String mode) {
		String refNo = null;
		final ZonedDateTime input = ZonedDateTime.now();
		String zeros = "000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;

		if (mode.equals("fed2fed")) {
			String prefix = "SMEFB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		} else if (mode.equals("neft")) {
			String prefix = "SMEMB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		} else if (mode.equals("imps")) {
			refNo = "IMPS" + s.toUpperCase();
		}
		return refNo;
	}

	public String generateSenderRefNo(String mode) {
		String refNo = null;
		final ZonedDateTime input = ZonedDateTime.now();
		String zeros = "000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;

		if (mode.equals("fed2fed")) {
			String prefix = "SMEFB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		} else if (mode.equals("neft")) {
			String prefix = "SMEMB";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		} else if (mode.equals("imps")) {
			String prefix = "SMEIM";
			DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("yy");
			String year = input.format(customDateFormatter);
			String yearofday = String.valueOf(input.getDayOfYear());
			refNo = prefix.concat(year).concat(yearofday).concat(s);
		}
		return refNo;
	}
	
	public static String minutesToDaysHoursMinutes(int time) {
        Duration d = Duration.ofMinutes(time);
        long days = d.toDays();
        long hours = d.toHours() % 24;
        long minutes = d.toMinutes() % 60;
        return String.format("%d_%d_%d", days, hours, minutes);
    }
	
	public boolean validDailyTransaction(String amount, String enterpriseId) {
		return dailyTransactionLimitExceeded(amount, enterpriseId);
	}

	public boolean validMonthlyTransaction(String amount, String enterpriseId) {
		return monthlyTransactionLimitExceeded(amount, enterpriseId);
	}

	public boolean dailyTransactionLimitExceeded(String amount, String enterpriseId) {
		Long totalLimitDaily = 0L;
		Long currentLimit = 0L;
		Optional<TransactionLimit> transactionDailyLimit = null;
		boolean validDailyTransaction = false;

		transactionDailyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId,
				"daily");
		if (transactionDailyLimit.isPresent()) {
			totalLimitDaily = transactionDailyLimit.get().getAmount().equals("0") ? Long.valueOf(transactionDaily)
					: Long.valueOf(transactionDailyLimit.get().getAmount());
		} else {
			totalLimitDaily = Long.valueOf(transactionDaily);
		}

		if (totalLimitDaily != null) {
			currentLimit = approvedAmount(enterpriseId) + Long.valueOf(amount);
			validDailyTransaction = currentLimit > totalLimitDaily;
		} else {
			validDailyTransaction = false;
		}
		return validDailyTransaction;
	}

	public boolean monthlyTransactionLimitExceeded(String amount, String enterpriseId) {
		Long totalLimitMonthly = 0L;
		Long currentLimit = 0L;
		Optional<TransactionLimit> transactionMonthlyLimit = null;
		boolean validMonthlyTransaction = false;

		transactionMonthlyLimit = transactionLimitRepository.findFirstByEnterpriseIdAndModeOrderByIdDesc(enterpriseId,
				"monthly");
		if (transactionMonthlyLimit.isPresent()) {
			totalLimitMonthly = transactionMonthlyLimit.get().getAmount().equals("0") ? Long.valueOf(transactionMonthly)
					: Long.valueOf(transactionMonthlyLimit.get().getAmount());
		} else {
			totalLimitMonthly = Long.valueOf(transactionMonthly);
		}
		if (totalLimitMonthly != null) {
			currentLimit = totalAmountMonth(enterpriseId) + Long.valueOf(amount);
			validMonthlyTransaction = currentLimit > totalLimitMonthly;
		} else {
			validMonthlyTransaction = false;
		}
		return validMonthlyTransaction;
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