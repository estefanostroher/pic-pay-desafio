package com.example.picpay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.picpay.dto.AutorizacaoDTO;
import com.example.picpay.dto.DadosCadastroTransacaoDTO;
import com.example.picpay.infra.TransacaoNaoAutorizadaException;

@Service
public class AutorizacaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutorizacaoService.class);
    private RestClient restClient;

    /**
     * Construtor que irá injetar o builder de RestClient. Construção do cliente passando uma url base.
     * @author Estefano Ströher
     * @param builder Builder de URIs.
     * 
     */
    public AutorizacaoService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://util.devi.tools/api/v2/authorize")
                .build();
    }

    /**
     * Retorna a chamada do serviço autorizador (resposta assíncrona) . Se a resposta for positiva, indica que a transação foi autorizada.
     * @author Estefano Ströher
     * @param transacao Dados da transação que deseja ser autorizada.
     */
    public void autorizarTransacao(DadosCadastroTransacaoDTO transacao) {
        LOGGER.info("Autorizando transação {}...", transacao);
        var resposta = restClient.get()
                .retrieve()
                .toEntity(AutorizacaoDTO.class);

        if(resposta.getStatusCode().isError() || !resposta.getBody().isAutorizado())
            throw new TransacaoNaoAutorizadaException("Transação não autorizada no método \"autorizarTransacao\" na classe \"AutorizacaoService\".");

        LOGGER.info("Transação {} autorizada!", transacao);
    }
}
