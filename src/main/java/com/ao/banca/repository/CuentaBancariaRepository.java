package com.ao.banca.repository;

import com.ao.banca.model.CuentaBancaria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaBancariaRepository extends ReactiveMongoRepository<CuentaBancaria,String> {
}
