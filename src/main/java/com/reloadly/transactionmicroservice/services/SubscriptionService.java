package com.reloadly.transactionmicroservice.services;

import com.reloadly.transactionmicroservice.models.Subscription;
import com.reloadly.transactionmicroservice.respositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription saveSubscription(Subscription request) {
        return subscriptionRepository.saveAndFlush(request);
    }
}