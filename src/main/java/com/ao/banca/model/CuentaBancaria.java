package com.ao.banca.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class CuentaBancaria {
    @Id
    private String nombreCuentaBancaria;
    private Double montoCuentaBancaria;
    private String numeroCuentaBancaria;
    private String dniCliente;
    private String rucEmpresa;
}
