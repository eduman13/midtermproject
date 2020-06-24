package com.ironhack.midtermproject.controller;

import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Saving;
import com.ironhack.midtermproject.model.users.ThirdParty;
import com.ironhack.midtermproject.model.dto.BalanceDTO;
import com.ironhack.midtermproject.model.dto.CheckingDTO;
import com.ironhack.midtermproject.model.dto.CreditCardDTO;
import com.ironhack.midtermproject.model.dto.SavingDTO;
import com.ironhack.midtermproject.service.AdminBlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AdminClController {

    @Autowired
    AdminBlService adminService;

    @PostMapping("/admin/create_checking/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingDTO createChecking(@PathVariable Integer id,
                                      @Validated @RequestBody Checking checking) {
        return adminService.createChecking(id, checking);

    }

    @PostMapping("/admin/create_saving/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingDTO createSaving(@PathVariable Integer id,
                                  @Validated @RequestBody Saving saving) {
        return adminService.createSaving(id, saving);
    }

    @PostMapping("/admin/create_credit_card/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardDTO createCreditCard(@PathVariable Integer id,
                                          @Validated @RequestBody CreditCard creditCard) {
        return adminService.createCreditCard(id, creditCard);
    }

    @GetMapping("/admin/find_all_credit_card/{id}")
    public CreditCard creditCard(@PathVariable Integer id) throws Exception {
        return adminService.findById(id);
    }

    @GetMapping("/admin/get_balance_by_account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO getBalance(@PathVariable ("id") Integer accountId) {
        return adminService.getBalance(accountId);
    }

    @PatchMapping("/admin/credit_account/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void creditAccount(@PathVariable ("id") Integer accountId,
                              @RequestParam BigDecimal amount,
                              @RequestParam(defaultValue="USD", required=false) String moneyType) {
        adminService.creditAccount(accountId, amount, moneyType);
    }

    @PatchMapping("/admin/debt_account/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void debtAccount(@PathVariable ("id") Integer accountId,
                            @RequestParam BigDecimal amount,
                            @RequestParam(defaultValue="USD", required=false) String moneyType) {
        adminService.debtAccount(accountId, amount, moneyType);
    }

    @PatchMapping("/admin/credit_credit_card/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void creditCreditCard(@PathVariable ("id") Integer accountId,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam(defaultValue="USD", required=false) String moneyType) {
        adminService.creditCreditCard(accountId, amount, moneyType);
    }

    @PatchMapping("/admin/debt_credit_card/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void debtCreditCard(@PathVariable ("id") Integer accountId,
                               @RequestParam BigDecimal amount,
                               @RequestParam(defaultValue="USD", required=false) String moneyType) {
        adminService.debtCreditCard(accountId, amount, moneyType);
    }

    @PostMapping("/admin/create_third_party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody ThirdParty thirdParty) {
        return adminService.createThirdParty(thirdParty);
    }

}
