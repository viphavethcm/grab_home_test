package com.grab.home_test.exception;

public enum DomainCode {


    // Common Code
    GENERIC_ERROR("00", "Internal Server Error"),
    BAD_REQUEST("01", "Bad Request"),
    SOMETHING_WENT_WRONG("02", "Something Went Wrong"),
    ACCOUNT_DOES_NOT_EXISTED("03", "Account does not exist"),
    PAYMENT_IS_NOT_ELIGIBLE("04", "Payment is not eligible"),
    PAYMENT_NOT_FOUND("05", "Payment Order not found");

    private String externalCode;
    
    private String message;
    
    DomainCode(String externalCode, String message) {
        this.externalCode = externalCode;
        this.message = message;
    }
    
    public String toUniversalCode() {
        return "010" + externalCode;
    }
    public String getMessage() {
        return message;
    }
}
