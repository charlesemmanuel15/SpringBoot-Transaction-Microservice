package com.reloadly.transactionmicroservice.respositories;


import com.reloadly.transactionmicroservice.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}