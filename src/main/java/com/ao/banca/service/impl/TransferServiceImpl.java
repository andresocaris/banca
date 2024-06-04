package com.ao.banca.service.impl;

import com.ao.banca.dto.request.BankTransferRequestDto;
import com.ao.banca.exception.BadRequest;
import com.ao.banca.model.BankAccount;
import com.ao.banca.model.BankTransfer;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.repository.BankTransferRepository;
import com.ao.banca.service.BankAccountService;
import com.ao.banca.service.TransferService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    BankTransferRepository bankTransferRepository;
    @Autowired
    BankAccountService bankAccountService;
    @Override
    public Mono<ResponseEntity<BankTransfer>> createTransfer(BankTransferRequestDto bankTransferRequestDto) {
        BankTransfer bankTransfer = new BankTransfer();
        BeanUtils.copyProperties( bankTransferRequestDto,bankTransfer);
        bankTransfer.setTransferDateTime(LocalDateTime.now());
        return validateTransfer(bankTransferRequestDto).filter(x->x
                ).flatMap(y->
                        registerTransfer(bankTransfer).then(bankTransferRepository.save(bankTransfer))
                )
                .flatMap(bankTransferData->Mono.just(ResponseEntity.ok(bankTransferData)))
                .switchIfEmpty(Mono.error(new BadRequest("The account does not exist")));
    }
    private Mono<Boolean> validateTransfer(BankTransferRequestDto bankTransferRequestDto) {

        Mono<BankAccount> bankAccountMono = bankAccountRepository.findByBankAccountNumber(bankTransferRequestDto.getBankAccountNumberDestination());
        if( !bankAccountMono.hasElement().block() ) return Mono.error(new BadRequest("no exist account destination"));
        if (bankTransferRequestDto.getTransferType().equals("transfer"))  return bankAccountService.validateBankAccountTransfer(bankTransferRequestDto.getBankAccountNumberOrigin(),bankTransferRequestDto.getAmount());
        return Mono.just(true);
    }
    private Mono<Void> registerTransfer(BankTransfer bankTransfer) {
        return Mono.just(bankTransfer.getBankAccountNumberOrigin())
                .flatMap(bankAccountNumber -> bankAccountRepository.findByBankAccountNumber(bankAccountNumber)
                )
                .flatMap(bankAccount -> {
                    Double amountCurrent = bankAccount.getBankAccountAmount();
                    bankAccount.setBankAccountAmount(amountCurrent-bankTransfer.getAmount());
                    return bankAccountRepository.save(bankAccount);
                }).then(bankAccountRepository.findByBankAccountNumber(bankTransfer.getBankAccountNumberDestination()))
                .flatMap(bankAccount -> {
                    Double amountCurrent = bankAccount.getBankAccountAmount();
                    bankAccount.setBankAccountAmount(amountCurrent+bankTransfer.getAmount());
                    return bankAccountRepository.save(bankAccount);
                }).then();
    }
}
