package br.senai.estoque.gerenciamento_estoque.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.estoque.gerenciamento_estoque.dto.AtivoPatrimonialForm;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.service.AtivoPatrimonialService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ativos")
public class ApiAtivoPatrimonialController {

	@Autowired
	private AtivoPatrimonialService ativoPatrimonialService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping
	public ResponseEntity<?> listarAtivos(HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		return ResponseEntity.ok(ativoPatrimonialService.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarAtivo(@PathVariable Long id, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			return ResponseEntity.ok(ativoPatrimonialService.buscarPorId(id));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage()));
		}
	}

	@PostMapping
	public ResponseEntity<?> salvarAtivo(@Valid @RequestBody AtivoPatrimonialForm ativoPatrimonialForm,
			HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			Funcionario funcionario = sessaoService.buscarFuncionarioLogado(session);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ativoPatrimonialService.salvar(ativoPatrimonialForm, funcionario));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("mensagem", ex.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarAtivo(@PathVariable Long id,
			@Valid @RequestBody AtivoPatrimonialForm ativoPatrimonialForm, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			return ResponseEntity.ok(ativoPatrimonialService.atualizar(id, ativoPatrimonialForm));
		} catch (IllegalArgumentException ex) {
			HttpStatus status = ex.getMessage().contains("nao encontrado") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(Map.of("mensagem", ex.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirAtivo(@PathVariable Long id, HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			ativoPatrimonialService.excluir(id);
			return ResponseEntity.ok(Map.of("mensagem", "ativo patrimonial excluido com sucesso"));
		} catch (IllegalArgumentException ex) {
			HttpStatus status = ex.getMessage().contains("nao encontrado") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(Map.of("mensagem", ex.getMessage()));
		}
	}
}
