package com.ao.banca.service.impl;

import com.ao.banca.dto.request.CreacionCuentaBancariaDtoRequest;
import com.ao.banca.dto.response.CreacionCuentaBancariaDtoResponse;
import com.ao.banca.model.Cliente;
import com.ao.banca.model.Credito;
import com.ao.banca.model.CuentaBancaria;
import com.ao.banca.repository.ClienteRepository;
import com.ao.banca.repository.CreditoRepository;
import com.ao.banca.repository.CuentaBancariaRepository;
import com.ao.banca.service.ClienteService;
import com.ao.banca.service.CuentaBancariaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService {
    public static final String TIPO_CUENTA_PARA_SOLO_EMPRESAS="Cuenta Corriente";
    ClienteRepository clienteRepository;
    CuentaBancariaRepository cuentaBancariaRepository;
    CuentaBancariaService cuentaBancariaService;
    CreditoRepository creditoRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, CuentaBancariaRepository cuentaBancariaRepository, CuentaBancariaService cuentaBancariaService, CreditoRepository creditoRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.cuentaBancariaService = cuentaBancariaService;
        this.creditoRepository = creditoRepository;
    }

    @Override
    public Flux<Cliente> getClients() {
        return clienteRepository.findAll();
    }

    @Override
    public Mono<Cliente> saveClient(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Mono<Credito> saveCredito(Credito credito) {
        return creditoRepository.save(credito);
    }

    @Override
    public Mono<ResponseEntity<CreacionCuentaBancariaDtoResponse>> creacionCuentaBancaria(CreacionCuentaBancariaDtoRequest creacionCuentaBancariaDtoRequest) {
        validateCreateBankAccountDto(creacionCuentaBancariaDtoRequest).subscribe();
        Mono<Cliente> clientFoundByDni
                = clienteRepository.findByDni(creacionCuentaBancariaDtoRequest.getDni());

        return clientFoundByDni.hasElement()
                .flatMap(existe -> {
                    if (!existe.booleanValue()) {
                        return creacionCliente(
                                creacionCuentaBancariaDtoRequest.getDni(),
                                creacionCuentaBancariaDtoRequest.getNombre(),
                                creacionCuentaBancariaDtoRequest.getApellido(),
                                creacionCuentaBancariaDtoRequest.getRucEmpresa()
                        )
                        .doOnSuccess(cliente -> log.info("Cliente creado exitosamente"))
                        .doOnError(error -> log.error("Error al crear cliente", error));
                    } else {
                        return clientFoundByDni
                                .doOnSuccess(cliente -> log.warn("Cliente ya existe"));
                    }
                })
                .zipWith(
                        cuentaBancariaService.creacionCuentaBancaria(
                        creacionCuentaBancariaDtoRequest.getDni(),
                        creacionCuentaBancariaDtoRequest.getNombreCuentaBancaria(),
                        creacionCuentaBancariaDtoRequest.getRucEmpresa()
                ))
                .flatMap(tuple -> {
                    CreacionCuentaBancariaDtoResponse response = CreacionCuentaBancariaDtoResponse.builder()
                            .cliente(tuple.getT1())
                            .cuentaBancaria(tuple.getT2())
                            .build();
                    Cliente cliente = tuple.getT1();
                    CuentaBancaria cuentaBancaria = tuple.getT2();
                    List<CuentaBancaria> list = new ArrayList<>();
                    if (cliente.getCuentasBancaria()==null){
                        cliente.setCuentasBancaria(list);
                    }
                    cliente.getCuentasBancaria().add(cuentaBancaria);
                    clienteRepository.save(cliente).block();
                    return Mono.just(ResponseEntity.ok(response));
                })
                .onErrorResume(ex -> {
                            log.error(ex.getMessage());
                            return Mono.just(
                                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()

                            );
                        }
                );

    }

    public Mono<Void> validateCreateBankAccountDto(CreacionCuentaBancariaDtoRequest creacionCuentaBancariaDtoRequest) {
        String dni = creacionCuentaBancariaDtoRequest.getDni();
        String bankAccountType = creacionCuentaBancariaDtoRequest.getNombreCuentaBancaria();
        String category = creacionCuentaBancariaDtoRequest.getTipoCliente();
        if (category.equals("Empresarial") && !bankAccountType.equals(TIPO_CUENTA_PARA_SOLO_EMPRESAS)) {
            return Mono.error(new RuntimeException("No puede existir ese tipo de cuenta para el cliente empresarial"));
        }
        if (!Arrays.asList("Cuenta Ahorro", TIPO_CUENTA_PARA_SOLO_EMPRESAS, "Cuenta Plazo Fijo").contains(bankAccountType)) {
            return Mono.error(new RuntimeException("No exist ese tipo de cuenta para crear"));
        }
        if (category.equals("Personal")) {
            return cuentaBancariaRepository.findByNombreCuentaBancariaAndDniCliente(bankAccountType, dni)
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
    public Mono<Cliente> creacionCliente(String dni,String nombre,String apellido,String rucEmpresa) {
        return Mono.just(Cliente.builder()
                        .dni(dni)
                        .nombre(nombre)
                        .apellido(apellido)
                        .rucEmpresa(rucEmpresa)
                        .build())
                .flatMap(clienteRepository::save);
    }

    @Override
    public Mono<ResponseEntity<Credito>> creacionCredito(Credito credito) {
        return creditoRepository.save(credito)
                .flatMap(creditoCreado ->  Mono.just(ResponseEntity.ok(creditoCreado)))
                .onErrorResume(ex-> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                );
    }

}
