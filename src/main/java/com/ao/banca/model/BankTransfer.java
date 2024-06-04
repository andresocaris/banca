package com.ao.banca.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class BankTransfer {
    @Id
    public String id;
    public String bankAccountNumberOrigin;
    public String bankAccountNumberDestination;
    public String transferType;
    public Double amount;
    public LocalDateTime transferDateTime;
}
