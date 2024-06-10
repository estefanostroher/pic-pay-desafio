package com.example.picpay.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.picpay.dto.DadosAtualizarUsuarioDTO;
import com.example.picpay.dto.DadosCadastroUsuarioDTO;
import com.example.picpay.dto.DadosListagemUsuarioDTO;
import com.example.picpay.service.UsuarioService;
import com.example.picpay.usuario.Usuario;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * @author Estefano Ströher
     * @param dados Dados que você quer cadastrar.
     * @param uriComponentsBuilder Builder de URIs.
     * @return 201 CREATED + Dados do usuário criado
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios
     * </p>
     * 
     * <p>
     * Exemplo de uso:
     * <pre>
     * {@code
     * {
     *    "nomeCompleto": "Teste",
     *    "cpf": "000000000-00",
     *    "email": "teste@example.com",
     *    "senha": "123456789",
     *    "saldo": 4800,
     *    "tipoDeUsuario": "ADMIN"
     * }
     * }
     * </pre>
     *
     */
    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuarioDTO> cadastrarUsuario(@RequestBody DadosCadastroUsuarioDTO dados, UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuario = usuarioService.cadastrarUsuario(dados);
        return ResponseEntity.created(uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(dados).toUri()).body(new DadosListagemUsuarioDTO(usuario));
    }

    /**
     * @author Estefano Ströher
     * @return 200 OK + Dados de todos os usuários
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios
     * </p>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste",
     *     "cpf": "000000000-00",
     *     "email": "teste@example.com",
     *     "saldo": 4800,
     *     "tipoDeUsuario": "ADMIN",
     *     "ativo": false | true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping
    public ResponseEntity<List<DadosListagemUsuarioDTO>> listarTodosUsuarios() {
        var lista = this.usuarioService.findAllUsuarios();
        return ResponseEntity.ok(lista.stream().map(DadosListagemUsuarioDTO::new).toList());
    }

    /**
     * @author Estefano Ströher
     * @return 200 OK + Dados de todos os usuários ativos
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios/ativos
     * </p>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste",
     *     "cpf": "000000000-00",
     *     "email": "teste@example.com",
     *     "saldo": 4800,
     *     "tipoDeUsuario": "ADMIN",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<DadosListagemUsuarioDTO>> listarTodosUsuariosAtivos() {
        List<DadosListagemUsuarioDTO> usuarios = usuarioService.findAllByAtivoTrue();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * @author Estefano Ströher
     * @param id Id do usuário que você quer procurar.
     * @return 200 OK + Dados do usuário buscado pelo id
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios/1
     * </p>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste",
     *     "cpf": "000000000-00",
     *     "email": "teste@example.com",
     *     "saldo": 4800,
     *     "tipoDeUsuario": "ADMIN",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemUsuarioDTO> listarUsuarioPeloId(@PathVariable Long id) {
        Usuario usuario = usuarioService.findUsuarioById(id);
        return ResponseEntity.ok(new DadosListagemUsuarioDTO(usuario));
    }

    /**
     * @author Estefano Ströher
     * @param email Email que você quer procurar.
     * @return 200 OK + Dados do usuário buscado pelo email
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios/email/teste@example.com
     * </p>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste",
     *     "cpf": "000000000-00",
     *     "email": "teste@example.com",
     *     "saldo": 4800,
     *     "tipoDeUsuario": "ADMIN",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<DadosListagemUsuarioDTO> listarUsuarioPeloEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.findUsuarioByEmail(email);
        return ResponseEntity.ok(new DadosListagemUsuarioDTO(usuario));
    }

    /**
     * @author Estefano Ströher
     * @param nomeCompleto
     * @return 200 OK + Dados do usuário buscado pelo nome completo
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios/nome-completo/Teste
     * </p>
     * 
     * <p>
     * Exemplo de uso:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste",
     *     "cpf": "000000000-00",
     *     "email": "teste@example.com",
     *     "saldo": 4800,
     *     "tipoDeUsuario": "ADMIN",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping("/nome-completo/{nomeCompleto}")
    public ResponseEntity<DadosListagemUsuarioDTO> listarUsuarioPeloNomeCompleto(@PathVariable String nomeCompleto) {
        Usuario usuario = usuarioService.findUsuarioByNomeCompleto(nomeCompleto);
        return ResponseEntity.ok(new DadosListagemUsuarioDTO(usuario));
    }

    /**
     * @author Estefano Ströher
     * @param cpf
     * @return 200 OK + Dados do usuário buscado pelo cpf
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios/cpf/000000000-00
     * </p>
     * 
     * <p>
     * Exemplo de uso:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste",
     *     "cpf": "000000000-00",
     *     "email": "teste@example.com",
     *     "saldo": 4800,
     *     "tipoDeUsuario": "ADMIN",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<DadosListagemUsuarioDTO> listarUsuarioPeloCpf(@PathVariable String cpf) {
        Usuario usuario = usuarioService.findUsuarioByCpf(cpf);
        return ResponseEntity.ok(new DadosListagemUsuarioDTO(usuario));
    }

    /**
     * @author Estefano Ströher
     * @param id
     * @return 404 NO CONTENT
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/usuarios/1
     * </p>
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id) {
        this.usuarioService.deletarUsuarioById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @author Estefano Ströher
     * @param id
     * @return 404 NO CONTENT
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/inativar-usuario/1
     * </p>
     */
    @DeleteMapping("inativar-usuario/{id}")
    @Transactional
    public ResponseEntity<Void> inativarUsuario(@PathVariable Long id) {
        var usuario = this.usuarioService.findUsuarioById(id);
        usuario.inativarUsuario();
        return ResponseEntity.noContent().build();
    }

    /**
     * @author Estefano Ströher
     * @param id
     * @return 404 NO CONTENT
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/ativar-usuario/1
     * </p>
     */
    @PutMapping("ativar-usuario/{id}")
    @Transactional
    public ResponseEntity<Void> ativarUsuario(@PathVariable Long id) {
        var usuario = this.usuarioService.findUsuarioById(id);
        usuario.ativarUsuario();
        return ResponseEntity.noContent().build();
    }

    /**
     * @author Estefano Ströher
     * @param dados Dados que você quer atualizar.
     * @return 404 NO CONTENT
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes/atualizar-usuario
     * </p>
     * 
     * <p>
     * Exemplo de uso:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste2",
     *     "cpf": "111111111-11",
     *     "email": "teste2@example.com",
     * }
     * }
     * </pre>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "nomeCompleto": "Teste2",
     *     "cpf": "111111111-11",
     *     "email": "teste2@example.com",
     *     "saldo": 4800,
     *     "tipoDeUsuario": "ADMIN",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @PutMapping("atualizar-usuario")
    @Transactional
    public ResponseEntity<DadosListagemUsuarioDTO> atualizarUsuario(@RequestBody DadosAtualizarUsuarioDTO dados) {
        Usuario usuario = this.usuarioService.atualizarUsuarioById(dados);
        return ResponseEntity.ok().body(new DadosListagemUsuarioDTO(usuario));
    }
}
