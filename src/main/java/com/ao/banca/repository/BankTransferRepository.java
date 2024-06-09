package com.ao.banca.repository;

import com.ao.banca.model.BankTransfer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BankTransferRepository extends ReactiveMongoRepository<BankTransfer,String> {
    Flux<BankTransfer> findByBankAccountNumberOrigin(String bankAccountNumber);
}
