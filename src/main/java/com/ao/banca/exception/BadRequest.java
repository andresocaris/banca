package com.ao.banca.exception;

public class BadRequest extends RuntimeException{
    public BadRequest() {
        super();
    }
    public BadRequest(String msg){
        super(msg);
    }
}
