package com.reloadly.transactionmicroservice.controllers;
import com.reloadly.transactionmicroservice.TransactionMicroServiceApplicationTests;
import com.reloadly.transactionmicroservice.constants.CommonConstants;
import com.reloadly.transactionmicroservice.dto.TransactionMicroServiceResponse;
import com.reloadly.transactionmicroservice.helpers.TestHelper;
import com.reloadly.transactionmicroservice.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.reloadly.transactionmicroservice.enums.ResponseCode.OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@Slf4j
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@AutoConfigureWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TransactionMicroServiceApplicationTests.class)
@ActiveProfiles("test")
@PropertySource("classpath:/test.properties")
public class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void subscribe() {
        doReturn(TestHelper.getSuccessfulTransactionMicroServiceResponse()).when(transactionService).processTransaction(any());
        webTestClient.post()
                .uri(CommonConstants.API_VERSION +"transactions")
                .body(BodyInserters.fromValue(TestHelper.transactionRequest()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer "+ TestHelper.TOKEN)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionMicroServiceResponse.class)
                .value(response -> response.getStatusCode().equals(OK.getCanonicalCode()));
    }

    @Test
    public void getTransactions() {
        doReturn(TestHelper.getSuccessfulTransactionMicroServiceResponse()).when(transactionService).findAllTransactions();
        webTestClient.get()
                .uri(CommonConstants.API_VERSION +"transactions")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionMicroServiceResponse.class)
                .value(response -> response.getStatusCode().equals(OK.getCanonicalCode()));
    }

    @Test
    public void getTransactionById() {
        doReturn(TestHelper.getSuccessfulTransactionMicroServiceResponse()).when(transactionService).findById(anyLong());
        webTestClient.get()
                .uri(CommonConstants.API_VERSION +"transactions/"+1)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionMicroServiceResponse.class)
                .value(response -> response.getStatusCode().equals(OK.getCanonicalCode()));
    }
}