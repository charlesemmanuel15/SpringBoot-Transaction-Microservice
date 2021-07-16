package com.reloadly.transactionmicroservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reloadly.transactionmicroservice.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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
@Table(name = "transaction")
public class Transaction extends BaseModel<Transaction> {
    @NotNull
    private String subscriberEmail;

    @NotNull
    private BigDecimal amount;

    @Column(name = "meta_data")
    private String metaData;

    @NotNull
    private String referenceNumber;

    @NotNull
    private Boolean valueGiven = Boolean.FALSE;

    @NotNull
    private TransactionStatus status;
}
