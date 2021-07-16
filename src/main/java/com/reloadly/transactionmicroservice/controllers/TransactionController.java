package com.reloadly.transactionmicroservice.controllers;

import com.reloadly.transactionmicroservice.constants.CommonConstants;
import com.reloadly.transactionmicroservice.dto.TransactionMicroServiceResponse;
import com.reloadly.transactionmicroservice.dto.TransactionRequest;
import com.reloadly.transactionmicroservice.services.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(CommonConstants.API_VERSION + "transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TransactionMicroServiceResponse>> subscribe(@RequestBody @Valid TransactionRequest request) {
        return transactionService.processTransaction(request)
                .map(response -> ResponseEntity.ok(response));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionMicroServiceResponse> getTransactions() {
        return transactionService.findAllTransactions();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionMicroServiceResponse> getTransactionById(@PathVariable Long id) {
        return transactionService.findById(id);
    }
}
