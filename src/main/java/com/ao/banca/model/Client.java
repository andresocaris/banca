package com.ao.banca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    private String id;
    private String firstName;
    private String lastname;
    private String dni;
    private String rucCompany;
    private String clientType;
    private List<BankAccount> bankAccounts;
    private List<Credit> credits;
}
