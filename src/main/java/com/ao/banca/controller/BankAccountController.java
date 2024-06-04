package com.ao.banca.controller;

import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.model.BankAccount;
import com.ao.banca.service.BankAccountService;
import com.ao.banca.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BankAccountController {
    @Autowired
    ClientService clientService;
    @Autowired
    BankAccountService bankAccountService;
    @PostMapping("/create-bankAccount")
    public Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccount(@RequestBody BankAccountCreationRequestDto bankAccountCreationRequestDto){
        return bankAccountService.createBankAccountByRequest(bankAccountCreationRequestDto);
    }
    @GetMapping("/bankAccounts")
    public Mono<ResponseEntity<Flux<BankAccount>>> getBankAccounts(){
        return bankAccountService.getBankAccounts();
    }
}
