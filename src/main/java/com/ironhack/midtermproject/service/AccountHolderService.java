package com.ironhack.midtermproject.service;

import com.ironhack.midtermproject.model.StudentChecking;
import com.ironhack.midtermproject.model.dto.BalanceDTO;
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
    public BalanceDTO getBalance(Integer id) {
        AccountHolder accountHolder = accountHolderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Account Holder does not exist"));
        Checking checking = Optional.ofNullable(accountHolder.getPrimaryChecking()).orElseThrow(() ->
                new RuntimeException("Account Holder " + id + " does not have any Account"));
        Utils.isFrozen(checking);
        return BalanceDTO.balanceToBalanceDTO(checking.getBalance(), checking.getMoneyType());
    }

    @Transactional
    public BalanceDTO creditAccount(Integer idTransfer, BigDecimal amount, Integer idOwner, String accountHolderTransfer) {
        AccountHolder accountHolder = accountHolderRepository.findById(idOwner).orElseThrow(() ->
                new RuntimeException("Account Holder does not exist"));
        BigDecimal balance = Optional.ofNullable(accountHolder.getPrimaryChecking()).orElseThrow(() ->
                new RuntimeException("Account Holder does not have account")).getBalance();
        if (idTransfer == accountHolder.getPrimaryChecking().getId()) {
            LOGGER.error("Account Holder is the owner of the beneficiary account (id: " + idTransfer + ")");
            throw new RuntimeException("Account Holder is the owner of the beneficiary account");
        }
        if (balance.compareTo(amount) < 0) {
            LOGGER.error("Amount for transfer not enough. Amount: " + amount);
            throw new RuntimeException("Amount for transfer not enough");
        }
        Checking checking = checkingRepository.findById(idTransfer).orElseThrow(() ->
                new RuntimeException("Beneficiary account does not exist"));
        accountHolder.getPrimaryChecking().setBalance(accountHolder.getPrimaryChecking().getBalance().subtract(amount));
        checking.setBalance(checking.getBalance().add(amount));
        if (Utils.applyPenaltyFee(Optional.ofNullable(accountHolder.getPrimaryChecking().getMinimumBalance()), accountHolder.getPrimaryChecking().getBalance())) {
            accountHolder.getPrimaryChecking().setBalance(accountHolder.getPrimaryChecking().getBalance().subtract(accountHolder.getPrimaryChecking().getPenaltyFee()));
        }
        checkingRepository.saveAll(List.of(accountHolder.getPrimaryChecking(), checking));
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimaryChecking().getBalance(), accountHolder.getPrimaryChecking().getMoneyType());
    }
}
