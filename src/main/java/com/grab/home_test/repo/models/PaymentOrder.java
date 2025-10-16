package com.grab.home_test.repo.models;

import com.grab.home_test.enums.PaymentMethod;
import com.grab.home_test.enums.PaymentStatus;
import com.grab.home_test.controller.models.PaymentRequest;
import com.grab.home_test.service.VersionControl;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentOrder implements VersionControl {

   private String paymentOrderId;
   private String fromAccountId;
   private String toAccountId;
   private PaymentMethod fromMethod;
   private PaymentMethod toMethod;
   private BigDecimal amount;
   private String paymentStatus;
   private int version;

   public static PaymentOrder fromRequest(PaymentRequest paymentRequest) {
      PaymentOrder paymentOrder = new PaymentOrder();
      paymentOrder.setPaymentOrderId(UUID.randomUUID().toString());
      paymentOrder.setFromAccountId(paymentRequest.getFromAccount().getAccountId());
      paymentOrder.setToAccountId(paymentRequest.getToAccount().getAccountId());
      paymentOrder.setFromMethod(paymentRequest.getFromAccount().getPaymentMethod());
      paymentOrder.setToMethod(paymentRequest.getToAccount().getPaymentMethod());
      paymentOrder.setAmount(paymentRequest.getAmount());
      paymentOrder.setPaymentStatus(PaymentStatus.INITIAL.name());
      paymentOrder.setVersion(1);
      return paymentOrder;
   }

   public void onUpdateProcessing() {
      this.paymentStatus = PaymentStatus.PROCESSING.name();
   }

   public void onFailed() {
      this.paymentStatus = PaymentStatus.FAILED.name();
   }

   public void onCompleted() {
      this.paymentStatus = PaymentStatus.COMPLETED.name();
   }

   public String getPaymentOrderId() {
      return paymentOrderId;
   }

   public void setPaymentOrderId(String paymentOrderId) {
      this.paymentOrderId = paymentOrderId;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public String getPaymentStatus() {
      return paymentStatus;
   }

   public void setPaymentStatus(String paymentStatus) {
      this.paymentStatus = paymentStatus;
   }

   public String getFromAccountId() {
      return fromAccountId;
   }

   public void setFromAccountId(String fromAccountId) {
      this.fromAccountId = fromAccountId;
   }

   public String getToAccountId() {
      return toAccountId;
   }

   public void setToAccountId(String toAccountId) {
      this.toAccountId = toAccountId;
   }

   public PaymentMethod getFromMethod() {
      return fromMethod;
   }

   public void setFromMethod(PaymentMethod fromMethod) {
      this.fromMethod = fromMethod;
   }

   public PaymentMethod getToMethod() {
      return toMethod;
   }

   public void setToMethod(PaymentMethod toMethod) {
      this.toMethod = toMethod;
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
