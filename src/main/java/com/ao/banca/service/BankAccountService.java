package com.ao.banca.service;

import com.ao.banca.model.BankAccount;
import reactor.core.publisher.Mono;

public interface BankAccountService {
    Mono<BankAccount> createBankAccount(String dni, String bankAccountName, String rucClient);
}
