package com.ao.banca.service;

import com.ao.banca.dto.request.CreditDto;
import com.ao.banca.model.Credit;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Mono<ResponseEntity<Credit>> createCredit(CreditDto credit);
    Mono<ResponseEntity<Flux<Credit>>> getCredits();
}
