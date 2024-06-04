package com.ao.banca.service;

import com.ao.banca.dto.request.BankTransferRequestDto;
import com.ao.banca.model.BankTransfer;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface TransferService {
    Mono<ResponseEntity<BankTransfer>> createTransfer(BankTransferRequestDto bankTransferRequestDto);

}
