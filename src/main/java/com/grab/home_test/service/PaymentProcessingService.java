package com.grab.home_test.service;

import com.grab.home_test.enums.PaymentMethod;
import com.grab.home_test.controller.models.PaymentOrderEvent;
import com.grab.home_test.exception.DomainCode;
import com.grab.home_test.exception.DomainException;
import com.grab.home_test.repo.DynamoDBRepository;
import com.grab.home_test.repo.models.Account;
import com.grab.home_test.repo.models.PaymentOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class PaymentProcessingService {

   public static final Queue<PaymentOrderEvent> queues = new LinkedBlockingQueue<>();

   private final DynamoDBRepository dynamoDBRepository;

   public PaymentProcessingService(DynamoDBRepository dynamoDBRepository) {
      this.dynamoDBRepository = dynamoDBRepository;

   }

   public void addToQueue(PaymentOrderEvent paymentOrderEvent) {
      queues.add(paymentOrderEvent);
      System.out.println("Event has been added to queue. Queue size: " + queues.size());

      this.processPaymentOrder(queues.poll());
   }

   private void processPaymentOrder(PaymentOrderEvent event) {
      // Step 1: Get Account/ Payment and validate rules check
      String paymentOrderId = event.getPaymentOrderId();

      PaymentOrder paymentOrder = (PaymentOrder) this.dynamoDBRepository.getItem(paymentOrderId)
            .orElseThrow(() -> new DomainException(DomainCode.PAYMENT_NOT_FOUND));
      BigDecimal inputAmount = paymentOrder.getAmount();

      Account fromAccount = (Account) this.dynamoDBRepository.getItem(paymentOrder.getFromAccountId())
            .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_DOES_NOT_EXISTED));
      Account toAccount = (Account) this.dynamoDBRepository.getItem(paymentOrder.getToAccountId())
            .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_DOES_NOT_EXISTED));
      if (!this.isEligibleToProcess(fromAccount, toAccount, paymentOrder.getAmount(),
            paymentOrder.getFromMethod(), paymentOrder.getToMethod())) {
         throw new DomainException(DomainCode.PAYMENT_IS_NOT_ELIGIBLE);
      }

      // Step 2: Acquire locking based on process status. Somehow if duplicate event happen
      // Perform update current status = PROCESSING(new status) where old status = INITIAL.
      paymentOrder.onUpdateProcessing();
      if (!this.dynamoDBRepository.updateItemIfCondition(paymentOrder.getPaymentOrderId(), paymentOrder, paymentOrder.getVersion())) {
         System.out.println("Duplicate event...");
         return;
      }

      // Step 3: Start handling logic fund in/out
      boolean fromUpdated = false;
      boolean toUpdated = false;
      try {
         fromAccount.updateBalance(inputAmount,paymentOrder.getFromMethod() );
         toAccount.updateBalance(inputAmount, paymentOrder.getToMethod());

         fromUpdated = this.dynamoDBRepository.updateItemIfCondition(fromAccount.getId(), fromAccount, fromAccount.getVersion());
         toUpdated = this.dynamoDBRepository.updateItemIfCondition(toAccount.getId(), toAccount, toAccount.getVersion());

         if (!fromUpdated || !toUpdated) {
            throw new ConcurrentModificationException("Failed to update accounts atomically");
         }
      }catch (Exception e) {
         // Roll back transactions
         System.out.println("Start checking and rollback transactions...");
         if (fromUpdated) {
            fromAccount.updateBalance(inputAmount, paymentOrder.getToMethod());
            this.dynamoDBRepository.putItem(fromAccount.getId(), fromAccount);
         }
         if (toUpdated) {
            toAccount.updateBalance(inputAmount, paymentOrder.getFromMethod());
            this.dynamoDBRepository.putItem(toAccount.getId(), toAccount);
         }
         // update Payment order as FAILED
         paymentOrder.onFailed();
         this.dynamoDBRepository.putItem(paymentOrderId, paymentOrder);
      }
      // Update Payment Order as COMPLETED
      paymentOrder.onCompleted();
      this.dynamoDBRepository.putItem(paymentOrderId, paymentOrder);
      // We should update account balance into cache if everything goes well but I didn't have enough time to do that
   }

   private boolean isEligibleToProcess(Account fromAccount, Account toAccount,
                                       BigDecimal inputAmount,
                                       PaymentMethod fromAccountMethod, PaymentMethod toAccountMethod) {
      if (!fromAccount.isAccountActive() || !toAccount.isAccountActive()) {
         return false;
      }
      // debit from Account A to B
      if (PaymentMethod.DEBIT == fromAccountMethod &&
            fromAccount.getActualBalance().subtract(inputAmount).compareTo(BigDecimal.ZERO) < 0) {
         return false;
      }
      // debit from Account B to A
      if (PaymentMethod.DEBIT == toAccountMethod &&
            toAccount.getActualBalance().subtract(inputAmount).compareTo(BigDecimal.ZERO) < 0) {
         return false;
      }
      return true;
   }
}
