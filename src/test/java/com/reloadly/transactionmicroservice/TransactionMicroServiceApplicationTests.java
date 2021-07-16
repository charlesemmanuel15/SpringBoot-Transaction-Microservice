package com.reloadly.transactionmicroservice;


import com.reloadly.transactionmicroservice.controllers.TransactionController;
		import com.reloadly.transactionmicroservice.services.SubscriptionService;
		import com.reloadly.transactionmicroservice.services.TransactionService;
		import org.junit.runner.RunWith;
		import org.junit.runners.Suite;
		import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Suite.SuiteClasses({
		TransactionController.class,
		TransactionService.class,
		SubscriptionService.class
})
public class TransactionMicroServiceApplicationTests {
}

