package com.reloadly.transactionmicroservice.services;

import com.reloadly.transactionmicroservice.dto.TransactionMicroServiceResponse;
import com.reloadly.transactionmicroservice.enums.ResponseCode;
import com.reloadly.transactionmicroservice.helpers.TestHelper;
import com.reloadly.transactionmicroservice.models.Subscription;
import com.reloadly.transactionmicroservice.respositories.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static com.reloadly.transactionmicroservice.enums.ResponseCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TransactionService.class, WebClientHttpService.class})
@EnableConfigurationProperties
@TestPropertySource("classpath:/test.properties")
@Slf4j
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private WebClientHttpService httpService;

    @Test
    public void processSuccessfulTransaction() {
        doReturn(TestHelper.getSubscription()).when(subscriptionService).saveSubscription(any());
        doReturn(TestHelper.getTransaction()).when(transactionRepository).saveAndFlush(any());
        doReturn(TestHelper.getSuccessfulTransactionMicroServiceResponse()).when(httpService).post(any(), any(), any());
        Mono<TransactionMicroServiceResponse> transaction = transactionService.processTransaction(TestHelper.transactionRequest());
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(ResponseCode.OK.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void processFailedTransaction() {
        doReturn(TestHelper.getSubscription()).when(subscriptionService).saveSubscription(any());
        doThrow(new RuntimeException()).when(transactionRepository).saveAndFlush(any());
        doReturn(TestHelper.getSuccessfulTransactionMicroServiceResponse()).when(httpService).post(any(), any(), any());
        Mono<TransactionMicroServiceResponse> transaction = transactionService.processTransaction(TestHelper.transactionRequest());
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(INTERNAL_SERVER_ERROR.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void processFailedTransactionWhenSubscriptionFails() {
        doReturn(TestHelper.getTransaction()).when(transactionRepository).saveAndFlush(any());
        doThrow(new RuntimeException()).when(subscriptionService).saveSubscription(any());
        doReturn(TestHelper.getSuccessfulTransactionMicroServiceResponse()).when(httpService).post(any(), any(), any());
        Mono<TransactionMicroServiceResponse> transaction = transactionService.processTransaction(TestHelper.transactionRequest());
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(INTERNAL_SERVER_ERROR.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void processFailedTransactionWhenSubscriptionIsNull() {
        doReturn(TestHelper.getTransaction()).when(transactionRepository).saveAndFlush(any());
        doReturn(TestHelper.getSubscription()).when(subscriptionService).saveSubscription(any());
        doReturn(TestHelper.getSuccessfulTransactionMicroServiceResponse()).when(httpService).post(any(), any(), any());
        Mono<TransactionMicroServiceResponse> transaction = transactionService.processTransaction(TestHelper.transactionRequest());
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(OK.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void testFailedDebit() {
        doReturn(TestHelper.getTransaction()).when(transactionRepository).saveAndFlush(any());
        doReturn(new Subscription()).when(subscriptionService).saveSubscription(any());
        Mono<TransactionMicroServiceResponse> transaction = transactionService.processTransaction(null);
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(DEBIT_FAILED.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void findAllTransactions() {
        doReturn(Arrays.asList(TestHelper.getTransaction())).when(transactionRepository).findAll();
        Mono<TransactionMicroServiceResponse> transaction = transactionService.findAllTransactions();
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(ResponseCode.OK.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void findById() {
        doReturn(TestHelper.getTransaction()).when(transactionRepository).findById(anyLong());
        Mono<TransactionMicroServiceResponse> transaction = transactionService.findById(1l);
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(ResponseCode.OK.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void findByIdWithNullResponse() {
        doReturn(null).when(transactionRepository).findById(anyLong());
        Mono<TransactionMicroServiceResponse> transaction = transactionService.findById(1l);
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(RECORD_NOT_FOUND.getCanonicalCode()))
                .verifyComplete();
    }

    @Test
    public void findAllTransactionsWithEmptyResponse() {
        doReturn(Collections.EMPTY_LIST).when(transactionRepository).findAll();
        Mono<TransactionMicroServiceResponse> transaction = transactionService.findAllTransactions();
        StepVerifier
                .create(transaction)
                .expectNextMatches(response -> response.getStatusCode().equals(RECORD_NOT_FOUND.getCanonicalCode()))
                .verifyComplete();
    }
}
