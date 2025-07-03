package com.senai.reservei.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CheckoutSoPodeSerRealizadoAposCheckinException extends RuntimeException{

    public CheckoutSoPodeSerRealizadoAposCheckinException() {
        super("Checkout só pode ser realizado apos o Checkin");
    }
}
