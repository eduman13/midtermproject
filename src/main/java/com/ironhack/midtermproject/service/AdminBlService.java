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
    public CheckingDTO createChecking(Integer accountHolderId, Checking checking) throws RuntimeException {
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
            if (Optional.ofNullable(accountHolder.getSecondaryChecking()).isEmpty()) {
                if (accountHolderId == accountHolder.getId()) {
                    throw new RuntimeException("Account Holder " + accountHolderId + " is already the primaryOwner");
                }
                Checking checkingSave = new Checking(checking);
                    checkingSave.setSecondaryOwner(accountHolder);
                return CheckingDTO.checkingToCheckingDTO(checkingRepository.save(checkingSave));
            } else {
                throw new RuntimeException("Account Holder does have two accounts associated");
            }
        }
    }

    @Transactional
    public SavingDTO createSaving(Integer accountHolderId, Saving saving) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountHolderId, "create");
        if (Optional.ofNullable(accountHolder.getPrimaryChecking()).isEmpty()) {
            Saving savingSave = new Saving(saving);
                savingSave.setPrimaryOwner(accountHolder);
            return SavingDTO.savingToSavingDTO(savingRepository.save(savingSave));
        }
        if (Optional.ofNullable(accountHolder.getSecondaryChecking()).isEmpty()) {
            if (accountHolderId == accountHolder.getId()) {
                throw new RuntimeException("Account Holder " + accountHolderId + " is already the primaryOwner");
            }
            Saving savingSave = new Saving(saving);
                savingSave.setSecondaryOwner(accountHolder);
            return SavingDTO.savingToSavingDTO(savingRepository.save(savingSave));
        } else {
            throw new RuntimeException("Account Holder does have two accounts associated");
        }
    }

    @Transactional
    public CreditCardDTO createCreditCard(Integer accountHolderId, CreditCard creditCard) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountHolderId, "create");
        if (Optional.ofNullable(accountHolder.getPrimarycreditCard()).isEmpty()) {
            CreditCard creditCardSave = new CreditCard(creditCard);
                creditCardSave.setPrimaryOwner(accountHolder);
            return CreditCardDTO.creditCardToCreditCardDTO(creditCardRepository.save(creditCardSave));
        } else {
            if (Optional.ofNullable(accountHolder.getSecondcreditCard()).isEmpty()) {
                if (accountHolderId == accountHolder.getId()) {
                    throw new RuntimeException("Account Holder " + accountHolderId + " is already the primaryOwner");
                }
                CreditCard creditCardSave = new CreditCard(creditCard);
                    creditCardSave.setSecondaryHolder(accountHolder);
                return CreditCardDTO.creditCardToCreditCardDTO(creditCardRepository.save(creditCardSave));
            } else {
                throw new RuntimeException("Account Holder does have two credit card associated");
            }
        }
    }

    public CreditCard findById(Integer id) throws Exception {
        return creditCardRepository.findById(id).orElseThrow(Exception::new);
    }

    @Transactional(readOnly=true)
    public BalanceDTO getBalance(Integer accountId) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "get");
        return BalanceDTO.balanceToBalanceDTO(accountHolder.getPrimaryChecking().getBalance(), accountHolder.getPrimaryChecking().getMoneyType());
    }

    @Transactional
    public void creditAccount(Integer accountId, BigDecimal amount, String moneyType) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "creditAccount");
        BigDecimal balanceUpdate = Utils.debtCreditTransaction("credit", amount, moneyType, accountHolder.getPrimaryChecking());
        accountHolder.getPrimaryChecking().setBalance(balanceUpdate);
        checkingRepository.save(accountHolder.getPrimaryChecking());
    }

    @Transactional
    public void debtAccount(Integer accountId, BigDecimal amount, String moneyType) {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "debtAccount");
        BigDecimal balanceUpdate = Utils.debtCreditTransaction("debt", amount, moneyType, accountHolder.getPrimaryChecking());
        accountHolder.getPrimaryChecking().setBalance(balanceUpdate);
        if (Utils.applyPenaltyFee(accountHolder.getPrimaryChecking().getMinimumBalance(), balanceUpdate)) {
            LOGGER.info("penaltyFee applied to account " + accountId);
            balanceUpdate = balanceUpdate.subtract(accountHolder.getPrimaryChecking().getPenaltyFee());
        }
        checkingRepository.save(accountHolder.getPrimaryChecking());
    }

    @Transactional
    public void creditCreditCard(Integer accountId, BigDecimal amount, String moneyType) throws RuntimeException {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "creditCreditCard");
        BigDecimal balanceUpdate = Utils.debtCreditTransactionCreditCard("credit", amount, moneyType, accountHolder.getPrimarycreditCard());
        accountHolder.getPrimarycreditCard().setBalance(balanceUpdate);
        creditCardRepository.save(accountHolder.getPrimarycreditCard());
    }

    @Transactional
    public void debtCreditCard(Integer accountId, BigDecimal amount, String moneyType) {
        AccountHolder accountHolder = findByIdAccountHolder(accountId, "debtCreditCard");
        if (amount.compareTo(accountHolder.getPrimarycreditCard().getCreditLimit()) > 0) {
            LOGGER.info("credit limit exceeded on account " + accountId);
            throw new RuntimeException("creditLimit Exceeded");
        }
        BigDecimal balanceUpdate = Utils.debtCreditTransactionCreditCard("debt", amount, moneyType, accountHolder.getPrimarycreditCard());
        accountHolder.getPrimarycreditCard().setBalance(balanceUpdate);
        creditCardRepository.save(accountHolder.getPrimarycreditCard());
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
