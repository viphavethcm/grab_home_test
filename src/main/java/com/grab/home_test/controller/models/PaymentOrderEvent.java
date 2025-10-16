package com.grab.home_test.controller.models;

import com.grab.home_test.repo.models.PaymentOrder;

import java.util.UUID;

public class PaymentOrderEvent {
   private String eventId;
   private String paymentOrderId;
   private int version;

   public static PaymentOrderEvent fromPaymentOrder(PaymentOrder paymentOrder) {
      PaymentOrderEvent paymentOrderEvent = new PaymentOrderEvent();
      paymentOrderEvent.setEventId(UUID.randomUUID().toString());
      paymentOrderEvent.setPaymentOrderId(paymentOrder.getPaymentOrderId());
      paymentOrderEvent.setVersion(paymentOrder.getVersion());
      return paymentOrderEvent;
   }

   public String getEventId() {
      return eventId;
   }

   public void setEventId(String eventId) {
      this.eventId = eventId;
   }

   public String getPaymentOrderId() {
      return paymentOrderId;
   }

   public void setPaymentOrderId(String paymentOrderId) {
      this.paymentOrderId = paymentOrderId;
   }

   public int getVersion() {
      return version;
   }

   public void setVersion(int version) {
      this.version = version;
   }
}
