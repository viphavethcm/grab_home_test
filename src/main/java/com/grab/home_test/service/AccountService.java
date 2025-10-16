package com.grab.home_test.service;


import static com.grab.home_test.repo.models.Account.ACCOUNT_ID_FORMAT;

import com.grab.home_test.controller.models.AccountCreationRequest;
import com.grab.home_test.controller.models.AccountResponse;
import com.grab.home_test.exception.DomainCode;
import com.grab.home_test.exception.DomainException;
import com.grab.home_test.repo.DynamoDBRepository;
import com.grab.home_test.repo.RedisCaching;
import com.grab.home_test.repo.models.Account;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class AccountService {

   private final DynamoDBRepository accountRepository;

   private final RedisCaching redisCaching;

   public AccountService(DynamoDBRepository accountRepository, RedisCaching redisCaching) {
      this.accountRepository = accountRepository;
      this.redisCaching = redisCaching;
   }

   public AccountResponse createAccount(AccountCreationRequest accountCreationRequest) {
      System.out.println("[Account Service] Start creating account");
      Account account = Account.initialize(accountCreationRequest);
      try {
         this.accountRepository.putItem(account.getId(), account);
      }catch (Exception e) {
         System.out.println("Error while creating account");
         throw new DomainException(DomainCode.SOMETHING_WENT_WRONG);
      }
      String accountKey = String.format(ACCOUNT_ID_FORMAT, account.getId());
      this.redisCaching.set(accountKey, "data", account);
      return AccountResponse.fromDB(account);
   }

   public AccountResponse getAccount(String accountId) {
      // Get from in-memory cache first before DB
      // We need to check if key is not existed or existed but invalid time to live --> we query from DB then
      // But in this home test scope, I check existed only because of limited time
      System.out.println("[Account Service] Start getting account");
      Account account = null;
      String accountKey = String.format(ACCOUNT_ID_FORMAT, accountId);
      Map<String, Object> inMemoryAccount = this.redisCaching.getAll(accountKey);
      if (inMemoryAccount != null && !inMemoryAccount.isEmpty()) {
         account = (Account) inMemoryAccount.get("data");
      }
      if (account == null) {
         account = (Account) this.accountRepository.getItem(accountId)
               .orElseThrow(() -> new DomainException(DomainCode.ACCOUNT_DOES_NOT_EXISTED));
      }
      return AccountResponse.fromDB(account);
   }
}
