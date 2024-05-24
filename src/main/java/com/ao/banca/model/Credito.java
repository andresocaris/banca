package com.ao.banca.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Credito {
    @Id
    String id;
    String nombreCredito;
    Double montoCredito;
    Double consumido;
}
