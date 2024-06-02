package com.ao.banca.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public class ClientDto {
    private String id;
    private String firstName;
    private String lastname;
    private String dni;
    private String rucCompany;
    private String clientType;
}
