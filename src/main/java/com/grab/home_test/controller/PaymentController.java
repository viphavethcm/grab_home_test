package com.grab.home_test.controller;

import com.grab.home_test.controller.models.PaymentRequest;
import com.grab.home_test.controller.models.PaymentResponse;
import com.grab.home_test.exception.DomainCode;
import com.grab.home_test.exception.DomainException;
import com.grab.home_test.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

   private final PaymentService paymentService;

   public PaymentController(PaymentService paymentService) {
      this.paymentService = paymentService;
   }

   @PostMapping("/transactions")
   public PaymentResponse payment(@RequestBody PaymentRequest paymentRequest) {
      if (!paymentRequest.validateRequest()){
         throw new DomainException(DomainCode.BAD_REQUEST);
      }
      return this.paymentService.processPayment(paymentRequest);
   }
}
