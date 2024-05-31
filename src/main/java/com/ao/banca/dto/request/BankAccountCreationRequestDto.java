package com.ao.banca.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountCreationRequestDto {
    private String dni;
    private String firstName;
    private String lastName;
    private String bankAccountName;
    private String clientType;
    private String rucCompany;
}
