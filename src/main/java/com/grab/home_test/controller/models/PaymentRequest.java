package com.grab.home_test.controller.models;

import com.grab.home_test.enums.PaymentMethod;

import java.math.BigDecimal;

public class PaymentRequest {

   private BigDecimal amount;
   private PaymentDetail fromAccount;
   private PaymentDetail toAccount;

   public class PaymentDetail {

      private String accountId;
      private PaymentMethod paymentMethod;

      public String getAccountId() {
         return accountId;
      }

      public void setAccountId(String accountId) {
         this.accountId = accountId;
      }

      public PaymentMethod getPaymentMethod() {
         return paymentMethod;
      }

      public void setPaymentMethod(PaymentMethod paymentMethod) {
         this.paymentMethod = paymentMethod;
      }
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public PaymentDetail getFromAccount() {
      return fromAccount;
   }

   public void setFromAccount(PaymentDetail fromAccount) {
      this.fromAccount = fromAccount;
   }

   public PaymentDetail getToAccount() {
      return toAccount;
   }

   public void setToAccount(PaymentDetail toAccount) {
      this.toAccount = toAccount;
   }

   public boolean validateRequest() {
      if (this.fromAccount == null || this.toAccount == null ||
            this.fromAccount.accountId.isEmpty() || this.toAccount.accountId.isEmpty()) {
         return false;
      }
      if (amount.compareTo(BigDecimal.ZERO) <= 0)
         return false;
      return true;
   }
}
