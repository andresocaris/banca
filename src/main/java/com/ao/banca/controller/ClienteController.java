package com.ao.banca.controller;

import com.ao.banca.dto.request.CreacionCuentaBancariaDtoRequest;
import com.ao.banca.dto.response.CreacionCuentaBancariaDtoResponse;
import com.ao.banca.model.Cliente;
import com.ao.banca.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ClienteController {
    @Autowired
    ClienteService clienteService;
    @GetMapping("/clientes")
    public Flux<Cliente> getClients(){
        return clienteService.getClients();
    }
    @PostMapping("/cliente")
    public Mono<Cliente> saveClient(@RequestBody Cliente cliente){
        return clienteService.saveClient(cliente);
    }
    @PostMapping("/creacion-cuenta-bancaria")
    public Mono<ResponseEntity<CreacionCuentaBancariaDtoResponse>> creacionCuentaBancaria(@RequestBody CreacionCuentaBancariaDtoRequest creacionCuentaBancariaDtoRequest){
        return clienteService.creacionCuentaBancaria(creacionCuentaBancariaDtoRequest);
    }
}
