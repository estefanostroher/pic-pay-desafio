package com.example.picpay.infra;

import org.springframework.validation.FieldError;

public record MensagemDosErros(String atributo, String mensagem) {

    public MensagemDosErros(FieldError erro) {
        this(erro.getField(), erro.getDefaultMessage());
    }
}
