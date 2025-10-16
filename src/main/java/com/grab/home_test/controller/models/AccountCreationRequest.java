package com.grab.home_test.controller.models;

import java.math.BigDecimal;

public class AccountCreationRequest {
   private String name;
   private BigDecimal balance;

   public String getName() {
      return name;
   }

   public BigDecimal getBalance() {
      return balance;
   }

   public AccountCreationRequest(String name) {
      this.name = name;
   }
}
