package com.grab.home_test.controller;

import com.grab.home_test.controller.models.AccountCreationRequest;
import com.grab.home_test.controller.models.AccountResponse;
import com.grab.home_test.exception.DomainCode;
import com.grab.home_test.exception.DomainException;
import com.grab.home_test.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class AccountController {

   private final AccountService accountService;

   public AccountController(AccountService accountService) {
      this.accountService = accountService;
   }

   @GetMapping("/account/{accountId}")
   public AccountResponse getAccount(@PathVariable("accountId") String accountId) {
      if (accountId == null || accountId.isEmpty()) {
         throw new DomainException(DomainCode.BAD_REQUEST);
      }
      return this.accountService.getAccount(accountId);
   }

   @PostMapping("/account")
   public AccountResponse createAccount(@RequestBody AccountCreationRequest accountCreationRequest) {
      if (accountCreationRequest == null || accountCreationRequest.getBalance().compareTo(BigDecimal.ZERO) < 0) {
         throw new DomainException(DomainCode.BAD_REQUEST);
      }
      return this.accountService.createAccount(accountCreationRequest);
   }
}
