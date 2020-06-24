package com.ironhack.midtermproject.model.dto;

import com.ironhack.midtermproject.model.CreditCard;

import java.math.BigDecimal;

public class CreditCardDTO {

    private BigDecimal balance;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;
    private BigDecimal penaltyFee;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public static CreditCardDTO creditCardToCreditCardDTO(CreditCard creditCard) {
        CreditCardDTO creditCardDTO = new CreditCardDTO();
            creditCardDTO.setBalance(creditCard.getBalance());
            creditCardDTO.setCreditLimit(creditCard.getCreditLimit());
            creditCardDTO.setInterestRate(creditCard.getInterestRate());
            creditCardDTO.setPenaltyFee(creditCard.getPenaltyFee());
        return creditCardDTO;
    }
}
