package com.ao.banca.controller;

import com.ao.banca.dto.request.BankDepositRequestDto;
import com.ao.banca.service.ClientService;
import com.ao.banca.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DepositController {
    @Autowired
    DepositService depositService;
    @PostMapping("/deposit")
    public Mono<ResponseEntity<String>> createDeposit(@RequestBody BankDepositRequestDto bankDepositRequestDto){
        return depositService.deposit(bankDepositRequestDto);
    }
}
