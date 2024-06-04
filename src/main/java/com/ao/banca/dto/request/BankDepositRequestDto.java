package com.ao.banca.dto.request;

import lombok.Data;

@Data
public class BankDepositRequestDto {
    public String bankAccountNumber;
    public Double amount;
}
