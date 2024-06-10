package com.example.picpay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.picpay.transacao.Transacao;

public record DadosListagemTransacaoDTO(Long id, Long remetenteId, Long destinatarioId, BigDecimal valor, LocalDateTime data, Boolean ativo) {

    public DadosListagemTransacaoDTO(Transacao transacao) {
        this(transacao.getId(), transacao.getRemetenteId().getId(), transacao.getDestinatarioId().getId(), transacao.getValor(), transacao.getData(), transacao.getAtivo());
    }
}
