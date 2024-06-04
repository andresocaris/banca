package com.ao.banca.service;

import com.ao.banca.dto.ClientDto;
import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.request.BankDepositRequestDto;
import com.ao.banca.dto.request.BankTransferRequestDto;
import com.ao.banca.dto.request.CreditDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.model.BankAccount;
import com.ao.banca.model.BankTransfer;
import com.ao.banca.model.Client;
import com.ao.banca.model.Credit;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ClientService {
    Flux<Client> getClients();
    Mono<ClientDto> saveClient(ClientDto client);





}
