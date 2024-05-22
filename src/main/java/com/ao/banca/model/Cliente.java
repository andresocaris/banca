package com.ao.banca.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Cliente {
    @Id
    String id;
    String nombre;
    String tipoCliente;
    List<CuentaBancaria> cuentasBancaria;
    List<Credito> creditos;
}
