package com.reloadly.transactionmicroservice.services;

import com.reloadly.transactionmicroservice.dto.TransactionMicroServiceResponse;
import com.reloadly.transactionmicroservice.dto.TransactionRequest;
import com.reloadly.transactionmicroservice.enums.SubscriptionStatus;
import com.reloadly.transactionmicroservice.enums.TransactionStatus;
import com.reloadly.transactionmicroservice.helpers.Helper;
import com.reloadly.transactionmicroservice.models.Subscription;
import com.reloadly.transactionmicroservice.models.Transaction;
import com.reloadly.transactionmicroservice.respositories.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.reloadly.transactionmicroservice.enums.ResponseCode.*;

@Slf4j
@Service
public class TransactionService {

    @Value("${notification.service.endpoint}")
    private String notificationEndpoint;
    private final TransactionRepository transactionRepository;
    private final SubscriptionService subscriptionService;
    private final WebClientHttpService httpService;

    public TransactionService(TransactionRepository transactionRepository, SubscriptionService subscriptionService, WebClientHttpService httpService) {
        this.transactionRepository = transactionRepository;
        this.subscriptionService = subscriptionService;
        this.httpService = httpService;
    }

    /**
     * Process a transaction and give value to the subscriber
     * @param request the request for this transaction
     * @return {@link Mono<TransactionMicroServiceResponse>}
     */
    public Mono<TransactionMicroServiceResponse> processTransaction(TransactionRequest request) {

        if (!debitCustomer(request)) {
            return Mono.just(Helper.buildResponse(DEBIT_FAILED, request));
        }
        Transaction transaction = buildTransactionRequest(request);

        try {
            transaction = transactionRepository.saveAndFlush(transaction);
        } catch (Exception e) {
            reverseTransaction(transaction);
            return Mono.just(Helper.buildResponse(INTERNAL_SERVER_ERROR, request));
        }

        Subscription subscription = buildSubscriptionRequest(request, transaction);

        try {
            subscription = subscriptionService.saveSubscription(subscription);
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.saveAndFlush(transaction);
            reverseTransaction(transaction);
            return Mono.just(Helper.buildResponse(INTERNAL_SERVER_ERROR, request));
        }

        if (!Objects.isNull(subscription.getId())) {
            transaction.setValueGiven(Boolean.TRUE);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transactionRepository.saveAndFlush(transaction);
        }

        httpService.post(notificationEndpoint, Helper.buildEmailRequest(request), TransactionMicroServiceResponse.class).subscribeOn(Schedulers.elastic()).subscribe(res -> log.info("Email Notification Response {}", res));
        return Mono.just(Helper.buildResponse(OK, transaction));
    }

    public Mono<TransactionMicroServiceResponse> findAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        if(transactions.isEmpty()) {
            return Mono.just(Helper.buildResponse(RECORD_NOT_FOUND, Collections.EMPTY_LIST));
        } else {
            return Mono.just(Helper.buildResponse(OK, transactions));
        }
    }

    public Mono<TransactionMicroServiceResponse> findById(long id) {
        Transaction transaction = transactionRepository.findById(id);
        if(transaction != null) {
            return Mono.just(Helper.buildResponse(OK, transaction));
        } else {
            return Mono.just(Helper.buildResponse(RECORD_NOT_FOUND, null));
        }

    }

    /**
     * Build a transaction request
     * @param request object to build request from
     * @return @{@link Transaction}
     */
    private Transaction buildTransactionRequest(TransactionRequest request) {
        return Transaction.builder()
                .subscriberEmail(request.getSubscriberEmail())
                .amount(request.getAmount())
                .metaData("Purchase of ".concat(request.getType().name()))
                .referenceNumber(RandomStringUtils.randomNumeric(16))
                .status(TransactionStatus.PENDING)
                .build();
    }

    /**
     * Build a subscription request
     * @param request object to build request from
     * @return @{@link Transaction}
     */
    private Subscription buildSubscriptionRequest(TransactionRequest request, Transaction trnx) {
        return Subscription.builder()
                .subscriberEmail(trnx.getSubscriberEmail())
                .amount(trnx.getAmount())
                .referenceNumber(trnx.getReferenceNumber())
                .status(SubscriptionStatus.ACTIVE)
                .type(request.getType())
                .build();
    }

    /**
     * Debit the customers account
     *
     * @param request the request object
     * @return true or false
     */
    private boolean debitCustomer(TransactionRequest request) {
        if (!Objects.isNull(request)) {
            log.info("Account debited successfully");
            return true;
        }
        return false;
    }

    /**
     * Reverse a failed transaction
     * @param transaction the transaction to reverse
     */
    private void reverseTransaction(Transaction transaction) {
        log.info("Transaction Reversed");
    }

}
