package com.ironhack.midtermproject.controller;

import com.ironhack.midtermproject.model.dto.BalanceDTO;
import com.ironhack.midtermproject.service.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @PatchMapping("/third_party/credit_account/{id_account}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO creditAccount(@PathVariable("id_account") Integer idAccount,
                                    @RequestParam BigDecimal amount,
                                    @RequestParam("secret_key") String secretKey,
                                    @RequestParam(name="money_type", defaultValue="USD", required=false) String moneyType,
                                    @RequestHeader("hash_key") String hash_key) {
        return thirdPartyService.creditAccount(idAccount, amount, moneyType);
    }

    @PatchMapping("/third_party/debt_account/{id_account}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO debtAccount(@PathVariable("id_account") Integer idAccount,
                              @RequestParam BigDecimal amount,
                              @RequestParam("secret_key") String secretKey,
                              @RequestParam(name="money_type", defaultValue="USD", required=false) String moneyType,
                              @RequestHeader("hash_key") String hash_key) {
        return thirdPartyService.debtAccount(idAccount, amount, moneyType);
    }

    @PatchMapping("/third_party/credit_credit_card/{id_account}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO creditCreditCard(@PathVariable("id_account") Integer idAccount,
                              @RequestParam BigDecimal amount,
                              @RequestParam("secret_key") String secretKey,
                              @RequestParam(name="money_type", defaultValue="USD", required=false) String moneyType,
                              @RequestHeader("hash_key") String hash_key) {
        return thirdPartyService.creditCreditCard(idAccount, amount, moneyType);
    }

    @PatchMapping("/third_party/debt_credit_card/{id_account}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO debtCreditCard(@PathVariable("id_account") Integer idAccount,
                              @RequestParam BigDecimal amount,
                              @RequestParam("secret_key") String secretKey,
                              @RequestParam(name="money_type", defaultValue="USD", required=false) String moneyType,
                              @RequestHeader("hash_key") String hash_key) {
        return thirdPartyService.debtCreditCard(idAccount, amount, moneyType);
    }
}
