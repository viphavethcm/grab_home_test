package com.grab.home_test.exception;

public class DomainException extends RuntimeException{
   private final String errorCode;

   private final String errorMessage;

   public DomainException(DomainCode domainCode) {
      super(domainCode.getMessage());
      this.errorCode = domainCode.toUniversalCode();
      this.errorMessage = domainCode.getMessage();
   }

   public String getErrorCode() {
      return errorCode;
   }

   public String getErrorMessage() {
      return errorMessage;
   }
}
