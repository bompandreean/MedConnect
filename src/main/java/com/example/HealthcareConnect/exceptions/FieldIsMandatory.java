package com.example.HealthcareConnect.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason = "Field is mandatory")
public class FieldIsMandatory extends RuntimeException{
    public FieldIsMandatory(String msg){
        super(msg);
    }
}
