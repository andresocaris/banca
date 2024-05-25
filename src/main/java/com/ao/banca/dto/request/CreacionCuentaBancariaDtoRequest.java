package com.ao.banca.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreacionCuentaBancariaDtoRequest {
    private String dni;
    private String nombre;
    private String apellido;
    private String nombreCuentaBancaria;
    private String tipoCliente;
    private String rucEmpresa;
}
