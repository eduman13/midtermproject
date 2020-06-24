package com.ironhack.midtermproject.security;

import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.users.AccountHolder;
import com.ironhack.midtermproject.model.users.ThirdParty;
import com.ironhack.midtermproject.repository.CheckingRepository;
import com.ironhack.midtermproject.repository.CreditCardRepository;
import com.ironhack.midtermproject.repository.users.AccountHolderRepository;
import com.ironhack.midtermproject.repository.users.ThirdPartyRepository;
import com.ironhack.midtermproject.service.AccountHolderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class SecurityAspect {

    private static final Logger LOGGER = LogManager.getLogger(SecurityAspect.class);

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Before("execution(* com.ironhack.midtermproject.controller.AccountHolderController.*(..))")
    public void securityAccountHolder(JoinPoint joinPoint) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer id = joinPoint.getSignature().toShortString().contains("get_balance") ? (Integer) joinPoint.getArgs()[0] : (Integer) joinPoint.getArgs()[2];
        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(id);
        if (accountHolder.isPresent()) {
            if (!accountHolder.get().getUsername().equals(username)) {
                LOGGER.error("Access denied. User " + username + " has no access to account " + id);
                throw new RuntimeException("Access denied");
            }
        }
    }

    @Before("execution(* com.ironhack.midtermproject.controller.ThirdPartyController.*(..))")
    public void securityThirdParty(JoinPoint joinPoint) {
        Integer accountId = (Integer) joinPoint.getArgs()[0];
        String secretKey = (String) joinPoint.getArgs()[2];
        String hashKey = (String) joinPoint.getArgs()[4];
        ThirdParty thirdParty = thirdPartyRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (joinPoint.getSignature().toShortString().contains("Account")) {
            Checking checking = checkingRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account " + accountId + " does not exist"));
            if (!thirdParty.getHashKey().equals(hashKey) || !checking.getSecretKey().equals(secretKey) ) {
                LOGGER.error("Access denied. User " + thirdParty.getUsername() + " has no access to account " + accountId);
                throw new RuntimeException("Access denied");
            }
        } else {
            CreditCard creditCard = creditCardRepository.findById(accountId).orElseThrow(() -> new RuntimeException("CreditCard " + accountId + " does not exist"));
            if (!thirdParty.getHashKey().equals(hashKey) || !creditCard.getSecretKey().equals(secretKey) ) {
                LOGGER.error("Access denied. User " + thirdParty.getUsername() + " has no access to credit card " + accountId);
                throw new RuntimeException("Access denied");
            }
        }
    }
}
