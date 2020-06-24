package com.ironhack.midtermproject.model.dto;

import com.ironhack.midtermproject.model.Saving;
import com.ironhack.midtermproject.model.enums.Status;

import java.math.BigDecimal;

public class SavingDTO {

    private BigDecimal balance;
    private String secreKey;
    private BigDecimal minimumBalance;
    private BigDecimal penaltyFee;
    private Status status;
    private BigDecimal interestRate;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecreKey() {
        return secreKey;
    }

    public void setSecreKey(String secreKey) {
        this.secreKey = secreKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public static SavingDTO savingToSavingDTO(Saving saving) {
        SavingDTO savingDTO = new SavingDTO();
            savingDTO.setBalance(saving.getBalance());
            savingDTO.setSecreKey(saving.getSecretKey());
            savingDTO.setMinimumBalance(saving.getMinimumBalance());
            savingDTO.setPenaltyFee(saving.getPenaltyFee());
            savingDTO.setStatus(saving.getStatus());
            savingDTO.setInterestRate(saving.getInterestRate());
        return savingDTO;
    }
}
