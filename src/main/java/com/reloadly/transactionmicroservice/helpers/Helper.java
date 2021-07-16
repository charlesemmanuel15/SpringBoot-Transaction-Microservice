package com.reloadly.transactionmicroservice.helpers;

import com.reloadly.transactionmicroservice.constants.CommonConstants;
import com.reloadly.transactionmicroservice.dto.EmailRequest;
import com.reloadly.transactionmicroservice.dto.TransactionMicroServiceResponse;
import com.reloadly.transactionmicroservice.dto.TransactionRequest;
import com.reloadly.transactionmicroservice.enums.ResponseCode;

import java.time.LocalDateTime;

public class Helper {

    public static TransactionMicroServiceResponse buildResponse(ResponseCode responseCode, Object data) {
        return TransactionMicroServiceResponse.builder()
                .statusCode(responseCode.getCanonicalCode())
                .statusMessage(responseCode.getDescription())
                .timestamp(LocalDateTime.now().toString())
                .data(data)
                .build();
    }

    public static EmailRequest buildEmailRequest(TransactionRequest request) {
        String message = CommonConstants.TRANSACTION_COMPLETED_MESSAGE
                .replace("{type}", request.getType().name())
                .replace("{amount}", request.getAmount().toString())
                .replace("{duration}", request.getDuration().name());

        return EmailRequest.builder()
                .to(request.getSubscriberEmail())
                .subject(CommonConstants.TRANSACTION_COMPLETED_SUBJECT)
                .message(message)
                .build();
    }
}
