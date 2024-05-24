package com.ao.banca.service;

import com.ao.banca.model.CuentaBancaria;
import reactor.core.publisher.Mono;

public interface CuentaBancariaService {
    Mono<CuentaBancaria> creacionCuentaBancaria(String dni, String nombreCuentaBancaria);
}
