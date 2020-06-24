package com.ironhack.midtermproject.util;

import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.enums.Status;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class Utils {

    public static boolean applyPenaltyFee(BigDecimal minimumBalance, BigDecimal balanceUpdate) {
        return (balanceUpdate.compareTo(minimumBalance) < 0) ? true : false;
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

    private boolean isFrozen(Checking checking) {
        return checking.getStatus().equals(Status.FROZEN) ? true : false;
    }
}
