package com.reloadly.transactionmicroservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reloadly.transactionmicroservice.enums.SubscriptionStatus;
import com.reloadly.transactionmicroservice.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
public class Subscription extends BaseModel<Subscription> {

    @NotNull
    private String subscriberEmail;

    @NotNull
    private String referenceNumber;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private SubscriptionStatus status;

    @NotNull
    private SubscriptionType type;

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }
}
