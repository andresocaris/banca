package com.ao.banca.service;

import com.ao.banca.dto.request.BankDepositRequestDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface DepositService {
    Mono<ResponseEntity<String>> deposit(BankDepositRequestDto bankDepositRequestDto);

}
