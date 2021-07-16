package com.reloadly.transactionmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reloadly.transactionmicroservice.enums.SubscriptionDuration;
import com.reloadly.transactionmicroservice.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMicroServiceResponse {
    private String statusCode;
    private String statusMessage;
    private String timestamp;
    private Object data;

    public TransactionMicroServiceResponse(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.timestamp = LocalDateTime.now().toString();
    }
}
