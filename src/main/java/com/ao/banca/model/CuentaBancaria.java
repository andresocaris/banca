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
    String id;
    String nombreCuentaBancaria;
    Double montoCuentaBancaria;
    String numeroCuentaBancaria;
}
