package com.ao.banca.repository;

import com.ao.banca.model.CuentaBancaria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CuentaBancariaRepository extends ReactiveMongoRepository<CuentaBancaria,String> {
    Mono<CuentaBancaria> findByNombreCuentaBancariaAndDniCliente(String numeroCuentaBancaria, String dniCliente);
    Mono<CuentaBancaria> findByNumeroCuentaBancaria(String cuentaBancaria);
}
