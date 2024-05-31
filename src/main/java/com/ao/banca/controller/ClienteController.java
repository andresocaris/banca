package com.ao.banca.controller;

import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.request.CreditoDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.model.Client;
import com.ao.banca.model.Credit;
import com.ao.banca.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ClienteController {
    ClientService clientService;

    public ClienteController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public Flux<Client> getClients(){
        return clientService.getClients();
    }
    @PostMapping("/client")
    public Mono<Client> saveClient(@RequestBody Client client){
        return clientService.saveClient(client);
    }
    @PostMapping("/create-bankAccount")
    public Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccount(@RequestBody BankAccountCreationRequestDto bankAccountCreationRequestDto){
        return clientService.createBankAccountByRequest(bankAccountCreationRequestDto);
    }
    @PostMapping("/create-credit")
    public Mono<ResponseEntity<Credit>> createCredit(@RequestBody CreditoDto creditDto){
        return clientService.createCredit(creditDto);
    }
}
