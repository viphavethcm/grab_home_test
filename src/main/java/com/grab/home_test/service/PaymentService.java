package com.grab.home_test.service;

import com.grab.home_test.controller.models.AccountResponse;
import com.grab.home_test.controller.models.PaymentOrderEvent;
import com.grab.home_test.controller.models.PaymentRequest;
import com.grab.home_test.controller.models.PaymentResponse;
import com.grab.home_test.exception.DomainCode;
import com.grab.home_test.exception.DomainException;
import com.grab.home_test.repo.DynamoDBRepository;
import com.grab.home_test.repo.models.Account;
import com.grab.home_test.repo.models.PaymentOrder;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

   private final PaymentProcessingService paymentProcessingService;

   private final DynamoDBRepository dynamoDBRepository;

   public PaymentService(PaymentProcessingService paymentProcessingService, DynamoDBRepository dynamoDBRepository) {
      this.paymentProcessingService = paymentProcessingService;
      this.dynamoDBRepository = dynamoDBRepository;
   }

   public PaymentResponse processPayment(PaymentRequest paymentRequest) {
      // Step 1: initialize payment order with transition state = INITIAL
      System.out.println("[Payment Service] Start processing payment");
      PaymentOrder paymentOrder = PaymentOrder.fromRequest(paymentRequest);
      this.dynamoDBRepository.putItem(paymentOrder.getPaymentOrderId(), paymentOrder);

      // Step 2: Send payment order event to Queue support for:
      // leverage retry mechanism in case something goes wrong due to network issue, server down, race condition..etc..
      PaymentOrderEvent event = PaymentOrderEvent.fromPaymentOrder(paymentOrder);
      this.paymentProcessingService.addToQueue(event);

      // Please noted that my best practice is just response the payment refId and process async then provide get order API
      // support for polling mechanism from UI but in this home test I return all for easily view

      paymentOrder = (PaymentOrder) this.dynamoDBRepository.getItem(paymentOrder.getPaymentOrderId())
            .orElseThrow(() -> new DomainException(DomainCode.PAYMENT_NOT_FOUND));

      System.out.println("[Payment Service] End processing payment with status: " + paymentOrder.getPaymentStatus());

      Account fromAccount = (Account) this.dynamoDBRepository.getItem(paymentRequest.getFromAccount().getAccountId())
            .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_DOES_NOT_EXISTED));

      Account toAccount = (Account) this.dynamoDBRepository.getItem(paymentRequest.getToAccount().getAccountId())
            .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_DOES_NOT_EXISTED));

      return PaymentResponse.fromPaymentOrder(paymentOrder,
            AccountResponse.fromDB(fromAccount), AccountResponse.fromDB(toAccount));
   }
}
