package com.ao.banca.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BankDepositRepository extends ReactiveMongoRepository<BankDepositRepository,String> {
}
