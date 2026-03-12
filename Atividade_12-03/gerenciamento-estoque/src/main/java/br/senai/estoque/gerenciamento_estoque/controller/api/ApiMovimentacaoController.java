package br.senai.estoque.gerenciamento_estoque.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.estoque.gerenciamento_estoque.dto.MovimentacaoForm;
import br.senai.estoque.gerenciamento_estoque.model.Funcionario;
import br.senai.estoque.gerenciamento_estoque.service.MovimentacaoService;
import br.senai.estoque.gerenciamento_estoque.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimentacoes")
public class ApiMovimentacaoController {

	@Autowired
	private MovimentacaoService movimentacaoService;

	@Autowired
	private SessaoService sessaoService;

	@GetMapping
	public ResponseEntity<?> listarMovimentacoes(HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		return ResponseEntity.ok(movimentacaoService.listarTodas());
	}

	@PostMapping
	public ResponseEntity<?> salvarMovimentacao(@Valid @RequestBody MovimentacaoForm movimentacaoForm,
			HttpSession session) {
		if (!sessaoService.estaLogado(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "acesso nao autorizado"));
		}
		try {
			Funcionario funcionario = sessaoService.buscarFuncionarioLogado(session);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(movimentacaoService.registrar(movimentacaoForm, funcionario));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(Map.of("mensagem", ex.getMessage()));
		}
	}
}
