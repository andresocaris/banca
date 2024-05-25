package com.ao.banca.repository;

import com.ao.banca.model.Cliente;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClienteRepository extends ReactiveMongoRepository<Cliente,String> {
    Mono<Cliente> save(Cliente cliente);
    Mono<Cliente> findByDni(String dni);
}
