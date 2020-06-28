package com.ironhack.midtermproject.fraud;

import com.ironhack.midtermproject.model.fraud.Transaction;
import com.ironhack.midtermproject.repository.fraud.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Aspect
@Component
public class FraudAspect {

    private static final Logger LOGGER = LogManager.getLogger(FraudAspect.class);

    @Autowired
    TransactionRepository transactionRepository;

    @Before(value="execution(public * com.ironhack.midtermproject.service.AdminBlService.*(..)) ||" +
            "execution(public * com.ironhack.midtermproject.service.ThirdPartyService.*(..)) ||" +
            "execution(public * com.ironhack.midtermproject.service.AccountHolderService.creditAccount(..))")
    public void detectFraudBySeconds(JoinPoint joinPoint) {
        if (joinPoint.getSignature().getName().contains("debtAccount") || joinPoint.getSignature().getName().contains("creditAccount")) {
            Integer id = (Integer) joinPoint.getArgs()[0];
            LocalDateTime now = LocalDateTime.now();
            if (transactionRepository.findAllByAccountId(id).stream().anyMatch(transaction ->
                    Duration.between(transaction.getTimestamp(), now).getSeconds() <= 1)) {
                LOGGER.error("Fraud detected on account " + id + ", more than one transaction per second");
                throw new RuntimeException("Fraud detected on account " + id);
            }
        }
    }

    @AfterReturning("execution(public * com.ironhack.midtermproject.service.AdminBlService.*(..)) ||" +
            "execution(public * com.ironhack.midtermproject.service.ThirdPartyService.*(..)) ||" +
            "execution(public * com.ironhack.midtermproject.service.AccountHolderService.*(..))")
    public void saveTransaction(JoinPoint joinPoint) {
        if (joinPoint.getSignature().getName().contains("debtAccount") || joinPoint.getSignature().getName().contains("creditAccount")) {
            Object[] args = joinPoint.getArgs();
            Integer accountId = (Integer) args[0];
            BigDecimal amount = (BigDecimal) args[1];
            Transaction transaction = new Transaction();
                transaction.setAccountId(accountId);
                transaction.setTimestamp(LocalDateTime.now());
                transaction.setTransactionAmount(amount);
            transactionRepository.save(transaction);
        }
    }
}
