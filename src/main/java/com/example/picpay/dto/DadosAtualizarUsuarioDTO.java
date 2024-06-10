package com.example.picpay.dto;

import org.springframework.lang.NonNull;

import com.example.picpay.usuario.TipoDeUsuario;

public record DadosAtualizarUsuarioDTO(
    @NonNull
    Long id,
    
    String nomeCompleto, 
    String cpf, 
    String email, 
    String senha, 
    TipoDeUsuario tipoDeUsuario) {

}
