package com.ironhack.midtermproject.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.StudentChecking;
import com.ironhack.midtermproject.model.enums.Status;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckingDTO {

    private BigDecimal balance;
    private String secretKey;
    private BigDecimal minimumBalance;
    private BigDecimal penaltyFee;
    private BigDecimal monthlyMaintenanceFee;
    private Status status;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    public Status getStatus() {
        return status;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static CheckingDTO studentCheckingToCheckingDTO(StudentChecking studentChecking) {
        CheckingDTO checkingDTO = new CheckingDTO();
            checkingDTO.setBalance(studentChecking.getBalance());
            checkingDTO.setSecretKey(studentChecking.getSecretKey());
            checkingDTO.setStatus(studentChecking.getStatus());
            checkingDTO.setPenaltyFee(studentChecking.getPenaltyFee());
        return checkingDTO;
    }

    public static CheckingDTO checkingToCheckingDTO(Checking checking) {
        CheckingDTO checkingDTO = new CheckingDTO();
            checkingDTO.setBalance(checking.getBalance());
            checkingDTO.setSecretKey(checking.getSecretKey());
            checkingDTO.setMinimumBalance(checking.getMinimumBalance());
            checkingDTO.setPenaltyFee(checking.getPenaltyFee());
            checkingDTO.setMonthlyMaintenanceFee(checking.getMonthlyMaintenanceFee());
            checkingDTO.setStatus(checking.getStatus());
        return checkingDTO;
    }
}
