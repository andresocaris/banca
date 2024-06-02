package com.ao.banca.service.impl;

import com.ao.banca.dto.ClientDto;
import com.ao.banca.dto.request.BankAccountCreationRequestDto;
import com.ao.banca.dto.request.CreditoDto;
import com.ao.banca.dto.response.BankAccountCreationResponseDto;
import com.ao.banca.exception.BadRequest;
import com.ao.banca.model.Client;
import com.ao.banca.model.Credit;
import com.ao.banca.model.BankAccount;
import com.ao.banca.repository.ClientRepository;
import com.ao.banca.repository.BankAccountRepository;
import com.ao.banca.repository.CreditRepository;
import com.ao.banca.service.ClientService;
import com.ao.banca.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    public static final String TIPO_CUENTA_PARA_SOLO_EMPRESAS = "Cuenta Corriente";
    ClientRepository clientRepository;
    BankAccountRepository bankAccountRepository;
    BankAccountService bankAccountService;
    CreditRepository creditRepository;

    public ClientServiceImpl(ClientRepository clientRepository, BankAccountRepository bankAccountRepository, BankAccountService bankAccountService, CreditRepository creditRepository) {
        this.clientRepository = clientRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
        this.creditRepository = creditRepository;
    }

    @Override
    public Flux<Client> getClients() {
        return clientRepository.findAll();
    }

    @Override
    public Mono<ClientDto> saveClient(ClientDto clientDto) {
        Client client = new Client();
        BeanUtils.copyProperties( clientDto,client);
        Mono<Client> clientSaved =  clientRepository.save(client);
        return clientSaved.flatMap(clientSaved1->{
            ClientDto clientDtoCreated = new ClientDto();
            BeanUtils.copyProperties( clientSaved1,clientDtoCreated);
            return Mono.just(clientDtoCreated);
        });
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
        .zipWith(bankAccountService.createBankAccount(bankAccountCreationRequestDto.getDni(), bankAccountCreationRequestDto.getBankAccountName(), bankAccountCreationRequestDto.getRucCompany())
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

    private Mono<Client> findOrCreateClientByDniOrRuc(String clientType, String dni, String rucCompany) {
        Mono<Client> clientFound;
        if ( clientType.equals("Personal")){
            clientFound = clientRepository.findByDni(dni);
        }else{
            clientFound = clientRepository.findByRucCompany(rucCompany);
        }
        return clientFound;
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
    public Mono<ResponseEntity<BankAccountCreationResponseDto>> createBankAccountCreationResponseDto(Client client, BankAccount bankAccount){
        BankAccountCreationResponseDto response = BankAccountCreationResponseDto.builder()
                .client(client)
                .bankAccount(bankAccount)
                .build();
        return Mono.just(ResponseEntity.ok(response));
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

    @Override
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

    @Override
    public Mono<ResponseEntity<Credit>> createCredit(CreditoDto creditDto) {

        Credit credit = new Credit();
        credit.setCreditAmount(creditDto.getCreditAmount());
        credit.setCreditType(creditDto.getCreditType());
        credit.setDni(creditDto.getDni());
        credit.setRucCompany(creditDto.getCompanyRuc());

        Mono<Client> clienteFoundByDniOrRuc = findOrCreateClientByDniOrRuc(creditDto.getCreditType(),creditDto.getDni(),creditDto.getCompanyRuc());

        if ( !clienteFoundByDniOrRuc.hasElement().block() )  throw new BadRequest("el cliente no existe");

        return creditRepository.save(credit)
                .flatMap(creditoCreado ->
                {
                    clienteFoundByDniOrRuc.subscribe(cliente-> {

                        if (cliente.getCredits()==null){
                            cliente.setCredits(new ArrayList<>());
                        }
                        cliente.getCredits().add(creditoCreado);
                        clientRepository.save(cliente).subscribe();

                    });
                    return Mono.just(ResponseEntity.ok(creditoCreado));
                })
                .onErrorResume(ex-> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                );
    }


}
