package com.ao.banca.service.impl;

import com.ao.banca.dto.ClientDto;
import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.request.BankDepositRequestDto;
import com.ao.banca.dto.request.BankTransferRequestDto;
import com.ao.banca.dto.request.CreditDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.exception.BadRequest;
import com.ao.banca.model.BankTransfer;
import com.ao.banca.model.Client;
import com.ao.banca.model.Credit;
import com.ao.banca.model.BankAccount;
import com.ao.banca.repository.BankTransferRepository;
import com.ao.banca.repository.ClientRepository;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.repository.CreditRepository;
import com.ao.banca.service.ClientService;
import com.ao.banca.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    ClientRepository clientRepository;
    BankAccountRepository bankAccountRepository;
    BankAccountService bankAccountService;
    CreditRepository creditRepository;
    @Autowired
    BankTransferRepository bankTransferRepository;
    public ClientServiceImpl(ClientRepository clientRepository, BankAccountRepository bankAccountRepository, BankAccountService bankAccountService, CreditRepository creditRepository) {
        this.clientRepository = clientRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
        this.creditRepository = creditRepository;
    }

    @Override
    public Flux<Client> getClients() {
        return clientRepository.findAll();
    }

    @Override
    public Mono<ClientDto> saveClient(ClientDto clientDto) {
        Client client = new Client();
        BeanUtils.copyProperties( clientDto,client);
        Mono<Client> clientSaved =  clientRepository.save(client);
        return clientSaved.flatMap(clientSaved1->{
            ClientDto clientDtoCreated = new ClientDto();
            BeanUtils.copyProperties( clientSaved1,clientDtoCreated);
            return Mono.just(clientDtoCreated);
        });
    }

    public Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccountCreationResponseDto(Client client, BankAccount bankAccount){
        BankAccountCreationResponseDto response = BankAccountCreationResponseDto.builder()
                .client(client)
                .bankAccount(bankAccount)
                .build();
        return Mono.just(ResponseEntity.ok(response));
    }
}
