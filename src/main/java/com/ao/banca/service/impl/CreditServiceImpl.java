package com.ao.banca.service.impl;

import com.ao.banca.dto.request.CreditDto;
import com.ao.banca.exception.BadRequest;
import com.ao.banca.model.Client;
import com.ao.banca.model.Credit;
import com.ao.banca.repository.ClientRepository;
import com.ao.banca.repository.CreditRepository;
import com.ao.banca.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
@Service
public class CreditServiceImpl implements CreditService {
    @Autowired
    CreditRepository creditRepository;
    @Autowired
    ClientRepository clientRepository;
    @Override
    public Mono<ResponseEntity<Credit>> createCredit(CreditDto creditDto) {
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

    @Override
    public Mono<ResponseEntity<Flux<Credit>>> getCredits() {
        return Mono.just(ResponseEntity.ok(creditRepository.findAll()));
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
}
