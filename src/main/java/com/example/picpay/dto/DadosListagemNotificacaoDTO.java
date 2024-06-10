package com.example.picpay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.picpay.transacao.Transacao;
import com.example.picpay.usuario.Usuario;

public record DadosListagemNotificacaoDTO(String remetenteNomeCompleto, BigDecimal valor, LocalDateTime data) {

    public DadosListagemNotificacaoDTO(Transacao transacao, Usuario remetente) {
        this(remetente.getNomeCompleto(), transacao.getValor(), transacao.getData());
    }
}
