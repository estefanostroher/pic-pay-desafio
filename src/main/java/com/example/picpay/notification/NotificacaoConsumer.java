package com.example.picpay.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestClient;

import com.example.picpay.dto.NotificacaoDTO;
import com.example.picpay.infra.NotificacaoException;
import com.example.picpay.transacao.Transacao;

public class NotificacaoConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacaoConsumer.class);
    private RestClient restClient;

    /**
     * Construtor que inicializa o RestClient para a chamada do serviço notificador.
     * @param builder
     * @author Estefano Ströher
     */
    public NotificacaoConsumer(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://util.devi.tools/api/v1/notify")
                .build();
    }

    /**
     * Método responsável por receber as notificações vindas do kafka.
     * @param transacao Dados da transação que deseja ser notificada.
     * @author Estefano Ströher
     */
    @KafkaListener(topics = "transacao-notificacao", groupId = "picpay-desafio")
    public void receberNotificacao(Transacao transacao) {
        var resposta = restClient.post()
                .retrieve()
                .toEntity(NotificacaoDTO.class);
        
        if(resposta.getStatusCode().isError() || !resposta.getBody().mensagem())
            throw new NotificacaoException("Falha ao notificar transação.");

        LOGGER.info("Notificação foi recebida {}...", resposta.getBody());
    }
}
 