package com.reloadly.transactionmicroservice.exception;

import lombok.Data;


@Data
public class TransactionMicroServiceException extends RuntimeException {
    private final Integer httpCode;
    private String statusCode;

    public TransactionMicroServiceException(Integer httpCode, String message, String statusCode) {
        super(message);
        this.httpCode = httpCode;
        this.statusCode = statusCode;
    }
}