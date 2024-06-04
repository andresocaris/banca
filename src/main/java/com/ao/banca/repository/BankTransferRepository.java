package com.ao.banca.repository;

import com.ao.banca.model.BankTransfer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransferRepository extends ReactiveMongoRepository<BankTransfer,String> {
}
