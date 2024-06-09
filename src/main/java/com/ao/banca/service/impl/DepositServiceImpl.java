package com.ao.banca.service.impl;

import com.ao.banca.dto.request.BankDepositRequestDto;
import com.ao.banca.model.BankDeposit;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.repository.BankDepositRepository;
import com.ao.banca.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class DepositServiceImpl implements DepositService {
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    BankDepositRepository bankDepositRepository;
    @Override
    public Mono<ResponseEntity<String>> deposit(BankDepositRequestDto bankDepositRequestDto) {

        return Mono.just(bankDepositRequestDto.getBankAccountNumber())
                .flatMap(bankAccountNumber -> bankAccountRepository.findByBankAccountNumber(bankAccountNumber))
                .flatMap(bankAccount -> {
                    Double amountCurrent = bankAccount.getBankAccountAmount();
                    bankAccount.setBankAccountAmount(amountCurrent+bankDepositRequestDto.getAmount());
                    return bankAccountRepository.save(bankAccount);
                }).flatMap(bankAccount -> {
                    BankDeposit bankDeposit = new BankDeposit();
                    bankDeposit.setBankAccountNumber(bankAccount.getBankAccountNumber());
                    bankDeposit.setAmount(bankDepositRequestDto.getAmount());
                    bankDeposit.setTransferDateTime(LocalDateTime.now());
                    return bankDepositRepository.save(bankDeposit);
                })
                .flatMap(bankAccount->Mono.just(ResponseEntity.ok("Deposited")));
    }
}
