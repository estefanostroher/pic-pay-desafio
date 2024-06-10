package com.example.picpay.dto;

public record AutorizacaoDTO(String message) {

    public Boolean isAutorizado() {
        return true;
        //return message.equals("Autorizado");
    }
}
