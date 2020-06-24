package com.ironhack.midtermproject.service;

import com.ironhack.midtermproject.exception.IdNotFoundException;
import com.ironhack.midtermproject.exception.InsufficientQuantityException;
import com.ironhack.midtermproject.model.users.AccountHolder;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.repository.users.AccountHolderRepository;
import com.ironhack.midtermproject.repository.CheckingRepository;
import com.ironhack.midtermproject.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderService {

    private static final Logger LOGGER = LogManager.getLogger(AccountHolderService.class);

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Transactional(readOnly=true)
    public BigDecimal getBalance(Integer id) throws IdNotFoundException {
        AccountHolder accountHolder = accountHolderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Account Holder does not have account"));
        return Optional.ofNullable(accountHolder.getPrimaryChecking()).orElseThrow(() ->
                new RuntimeException("Account Holder " + id + " does not have any Account")).getBalance();
    }

    @Transactional
    public void creditAccount(Integer idTransfer, BigDecimal amount, Integer idOwner, String accountHolderTransfer) throws IdNotFoundException, InsufficientQuantityException {
        AccountHolder accountHolder = accountHolderRepository.findById(idOwner).orElseThrow(() ->
                new RuntimeException("Account Holder does not have account"));
        if (idTransfer == accountHolder.getPrimaryChecking().getId()) {
            LOGGER.error("Account Holder is the owner of the beneficiary account (id: " + idTransfer + ")");
            throw new RuntimeException("Account Holder is the owner of the beneficiary account");
        }
        BigDecimal balance = Optional.ofNullable(accountHolder.getPrimaryChecking()).orElseThrow(() ->
                new RuntimeException("Account Holder does not have account")).getBalance();
        if (balance.compareTo(amount) < 0) {
            LOGGER.error("Amount for transfer not enough. Amount: " + amount);
            throw new RuntimeException("Amount for transfer not enough");
        }
        Checking checking = checkingRepository.findById(idTransfer).orElseThrow(() ->
                new RuntimeException("Beneficiary account does not exist"));
        accountHolder.getPrimaryChecking().setBalance(accountHolder.getPrimaryChecking().getBalance().subtract(amount));
        checking.setBalance(checking.getBalance().add(amount));
        if (Utils.applyPenaltyFee(accountHolder.getPrimaryChecking().getMinimumBalance(), accountHolder.getPrimaryChecking().getBalance())) {
            accountHolder.getPrimaryChecking().setBalance(accountHolder.getPrimaryChecking().getBalance().subtract(accountHolder.getPrimaryChecking().getPenaltyFee()));
        }
        checkingRepository.saveAll(List.of(accountHolder.getPrimaryChecking(), checking));
    }
}
