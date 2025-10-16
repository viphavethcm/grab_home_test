package com.grab.home_test;

import com.grab.home_test.controller.models.AccountCreationRequest;
import com.grab.home_test.repo.DynamoDBRepository;
import com.grab.home_test.repo.RedisCaching;
import com.grab.home_test.repo.models.Account;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class HomeTestApplication {

   public static void main(String[] args) {
      // Set some available accounts for testing

      // Put to redis cache
      RedisCaching redisCaching = new RedisCaching();
      String accountId1 = "c914b91f-8675-4002-a5f8-fd5135d22c87";
      AccountCreationRequest accountRequest = new AccountCreationRequest("Love Grab!!");
      Account account = Account.initialize(accountRequest);
      account.setId(accountId1);
      account.setActualBalance(new BigDecimal("10000.00"));
      redisCaching.set("account:"+accountId1, "data", account);


      String accountId2 = "bca0a12a-ef57-4900-bfb0-efb570cc4f7e";
      AccountCreationRequest accountRequest2= new AccountCreationRequest("HomeTest!!");
      Account account2 = Account.initialize(accountRequest2);
      account2.setId(accountId2);
      account2.setActualBalance(new BigDecimal("20000.00"));
      redisCaching.set("account:"+accountId2, "data", account2);

      // Put to DynamoDB
      DynamoDBRepository dynamoDBRepository = new DynamoDBRepository();
      dynamoDBRepository.putItem(accountId1, account);
      dynamoDBRepository.putItem(accountId2, account2);

      SpringApplication.run(HomeTestApplication.class, args);
   }

}
