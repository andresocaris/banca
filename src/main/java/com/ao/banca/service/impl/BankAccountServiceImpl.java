package com.ao.banca.service.impl;

import com.ao.banca.model.BankAccount;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private Random random = new Random();

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }
    @Override
    public Mono<BankAccount> createBankAccount(String dni, String bankAccountName, String rucClient) {
        return  generateValidBankAccountNumber()
                .flatMap(createdBankAccountNumber-> {
                    BankAccount bankAccount = BankAccount.builder()
                            .bankAccountName(bankAccountName)
                            .bankAccountAmount(0.0)
                            .bankAccountNumber(createdBankAccountNumber)
                            .dniClient(dni)
                            .rucCompany(rucClient)
                            .build();
                    return Mono.just(bankAccount);
                })
                  .flatMap(bankAccountRepository::save)
                  .doOnNext(bankAccountSaved -> log.info("cuentaBancariaCreada:"+bankAccountSaved));
    }
    public  Mono<String> generateValidBankAccountNumber() {
       return Flux.range(0, Integer.MAX_VALUE)
                .flatMap(number -> generateBankAccountNumber())
                .filter( bankAccountNumber -> bankAccountRepository.findByBankAccountNumber(bankAccountNumber).hasElement().block() )
                .take(1)
                .single();
    }
    public Mono<String> generateBankAccountNumber() {
        return Flux.range(0, 14)
                .map(i -> random.nextInt() * 10)
                .map(String::valueOf)
                .reduce(String::concat)
                .flux()
                .single();
    }
}
