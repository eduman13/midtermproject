package com.ironhack.midtermproject.controller;

import com.ironhack.midtermproject.model.dto.BalanceDTO;
import com.ironhack.midtermproject.service.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AccountHolderController {

    @Autowired
    AccountHolderService accountHolderService;

    @GetMapping("/account_holder/get_balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO getBalance(@PathVariable Integer id) {
        return accountHolderService.getBalance(id);
    }

    @PatchMapping("/account_holder/transfer_account/{id_owner}/{id_transfer_account}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO creditAccount(@PathVariable("id_transfer_account") Integer idTransferAccount,
                              @RequestParam BigDecimal amount,
                              @PathVariable("id_owner") Integer idOwner,
                              @RequestParam String beneficiaryName) {
        return accountHolderService.creditAccount(idTransferAccount, amount, idOwner, beneficiaryName);
    }
}
