package com.ao.banca.repository;

import com.ao.banca.model.Credito;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditoRepository extends ReactiveMongoRepository<Credito,String> {
}
