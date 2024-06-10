package com.example.picpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.picpay.transacao.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
    List<Transacao> findAllByAtivoTrue();
}
