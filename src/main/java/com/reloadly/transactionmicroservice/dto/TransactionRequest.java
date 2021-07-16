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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotNull
    private BigDecimal amount;
    @NotNull
    private SubscriptionType type;
    @NotNull
    private SubscriptionDuration duration;
    @NotNull
    @JsonProperty(value = "subscriber_email")
    private String subscriberEmail;
}
