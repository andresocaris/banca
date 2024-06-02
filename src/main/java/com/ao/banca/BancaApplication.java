package com.ao.banca;

import com.ao.banca.model.Client;
import com.ao.banca.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BancaApplication {
	public static void main(String[] args) {
		SpringApplication.run(BancaApplication.class, args);

	}
}
