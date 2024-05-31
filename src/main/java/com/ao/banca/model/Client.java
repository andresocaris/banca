package com.ao.banca.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
@Data
@Document
@Builder
public class Client {
    @Id
    private String id;
    private String firstName;
    private String lastname;
    private String dni;
    private String rucEmpresa;
    private String clientType;
    private List<BankAccount> bankAccounts;
    private List<Credit> credits;
}
