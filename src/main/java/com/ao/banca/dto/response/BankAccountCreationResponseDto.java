package com.ao.banca.dto.response;

import com.ao.banca.model.Client;
import com.ao.banca.model.BankAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountCreationResponseDto {
    private Client client;
    private BankAccount bankAccount;
}
