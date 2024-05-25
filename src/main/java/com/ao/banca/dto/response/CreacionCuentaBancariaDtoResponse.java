package com.ao.banca.dto.response;

import com.ao.banca.model.Cliente;
import com.ao.banca.model.CuentaBancaria;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreacionCuentaBancariaDtoResponse {
    private Cliente cliente;
    private CuentaBancaria cuentaBancaria;
}
