package com.ironhack.midtermproject.model.dto;

import java.math.BigDecimal;

public class BalanceDTO {

    private BigDecimal balance;
    private String monetType;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMonetType() {
        return monetType;
    }

    public void setMonetType(String monetType) {
        this.monetType = monetType;
    }

    public static BalanceDTO balanceToBalanceDTO(BigDecimal balance, String monetType) {
        BalanceDTO balanceDTO = new BalanceDTO();
            balanceDTO.setBalance(balance);
            balanceDTO.setMonetType(monetType);
        return balanceDTO;
    }
}
