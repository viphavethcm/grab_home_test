package com.grab.home_test.repo.models;

import com.grab.home_test.enums.AccountStatus;
import com.grab.home_test.enums.PaymentMethod;
import com.grab.home_test.controller.models.AccountCreationRequest;
import com.grab.home_test.service.VersionControl;

import java.math.BigDecimal;
import java.util.UUID;

public class Account implements VersionControl {
   public static final String ACCOUNT_ID_FORMAT = "account:%s";

   private String id;
   private String name;
   private BigDecimal actualBalance;
   private AccountStatus status;
   private int version = 0;

   public static Account initialize(AccountCreationRequest accountCreationRequest) {
      Account account = new Account();
      account.setId(UUID.randomUUID().toString());
      account.setName(accountCreationRequest.getName());
      account.setActualBalance(accountCreationRequest.getBalance());
      account.setStatus(AccountStatus.ACTIVE);
      account.setVersion(0);
      return account;
   }

   public boolean isAccountActive() {
      return AccountStatus.ACTIVE == this.status;
   }

   public void updateBalance(BigDecimal amount, PaymentMethod paymentMethod) {
      if (PaymentMethod.DEBIT == paymentMethod) {
         this.actualBalance = this.getActualBalance().subtract(amount);
      }
      else if (PaymentMethod.CREDIT == paymentMethod) {
         this.actualBalance = this.getActualBalance().add(amount);
      }
      this.version += 1;
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

   public BigDecimal getActualBalance() {
      return actualBalance;
   }

   public void setActualBalance(BigDecimal actualBalance) {
      this.actualBalance = actualBalance;
   }

   public AccountStatus getStatus() {
      return status;
   }

   public void setStatus(AccountStatus status) {
      this.status = status;
   }

   @Override
   public int getVersion() {
      return version;
   }

   @Override
   public void setVersion(int version) {
      this.version = version;
   }
}
