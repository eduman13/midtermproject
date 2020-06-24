package com.ironhack.midtermproject.controller;

import com.ironhack.midtermproject.exception.IdNotFoundException;
import com.ironhack.midtermproject.exception.InsufficientQuantityException;
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
    public BigDecimal getBalance(@PathVariable Integer id) throws IdNotFoundException {
        return accountHolderService.getBalance(id);
    }

    @PatchMapping("/account_holder/transfer_account/{id_owner}/{id_transfer_account}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void creditAccount(@PathVariable("id_transfer_account") Integer idTransferAccount,
                              @RequestParam BigDecimal amount,
                              @PathVariable("id_owner") Integer idOwner,
                              @RequestParam String beneficiaryName) throws IdNotFoundException, InsufficientQuantityException {
        accountHolderService.creditAccount(idTransferAccount, amount, idOwner, beneficiaryName);
    }
}
