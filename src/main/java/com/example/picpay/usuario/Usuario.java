package com.example.picpay.usuario;

import java.math.BigDecimal;

import com.example.picpay.dto.DadosAtualizarUsuarioDTO;
import com.example.picpay.dto.DadosCadastroUsuarioDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

@Entity(name = "Usuarios")
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", unique = true, nullable = false)
    private String nomeCompleto;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoDeUsuario tipoDeUsuario;

    @Column(nullable = false)
    private Boolean ativo;

    public Usuario(DadosCadastroUsuarioDTO dados) {
        this.nomeCompleto = dados.nomeCompleto();
        this.cpf = dados.cpf();
        this.email = dados.email();
        this.senha = dados.senha();
        this.saldo = dados.saldo();
        this.tipoDeUsuario = dados.tipoDeUsuario();
        this.ativo = true;
    }

    public void inativarUsuario() {
        this.ativo = false;
    }

    public void ativarUsuario() {
        this.ativo = true;
    }

    public void atualizarUsuario(DadosAtualizarUsuarioDTO dados) {
        if(dados.nomeCompleto() != null)
            this.nomeCompleto = dados.nomeCompleto();
        if(dados.cpf() != null)
            this.cpf = dados.cpf();
        if(dados.email() != null)
            this.email = dados.email();
        if(dados.senha() != null)
            this.senha = dados.senha();
        if(dados.tipoDeUsuario() != null)
            this.tipoDeUsuario = dados.tipoDeUsuario();
    }
}
