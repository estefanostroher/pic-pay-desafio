package com.example.picpay.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.example.picpay.dto.DadosCadastroTransacaoDTO;
import com.example.picpay.dto.DadosListagemNotificacaoDTO;
import com.example.picpay.infra.SaldoInsuficienteException;
import com.example.picpay.infra.TransacaoEntreMesmosUsuariosException;
import com.example.picpay.infra.UsuarioNaoAutorizadoException;
import com.example.picpay.repository.TransacaoRepository;
import com.example.picpay.transacao.Transacao;
import com.example.picpay.usuario.TipoDeUsuario;
import com.example.picpay.usuario.Usuario;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TransacaoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private AutorizacaoService autorizacaoService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Transactional
    public Transacao criarTransacao(DadosCadastroTransacaoDTO transacao) {
        try {
            Usuario remetente = this.usuarioService.findUsuarioById(transacao.remetenteId());
            Usuario destinatario = this.usuarioService.findUsuarioById(transacao.destinatarioId());

            this.validarTransacao(transacao, remetente);

            Transacao novaTransacao = new Transacao();
            novaTransacao.setValor(transacao.valor());
            novaTransacao.setRemetenteId(remetente);
            novaTransacao.setDestinatarioId(destinatario);
            novaTransacao.setData(LocalDateTime.now().atOffset(ZoneOffset.of("-03:00")).toLocalDateTime());
            novaTransacao.setAtivo(true);
            
            remetente.setSaldo(remetente.getSaldo().subtract(transacao.valor()));
            destinatario.setSaldo(destinatario.getSaldo().add(transacao.valor()));

            transacaoRepository.save(novaTransacao);
            usuarioService.salvarUsuario(remetente);
            usuarioService.salvarUsuario(destinatario);

            DadosListagemNotificacaoDTO dadosNotificacao = new DadosListagemNotificacaoDTO(novaTransacao, remetente);

            this.autorizacaoService.autorizarTransacao(transacao);
            this.notificacaoService.notificarTransacao(dadosNotificacao);

            return novaTransacao;
         } catch (NestedRuntimeException restClientException) {
            throw new RestClientException("Erro ao enviar notificação do tipo POST na classe \"TransacaoService\".");
        }
    }

    public void validarTransacao(DadosCadastroTransacaoDTO transacao, Usuario remetente) {
        if(remetente.getSaldo().compareTo(transacao.valor()) < 0)
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        else if(remetente.getTipoDeUsuario().equals(TipoDeUsuario.LOJISTA))
            throw new UsuarioNaoAutorizadoException("Usuário do tipo \"LOJISTA\" não pode realizar esse tipo de transação.");
        else if(remetente.getId().equals(transacao.destinatarioId()))
            throw new TransacaoEntreMesmosUsuariosException("O remetente deve ser diferente do destinatário para realizar a transação.");
    }

    public Transacao findTransacaoById(Long id) {
        return transacaoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Transação não encontrada com Id " + id));
    }

    public List<Transacao> findAllTransacoes() {
        return transacaoRepository.findAll();
    }

    public void deletarTransacaoById(Long id) {
        var transacao = this.findTransacaoById(id);
        this.transacaoRepository.delete(transacao);
    }

    public List<Transacao> findAllByAtivoTrue() {
        var lista = this.transacaoRepository.findAllByAtivoTrue();
        return lista;
    }
}
