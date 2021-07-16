package com.reloadly.transactionmicroservice.services;

import com.reloadly.transactionmicroservice.helpers.TestHelper;
import com.reloadly.transactionmicroservice.models.Subscription;
import com.reloadly.transactionmicroservice.respositories.SubscriptionRepository;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SubscriptionService.class})
@EnableConfigurationProperties
@TestPropertySource("classpath:/test.properties")
@Slf4j
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @Test
    public void saveSubscription() {
        doReturn(TestHelper.getSubscription()).when(subscriptionRepository).saveAndFlush(any());

        Subscription subscription = subscriptionService.saveSubscription(TestHelper.getSubscription());

        StepVerifier
                .create(Mono.just(subscription))
                .expectNextMatches(response -> subscription.getReferenceNumber() != null)
                .verifyComplete();
    }
}