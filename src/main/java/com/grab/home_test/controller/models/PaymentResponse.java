package com.grab.home_test.controller.models;

import com.grab.home_test.repo.models.PaymentOrder;

public class PaymentResponse {
   private String paymentOrderId;
   private String paymentStatus;
   private AccountResponse fromAccount;
   private AccountResponse toAccount;

   public static PaymentResponse fromPaymentOrder(PaymentOrder paymentOrder, AccountResponse fromAccount, AccountResponse toAccount) {
      PaymentResponse paymentResponse = new PaymentResponse();
      paymentResponse.setPaymentOrderId(paymentOrder.getPaymentOrderId());
      paymentResponse.setPaymentStatus(paymentOrder.getPaymentStatus());
      paymentResponse.setFromAccount(fromAccount);
      paymentResponse.setToAccount(toAccount);
      return paymentResponse;
   }

   public String getPaymentOrderId() {
      return paymentOrderId;
   }

   public void setPaymentOrderId(String paymentOrderId) {
      this.paymentOrderId = paymentOrderId;
   }

   public String getPaymentStatus() {
      return paymentStatus;
   }

   public void setPaymentStatus(String paymentStatus) {
      this.paymentStatus = paymentStatus;
   }

   public AccountResponse getFromAccount() {
      return fromAccount;
   }

   public void setFromAccount(AccountResponse fromAccount) {
      this.fromAccount = fromAccount;
   }

   public AccountResponse getToAccount() {
      return toAccount;
   }

   public void setToAccount(AccountResponse toAccount) {
      this.toAccount = toAccount;
   }
}
