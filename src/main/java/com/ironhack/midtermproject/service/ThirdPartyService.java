package com.ironhack.midtermproject.service;

import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.StudentChecking;
import com.ironhack.midtermproject.model.dto.BalanceDTO;
import com.ironhack.midtermproject.repository.CheckingRepository;
import com.ironhack.midtermproject.repository.CreditCardRepository;
import com.ironhack.midtermproject.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ThirdPartyService {

    private static final Logger LOGGER = LogManager.getLogger(ThirdPartyService.class);

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    public BalanceDTO creditAccount(Integer accountId, BigDecimal amount, String moneyType) {
        Checking checking = checkingRepository.findByIdWithoutOptional(accountId);
        Utils.isFrozen(checking);
        BigDecimal balanceUpdate = Utils.debtCreditTransaction("credit", amount, moneyType, checking);
        checking.setBalance(balanceUpdate);
        checkingRepository.save(checking);
        return BalanceDTO.balanceToBalanceDTO(checking.getBalance(), checking.getMoneyType());
    }

    public BalanceDTO debtAccount(Integer accountId, BigDecimal amount, String moneyType) {
        Checking checking = checkingRepository.findByIdWithoutOptional(accountId);
        Utils.isFrozen(checking);
        BigDecimal balanceUpdate = Utils.debtCreditTransaction("debt", amount, moneyType, checking);
        if (Utils.applyPenaltyFee(Optional.ofNullable(checking.getMinimumBalance()), balanceUpdate)) {
            LOGGER.info("penaltyFee applied to account " + accountId);
            balanceUpdate = balanceUpdate.subtract(checking.getPenaltyFee());
        }
        checking.setBalance(balanceUpdate);
        checkingRepository.save(checking);
        return BalanceDTO.balanceToBalanceDTO(checking.getBalance(), checking.getMoneyType());
    }

    public BalanceDTO creditCreditCard(Integer creditCardId, BigDecimal amount, String moneyType) {
        CreditCard creditCard = creditCardRepository.findByIdWithoutOptional(creditCardId);
        BigDecimal balanceUpdate = Utils.debtCreditTransactionCreditCard("credit", amount, moneyType, creditCard);
        creditCard.setBalance(balanceUpdate);
        creditCardRepository.save(creditCard);
        return BalanceDTO.balanceToBalanceDTO(creditCard.getBalance(), creditCard.getMoneyType());
    }

    public BalanceDTO debtCreditCard(Integer creditCardId, BigDecimal amount, String moneyType) {
        CreditCard creditCard = creditCardRepository.findByIdWithoutOptional(creditCardId);
        if (amount.compareTo(creditCard.getCreditLimit()) > 0) {
            throw new RuntimeException("creditLimit Exceeded");
        }
        BigDecimal balanceUpdate = Utils.debtCreditTransactionCreditCard("debt", amount, moneyType, creditCard);
        creditCard.setBalance(balanceUpdate);
        creditCardRepository.save(creditCard);
        return BalanceDTO.balanceToBalanceDTO(creditCard.getBalance(), creditCard.getMoneyType());
    }
}
