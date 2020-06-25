package com.ironhack.midtermproject.schedule;

import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Saving;
import com.ironhack.midtermproject.model.enums.Status;
import com.ironhack.midtermproject.model.fraud.AmountTransaction;
import com.ironhack.midtermproject.model.fraud.Transaction;
import com.ironhack.midtermproject.repository.CheckingRepository;
import com.ironhack.midtermproject.repository.CreditCardRepository;
import com.ironhack.midtermproject.repository.SavingRepository;
import com.ironhack.midtermproject.repository.fraud.AmountTransactionRepository;
import com.ironhack.midtermproject.repository.fraud.TransactionRepository;
import com.ironhack.midtermproject.security.EntryPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ScheduleTasks {

    private static final Logger LOGGER = LogManager.getLogger(ScheduleTasks.class);

    @Autowired
    SavingRepository savingRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AmountTransactionRepository amountTransactionRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Scheduled(cron="0 0 0 * * *")
    public void savingInteresteRate() {
        LOGGER.info("savingInteresteRate starts to run");
        LocalDate today = LocalDate.now();
        List<Saving> savings = savingRepository.findAll().stream().filter(saving -> {
            if (saving.getCreationDate().getDayOfMonth() - today.getDayOfMonth() == 0 &&
                    saving.getCreationDate().getMonthValue() - today.getMonthValue() == 0 &&
                    today.getYear() - saving.getCreationDate().getYear() >= 1) {
                BigDecimal interest = saving.getBalance().multiply(saving.getInterestRate()).setScale(2, RoundingMode.HALF_EVEN);
                saving.setBalance(interest.add(saving.getBalance()));
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        savingRepository.saveAll(savings);
        LOGGER.info("savingInteresteRate finish to run");
    }

    @Scheduled(cron="0 0 0 * * *")
    public void creditCardInterestRate() {
        LOGGER.info("creditCardInterestRate starts to run");
        LocalDate today = LocalDate.now();
        List<CreditCard> creditCards = creditCardRepository.findAll().stream().filter(creditCard -> {
            if (creditCard.getCreationDate().getDayOfMonth() - today.getDayOfMonth() == 0 &&
                    creditCard.getCreationDate().getMonthValue() - today.getMonthValue() == 0 &&
                    today.getYear() - creditCard.getCreationDate().getYear() >= 1) {
                BigDecimal interestRatePerMonth = creditCard.getInterestRate().divide(new BigDecimal("12")).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal interest = creditCard.getBalance().multiply(interestRatePerMonth).setScale(2, RoundingMode.HALF_EVEN);
                creditCard.setBalance(interest.add(creditCard.getBalance()));
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        creditCardRepository.saveAll(creditCards);
        LOGGER.info("creditCardInterestRate finish to run");
    }

    @Scheduled(cron="0 0 0 * * *")
    public void fraudDetection24Hours() {
        LOGGER.info("fraudDetection24Hours starts to run");
        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> LocalDate.now().isEqual(transaction.getTimestamp().toLocalDate()))
                .collect(Collectors.toList());
        Map<Integer, Double> decimalMap = transactionList.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId, Collectors.summingDouble(value -> value.getTransactionAmount().doubleValue())));
        decimalMap.forEach((accountId, amount) -> {
            System.out.println("id " + accountId);
            System.out.println("amount " + amount);
            Optional<AmountTransaction> amountTransaction = amountTransactionRepository.findById(accountId);
            if (amountTransaction.isPresent()) {
                BigDecimal amountFraud = amountTransaction.get().getAmount().multiply(new BigDecimal("1.5")).setScale(2, RoundingMode.HALF_EVEN);
                if (amountFraud.compareTo(new BigDecimal(amount)) > 0) {
                    Optional<Checking> checking = checkingRepository.findById(accountId);
                    if (checking.isPresent()) {
                        LOGGER.error("Fraud detected on Account " + accountId);
                        checking.get().setStatus(Status.FROZEN);
                        checkingRepository.save(checking.get());
                    }
                } else {
                    if (new BigDecimal(amount).compareTo(amountTransaction.get().getAmount()) > 0) {
                        amountTransaction.get().setAmount(new BigDecimal(amount));
                        amountTransactionRepository.save(amountTransaction.get());
                    }
                }
            } else {
                AmountTransaction amountTransaction1 = new AmountTransaction();
                    amountTransaction1.setAccountId(accountId);
                    amountTransaction1.setAmount(new BigDecimal(amount));
                amountTransactionRepository.save(amountTransaction1);
            }
        });
        LOGGER.info("fraudDetection24Hours finish to run");
    }
}
