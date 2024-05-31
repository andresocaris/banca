package com.ao.banca.repository;

import com.ao.banca.model.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditRepository extends ReactiveMongoRepository<Credit,String> {
}
