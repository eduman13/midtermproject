package com.ironhack.midtermproject.service;

import com.ironhack.midtermproject.model.*;
import com.ironhack.midtermproject.model.dto.BalanceDTO;
import com.ironhack.midtermproject.model.dto.CheckingDTO;
import com.ironhack.midtermproject.model.dto.CreditCardDTO;
import com.ironhack.midtermproject.model.dto.SavingDTO;
import com.ironhack.midtermproject.model.enums.Status;
import com.ironhack.midtermproject.model.users.AccountHolder;
import com.ironhack.midtermproject.model.users.ThirdParty;
import com.ironhack.midtermproject.repository.*;
import com.ironhack.midtermproject.repository.security.RoleRepository;
import com.ironhack.midtermproject.repository.users.AccountHolderRepository;
import com.ironhack.midtermproject.repository.users.ThirdPartyRepository;
import com.ironhack.midtermproject.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class AdminBlService {

    private static final Logger LOGGER = LogManager.getLogger(AdminBlService.class);

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingRepository savingRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    RoleRepository roleRepository;

    @Transactional
    public CheckingDTO createCheckingPrimaryOwner(Integer accountHolderId, Checking checking) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountHolderId, "create");
        if (Optional.ofNullable(accountHolder.getPrimaryChecking()).isEmpty()) {
            if (isUnder24(accountHolder)) {
                StudentChecking studentChecking = StudentChecking.checkingToStudentChecking(checking);
                    studentChecking.setPrimaryOwner(accountHolder);
                return CheckingDTO.studentCheckingToCheckingDTO(studentCheckingRepository.save(studentChecking));
            } else {
                Checking checkingSave = new Checking(checking);
                    checkingSave.setPrimaryOwner(accountHolder);
                return CheckingDTO.checkingToCheckingDTO(checkingRepository.save(checkingSave));
            }
        } else {
            throw new RuntimeException("AccountHolder " + accountHolder.getId() +  " has already a primary Account");
        }
    }

    @Transactional
    public void putCheckingSecondaryOwner(Integer accountHolderId, Integer creditCardId) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountHolderId, "create");
        if (Optional.ofNullable(accountHolder.getSecondaryChecking()).isEmpty()) {
            Checking checking = checkingRepository.findById(creditCardId).orElseThrow(() ->
                    new RuntimeException("Account does not exist"));
            if (accountHolderId == checking.getPrimaryOwner().getId()) {
                throw new RuntimeException("Primary owner and second owner are the same AccountHolder");
            }
            if (Optional.ofNullable(checking.getSecondaryOwner()).isEmpty()) {
                checking.setSecondaryOwner(accountHolder);
                accountHolder.setSecondaryChecking(checking);
                checkingRepository.save(checking);
            } else {
                throw new RuntimeException("Account " + checking.getId() + " has already a secondary owner");
            }
        } else {
            throw new RuntimeException("AccountHolder " + accountHolder.getId() +  " has already a secondary Account");
        }
    }

    @Transactional
    public SavingDTO createSaving(Integer accountHolderId, Saving saving) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountHolderId, "create");
        if (Optional.ofNullable(accountHolder.getPrimaryChecking()).isEmpty()) {
            Saving savingSave = new Saving(saving);
                savingSave.setPrimaryOwner(accountHolder);
            return SavingDTO.savingToSavingDTO(savingRepository.save(savingSave));
        } else {
            throw new RuntimeException("AccountHolder " + accountHolder.getId() +  " has already a primary Account");
        }
    }

    @Transactional
    public CreditCardDTO createCreditCard(Integer accountHolderId, CreditCard creditCardId) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountHolderId, "create");
        if (Optional.ofNullable(accountHolder.getPrimarycreditCard()).isEmpty()) {
            CreditCard creditCardSave = new CreditCard(creditCardId);
                creditCardSave.setPrimaryOwner(accountHolder);
            return CreditCardDTO.creditCardToCreditCardDTO(creditCardRepository.save(creditCardSave));
        } else {
            throw new RuntimeException("AccountHolder " + accountHolder.getId() +  " has already a CreditCard");
        }
    }

    @Transactional
    public void putCreditCardSecondaryOwner(Integer accountHolderId, Integer creditCardId) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountHolderId, "create");
        if (Optional.ofNullable(accountHolder.getSecondcreditCard()).isEmpty()) {
            CreditCard creditCard = creditCardRepository.findById(creditCardId).orElseThrow(() ->
                    new RuntimeException("CreditCard does not exist"));
            if (accountHolderId == creditCard.getPrimaryOwner().getId()) {
                throw new RuntimeException("Primary owner and second owner are the same AccountHolder");
            }
            if (Optional.ofNullable(creditCard.getSecondaryOwner()).isEmpty()) {
                creditCard.setSecondaryOwner(accountHolder);
                accountHolder.setSecondcreditCard(creditCard);
                creditCardRepository.save(creditCard);
            } else {
                throw new RuntimeException("CreditCard " + creditCard.getId() + " has already a secondary owner");
            }
        } else {
            throw new RuntimeException("AccountHolder " + accountHolder.getId() +  " has already a secondary CreditCard");
        }
    }

    @Transactional(readOnly=true)
    public BalanceDTO getBalance(Integer accountId) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "Account");
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimaryChecking().getBalance(), accountHolder.getPrimaryChecking().getMoneyType());
    }

    @Transactional(readOnly=true)
    public BalanceDTO getBalanceByCreditCard(Integer creditCardId) {
        AccountHolder accountHolder = findByIdAccountHolder(creditCardId, "CreditCard");
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimarycreditCard().getBalance(), accountHolder.getPrimarycreditCard().getMoneyType());
    }

    @Transactional
    public BalanceDTO creditAccount(Integer accountId, BigDecimal amount, String moneyType) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "Account");
        BigDecimal balanceUpdate = Utils.debtCreditTransaction("credit", amount, moneyType, accountHolder.getPrimaryChecking());
        accountHolder.getPrimaryChecking().setBalance(balanceUpdate);
        checkingRepository.save(accountHolder.getPrimaryChecking());
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimaryChecking().getBalance(), accountHolder.getPrimaryChecking().getMoneyType());
    }

    @Transactional
    public BalanceDTO debtAccount(Integer accountId, BigDecimal amount, String moneyType) {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "Account");
        BigDecimal balanceUpdate = Utils.debtCreditTransaction("debt", amount, moneyType, accountHolder.getPrimaryChecking());
        accountHolder.getPrimaryChecking().setBalance(balanceUpdate);
        if (Utils.applyPenaltyFee(Optional.ofNullable(accountHolder.getPrimaryChecking().getMinimumBalance()), balanceUpdate)) {
            LOGGER.info("penaltyFee applied to account " + accountId);
            balanceUpdate = balanceUpdate.subtract(accountHolder.getPrimaryChecking().getPenaltyFee());
            accountHolder.getPrimaryChecking().setBalance(balanceUpdate);
        }
        checkingRepository.save(accountHolder.getPrimaryChecking());
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimaryChecking().getBalance(), accountHolder.getPrimaryChecking().getMoneyType());
    }

    @Transactional
    public BalanceDTO creditCreditCard(Integer accountId, BigDecimal amount, String moneyType) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "CreditCard");
        BigDecimal balanceUpdate = Utils.debtCreditTransactionCreditCard("credit", amount, moneyType, accountHolder.getPrimarycreditCard());
        accountHolder.getPrimarycreditCard().setBalance(balanceUpdate);
        creditCardRepository.save(accountHolder.getPrimarycreditCard());
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimarycreditCard().getBalance(), accountHolder.getPrimarycreditCard().getMoneyType());
    }

    @Transactional
    public BalanceDTO debtCreditCard(Integer accountId, BigDecimal amount, String moneyType) {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "CreditCard");
        if (amount.compareTo(accountHolder.getPrimarycreditCard().getCreditLimit()) > 0) {
            LOGGER.info("credit limit exceeded on account " + accountId);
            throw new RuntimeException("creditLimit Exceeded");
        }
        BigDecimal balanceUpdate = Utils.debtCreditTransactionCreditCard("debt", amount, moneyType, accountHolder.getPrimarycreditCard());
        accountHolder.getPrimarycreditCard().setBalance(balanceUpdate);
        creditCardRepository.save(accountHolder.getPrimarycreditCard());
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimarycreditCard().getBalance(), accountHolder.getPrimarycreditCard().getMoneyType());
    }

    public ThirdParty createThirdParty(ThirdParty thirdParty) {
        ThirdParty thirdPartySave = new ThirdParty(thirdParty);
        thirdPartySave.getRoles().forEach(role -> {
            role.setUser(thirdPartySave);
        });
        roleRepository.saveAll(thirdParty.getRoles());
        return thirdPartyRepository.save(thirdPartySave);
    }

    private AccountHolder findByIdAccountHolder(Integer id, String operation) {
        AccountHolder accountHolder = accountHolderRepository.findById(id).orElseThrow(() -> new RuntimeException("Primary Owner does not exist (id: " + id + ")"));
        if (operation.contains("CreditCard") && Optional.ofNullable(accountHolder.getPrimarycreditCard()).isEmpty()) {
            LOGGER.error("Fail in transaction. Account Holder " + id + " does not have creditCard");
            throw new RuntimeException("Account Holder " + id + " does not have creditCard");
        }
        if (operation.contains("Account") && Optional.ofNullable(accountHolder.getPrimaryChecking()).isEmpty()) {
            LOGGER.error("Fail in transaction. Account " + id + " does not have Account");
            throw new RuntimeException("Account Holder " + id + " does not have Account");
        }
        if (operation.contains("Account") && accountHolder.getPrimaryChecking().getStatus().equals(Status.FROZEN)) {
            LOGGER.error("Fail in transaction. Account " + id + " is Frozen");
            throw new RuntimeException("Account " + id + " is Frozen");
        }
        return accountHolder;
    }

    private boolean isUnder24(AccountHolder accountHolder) {
        return (LocalDate.now().getYear() - accountHolder.getBirthday().getYear() < 24) ? true : false;
    }
}
