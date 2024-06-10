package com.example.picpay.dto;

import java.math.BigDecimal;

import com.example.picpay.usuario.TipoDeUsuario;

public record DadosCadastroUsuarioDTO(
    String nomeCompleto,
    String cpf,
    String email,
    String senha,
    BigDecimal saldo,
    TipoDeUsuario tipoDeUsuario) {

}
