package com.ao.banca.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document
@Builder
public class Cliente {
    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String dni;
    private String tipoCliente;
    private List<CuentaBancaria> cuentasBancaria;
    private List<Credito> creditos;
}
