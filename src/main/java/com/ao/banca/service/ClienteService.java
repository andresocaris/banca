package com.ao.banca.service;

import com.ao.banca.dto.request.CreacionCuentaBancariaDtoRequest;
import com.ao.banca.dto.response.CreacionCuentaBancariaDtoResponse;
import com.ao.banca.model.Cliente;
import com.ao.banca.model.Credito;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteService {
    Flux<Cliente> getClients();
    Mono<Cliente> saveClient(Cliente cliente);
    Mono<Credito> saveCredito(Credito credito);
    Mono<ResponseEntity<CreacionCuentaBancariaDtoResponse>> creacionCuentaBancaria(CreacionCuentaBancariaDtoRequest creacionCuentaBancariaDtoRequest);
    Mono<Cliente> creacionCliente(String dni,String nombre,String apellido,String rucEmpresa);
    Mono<ResponseEntity<Credito>> creacionCredito(Credito credito);
}
