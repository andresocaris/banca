package com.ao.banca.dto.request;

import lombok.Data;

@Data
public class BankTransferRequestDto {
    public String bankAccountNumberOrigin;
    public String bankAccountNumberDestination;
    public String transferType;
    public Double amount;
}
