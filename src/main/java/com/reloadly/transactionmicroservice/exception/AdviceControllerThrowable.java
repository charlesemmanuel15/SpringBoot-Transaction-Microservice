package com.reloadly.transactionmicroservice.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.reloadly.transactionmicroservice.dto.TransactionMicroServiceResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.PersistenceException;
import java.net.SocketTimeoutException;

import static com.reloadly.transactionmicroservice.enums.ResponseCode.*;


@Slf4j
@ResponseBody
@ControllerAdvice(annotations = RestController.class, basePackages = "com.reloadly.accountmicroservice.controllers")
public class AdviceControllerThrowable {

    @ExceptionHandler(NullPointerException.class)
    public TransactionMicroServiceResponse noAccessException(NullPointerException e) {
        log.error("Null Pointer exception", e);
        return new TransactionMicroServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public TransactionMicroServiceResponse handleLockedException(HttpClientErrorException e) {
        log.error("Error", e);
        return new TransactionMicroServiceResponse(String.valueOf(e.getRawStatusCode()), e.getStatusText() + " " + e.getResponseBodyAsString());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public TransactionMicroServiceResponse handleLoginException(PersistenceException e) {
        log.error("PersistenceException ", e);
        return new TransactionMicroServiceResponse(BAD_REQUEST.getCanonicalCode(), BAD_REQUEST.getDescription());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public TransactionMicroServiceResponse handleLoginException(IllegalArgumentException e) {
        log.error("IllegalArgumentException ", e);
        return new TransactionMicroServiceResponse(BAD_REQUEST.getCanonicalCode(), BAD_REQUEST.getDescription());
    }

    @ExceptionHandler(TransactionMicroServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public TransactionMicroServiceResponse jsonException(TransactionMicroServiceException e) {
        return new TransactionMicroServiceResponse(e.getStatusCode(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public TransactionMicroServiceResponse noAccessException(AuthenticationException e) {
        log.error("AuthenticationException ", e);
        return new TransactionMicroServiceResponse("401", e.getMessage() + ", you do not privilege to access this resource");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public TransactionMicroServiceResponse noAccessException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException ", e);
        return new TransactionMicroServiceResponse(BAD_REQUEST.getCanonicalCode(), "Wrong message was sent or some required fields were not provided");
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public TransactionMicroServiceResponse timeOutException(SocketTimeoutException e) {
        log.error("SocketTimeoutException ", e);
        return new TransactionMicroServiceResponse("408", "Sorry...request is taking longer than expected.. please trying again later");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public TransactionMicroServiceResponse noAccessException(AccessDeniedException e) {
        log.error("AccessDeniedException ", e);
        return new TransactionMicroServiceResponse("403", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public TransactionMicroServiceResponse noAccessException(Exception e) {
        log.error("Unknown Exception", e);
        return new TransactionMicroServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription());
    }

    @ExceptionHandler(JsonParseException.class)
    public TransactionMicroServiceResponse noAccessException(JsonParseException e) {
        log.error("Unknown Exception", e);
        return new TransactionMicroServiceResponse(UNMARSHALL_EXCEPTION.getCanonicalCode(), UNMARSHALL_EXCEPTION.getDescription());
    }
}
