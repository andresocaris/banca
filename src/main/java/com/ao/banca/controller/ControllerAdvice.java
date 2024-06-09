package com.ao.banca.controller;

import com.ao.banca.exception.BadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping("/api/v1")
public class ControllerAdvice {
    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<String> handleException(BadRequest ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
