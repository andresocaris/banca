package com.ao.banca.controller;

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
import com.ao.banca.repository.ClientRepository;
import com.ao.banca.service.ClientService;
import com.ao.banca.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ClienteController {
    ClientService clientService;
    @Autowired
    TransferService transferService;
    public ClienteController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public Flux<Client> getClients(){
        return clientService.getClients();
    }
    @PostMapping("/client")
    public Mono<ClientDto> saveClient(@RequestBody ClientDto client){
        return clientService.saveClient(client);
    }





}
