package com.ao.banca.controller;

import com.ao.banca.dto.request.CreditDto;
import com.ao.banca.model.Credit;
import com.ao.banca.service.ClientService;
import com.ao.banca.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class CreditController {
    @Autowired
    CreditService creditService;
    @PostMapping("/create-credit")
    public Mono<ResponseEntity<Credit>> createCredit(@RequestBody CreditDto creditDto){
        return creditService.createCredit(creditDto);
    }
    @GetMapping("/credits")
    public Mono<ResponseEntity<Flux<Credit>>> getCredits(){
        return creditService.getCredits();
    }
}
