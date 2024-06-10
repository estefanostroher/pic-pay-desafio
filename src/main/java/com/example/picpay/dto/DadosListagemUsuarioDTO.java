package com.example.picpay.dto;

import java.math.BigDecimal;

import com.example.picpay.usuario.TipoDeUsuario;
import com.example.picpay.usuario.Usuario;

public record DadosListagemUsuarioDTO(Long id, String nomeCompleto, String email, BigDecimal saldo, TipoDeUsuario tipoDeUsuario, Boolean ativo) {

    public DadosListagemUsuarioDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getSaldo(),
                usuario.getTipoDeUsuario(),
                usuario.getAtivo()
        );
    }
}
