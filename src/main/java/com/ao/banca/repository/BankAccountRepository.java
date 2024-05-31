package com.ao.banca.repository;

import com.ao.banca.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount,String> {
    Mono<BankAccount> findByBankAccountNameAndDniClient(String bankAccountNumber, String dniClient);
    Mono<BankAccount> findByBankAccountNumber(String bankAccountNumber);
}
