package com.ao.banca.dto.request;

import lombok.Data;

@Data
public class CreditoDto {
    private String id;
    private String creditType;
    private String companyRuc;
    private String dni;
    private Double consumed;
    private Double creditAmount;
}
