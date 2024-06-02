package com.ao.banca.service.impl;

import com.ao.banca.model.BankAccount;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    private Random random = new Random();

    @Override
    public Mono<BankAccount> createBankAccount(String dni, String bankAccountName, String rucClient) {
        return  generateValidBankAccountNumber()
                .flatMap(createdBankAccountNumber->
                    createBankAccountWithNumber(dni,bankAccountName,rucClient,createdBankAccountNumber)
                )
                .flatMap(bankAccount -> bankAccountRepository.save(bankAccount)
                ).doOnNext(bankAccountSaved -> log.info("cuentaBancariaCreada:"+bankAccountSaved));
    }

    public  Mono<String> generateValidBankAccountNumber() {
       return Flux.range(0, 100)
                .flatMap(number -> generateBankAccountNumber())
                .filter( bankAccountNumber -> !bankAccountRepository.findByBankAccountNumber(bankAccountNumber).hasElement().block() )
                .take(1)
                .single();
    }

    public Mono<String> generateBankAccountNumber() {
        return Flux.range(0, 14)
                .map(i -> random.nextInt(10) )
                .map(String::valueOf)
                .reduce(String::concat)
                .flux()
                .single();
    }
    private Mono<BankAccount> createBankAccountWithNumber(String dni, String bankAccountName, String rucClient, String createdBankAccountNumber) {
        BankAccount bankAccount = BankAccount.builder()
                .bankAccountName(bankAccountName)
                .bankAccountAmount(0.0)
                .bankAccountNumber(createdBankAccountNumber)
                .dniClient(dni)
                .rucCompany(rucClient)
                .build();
        return Mono.just(bankAccount);
    }
}
