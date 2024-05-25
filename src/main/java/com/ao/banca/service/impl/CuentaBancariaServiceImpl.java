package com.ao.banca.service.impl;

import com.ao.banca.model.CuentaBancaria;
import com.ao.banca.repository.CuentaBancariaRepository;
import com.ao.banca.service.CuentaBancariaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
@Slf4j
public class CuentaBancariaServiceImpl implements CuentaBancariaService {
    private CuentaBancariaRepository cuentaBancariaRepository;
    private Random random = new Random();

    public CuentaBancariaServiceImpl(CuentaBancariaRepository cuentaBancariaRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
    }
    @Override
    public Mono<CuentaBancaria> creacionCuentaBancaria(String dni, String nombreCuentaBancaria, String rucEmpresa) {
        return  generarNumeroCuentaBancaria()
                .flatMap(numeroCuentaBancariaCreada-> {
                    CuentaBancaria cuentaBancaria = CuentaBancaria.builder()
                            .nombreCuentaBancaria(nombreCuentaBancaria)
                            .montoCuentaBancaria(0.0)
                            .numeroCuentaBancaria(numeroCuentaBancariaCreada)
                            .dniCliente(dni)
                            .rucEmpresa(rucEmpresa)
                            .build();
                    return Mono.just(cuentaBancaria);
                })
                  .flatMap(cuentaBancariaRepository::save)
                  .doOnNext(cuentaBancariaGuardada -> log.info("cuentaBancariaCreada:"+cuentaBancariaGuardada));
    }
    public  Mono<String> generarNumeroCuentaBancariaValida() {
       return Flux.range(0, Integer.MAX_VALUE)
                .flatMap(numero -> generarNumeroCuentaBancaria())
                .filter( numeroCuenta -> cuentaBancariaRepository.findByNumeroCuentaBancaria(numeroCuenta).hasElement().block() )
                .take(1)
                .single();
    }
    public Mono<String> generarNumeroCuentaBancaria() {
        return Flux.range(0, 14)
                .map(i -> random.nextInt() * 10)
                .map(String::valueOf)
                .reduce(String::concat)
                .flux()
                .single();
    }
}
