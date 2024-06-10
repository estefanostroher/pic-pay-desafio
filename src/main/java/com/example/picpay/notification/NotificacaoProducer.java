package com.example.picpay.notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.picpay.dto.DadosListagemNotificacaoDTO;

@Service
public class NotificacaoProducer {
    
    private KafkaTemplate<String, DadosListagemNotificacaoDTO> kafkaTemplate;

    public NotificacaoProducer(KafkaTemplate<String, DadosListagemNotificacaoDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Método responsável por enviar a notificação para o kafka.
     * @param transacao Dados da transação que deseja ser notificada.
     * @author Estefano Ströher
     */
    public void enviarNotificacao(DadosListagemNotificacaoDTO transacao) {
        this.kafkaTemplate.send("transacao-notificacao", transacao);
    }
}
