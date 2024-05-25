package com.ao.banca.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Credito {
    @Id
    private String id;
    private String tipoCredito;
    private String rucEmpresa;
    private String dni;
    private Double consumido;
    private Double montoCredito;
}
