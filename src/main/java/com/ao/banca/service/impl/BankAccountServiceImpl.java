package com.ao.banca.service.impl;

import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.exception.BadRequest;
import com.ao.banca.model.BankAccount;
import com.ao.banca.model.Client;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.repository.ClientRepository;
import com.ao.banca.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    public static final String TIPO_CUENTA_PARA_SOLO_EMPRESAS = "Cuenta Corriente";

    @Autowired
    private BankAccountRepository bankAccountRepository;
    private Random random = new Random();
    @Autowired
    ClientRepository clientRepository;
    @Override
    public Mono<BankAccount> createBankAccount(String dni, String bankAccountName, String rucClient) {
        return  generateValidBankAccountNumber()
                .flatMap(createdBankAccountNumber->
                    createBankAccountWithNumber(dni,bankAccountName,rucClient,createdBankAccountNumber)
                )
                .flatMap(bankAccount -> bankAccountRepository.save(bankAccount)
                ).doOnNext(bankAccountSaved -> log.info("cuentaBancariaCreada:"+bankAccountSaved));
    }

    @Override
    public Mono<Boolean> validateBankAccountTransfer(String bankAccountNumber,Double amount) {
        Mono<BankAccount> bankAccountMono = bankAccountRepository.findByBankAccountNumber(bankAccountNumber);
        return bankAccountMono.flatMap(bankAccount ->{
             if (bankAccount.getBankAccountAmount()>=amount){
                 return Mono.just(true);
             }else{
                 return Mono.error(new BadRequest("no hay suficiente limite de dinero"));
             }
        }).hasElement();
    }

    @Override
    public Mono<ResponseEntity<Flux<BankAccount>>> getBankAccounts() {
        return Mono.just(ResponseEntity.ok(bankAccountRepository.findAll()));

    }

    @Override
    public Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccountByRequest(BankAccountCreationRequestDto bankAccountCreationRequestDto) {
        Mono<Client> clienteFoundByDniOrRuc = findOrCreateClientByDniOrRuc(bankAccountCreationRequestDto.getClientType(),bankAccountCreationRequestDto.getDni(),bankAccountCreationRequestDto.getRucCompany());
        return validateCreateBankAccountDto(bankAccountCreationRequestDto).then(
                        clienteFoundByDniOrRuc.hasElement()
                ).flatMap(exist -> {
                    if ( !exist.booleanValue()) {
                        return createClient(
                                bankAccountCreationRequestDto.getDni(), bankAccountCreationRequestDto.getFirstName(),
                                bankAccountCreationRequestDto.getLastName(), bankAccountCreationRequestDto.getRucCompany(),
                                bankAccountCreationRequestDto.getClientType()
                        );
                    }else {
                        return clienteFoundByDniOrRuc;
                    }

                })
                .zipWith(createBankAccount(bankAccountCreationRequestDto.getDni(), bankAccountCreationRequestDto.getBankAccountName(), bankAccountCreationRequestDto.getRucCompany())
                )
                .flatMap(tuple -> addBankAccountToClient(tuple.getT1(), tuple.getT2())
                        .then( createBankAccountCreationResponseDto(tuple.getT1(), tuple.getT2()))
                )
                .onErrorResume(ex -> {
                    log.error(ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                    );
                });
    }
    public Mono<Client> createClient(String dni, String firstName, String lastName, String rucCompany,String clientType) {
        return Mono.just(Client.builder()
                        .dni(dni)
                        .firstName(firstName)
                        .lastname(lastName)
                        .rucCompany(rucCompany)
                        .clientType(clientType)
                        .build())
                .flatMap(clientRepository::save);
    }
    private Mono<Client> findOrCreateClientByDniOrRuc(String clientType, String dni, String rucCompany) {
        Mono<Client> clientFound;
        if ( clientType.equals("Personal")){
            clientFound = clientRepository.findByDni(dni);
        }else{
            clientFound = clientRepository.findByRucCompany(rucCompany);
        }
        return clientFound;
    }
    public Mono<Void> validateCreateBankAccountDto(BankAccountCreationRequestDto bankAccountCreationRequestDto) {
        String dni = bankAccountCreationRequestDto.getDni();
        String bankAccountType = bankAccountCreationRequestDto.getBankAccountName();
        String category = bankAccountCreationRequestDto.getClientType();
        if (category.equals("Empresarial") && !bankAccountType.equals(TIPO_CUENTA_PARA_SOLO_EMPRESAS)) {
            return Mono.error(new RuntimeException("No puede existir ese tipo de cuenta para el cliente empresarial"));
        }
        if (!Arrays.asList("Cuenta Ahorro", TIPO_CUENTA_PARA_SOLO_EMPRESAS, "Cuenta Plazo Fijo").contains(bankAccountType)) {
            return Mono.error(new RuntimeException("No exist ese tipo de cuenta para crear"));
        }
        if (category.equals("Personal")) {
            return bankAccountRepository.findByBankAccountNameAndDniClient(bankAccountType, dni)
                    .hasElement()
                    .flatMap(exists -> {
                        if ((bankAccountType.equals("Cuenta Ahorro") || bankAccountType.equals(TIPO_CUENTA_PARA_SOLO_EMPRESAS)) && exists.booleanValue()) {
                            return Mono.error(new RuntimeException("Ya existe"));
                        } else {
                            return Mono.empty();
                        }
                    });
        }

        return Mono.empty();
    }
    public Mono<Void> addBankAccountToClient(Client client, BankAccount bankAccount) {
        return Mono.just(client)
                .flatMap(clientToSaved -> {
                    if (clientToSaved.getBankAccounts() == null) {
                        clientToSaved.setBankAccounts(new ArrayList<>());
                    }
                    clientToSaved.getBankAccounts().add(bankAccount);
                    return clientRepository.save(clientToSaved);
                })
                .then();
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
    public Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccountCreationResponseDto(Client client, BankAccount bankAccount){
        BankAccountCreationResponseDto response = BankAccountCreationResponseDto.builder()
                .client(client)
                .bankAccount(bankAccount)
                .build();
        return Mono.just(ResponseEntity.ok(response));
    }
}
