package com.ao.banca.controller;

import com.ao.banca.dto.request.BankTransferRequestDto;
import com.ao.banca.model.BankTransfer;
import com.ao.banca.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class TransferController {
    @Autowired
    TransferService transferService;
    @GetMapping("/transactions/{dniOrRuc}/{bankAccountNumber}")
    public Mono<ResponseEntity<Flux<Object>>> createTransfer(@PathVariable String dniOrRuc, @PathVariable String bankAccountNumber){
        return transferService.findTransfersByDniorRucByAccountNumber(dniOrRuc,bankAccountNumber);
    }
}
