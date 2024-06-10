package com.example.picpay.infra;

public class TransacaoNaoAutorizadaException extends RuntimeException {

    public TransacaoNaoAutorizadaException(String message) {
        super(message);
    }
}
