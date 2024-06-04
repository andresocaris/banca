package com.ao.banca.controller;

import com.ao.banca.dto.request.BankTransferRequestDto;
import com.ao.banca.model.BankTransfer;
import com.ao.banca.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TransferController {
    @Autowired
    TransferService transferService;
    @PostMapping("/transfer")
    public Mono<ResponseEntity<BankTransfer>> createTransfer(@RequestBody BankTransferRequestDto bankTransferRequestDto){
        return transferService.createTransfer(bankTransferRequestDto);
    }
}
