package com.example.picpay.infra;

public class TransacaoEntreMesmosUsuariosException extends RuntimeException {

    public TransacaoEntreMesmosUsuariosException(String message) {
        super(message);
    }
}
