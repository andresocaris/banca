package com.ao.banca.service.impl;

import com.ao.banca.model.CuentaBancaria;
import com.ao.banca.repository.CuentaBancariaRepository;
import com.ao.banca.service.CuentaBancariaService;
import org.bson.internal.BsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class CuentaBancariaServiceImpl implements CuentaBancariaService {
    @Autowired
    CuentaBancariaRepository cuentaBancariaRepository;
    @Override
    public Mono<CuentaBancaria> creacionCuentaBancaria(String dni, String nombreCuentaBancaria) {
        return Mono.just(CuentaBancaria.builder()
                  .nombreCuentaBancaria(nombreCuentaBancaria)
                  .montoCuentaBancaria(0.0)
                  .numeroCuentaBancaria("19483994274837483")
                  .build())
                  .flatMap(cuentaBancariaRepository::save)
                  .doOnNext(cuentaBancariaGuardada -> System.out.println(cuentaBancariaGuardada));
    }
}
