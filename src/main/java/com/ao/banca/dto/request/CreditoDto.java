package com.ao.banca.dto.request;

import lombok.Data;

@Data
public class CreditoDto {
    private String id;
    private String tipoCredito;
    private String rucEmpresa;
    private String dni;
    private Double consumido;
    private Double montoCredito;
}
