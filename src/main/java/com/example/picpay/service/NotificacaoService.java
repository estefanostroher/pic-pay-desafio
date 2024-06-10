package com.example.picpay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.picpay.dto.DadosListagemNotificacaoDTO;
import com.example.picpay.notification.NotificacaoProducer;

@Service
public class NotificacaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacaoService.class);
    private NotificacaoProducer notificacaoProducer;

    /**
     * Construtor da classe NotificacaoService.
     * 
     * @param notificacaoProducer O produtor de notificações responsável por enviar notificações para o Kafka.
     */
    public NotificacaoService(NotificacaoProducer notificacaoProducer) {
        this.notificacaoProducer = notificacaoProducer;
    }

    /**
     * Notifica uma transação enviando os dados relevantes para o Kafka.
     * 
     * @param transacao Os dados da transação a serem notificados.
     */
    public void notificarTransacao(DadosListagemNotificacaoDTO transacao) {
        LOGGER.info("Notificando transação {}...", transacao);
        notificacaoProducer.enviarNotificacao(transacao);
        LOGGER.info("Transação {} notificada!", transacao);
    }
}
