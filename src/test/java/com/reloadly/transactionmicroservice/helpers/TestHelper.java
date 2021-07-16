package com.reloadly.transactionmicroservice.helpers;

import com.reloadly.transactionmicroservice.dto.TransactionMicroServiceResponse;
import com.reloadly.transactionmicroservice.dto.TransactionRequest;
import com.reloadly.transactionmicroservice.enums.SubscriptionDuration;
import com.reloadly.transactionmicroservice.enums.SubscriptionType;
import com.reloadly.transactionmicroservice.models.Subscription;
import com.reloadly.transactionmicroservice.models.Transaction;
import org.apache.commons.lang3.RandomStringUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.reloadly.transactionmicroservice.enums.ResponseCode.OK;

public class TestHelper {

    public static String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiJ1bm9nd3VkYW5AZ21haWwuY29tIiwiaWF0IjoxNjE2MDEzMzY2LCJleHAiOjE2MzE3OTMzNjZ9.Kk9U0pnPkDv3U54RwbUpREkCCUtutEWwujkL8GkNA7ijEJPSAJW4EnCdvRpszSGTDu8aB-U7RHZxj2VoA5BAFQ";

    public static TransactionRequest transactionRequest() {
        return TransactionRequest.builder()
                .amount(BigDecimal.TEN)
                .duration(SubscriptionDuration.DAILY)
                .type(SubscriptionType.DATA)
                .build();
    }

    public static Mono<TransactionMicroServiceResponse> getSuccessfulTransactionMicroServiceResponse() {
        return Mono.just(TransactionMicroServiceResponse.builder()
                .statusCode(OK.getCanonicalCode())
                .statusMessage(OK.getDescription())
                .timestamp(LocalDateTime.now().toString())
                .build());

    }

    public static Transaction getTransaction() {
        return Transaction.builder()
                .referenceNumber(RandomStringUtils.randomNumeric(16))
                .amount(BigDecimal.TEN)
                .subscriberEmail("abc@xyz.com")
                .build();
    }

    public static Subscription getSubscription() {
        Subscription subscription = Subscription.builder()
                .referenceNumber(RandomStringUtils.randomNumeric(16))
                .amount(BigDecimal.TEN)
                .subscriberEmail("abc@xyz.com")
                .build();
        subscription.setId(1l);
        return subscription;
    }
}

