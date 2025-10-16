package com.grab.home_test.controller.models;

import com.grab.home_test.repo.models.Account;

import java.math.BigDecimal;

public class AccountResponse {

   private String id;
   private String name;
   private BigDecimal accountBalance;
   private String status;

   public static AccountResponse fromDB(Account account) {
      AccountResponse accountResponse = new AccountResponse();
      accountResponse.setId(account.getId());
      accountResponse.setName(account.getName());
      accountResponse.setAccountBalance(account.getActualBalance());
      accountResponse.setStatus(account.getStatus().name());
      return accountResponse;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public BigDecimal getAccountBalance() {
      return accountBalance;
   }

   public void setAccountBalance(BigDecimal accountBalance) {
      this.accountBalance = accountBalance;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }
}
