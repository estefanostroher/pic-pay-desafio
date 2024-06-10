package com.example.picpay.dto;

import java.math.BigDecimal;

public record DadosCadastroTransacaoDTO(
    Long remetenteId, 
    Long destinatarioId, 
    BigDecimal valor) {

}
