package com.ao.banca.repository;

import com.ao.banca.model.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client,String> {
    Mono<Client> findByDni(String dni);
    Mono<Client> findByRucCompany(String rucCompany);
}
