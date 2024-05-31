package com.ao.banca.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Credit {
    @Id
    private String id;
    private String creditType;
    private String rucCompany;
    private String dni;
    private Double consumed;
    private Double creditAmount;
}
