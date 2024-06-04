package com.ao.banca.service;

import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.model.BankAccount;
import com.ao.banca.model.Client;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {
    Mono<BankAccount> createBankAccount(String dni, String bankAccountName, String rucClient);
    Mono<Boolean> validateBankAccountTransfer(String bankAccountNumber, Double amount);
    Mono<ResponseEntity<Flux<BankAccount>>> getBankAccounts();
    Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccountByRequest(BankAccountCreationRequestDto bankAccountCreationRequestDto);
    Mono<Client> createClient(String dni, String firstName, String lastName, String rucCompany, String clientType);

}
