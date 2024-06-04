package com.ao.banca.service.impl;

import com.ao.banca.dto.request.BankDepositRequestDto;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class DepositServiceImpl implements DepositService {
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Override
    public Mono<ResponseEntity<String>> deposit(BankDepositRequestDto bankDepositRequestDto) {
        return Mono.just(bankDepositRequestDto.getBankAccountNumber())
                .flatMap(bankAccountNumber -> bankAccountRepository.findByBankAccountNumber(bankAccountNumber))
                .flatMap(bankAccount -> {
                    Double amountCurrent = bankAccount.getBankAccountAmount();
                    bankAccount.setBankAccountAmount(amountCurrent+bankDepositRequestDto.getAmount());
                    return bankAccountRepository.save(bankAccount);
                })
                .flatMap(bankAccount->Mono.just(ResponseEntity.ok("Deposited")));
    }
}
