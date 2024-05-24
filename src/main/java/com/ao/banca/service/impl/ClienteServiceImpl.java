package com.ao.banca.service.impl;

import com.ao.banca.dto.request.CreacionCuentaBancariaDtoRequest;
import com.ao.banca.dto.response.CreacionCuentaBancariaDtoResponse;
import com.ao.banca.model.Cliente;
import com.ao.banca.model.CuentaBancaria;
import com.ao.banca.repository.ClienteRepository;
import com.ao.banca.service.ClienteService;
import com.ao.banca.service.CuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    CuentaBancariaService cuentaBancariaService;

    @Override

    public Flux<Cliente> getClients() {
        return clienteRepository.findAll();
    }

    @Override
    public Mono<Cliente> saveClient(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Mono<ResponseEntity<CreacionCuentaBancariaDtoResponse>> creacionCuentaBancaria(CreacionCuentaBancariaDtoRequest creacionCuentaBancariaDtoRequest) {

        return creacionCliente(
                        creacionCuentaBancariaDtoRequest.getDni(),
                        creacionCuentaBancariaDtoRequest.getNombre(),
                        creacionCuentaBancariaDtoRequest.getDni())
                .zipWith(
                        cuentaBancariaService.creacionCuentaBancaria(
                        creacionCuentaBancariaDtoRequest.getDni(),
                        creacionCuentaBancariaDtoRequest.getNombreCuentaBancaria()
                ))
                .flatMap(tuple -> {
                    CreacionCuentaBancariaDtoResponse response = CreacionCuentaBancariaDtoResponse.builder()
                            .cliente(tuple.getT1())
                            .cuentaBancaria(tuple.getT2())
                            .build();
                    return Mono.just(ResponseEntity.ok(response));
                })
                .onErrorResume(ex ->
                        Mono.just(
                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                        ));


    }
    @Override
    public Mono<Cliente> creacionCliente(String dni,String nombre,String apellido) {
        return Mono.just(Cliente.builder()
                        .dni(dni)
                        .nombre(nombre)
                        .apellido(apellido)
                        .build())
                .flatMap(clienteRepository::save);
    }

}
