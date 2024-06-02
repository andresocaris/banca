package com.ao.banca.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class BankAccount {
    @Id
    private String id;
    private String bankAccountName;
    private Double bankAccountAmount;
    private String bankAccountNumber;
    private String dniClient;
    private String rucCompany;
}
