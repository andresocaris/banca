package com.ao.banca.service;

import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.request.CreditoDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.model.Client;
import com.ao.banca.model.Credit;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Flux<Client> getClients();
    Mono<Client> saveClient(Client client);
    Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccountByRequest(BankAccountCreationRequestDto bankAccountCreationRequestDto);
    Mono<Client> createClient(String dni, String firstName, String lastName, String rucCompany);
    Mono<ResponseEntity<Credit>> createCredit(CreditoDto credit);
}
