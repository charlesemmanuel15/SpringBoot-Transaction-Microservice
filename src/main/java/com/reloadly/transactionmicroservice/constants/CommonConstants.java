package com.reloadly.transactionmicroservice.constants;

public interface CommonConstants {
    String API_VERSION = "/api/v1/";
    String TRANSACTION_COMPLETED_SUBJECT = "Transaction Completed Successfully!";
    String TRANSACTION_COMPLETED_MESSAGE = "Hello, your subscription was completed successfully. Please find transaction details below. \n" +
            "Subscription Type: {type} \n" +
            "Amount: {amount} \n" +
            "Duration: {duration}";
}
