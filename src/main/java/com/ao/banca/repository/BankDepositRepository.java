package com.ao.banca.repository;

import com.ao.banca.model.BankDeposit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankDepositRepository extends ReactiveMongoRepository<BankDeposit,String> {
}
