package com.ironhack.midtermproject.util;

import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.enums.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Optional;

public class Utils {

    private static final Logger LOGGER = LogManager.getLogger(Utils.class);

    public static boolean applyPenaltyFee(Optional<BigDecimal> minimumBalance, BigDecimal balanceUpdate) {
        if (minimumBalance.isEmpty()) {
            return false;
        }
        return (balanceUpdate.compareTo(minimumBalance.get()) < 0) ? true : false;
    }

    public static BigDecimal debtCreditTransaction(String operation, BigDecimal amount, String moneyType, Checking checking) {
        CurrencyUnit currencyUnitAccount = Monetary.getCurrency(checking.getMoneyType());
        MonetaryAmount monetaryAmountAccount = Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnitAccount).setNumber(checking.getBalance()).create();
        CurrencyUnit currencyUnitCredit = Monetary.getCurrency(moneyType);
        MonetaryAmount monetaryAmountCredit = Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnitCredit).setNumber(amount).create();
        MonetaryAmount balanceUpdate = operation.equals("credit") ? monetaryAmountAccount.add(monetaryAmountCredit) : monetaryAmountAccount.subtract(monetaryAmountCredit);
        return new BigDecimal(balanceUpdate.getNumber().toString());
    }

    public static BigDecimal debtCreditTransactionCreditCard(String operation, BigDecimal amount, String moneyType, CreditCard creditCard) {
        CurrencyUnit currencyUnitAccount = Monetary.getCurrency(creditCard.getMoneyType());
        MonetaryAmount monetaryAmountAccount = Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnitAccount).setNumber(creditCard.getBalance()).create();
        CurrencyUnit currencyUnitCredit = Monetary.getCurrency(moneyType);
        MonetaryAmount monetaryAmountCredit = Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnitCredit).setNumber(amount).create();
        MonetaryAmount balanceUpdate = operation.equals("credit") ? monetaryAmountAccount.add(monetaryAmountCredit) : monetaryAmountAccount.subtract(monetaryAmountCredit);
        return new BigDecimal(balanceUpdate.getNumber().toString());
    }

    public static void isFrozen(Checking checking) {
        if (checking.getStatus().equals(Status.FROZEN)) {
            LOGGER.error("Fail in transaction. Account " + checking.getId() + " is Frozen");
            throw new RuntimeException("Account " + checking.getId() + " is Frozen");
        }
    }
}
